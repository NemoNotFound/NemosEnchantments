package com.devnemo.nemos.enchantments.mixin;

import com.devnemo.nemos.enchantments.enchantment.NemosEnchantments;
import com.devnemo.nemos.enchantments.utils.EnchantmentUtils;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(Block.class)
//TODO: Replace with enchantment effect
public class NeoForgeBlockMixin {

    @ModifyExpressionValue(method = "dropResources(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;"))
    private static List<ItemStack> handleDrops(List<ItemStack> originalDrops, @Local(argsOnly = true) Level level, @Local(argsOnly = true) ItemStack tool, @Local(argsOnly = true) Entity entity) {
        if (!(level instanceof ServerLevel serverLevel) || !(entity instanceof Player player)) {
            return originalDrops;
        }

        var hasCollectorEnchantment = EnchantmentUtils.hasEnchantment(serverLevel, NemosEnchantments.COLLECTOR, tool);

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
                var pitch = ((serverLevel.random.nextFloat() - serverLevel.random.nextFloat()) * 0.7F + 1.0F) * 2.0F;
                var volume = 0.2F;

                serverLevel.playSound(null, player.getX(), player.getY() + 0.5, player.getZ(),
                        SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, volume, pitch);
            }
        }

        return remainingDrops;
    }
}
