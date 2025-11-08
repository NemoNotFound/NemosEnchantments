package com.devnemo.nemos.enchantments.mixin;

import com.devnemo.nemos.enchantments.enchantment.NemosEnchantments;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

import static com.devnemo.nemos.enchantments.utils.EnchantmentUtils.hasEnchantment;

@Mixin(Block.class)
//TODO: Replace with enchantment effect
public class BlockMixin {

    @ModifyReturnValue(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;", at = @At("RETURN"))
    private static List<ItemStack> getDrops(
            List<ItemStack> original,
            BlockState blockState,
            ServerLevel serverLevel,
            BlockPos blockPos,
            BlockEntity blockEntity,
            Entity entity,
            ItemStack itemStack
    ) {
        Block block = blockState.getBlock();

        if (block instanceof CropBlock && itemStack.is(ItemTags.HOES) && hasEnchantment(serverLevel, NemosEnchantments.REPLANTING, itemStack)) {
            nemosFarming_replantCrops(serverLevel, blockPos, blockState, block, original);
        }

        return original;
    }

    //TODO: Put replanting logic in cropBlock
    @Unique
    private static void nemosFarming_replantCrops(ServerLevel serverLevel, BlockPos pos, BlockState state, Block block, List<ItemStack> original) {
        serverLevel.setBlock(pos, state.setValue(((CropBlock) block).getAgeProperty(), 1), Block.UPDATE_ALL, 512);

        for (ItemStack itemStack : original) {
            if (itemStack.is(ItemTags.VILLAGER_PLANTABLE_SEEDS)) {
                itemStack.setCount(itemStack.getCount() - 1);

                break;
            }
        }
    }
}
