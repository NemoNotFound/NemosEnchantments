package com.nemonotfound.nemos.enchantments.tree;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.LongFunction;

/**
 * Finds logs connected through direct or diagonal contact.
 *
 * <p>Tracked trees can connect through leaves or wart blocks because their IDs separate neighboring
 * trees. Untracked trees connect through logs only, so touching canopies do not merge them.
 */
//TODO: Rethink and refactor
public final class ConnectedTreeLogSearch {

    static final int MAX_CONNECTED_BLOCKS = 4096;
    private static final int UNKNOWN_LOG_COUNT = -1;

    private final LongFunction<LevelChunk> loadedChunkLookup;
    private final UUID treeId;
    private final int logLimit;
    private final int expectedLogCount;

    public ConnectedTreeLogSearch(LongFunction<LevelChunk> loadedChunkLookup, UUID treeId, int logLimit) {
        this(loadedChunkLookup, treeId, logLimit, UNKNOWN_LOG_COUNT);
    }

    public ConnectedTreeLogSearch(LongFunction<LevelChunk> loadedChunkLookup, UUID treeId, int logLimit,
                                  int expectedLogCount) {
        this.loadedChunkLookup = loadedChunkLookup;
        this.treeId = treeId;
        this.logLimit = logLimit;
        this.expectedLogCount = expectedLogCount;
    }

    /**
     * Returns connected logs including the origin, in an independent, mutable list.
     *
     * <p>Exceeding the log limit or {@value #MAX_CONNECTED_BLOCKS} connected blocks rejects the
     * search and returns an empty list instead of a partial felling result.
     */
    public List<BlockPos> findLogs(BlockPos origin) {
        SearchResult result = search(origin, MAX_CONNECTED_BLOCKS);

        return result.status() == SearchStatus.COMPLETE ? new ArrayList<>(result.logs()) : List.of();
    }

    SearchResult search(BlockPos origin, int blockLimit) {
        var pendingPositions = new ArrayDeque<BlockPos>();

        Set<BlockPos> scheduledPositions = new HashSet<>();
        Map<Long, LevelChunk> cachedChunks = new HashMap<>();
        List<BlockPos> logs = new ArrayList<>();

        int connectedBlockCount = 0;

        pendingPositions.add(origin.immutable());
        scheduledPositions.add(origin.immutable());

        while (!pendingPositions.isEmpty()) {
            BlockPos position = pendingPositions.removeFirst();
            BlockType blockType = classifyBlock(position, cachedChunks);

            if (blockType == BlockType.UNRELATED) {
                continue;
            }

            if (blockType == BlockType.LOG && logs.size() >= logLimit) {
                return new SearchResult(logs, SearchStatus.LOG_LIMIT_REACHED, connectedBlockCount);
            }

            if (connectedBlockCount >= blockLimit) {
                return new SearchResult(logs, SearchStatus.BLOCK_LIMIT_REACHED, connectedBlockCount);
            }

            connectedBlockCount++;

            if (blockType == BlockType.LOG) {
                logs.add(position);
            }

            if (expectedLogCount >= 0 && logs.size() >= expectedLogCount) {
                break;
            }

            enqueueNeighbors(position, pendingPositions, scheduledPositions);
        }

        return new SearchResult(logs, SearchStatus.COMPLETE, connectedBlockCount);
    }

    private BlockType classifyBlock(BlockPos position, Map<Long, LevelChunk> cachedChunks) {
        LevelChunk chunk = getLoadedChunk(position, cachedChunks);

        if (chunk == null) {
            return BlockType.UNRELATED;
        }

        var state = chunk.getBlockState(position);

        if (TreeBlockHelper.isTreeBlock(state)) {
            return belongsToSelectedTree(chunk, position) ? BlockType.LOG : BlockType.UNRELATED;
        }

        if (treeId != null && TreeBlockHelper.connectsLogs(state)) {
            return BlockType.CONNECTING_BLOCK;
        }

        return BlockType.UNRELATED;
    }

    private void enqueueNeighbors(BlockPos position, ArrayDeque<BlockPos> pendingPositions,
                                  Set<BlockPos> scheduledPositions) {
        for (BlockPos neighbor : BlockPos.betweenClosed(position.offset(-1, -1, -1), position.offset(1, 1, 1))) {
            BlockPos immutableNeighbor = neighbor.immutable();

            if (scheduledPositions.add(immutableNeighbor)) {
                pendingPositions.addLast(immutableNeighbor);
            }
        }
    }

    private LevelChunk getLoadedChunk(BlockPos position, Map<Long, LevelChunk> cachedChunks) {
        long chunkPosition = ChunkPos.pack(position);

        if (!cachedChunks.containsKey(chunkPosition)) {
            cachedChunks.put(chunkPosition, loadedChunkLookup.apply(chunkPosition));
        }

        return cachedChunks.get(chunkPosition);
    }

    private boolean belongsToSelectedTree(LevelChunk chunk, BlockPos position) {
        TreeChunkData treeData = chunk.getAttached(TreeTracking.TREES);
        UUID owner = treeData == null ? null : treeData.getTreeId(position.asLong());

        return Objects.equals(treeId, owner);
    }

    private enum BlockType {
        LOG, CONNECTING_BLOCK, UNRELATED
    }

    enum SearchStatus {
        COMPLETE, BLOCK_LIMIT_REACHED, LOG_LIMIT_REACHED
    }

    record SearchResult(List<BlockPos> logs, SearchStatus status, int connectedBlockCount) {
        SearchResult {
            logs = List.copyOf(logs);
        }
    }
}
