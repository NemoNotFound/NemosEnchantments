package com.nemonotfound.nemos.enchantments.tree;

import com.nemonotfound.nemos.enchantments.NemosEnchantments;
import com.nemonotfound.nemos.enchantments.tree.PendingTreeChunkReferenceRemovals.Removal;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;

/**
 * Keeps a tree's cross-chunk references consistent without loading additional chunks.
 *
 * <p>Updates are applied immediately when the affected chunk is loaded. Otherwise, they are
 * stored on the level and applied when that chunk loads later.
 */
//TODO: Rethink and refactor
final class TreeChunkReferenceCleanup {

    static final AttachmentType<PendingTreeChunkReferenceRemovals> PENDING_REMOVALS = AttachmentRegistry.createPersistent(
            NemosEnchantments.modIdentifier("pending_tree_chunk_removals"),
            PendingTreeChunkReferenceRemovals.CODEC
    );

    private TreeChunkReferenceCleanup() {
    }

    static void registerChunkLoadListener() {
        ServerChunkEvents.CHUNK_LOAD.register((level, chunk, _) -> handleChunkLoad(level, chunk));
    }

    static void handleChunkLoad(ServerLevel level, LevelChunk chunk) {
        applyPendingRemovals(level, chunk);
        removeEmptyTreeParts(level, chunk);
    }

    static void saveLogRemoval(ChunkAccess chunk, TreeChunkData remainingData, TreeChunkData.TreePart originalPart) {
        if (remainingData.getTreePart(originalPart.id()) != null) {
            TreeTracking.storeTreeData(chunk, remainingData);

            return;
        }

        scheduleCleanup(chunk, remainingData, originalPart);
    }

    /**
     * Proto chunks retain the marker until promotion; live server chunks process it immediately.
     */
    private static void scheduleCleanup(ChunkAccess chunk, TreeChunkData treeData, TreeChunkData.TreePart removedPart) {
        var marker = TreeChunkData.TreePart.cleanupMarker(removedPart.id(), removedPart.chunkPositions());
        TreeTracking.storeTreeData(chunk, treeData.putTreePart(marker));

        if (chunk instanceof LevelChunk levelChunk && levelChunk.getLevel() instanceof ServerLevel level) {
            removeEmptyTreeParts(level, levelChunk);
        }
    }

    /**
     * Removes cleanup markers from a chunk and removes that chunk from all referenced tree parts.
     *
     * <p>A tree part without local logs is kept temporarily as a cleanup marker. Its chunk list
     * identifies every other chunk that must stop referring to this chunk.
     */
    static void removeEmptyTreeParts(ServerLevel level, LevelChunk chunk) {
        TreeChunkData treeData = chunk.getAttached(TreeTracking.TREES);

        if (treeData == null) {
            return;
        }

        long emptyChunkPosition = chunk.getPos().pack();

        for (TreeChunkData.TreePart treePart : treeData.getTreeParts()) {
            if (!treePart.isCleanupMarker()) {
                continue;
            }

            removeChunkFromReferencedTreeParts(level, emptyChunkPosition, treePart);
            treeData = treeData.removeTreePart(treePart.id());
        }

        TreeTracking.storeTreeData(chunk, treeData);
    }

    private static void removeChunkFromReferencedTreeParts(
            ServerLevel level,
            long removedChunkPosition,
            TreeChunkData.TreePart emptyTreePart
    ) {
        for (long targetChunkPosition : emptyTreePart.chunkPositions()) {
            if (targetChunkPosition == removedChunkPosition) {
                continue;
            }

            var removal = new Removal(targetChunkPosition, emptyTreePart.id(), removedChunkPosition);
            applyOrQueueRemoval(level, removal);
        }
    }

    private static void applyOrQueueRemoval(ServerLevel level, Removal removal) {
        LevelChunk targetChunk = TreeTracking.getLoadedChunk(level, removal.targetChunk());

        if (targetChunk == null) {
            queueRemoval(level, removal);

            return;
        }

        removeChunkReference(targetChunk, removal);
    }

    private static void queueRemoval(ServerLevel level, Removal removal) {
        PendingTreeChunkReferenceRemovals pendingRemovals = level.getAttachedOrElse(
                PENDING_REMOVALS,
                PendingTreeChunkReferenceRemovals.EMPTY
        );
        PendingTreeChunkReferenceRemovals updatedRemovals = pendingRemovals.withRemoval(removal);

        if (updatedRemovals != pendingRemovals) {
            level.setAttached(PENDING_REMOVALS, updatedRemovals);
        }
    }

    private static void applyPendingRemovals(ServerLevel level, LevelChunk chunk) {
        PendingTreeChunkReferenceRemovals pendingRemovals = level.getAttached(PENDING_REMOVALS);

        if (pendingRemovals == null) {
            return;
        }

        long chunkPosition = chunk.getPos().pack();
        var removalsForChunk = pendingRemovals.getRemovalsForChunk(chunkPosition);

        if (removalsForChunk.isEmpty()) {
            return;
        }

        removalsForChunk.forEach(removal -> removeChunkReference(chunk, removal));
        storePendingRemovals(level, pendingRemovals.removeRemovalsForChunk(chunkPosition));
    }

    private static void storePendingRemovals(ServerLevel level, PendingTreeChunkReferenceRemovals pendingRemovals) {
        if (pendingRemovals.isEmpty()) {
            level.removeAttached(PENDING_REMOVALS);
        } else {
            level.setAttached(PENDING_REMOVALS, pendingRemovals);
        }
    }

    private static void removeChunkReference(LevelChunk chunk, Removal removal) {
        TreeChunkData treeData = chunk.getAttached(TreeTracking.TREES);

        if (treeData == null) {
            return;
        }

        TreeChunkData updatedTreeData = treeData.removeChunkReference(removal.treeId(), removal.removedChunk());
        TreeTracking.storeTreeData(chunk, updatedTreeData);
    }
}
