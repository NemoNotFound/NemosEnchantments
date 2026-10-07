package com.nemonotfound.nemos.enchantments.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Stores reference removals waiting for their target chunk to load.
 *
 * <p>Neighboring chunks are usually loaded near a player, but during world generation they
 * may still be proto chunks and unavailable through the loaded-chunk lookup. Keeping removals
 * until the target chunk loads prevents stale references from blocking later felling when
 * the referenced chunk is unloaded.
 *
 * <p> {@link TreeChunkReferenceCleanup} applies the stored removals.
 */
//TODO: Rethink and refactor
final class PendingTreeChunkReferenceRemovals {

    static final PendingTreeChunkReferenceRemovals EMPTY = new PendingTreeChunkReferenceRemovals(Map.of());

    static final Codec<PendingTreeChunkReferenceRemovals> CODEC = Removal.CODEC.listOf()
            .xmap(PendingTreeChunkReferenceRemovals::indexByTargetChunk, PendingTreeChunkReferenceRemovals::getAllRemovals);

    private final Map<Long, List<Removal>> removalsByTargetChunk;

    private PendingTreeChunkReferenceRemovals(Map<Long, List<Removal>> removalsByTargetChunk) {
        this.removalsByTargetChunk = Map.copyOf(removalsByTargetChunk);
    }

    List<Removal> getRemovalsForChunk(long targetChunkPosition) {
        return removalsByTargetChunk.getOrDefault(targetChunkPosition, List.of());
    }

    boolean isEmpty() {
        return removalsByTargetChunk.isEmpty();
    }

    PendingTreeChunkReferenceRemovals withRemoval(Removal removal) {
        List<Removal> existingRemovals = getRemovalsForChunk(removal.targetChunk());

        if (existingRemovals.contains(removal)) {
            return this;
        }

        var updatedChunkRemovals = new ArrayList<>(existingRemovals);
        updatedChunkRemovals.add(removal);

        var updatedIndex = new HashMap<>(removalsByTargetChunk);
        updatedIndex.put(removal.targetChunk(), List.copyOf(updatedChunkRemovals));

        return new PendingTreeChunkReferenceRemovals(updatedIndex);
    }

    PendingTreeChunkReferenceRemovals removeRemovalsForChunk(long targetChunkPosition) {
        if (!removalsByTargetChunk.containsKey(targetChunkPosition)) {
            return this;
        }

        var updatedIndex = new HashMap<>(removalsByTargetChunk);
        updatedIndex.remove(targetChunkPosition);

        return new PendingTreeChunkReferenceRemovals(updatedIndex);
    }

    private static PendingTreeChunkReferenceRemovals indexByTargetChunk(List<Removal> removals) {
        Map<Long, List<Removal>> removalsByTargetChunk = new HashMap<>();

        for (Removal removal : removals) {
            removalsByTargetChunk.computeIfAbsent(removal.targetChunk(), ignored -> new ArrayList<>())
                    .add(removal);
        }

        removalsByTargetChunk.replaceAll((_, chunkRemovals) -> List.copyOf(chunkRemovals));

        return new PendingTreeChunkReferenceRemovals(removalsByTargetChunk);
    }

    private List<Removal> getAllRemovals() {
        return removalsByTargetChunk.values().stream()
                .flatMap(List::stream)
                .toList();
    }

    /**
     * @param targetChunk packed position of the chunk whose data must be updated
     * @param treeId ID of the affected tree
     * @param removedChunk packed position of the chunk reference to remove
     */
    record Removal(long targetChunk, UUID treeId, long removedChunk) {
        static final Codec<Removal> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.LONG.fieldOf("target_chunk").forGetter(Removal::targetChunk),
                UUIDUtil.CODEC.fieldOf("tree_id").forGetter(Removal::treeId),
                Codec.LONG.fieldOf("removed_chunk").forGetter(Removal::removedChunk)
        ).apply(instance, Removal::new));
    }
}
