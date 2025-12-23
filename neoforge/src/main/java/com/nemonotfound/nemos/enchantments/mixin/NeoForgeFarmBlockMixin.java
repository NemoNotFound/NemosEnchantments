package com.nemonotfound.nemos.enchantments.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.nemonotfound.nemos.enchantments.utils.EnchantmentUtils;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FarmBlock.class)
public class NeoForgeFarmBlockMixin {

    @ModifyExpressionValue(method = "fallOn", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/common/CommonHooks;onFarmlandTrample(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;DLnet/minecraft/world/entity/Entity;)Z"))
    private boolean fallOn(boolean original, @Local(argsOnly = true) Entity entity, @Local(argsOnly = true) Level level) {
        if (original && entity instanceof Player player) {
            ItemStack boots = player.getItemBySlot(EquipmentSlot.FEET);

            if (boots.is(ItemTags.FOOT_ARMOR)) {
                return !EnchantmentUtils.hasEnchantment(level, Enchantments.FEATHER_FALLING, boots);
            }
        }

        return original;
    }
}