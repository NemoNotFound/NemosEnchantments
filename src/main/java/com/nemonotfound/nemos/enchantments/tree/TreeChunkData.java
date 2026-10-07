package com.nemonotfound.nemos.enchantments.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Stores the parts of tracked trees that occupy one chunk.
 *
 * <p>The tree parts are indexed by their tree ID and by their local log positions. Both indexes
 * belong to the same immutable snapshot, so looking up the tree at a position does not require
 * scanning every tree part in the chunk.
 *
 * <p>Methods such as {@code removeLog} and {@code putTreePart} return an updated snapshot;
 * they never change this instance.
 */
//TODO: Rethink and refactor
public final class TreeChunkData {

    public static final Codec<TreeChunkData> CODEC = TreePart.CODEC.listOf()
            .xmap(TreeChunkData::new, data -> List.copyOf(data.treePartsById.values()));

    private final Map<UUID, TreePart> treePartsById;
    private final Map<Long, UUID> treeIdsByLogPosition;

    public TreeChunkData(List<TreePart> treeParts) {
        Map<UUID, TreePart> treePartsById = new HashMap<>();
        Map<Long, UUID> treeIdsByLogPosition = new HashMap<>();

        for (TreePart treePart : treeParts) {
            addTreePartToIndexes(treePartsById, treeIdsByLogPosition, treePart);
        }

        this.treePartsById = Collections.unmodifiableMap(treePartsById);
        this.treeIdsByLogPosition = Collections.unmodifiableMap(treeIdsByLogPosition);
    }

    private TreeChunkData(Map<UUID, TreePart> treePartsById, Map<Long, UUID> treeIdsByLogPosition) {
        this.treePartsById = Collections.unmodifiableMap(treePartsById);
        this.treeIdsByLogPosition = Collections.unmodifiableMap(treeIdsByLogPosition);
    }

    public UUID getTreeId(long logPosition) {
        return treeIdsByLogPosition.get(logPosition);
    }

    public TreePart getTreePart(UUID treeId) {
        return treePartsById.get(treeId);
    }

    public boolean isEmpty() {
        return treePartsById.isEmpty();
    }

    public List<TreePart> getTreeParts() {
        return List.copyOf(treePartsById.values());
    }

    public TreeChunkData removeTreePart(UUID treeId) {
        TreePart treePart = getTreePart(treeId);

        if (treePart == null) {
            return this;
        }

        Map<UUID, TreePart> updatedTreeParts = copyTreeParts();
        Map<Long, UUID> updatedPositionIndex = copyPositionIndex();
        updatedTreeParts.remove(treeId);
        removeFromPositionIndex(updatedPositionIndex, treePart);

        return new TreeChunkData(updatedTreeParts, updatedPositionIndex);
    }

    public TreeChunkData removeChunkReference(UUID treeId, long chunkPosition) {
        TreePart treePart = getTreePart(treeId);

        if (treePart == null || !treePart.chunkPositions().contains(chunkPosition)) {
            return this;
        }

        List<Long> remainingChunkPositions = new ArrayList<>(treePart.chunkPositions());
        remainingChunkPositions.remove(chunkPosition);

        Map<UUID, TreePart> updatedTreeParts = copyTreeParts();
        updatedTreeParts.put(treeId, treePart.withChunkPositions(remainingChunkPositions));

        return new TreeChunkData(updatedTreeParts, treeIdsByLogPosition);
    }

    public TreeChunkData removeLog(long logPosition) {
        UUID treeId = getTreeId(logPosition);
        if (treeId == null) {
            return this;
        }

        TreePart updatedTreePart = getTreePart(treeId).removeLog(logPosition);
        Map<UUID, TreePart> updatedTreeParts = copyTreeParts();
        replaceOrRemoveTreePart(updatedTreeParts, updatedTreePart);

        Map<Long, UUID> updatedPositionIndex = copyPositionIndex();
        updatedPositionIndex.remove(logPosition);

        return new TreeChunkData(updatedTreeParts, updatedPositionIndex);
    }

    /**
     * Adds or replaces a tree part and assigns its log positions exclusively to that tree.
     *
     * <p>If stale data assigns one of these positions to another tree, that position is removed
     * from the older tree part before the new snapshot is created.
     */
    public TreeChunkData putTreePart(TreePart treePart) {
        Map<UUID, TreePart> updatedTreeParts = copyTreeParts();
        Map<Long, UUID> updatedPositionIndex = copyPositionIndex();

        removeClaimedLogsFromOtherTrees(updatedTreeParts, treePart);
        addTreePartToIndexes(updatedTreeParts, updatedPositionIndex, treePart);

        return new TreeChunkData(updatedTreeParts, updatedPositionIndex);
    }

