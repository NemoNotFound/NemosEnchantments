package com.nemonotfound.nemos.enchantments.tree;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Collects the logs placed by one tree or huge-fungus feature invocation.
 *
 * <p>A capture exists only while the feature's {@code place(...)} method is running. Logs are
 * grouped by chunk because each chunk stores its own {@link TreeChunkData.TreePart}. After a
 * successful placement, every part receives the same tree ID and list of occupied chunks.
 */
//TODO: Rethink and refactor
final class TreePlacementCapture {

    private static final int MAX_CAPTURED_LOGS = 4096;

    private final Map<ChunkAccess, Set<BlockPos>> logsByChunk = new HashMap<>();
    private final TreePlacementCapture parentCapture;
    private int capturedLogCount;
    private boolean limitExceeded;

    TreePlacementCapture(TreePlacementCapture parentCapture) {
        this.parentCapture = parentCapture;
    }

    /** A nested placement owns its writes even if it later fails or exceeds the capture limit. */
    private void forgetInParentCaptures(ChunkAccess chunk, BlockPos pos) {
        for (TreePlacementCapture parent = parentCapture; parent != null; parent = parent.parentCapture) {
            Set<BlockPos> logs = parent.logsByChunk.get(chunk);

            if (logs != null && logs.remove(pos)) {
                parent.capturedLogCount--;
            }
        }
    }

    /**
     * Records a tree block placed during the active feature invocation.
     *
     * <p>Positions are stored once per chunk. If the capture grows beyond the safety limit, all
     * collected data is discarded and later felling falls back to normal tree detection.
     */
    void recordLog(ChunkAccess chunk, BlockPos pos) {
        forgetInParentCaptures(chunk, pos);

        if (limitExceeded) {
            return;
        }

        Set<BlockPos> logs = logsByChunk.computeIfAbsent(chunk, ignored -> new HashSet<>());

        if (!logs.add(pos.immutable())) {
            return;
        }

        capturedLogCount++;

        if (capturedLogCount > MAX_CAPTURED_LOGS) {
            limitExceeded = true;
            logsByChunk.clear();
        }
    }

    /**
     * Saves the completed tree to the persistent data of every occupied chunk.
     *
     * <p>Blocks replaced before feature placement finished are removed first. One UUID identifies
     * the complete tree, while each chunk stores only its local log positions together with the
     * full list of chunks containing the tree. An oversized capture is not saved.
     */
    void saveTree() {
        if (limitExceeded) {
            return;
        }

        removeReplacedLogs();
        UUID treeId = UUID.randomUUID();
        List<Long> packedChunkPositions = getPackedChunkPositions();

        logsByChunk.forEach((chunk, logs) -> saveChunkLogs(chunk, logs, treeId, packedChunkPositions));
    }

    private void removeReplacedLogs() {
        logsByChunk.forEach((chunk, logs) ->
                logs.removeIf(pos -> !TreeBlockHelper.isTreeBlock(chunk.getBlockState(pos))));
        logsByChunk.values().removeIf(Set::isEmpty);
    }

    private List<Long> getPackedChunkPositions() {
        return logsByChunk.keySet().stream()
                .map(chunk -> chunk.getPos().pack())
                .toList();
    }

    private void saveChunkLogs(ChunkAccess chunk, Set<BlockPos> logs, UUID treeId, List<Long> packedChunkPositions) {
        List<Long> positions = logs.stream()
                .map(BlockPos::asLong)
                .toList();

        TreeTracking.saveTreePart(chunk, new TreeChunkData.TreePart(treeId, positions, packedChunkPositions));
    }
}
