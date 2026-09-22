package com.nemonotfound.nemos.enchantments.mixin;

import com.nemonotfound.nemos.enchantments.enchantment.Enchantments;
import com.nemonotfound.nemos.enchantments.utils.EnchantmentUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.nemonotfound.nemos.enchantments.utils.EnchantmentUtils.hasEnchantment;

@Mixin(Item.class)
//TODO: Replace with enchantment effect
public abstract class HoeItemMixin {

    @Inject(method = "canDestroyBlock", at = @At("HEAD"), cancellable = true)
    private void nemosEnchantments$preventUnripeCropHarvest(ItemStack itemStack, BlockState state, Level level,
                                                            BlockPos pos, LivingEntity user,
                                                            CallbackInfoReturnable<Boolean> cir) {
        if (!itemStack.is(ItemTags.HOES) || !(user instanceof Player)) {
            return;
        }

        Block block = state.getBlock();

        if (block instanceof CropBlock && hasEnchantment(level, Enchantments.FARMERS_KNOWLEDGE, itemStack)) {
            cir.setReturnValue(nemosFarming_canDestroyCrop(state, (Player) user));
        }
    }

    @Inject(method = "mineBlock", at = @At("HEAD"))
    private void nemosEnchantments$reapNearbyCrops(ItemStack stack, Level level, BlockState state, BlockPos pos,
                                                   LivingEntity miner, CallbackInfoReturnable<Boolean> cir) {
        if (!stack.is(ItemTags.HOES)) {
            return;
        }

        ItemStack hoe = miner.getMainHandItem();
        boolean isBlockCropBlock = state.getBlock() instanceof CropBlock;
        boolean hasHoeReaperEnchantment = hasEnchantment(level, Enchantments.REAPER, hoe);

        if (isBlockCropBlock && hasHoeReaperEnchantment) {
            int enchantmentLevel = EnchantmentUtils.getEnchantmentLevel(level, Enchantments.REAPER, hoe);
            int breakingRange = 2 * enchantmentLevel + 1;

            for (int i = 0; i < Math.pow(breakingRange, 3); i++) {
                BlockPos nextPos = nemosFarming_getNextBlockPos(pos, i, breakingRange);

                nemosFarming_breakCrop(level, nextPos, miner);
            }
        }

    }

    @Unique
    private BlockPos nemosFarming_getNextBlockPos(BlockPos pos, int i, int breakingRange) {
        int halfRange = breakingRange / 2;
        int x = (i / (breakingRange * breakingRange)) - halfRange;
        int y = ((i / breakingRange) % breakingRange) - halfRange;
        int z = (i % breakingRange) - halfRange;

        return pos.offset(x, y, z);
    }

    @Unique
    private void nemosFarming_breakCrop(Level level, BlockPos pos, LivingEntity user) {
        if (!(user instanceof Player)) {
            return;
        }

        BlockState nextBlockState = level.getBlockState(pos);
        Block nextBlock = nextBlockState.getBlock();

        if (nextBlock instanceof CropBlock) {
            ItemStack hoe = user.getMainHandItem();
            boolean canDestroyCrop = !hasEnchantment(level, Enchantments.FARMERS_KNOWLEDGE, hoe)
                    || nemosFarming_canDestroyCrop(nextBlockState, (Player) user);

            if (canDestroyCrop) {
                nemosFarming_breakBlock(level, nextBlockState, pos, user);
            }
        }
    }

    @Unique
    private boolean nemosFarming_canDestroyCrop(BlockState state, Player player) {
        return player.isCreative() || ((CropBlock) state.getBlock()).isMaxAge(state);
    }

    @Unique
    private void nemosFarming_breakBlock(Level level, BlockState blockState, BlockPos pos, LivingEntity breakingEntity) {
        if (!(blockState.getBlock() instanceof BaseFireBlock)) {
            level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(blockState));
        }

        BlockEntity blockEntity = blockState.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        Block.dropResources(blockState, level, pos, blockEntity, breakingEntity, breakingEntity.getMainHandItem());

        if (!hasEnchantment(level, Enchantments.REPLANTING, breakingEntity.getMainHandItem()) && nemosFarming_setBlockState(level, pos)) {
            level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(breakingEntity, blockState));
        }
    }

    @Unique
    private boolean nemosFarming_setBlockState(Level level, BlockPos pos) {
        FluidState fluidState = level.getFluidState(pos);

        return level.setBlock(pos, fluidState.createLegacyBlock(), Block.UPDATE_ALL, 512);
    }
}
