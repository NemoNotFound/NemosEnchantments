package com.nemonotfound.nemos.enchantments.mixin;

import com.nemonotfound.nemos.enchantments.tree.TreeTracking;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.ProtoChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Reports block changes to tree tracking during world generation and normal gameplay.
 *
 * <p>{@link ProtoChunk} holds a chunk while it is being generated, including naturally placed
 * trees. {@link LevelChunk} represents a fully generated chunk, where saplings grow and players
 * break or transform logs. Both implement {@code setBlockState(...)}, so both need this hook.
 */
@Mixin({LevelChunk.class, ProtoChunk.class})
public abstract class TreeChunkMixin {

    /**
     * Forwards the previous and requested block states after the chunk write returns.
     *
     * <p>The return value supplies the previous state and can be {@code null}.
     * {@link TreeTracking#onBlockChanged(ChunkAccess, BlockPos, BlockState, BlockState)} decides
     * whether the change affects tree membership or belongs to an active tree growth capture.
     */
    @Inject(method = "setBlockState", at = @At("RETURN"))
    private void nemosEnchantments$onBlockChanged(BlockPos pos, BlockState after, int flags,
                                                CallbackInfoReturnable<BlockState> callback) {
        ChunkAccess chunk = (ChunkAccess) (Object) this;
        BlockState before = callback.getReturnValue();

        TreeTracking.onBlockChanged(chunk, pos, before, after);
    }
}
