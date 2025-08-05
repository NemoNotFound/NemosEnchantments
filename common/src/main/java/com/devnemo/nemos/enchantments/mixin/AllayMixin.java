package com.devnemo.nemos.enchantments.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.devnemo.nemos.enchantments.enchantment.NemosEnchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.devnemo.nemos.enchantments.utils.EnchantmentUtils.hasEnchantment;

@Mixin(Allay.class)
public abstract class AllayMixin extends Entity {

    public AllayMixin(net.minecraft.world.entity.EntityType<?> type, Level level) {
        super(type, level);
    }

    @ModifyExpressionValue(method = "dropEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;has(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/component/DataComponentType;)Z"))
    private boolean shouldNotDrop(boolean original, @Local net.minecraft.world.item.ItemStack itemStack) {
        return original && !hasEnchantment(this.level(), NemosEnchantments.SOUL_BINDING, itemStack);
    }
}
