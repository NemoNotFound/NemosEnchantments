package com.nemonotfound.nemos.enchantments.tree;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.LongFunction;

import static com.nemonotfound.nemos.enchantments.tree.ConnectedTreeLogSearch.MAX_CONNECTED_BLOCKS;
import static com.nemonotfound.nemos.enchantments.tree.ConnectedTreeLogSearch.SearchStatus;

/** Finds the connected part of a tracked tree without loading its missing chunks. */
//TODO: Rethink and refactor
public final class TrackedTreeLogSearch {

    private final LongFunction<LevelChunk> loadedChunks;
    private final BlockPos origin;
    private final int logLimit;

    public TrackedTreeLogSearch(ServerLevel level, BlockPos origin, int logLimit) {
        this(chunkPos -> TreeTracking.getLoadedChunk(level, chunkPos), origin, logLimit);
    }

    public TrackedTreeSearchResult findLogs() {
        LevelChunk chunk = loadedChunks.apply(ChunkPos.pack(origin));

        if (chunk == null) {
            return TrackedTreeSearchResult.REJECTED;
        }

        TreeChunkData treeData = chunk.getAttached(TreeTracking.TREES);
        UUID treeId = treeData == null ? null : treeData.getTreeId(origin.asLong());

        if (treeId == null) {
            return TrackedTreeSearchResult.UNTRACKED;
        }

        return findLogs(treeData.getTreePart(treeId));
    }

    TrackedTreeLogSearch(LongFunction<LevelChunk> loadedChunks, BlockPos origin, int logLimit) {
        this.loadedChunks = loadedChunks;
        this.origin = origin;
        this.logLimit = logLimit;
    }

    /**
     * Returns connected logs to fell, excluding the origin.
     *
     * <p>A missing referenced chunk or an exceeded log limit rejects felling. After a block-limit
     * abort, partial logs are accepted only if all remaining logs are proven disconnected within
     * the additional search budget. A ready result may be empty when only the origin remains.
     */
    TrackedTreeSearchResult findLogs(TreeChunkData.TreePart tree) {
        Optional<Set<BlockPos>> existingLogs = collectExistingLogs(tree);

        if (existingLogs.isEmpty() || !existingLogs.get().contains(origin)) {
            return TrackedTreeSearchResult.REJECTED;
        }

        Set<BlockPos> treeLogs = existingLogs.get();

        var search = new ConnectedTreeLogSearch(loadedChunks, tree.id(), logLimit, treeLogs.size());
        var result = search.search(origin, MAX_CONNECTED_BLOCKS);

        if (!isCompleteComponent(tree.id(), treeLogs, result)) {
            return TrackedTreeSearchResult.REJECTED;
        }

        List<BlockPos> logsToFell = new ArrayList<>(result.logs());
        logsToFell.remove(origin);

        return TrackedTreeSearchResult.ready(logsToFell);
    }

    private boolean isCompleteComponent(UUID treeId, Set<BlockPos> treeLogs,
                                         ConnectedTreeLogSearch.SearchResult result) {
        if (result.status() == SearchStatus.COMPLETE) {
            return true;
        }

        if (result.status() == SearchStatus.LOG_LIMIT_REACHED) {
            return false;
        }

        return remainingLogsAreDisconnected(treeId, treeLogs, result.logs());
    }

    /**
     * Proves that every log missing from a limited canopy search belongs to another component.
     *
     * <p>A split tree can leave a small isolated stump below a large crown. Searching that stump
     * to completion proves that it cannot connect to the crown, even when exploring every leaf
     * of the crown would exceed the limit. All extra searches share one block budget. If any
     * component cannot be fully checked, or reaches an already found log, felling is rejected.
     */
    private boolean remainingLogsAreDisconnected(UUID treeId, Set<BlockPos> treeLogs, List<BlockPos> foundLogs) {
        Set<BlockPos> remainingLogs = new HashSet<>(treeLogs);
        remainingLogs.removeAll(foundLogs);

        int remainingBudget = MAX_CONNECTED_BLOCKS;
        var componentSearch = new ConnectedTreeLogSearch(loadedChunks, treeId, treeLogs.size());

        while (!remainingLogs.isEmpty()) {
            var component = componentSearch.search(remainingLogs.iterator().next(), remainingBudget);

            if (component.status() != SearchStatus.COMPLETE || component.logs().isEmpty()
                    || !remainingLogs.containsAll(component.logs())) {
                return false;
            }

            remainingLogs.removeAll(component.logs());
            remainingBudget -= component.connectedBlockCount();
        }

        return true;
    }

    private Optional<Set<BlockPos>> collectExistingLogs(TreeChunkData.TreePart tree) {
        Set<BlockPos> logs = new HashSet<>();

        for (long chunkPosition : tree.chunkPositions()) {
            LevelChunk chunk = loadedChunks.apply(chunkPosition);

            if (chunk == null) {
                return Optional.empty();
            }

            collectExistingLogs(chunk, tree.id(), logs);
        }

        return Optional.of(logs);
    }

    private void collectExistingLogs(LevelChunk chunk, UUID treeId, Set<BlockPos> logs) {
        TreeChunkData treeData = chunk.getAttached(TreeTracking.TREES);
        TreeChunkData.TreePart treePart = treeData == null ? null : treeData.getTreePart(treeId);

        if (treePart == null) {
            return;
        }

        for (long position : treePart.logPositions()) {
            BlockPos logPosition = BlockPos.of(position);

            if (TreeBlockHelper.isTreeBlock(chunk.getBlockState(logPosition))) {
                logs.add(logPosition);
            }
        }
    }
}
