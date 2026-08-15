package com.nemonotfound.nemos.enchantments.mixin;

import com.nemonotfound.nemos.enchantments.enchantment.Enchantments;
import com.nemonotfound.nemos.enchantments.utils.EnchantmentUtils;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.PowderSnowBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {

    @ModifyReturnValue(method = "canEntityWalkOnPowderSnow", at = @At("RETURN"))
    private static boolean canEntityWalkOnPowderSnow(boolean original, @Local(argsOnly = true, name = "entity") Entity entity) {
        if (original) {
            return true;
        }

        return entity instanceof LivingEntity && EnchantmentUtils.hasEnchantment(entity.level(), Enchantments.SNOW_WALKER, ((LivingEntity) entity).getItemBySlot(EquipmentSlot.FEET));
    }
}
