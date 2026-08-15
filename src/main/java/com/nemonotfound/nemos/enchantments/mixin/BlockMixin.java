package com.nemonotfound.nemos.enchantments.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.nemonotfound.nemos.enchantments.enchantment.Enchantments;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.nemonotfound.nemos.enchantments.utils.EnchantmentUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.List;

import static com.nemonotfound.nemos.enchantments.utils.EnchantmentUtils.hasEnchantment;

@Mixin(Block.class)
//TODO: Replace with enchantment effect
public class BlockMixin {

    @ModifyArg(
            method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;spawnAfterBreak(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;Z)V"),
            index = 3
    )
    private static boolean nemosEnchantments$suppressSpawnerExperienceWithSoulTouch(
            boolean dropExperience,
            @Local(argsOnly = true, name = "state") BlockState state,
            @Local(argsOnly = true, name = "level") Level level,
            @Local(argsOnly = true, name = "tool") ItemStack tool
    ) {
        return dropExperience && !(state.is(Blocks.SPAWNER)
                && hasEnchantment(level, Enchantments.SOUL_TOUCH, tool));
    }

    @ModifyReturnValue(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemInstance;)Ljava/util/List;", at = @At("RETURN"))
    private static List<ItemStack> getDrops(
            List<ItemStack> original,
            BlockState blockState,
            ServerLevel serverLevel,
            BlockPos blockPos,
            BlockEntity blockEntity,
            Entity entity,
            ItemInstance tool
    ) {
        Block block = blockState.getBlock();

        if (block instanceof CropBlock && tool.is(ItemTags.HOES) && hasEnchantment(serverLevel, Enchantments.REPLANTING, tool)) {
            nemosFarming_replantCrops(serverLevel, blockPos, block, original);
        }

        return original;
    }

    //TODO: Put replanting logic in cropBlock
    @Unique
    private static void nemosFarming_replantCrops(ServerLevel serverLevel, BlockPos pos, Block block, List<ItemStack> original) {
        serverLevel.setBlock(pos, ((CropBlock) block).getStateForAge(1), Block.UPDATE_ALL, 512);

        for (ItemStack itemStack : original) {
            if (itemStack.is(ItemTags.VILLAGER_PLANTABLE_SEEDS)) {
                itemStack.setCount(itemStack.getCount() - 1);

                break;
            }
        }
    }

    //TODO: Replace with enchantment effect
    @ModifyExpressionValue(method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemInstance;)Ljava/util/List;"))
    private static List<ItemStack> handleDrops(List<ItemStack> originalDrops, @Local(argsOnly = true, name = "level") Level level, @Local(argsOnly = true, name = "tool") ItemStack tool, @Local(argsOnly = true, name = "breaker") Entity entity) {
        if (!(level instanceof ServerLevel serverLevel) || !(entity instanceof Player player)) {
            return originalDrops;
        }

        var hasCollectorEnchantment = EnchantmentUtils.hasEnchantment(serverLevel, Enchantments.COLLECTOR, tool);

        if (!hasCollectorEnchantment) {
            return originalDrops;
        }

        return nemosEnchantments$getDropsAfterCollecting(originalDrops, player, serverLevel);
    }

    @Unique
    private static List<ItemStack> nemosEnchantments$getDropsAfterCollecting(List<ItemStack> originalDrops, Player player, ServerLevel serverLevel) {
        List<ItemStack> remainingDrops = new ArrayList<>();

        for (ItemStack drop : originalDrops) {
            var originalCount = drop.getCount();
            player.getInventory().add(drop);

            if (drop.getCount() > 0) {
                remainingDrops.add(drop);
            }

            if (originalCount != drop.getCount()) {
                var pitch = ((serverLevel.getRandom().nextFloat() - serverLevel.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F;
                serverLevel.playSound(null, player.getX(), player.getY() + 0.5, player.getZ(),
                        SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, pitch);
            }
        }

        return remainingDrops;
    }
}
