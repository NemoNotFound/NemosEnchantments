package com.nemonotfound.nemosenchantments.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.nemonotfound.nemosenchantments.enchantment.ModEnchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.nemonotfound.nemosenchantments.utils.EnchantmentUtils.hasEnchantment;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityMixin extends Entity {

    public AbstractHorseEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyExpressionValue(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;hasAnyEnchantmentsWith(Lnet/minecraft/item/ItemStack;Lnet/minecraft/component/ComponentType;)Z"))
    private boolean shouldNotDrop(boolean original, @Local ItemStack itemStack) {
        return original && !hasEnchantment(this.getWorld(), ModEnchantments.SOUL_BINDING, itemStack);
    }
}