    private Map<UUID, TreePart> copyTreeParts() {
        return new HashMap<>(treePartsById);
    }

    private Map<Long, UUID> copyPositionIndex() {
        return new HashMap<>(treeIdsByLogPosition);
    }

    private void removeClaimedLogsFromOtherTrees(Map<UUID, TreePart> treeParts, TreePart newTreePart) {
        var claimedLogPositions = new HashSet<>(newTreePart.logPositions());
        Set<UUID> affectedTreeIds = findTreesClaiming(claimedLogPositions, newTreePart.id());

        for (UUID treeId : affectedTreeIds) {
            TreePart existingTreePart = treeParts.get(treeId);
            List<Long> remainingLogPositions = existingTreePart.logPositions().stream()
                    .filter(position -> !claimedLogPositions.contains(position))
                    .toList();

            replaceOrRemoveTreePart(treeParts, existingTreePart.withLogPositions(remainingLogPositions));
        }
    }

    private Set<UUID> findTreesClaiming(Iterable<Long> logPositions, UUID excludedTreeId) {
        Set<UUID> treeIds = new HashSet<>();

        for (long logPosition : logPositions) {
            UUID treeId = getTreeId(logPosition);

            if (treeId != null && !treeId.equals(excludedTreeId)) {
                treeIds.add(treeId);
            }
        }

        return treeIds;
    }

    private static void addTreePartToIndexes(Map<UUID, TreePart> treeParts,
                                             Map<Long, UUID> treeIdsByLogPosition,
                                             TreePart treePart) {
        TreePart replacedTreePart = treeParts.put(treePart.id(), treePart);
        removeFromPositionIndex(treeIdsByLogPosition, replacedTreePart);
        treePart.logPositions().forEach(position -> treeIdsByLogPosition.put(position, treePart.id()));
    }

    private static void removeFromPositionIndex(Map<Long, UUID> treeIdsByLogPosition, TreePart treePart) {
        if (treePart != null) {
            treePart.logPositions().forEach(position -> treeIdsByLogPosition.remove(position, treePart.id()));
        }
    }

    private static void replaceOrRemoveTreePart(Map<UUID, TreePart> treeParts, TreePart treePart) {
        if (treePart.logPositions().isEmpty()) {
            treeParts.remove(treePart.id());
        } else {
            treeParts.put(treePart.id(), treePart);
        }
    }

    /**
     * Describes the part of one tree stored in this chunk.
     *
     * @param id unique ID shared by every part of the tree
     * @param logPositions packed positions of the tree blocks in this chunk
     * @param chunkPositions packed positions of every chunk containing a part of the tree
     */
    public record TreePart(UUID id, List<Long> logPositions, List<Long> chunkPositions) {
        public static final Codec<TreePart> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                UUIDUtil.CODEC.fieldOf("id").forGetter(TreePart::id),
                Codec.LONG.listOf().fieldOf("logs").forGetter(TreePart::logPositions),
                Codec.LONG.listOf().fieldOf("chunks").forGetter(TreePart::chunkPositions)
        ).apply(instance, TreePart::new));

        public TreePart {
            logPositions = List.copyOf(logPositions);
            chunkPositions = List.copyOf(chunkPositions);
        }

        /** Keeps chunk references until the removed part's cleanup can be processed. */
        public static TreePart cleanupMarker(UUID treeId, List<Long> chunkPositions) {
            return new TreePart(treeId, List.of(), chunkPositions);
        }

        public boolean isCleanupMarker() {
            return logPositions.isEmpty();
        }

        private TreePart removeLog(long logPosition) {
            List<Long> remainingLogPositions = new ArrayList<>(logPositions);
            remainingLogPositions.remove(logPosition);
            return withLogPositions(remainingLogPositions);
        }

        private TreePart withLogPositions(List<Long> logPositions) {
            return new TreePart(id, logPositions, chunkPositions);
        }

        private TreePart withChunkPositions(List<Long> chunkPositions) {
            return new TreePart(id, logPositions, chunkPositions);
        }
    }
}
