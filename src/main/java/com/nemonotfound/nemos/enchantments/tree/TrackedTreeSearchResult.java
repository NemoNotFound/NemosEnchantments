package com.nemonotfound.nemos.enchantments.tree;

import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.Objects;

//TODO: Rethink and refactor
public record TrackedTreeSearchResult(Status status, List<BlockPos> logs) {

    public static final TrackedTreeSearchResult UNTRACKED = new TrackedTreeSearchResult(Status.UNTRACKED, List.of());
    public static final TrackedTreeSearchResult REJECTED = new TrackedTreeSearchResult(Status.REJECTED, List.of());

    public TrackedTreeSearchResult {
        Objects.requireNonNull(status);
        logs = List.copyOf(logs);

        if (status != Status.READY && !logs.isEmpty()) {
            throw new IllegalArgumentException("Tree search status is not ready");
        }
    }

    public static TrackedTreeSearchResult ready(List<BlockPos> logs) {
        return new TrackedTreeSearchResult(Status.READY, logs);
    }

    public enum Status {
        UNTRACKED,
        READY,
        REJECTED
    }
}
