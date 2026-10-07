package com.nemonotfound.nemos.enchantments.tree;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.Optional;
import java.util.function.BooleanSupplier;

//TODO: Rethink and refactor
public final class TreeLogTransformationContext {

    private static final ThreadLocal<LogTransformation> ACTIVE_TRANSFORMATION = new ThreadLocal<>();

    private TreeLogTransformationContext() {
    }

    /**
     * Executes the supplied block change, keeping tree membership when both states are tree blocks.
     *
     * <p>The supplier runs the original {@code Level.setBlock(...)} call, which updates the chunk.
     * {@code TreeChunkMixin} then calls {@code TreeTracking.onBlockChanged(...)}. For a transformation
     * between tree blocks on the server, that method calls
     * {@link #matchesActiveTransformation(ChunkAccess, BlockPos, BlockState, BlockState)}
     * to decide whether to keep the tree membership.
     *
     * <p>This happens synchronously on the same thread, before the supplier returns. The active
     * transformation must therefore be set before invoking the supplier. The {@code finally} block
     * restores any outer transformation or clears the context, even if the block change throws,
     * so later changes cannot accidentally inherit this protection.
     */
    public static boolean transformKeepingTreeMembership(Level level, BlockPos pos, BlockState after, BooleanSupplier transformationSupplier) {
        BlockState before = level.getBlockState(pos);

        if (!TreeBlockHelper.isTreeBlock(before) || !TreeBlockHelper.isTreeBlock(after)) {
            return transformationSupplier.getAsBoolean();
        }

        LogTransformation previousTransformation = ACTIVE_TRANSFORMATION.get();
        ACTIVE_TRANSFORMATION.set(new LogTransformation(level, pos.immutable(), before, after));

        try {
            return transformationSupplier.getAsBoolean();
        } finally {
            restorePreviousTransformation(previousTransformation);
        }
    }

    private static void restorePreviousTransformation(LogTransformation previousTransformation) {
        Optional.ofNullable(previousTransformation)
                .ifPresentOrElse(ACTIVE_TRANSFORMATION::set, ACTIVE_TRANSFORMATION::remove);
    }

    /**
     * Checks whether the reported block change belongs to the active transformation on this thread.
     *
     * <p>{@code TreeTracking.onBlockChanged(...)} calls this while the transformation supplier is
     * updating the block. A match requires a {@link LevelChunk} in the same world, the same position,
     * and the exact block states recorded before and after the transformation. This prevents other
     * block changes triggered during the operation from inheriting its protection.
     *
     * <p>A matching change tells the caller to keep the existing tree membership. This method only
     * checks the context; it does not change membership or clear the active transformation.
     * Without an active transformation, it returns {@code false}.
     */
    static boolean matchesActiveTransformation(ChunkAccess chunk, BlockPos pos, BlockState before, BlockState after) {
        LogTransformation transformation = ACTIVE_TRANSFORMATION.get();

        if (transformation == null || !(chunk instanceof LevelChunk levelChunk)) {
            return false;
        }

        boolean sameLevel = transformation.level() == levelChunk.getLevel();
        boolean samePosition = transformation.pos().equals(pos);
        boolean sameBlockStates = transformation.before() == before && transformation.after() == after;

        return sameLevel && samePosition && sameBlockStates;
    }

    private record LogTransformation(Level level, BlockPos pos, BlockState before, BlockState after) {
    }
}
