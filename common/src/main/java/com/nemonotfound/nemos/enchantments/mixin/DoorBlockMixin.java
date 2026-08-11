package com.nemonotfound.nemos.enchantments.mixin;

import com.nemonotfound.nemos.enchantments.enchantment.NemosEnchantments;
import com.nemonotfound.nemos.enchantments.utils.EnchantmentUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DoorBlock.class)
public class DoorBlockMixin {

    @Inject(method = "playerWillDestroy", at = @At("HEAD"))
    private void nemosEnchantments$collectUpperDoor(
            Level level,
            BlockPos pos,
            BlockState state,
            Player player,
            CallbackInfoReturnable<BlockState> cir
    ) {
        if (level.isClientSide()
                || state.getValue(DoorBlock.HALF) != DoubleBlockHalf.UPPER
                || !EnchantmentUtils.hasEnchantment(level, NemosEnchantments.COLLECTOR, player.getMainHandItem())) {
            return;
        }

        BlockPos lowerPos = pos.below();
        BlockState lowerState = level.getBlockState(lowerPos);
        if (!lowerState.is(state.getBlock())
                || lowerState.getValue(DoorBlock.HALF) != DoubleBlockHalf.LOWER) {
            return;
        }

        Block.dropResources(lowerState, level, lowerPos, null, player, player.getMainHandItem());
        level.setBlock(lowerPos, lowerState.getFluidState().createLegacyBlock(), Block.UPDATE_ALL);
    }
}
