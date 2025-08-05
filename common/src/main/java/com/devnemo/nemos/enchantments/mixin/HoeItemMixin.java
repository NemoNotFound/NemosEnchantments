package com.devnemo.nemos.enchantments.mixin;

import com.devnemo.nemos.enchantments.enchantment.NemosEnchantments;
import com.devnemo.nemos.enchantments.utils.EnchantmentUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import static com.devnemo.nemos.enchantments.utils.EnchantmentUtils.hasEnchantment;

@Mixin(HoeItem.class)
//TODO: Replace with enchantment effect
public class HoeItemMixin extends Item {

    public HoeItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canDestroyBlock(@NotNull ItemStack itemStack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull LivingEntity user) {
        if (!(user instanceof Player)) {
            return super.canDestroyBlock(itemStack, state, level, pos, user);
        }

        ItemStack hoe = user.getMainHandItem();
        Block block = state.getBlock();
        boolean isBlockCropBlock = block instanceof CropBlock;

        if (isBlockCropBlock) {
            boolean hasFarmersKnowledge = hasEnchantment(level, NemosEnchantments.FARMERS_KNOWLEDGE, hoe);
            boolean isCropRipe = ((CropBlock) block).isMaxAge(state);

            if (hasFarmersKnowledge) {
                return ((Player) user).isCreative() || isCropRipe;
            }
        }

        return true;
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack stack, @NotNull Level level, BlockState state, @NotNull BlockPos pos, LivingEntity miner) {
        ItemStack hoe = miner.getMainHandItem();
        boolean isBlockCropBlock = state.getBlock() instanceof CropBlock;
        boolean hasHoeReaperEnchantment = hasEnchantment(level, NemosEnchantments.REAPER, hoe);

        if (isBlockCropBlock && hasHoeReaperEnchantment) {
            int enchantmentLevel = EnchantmentUtils.getEnchantmentLevel(level, NemosEnchantments.REAPER, hoe);
            int breakingRange = 2 * enchantmentLevel + 1;

            for (int i = 0; i < Math.pow(breakingRange, 3); i++) {
                BlockPos nextPos = nemosFarming_getNextBlockPos(pos, i, breakingRange);

                nemosFarming_breakCrop(level, nextPos, miner);
            }
        }

        return super.mineBlock(stack, level, state, pos, miner);
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

        if (nextBlock instanceof CropBlock && canDestroyBlock(user.getMainHandItem(), nextBlockState, level, pos, user)) {
            nemosFarming_breakBlock(level, nextBlockState, pos, user);
        }
    }

    @Unique
    private void nemosFarming_breakBlock(Level level, BlockState blockState, BlockPos pos, LivingEntity breakingEntity) {

        if (!(blockState.getBlock() instanceof BaseFireBlock)) {
            level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, pos, Block.getId(blockState));
        }

        if (!hasEnchantment(level, NemosEnchantments.REPLANTING, breakingEntity.getMainHandItem()) && nemosFarming_setBlockState(level, pos)) {
            level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(breakingEntity, blockState));
        }
    }

    @Unique
    private boolean nemosFarming_setBlockState(Level level, BlockPos pos) {
        FluidState fluidState = level.getFluidState(pos);

        return level.setBlock(pos, fluidState.createLegacyBlock(), Block.UPDATE_ALL, 512);
    }
}
