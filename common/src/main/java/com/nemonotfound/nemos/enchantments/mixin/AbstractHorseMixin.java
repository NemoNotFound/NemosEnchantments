package com.nemonotfound.nemos.enchantments.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.nemonotfound.nemos.enchantments.enchantment.ModEnchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.nemonotfound.nemos.enchantments.utils.EnchantmentUtils.hasEnchantment;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin extends Entity {

    public AbstractHorseMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @ModifyExpressionValue(method = "dropEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;has(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/component/DataComponentType;)Z"))
    private boolean shouldNotDrop(boolean original, @Local ItemStack itemStack) {
        return original && !hasEnchantment(this.level(), ModEnchantments.SOUL_BINDING, itemStack);
    }
}
