package com.nemonotfound.nemos.enchantments.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.nemonotfound.nemos.enchantments.tree.TreeLogTransformationContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Preserves tree membership when a tool transforms one tree block into another,
 * such as when an axe strips a log, so Felling can still find the rest of the tree.
 */
@Mixin(BlockTransformer.class)
public abstract class BlockTransformerMixin {

    @WrapOperation(method = "transformBlock", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"))
    private boolean nemosEnchantments$preserveTreeMembership(Level level, BlockPos pos, BlockState state,
            int flags, Operation<Boolean> original) {
        return TreeLogTransformationContext.transformKeepingTreeMembership(level, pos, state,
                () -> original.call(level, pos, state, flags));
    }
}
