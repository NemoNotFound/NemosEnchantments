package com.nemonotfound.nemos.enchantments.tree;

import com.nemonotfound.nemos.enchantments.NemosEnchantments;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BooleanSupplier;

/**
 * Coordinates tree placement capture and block membership updates.
 */
//TODO: Rethink and refactor
public final class TreeTracking {

    static final AttachmentType<TreeChunkData> TREES = AttachmentRegistry.createPersistent(
            NemosEnchantments.modIdentifier("trees"),
            TreeChunkData.CODEC
    );
    private static final ThreadLocal<TreePlacementCapture> ACTIVE_PLACEMENT = new ThreadLocal<>();

    private TreeTracking() {
    }

    public static void init() {
        TreeChunkReferenceCleanup.registerChunkLoadListener();
    }

    /**
     * Runs one feature placement while collecting every tree block it adds.
     *
     * <p>{@code TreeFeatureMixin} supplies the original {@code place(...)} call. While it runs,
     * block changes reported by {@code TreeChunkMixin} are added to a temporary
     * {@link TreePlacementCapture}. A successful placement saves all captured logs under one tree ID;
     * a failed placement leaves them untracked.
     *
     * <p>The previous capture is restored in a {@code finally} block. This supports nested feature
     * placements and prevents a failed or throwing placement from affecting later block changes.
     *
     * @param placeSupplier the feature placement to execute
     * @return the result returned by the feature placement
     */
    public static boolean capturePlacement(BooleanSupplier placeSupplier) {
        TreePlacementCapture previousCapture = ACTIVE_PLACEMENT.get();
        TreePlacementCapture currentCapture = new TreePlacementCapture(previousCapture);
        ACTIVE_PLACEMENT.set(currentCapture);

        try {
            boolean placed = placeSupplier.getAsBoolean();

            if (placed) {
                currentCapture.saveTree();
            }

            return placed;
        } finally {
            restorePlacementCapture(previousCapture);
        }
    }

    private static void restorePlacementCapture(TreePlacementCapture previousCapture) {
        if (previousCapture == null) {
            ACTIVE_PLACEMENT.remove();
        } else {
            ACTIVE_PLACEMENT.set(previousCapture);
        }
    }

    /**
     * Updates tree membership after a chunk reports a block change.
     *
     * <p>{@code TreeChunkMixin} calls this when {@code setBlockState(...)} returns, both during
     * world generation and in loaded chunks.
     *
     * <p>A tree-to-tree change matching the active tool transformation keeps its existing
     * membership. Otherwise, any membership at the position is removed before the new tree
     * block is recorded in the active placement capture. Without an active capture, a newly
     * placed tree block remains untracked; it does not inherit the previous block's tree ID.
     */
    public static void onBlockChanged(ChunkAccess chunk, BlockPos pos, BlockState before, BlockState after) {
        if (shouldIgnoreChange(chunk, before, after)) {
            return;
        }

        boolean wasTreeBlock = TreeBlockHelper.isTreeBlock(before);
        boolean becomesTreeBlock = TreeBlockHelper.isTreeBlock(after);

        if (!wasTreeBlock && !becomesTreeBlock) {
            return;
        }

        boolean preservesMembership = wasTreeBlock && becomesTreeBlock
                && TreeLogTransformationContext.matchesActiveTransformation(chunk, pos, before, after);
        if (preservesMembership) {
            return;
        }

        removeLogMembership(chunk, pos);

        if (becomesTreeBlock) {
            capturePlacedLog(chunk, pos);
        }
    }

    private static boolean shouldIgnoreChange(ChunkAccess chunk, BlockState before, BlockState after) {
        return before == null || isClientChunk(chunk) || before.getBlock() == after.getBlock();
    }

    private static boolean isClientChunk(ChunkAccess chunk) {
        return chunk instanceof LevelChunk levelChunk && levelChunk.getLevel().isClientSide();
    }

    private static void removeLogMembership(ChunkAccess chunk, BlockPos pos) {
        TreeChunkData treeData = chunk.getAttached(TREES);

        if (treeData == null) {
            return;
        }

        long logPosition = pos.asLong();
        UUID treeId = treeData.getTreeId(logPosition);

        if (treeId == null) {
            return;
        }

        TreeChunkData.TreePart treePart = treeData.getTreePart(treeId);
        TreeChunkData remainingData = treeData.removeLog(logPosition);
        TreeChunkReferenceCleanup.saveLogRemoval(chunk, remainingData, treePart);
    }

    /**
     * Replaces the immutable tree-data snapshot attached to a chunk.
     *
     * <p>Removing the attachment when no tree parts remain avoids saving an empty payload and
     * lets callers treat a missing attachment as a chunk without tracked trees.
     */
    static void storeTreeData(ChunkAccess chunk, TreeChunkData treeData) {
        if (treeData.isEmpty()) {
            chunk.removeAttached(TREES);
        } else {
            chunk.setAttached(TREES, treeData);
        }
    }

    private static void capturePlacedLog(ChunkAccess chunk, BlockPos pos) {
        TreePlacementCapture capture = ACTIVE_PLACEMENT.get();
        if (capture != null) {
            capture.recordLog(chunk, pos);
        }
    }

    /**
     * Returns the tree ID at a loaded position, or an empty optional if the log is not tracked.
     */
    public static Optional<UUID> getTreeId(ServerLevel level, BlockPos pos) {
        return Optional.ofNullable(getLoadedTreeData(level, pos))
                .map(treeChunkData -> treeChunkData.getTreeId(pos.asLong()));
    }

    private static TreeChunkData getLoadedTreeData(ServerLevel level, BlockPos pos) {
        LevelChunk chunk = getLoadedChunk(level, ChunkPos.pack(pos));
        return chunk == null ? null : chunk.getAttached(TREES);
    }

    static LevelChunk getLoadedChunk(ServerLevel level, long chunkPos) {
        return level.getChunkSource().getChunkNow(ChunkPos.getX(chunkPos), ChunkPos.getZ(chunkPos));
    }

    static void saveTreePart(ChunkAccess chunk, TreeChunkData.TreePart part) {
        TreeChunkData treeData = Optional.ofNullable(chunk.getAttached(TREES))
                .orElse(new TreeChunkData(List.of()));

        chunk.setAttached(TREES, treeData.putTreePart(part));
    }
}
