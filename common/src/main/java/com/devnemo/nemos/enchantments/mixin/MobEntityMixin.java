package com.devnemo.nemos.enchantments.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.devnemo.nemos.enchantments.enchantment.ModEnchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.devnemo.nemos.enchantments.utils.EnchantmentUtils.hasEnchantment;

@Mixin(Mob.class)
public abstract class MobEntityMixin extends Entity {

    public MobEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @ModifyExpressionValue(method = "dropCustomDeathLoot*", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;has(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/component/DataComponentType;)Z"))
    private boolean shouldNotDrop(boolean original, @Local ItemStack itemStack) {
        return original && !hasEnchantment(this.level(), ModEnchantments.SOUL_BINDING, itemStack);
    }

}
