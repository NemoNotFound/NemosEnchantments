package com.nemonotfound.nemos.enchantments.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.nemonotfound.nemos.enchantments.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static com.nemonotfound.nemos.enchantments.utils.EnchantmentUtils.hasEnchantment;

@Mixin(MobEntity.class)
public abstract class MobEntityMixin extends Entity {

    public MobEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyExpressionValue(method = "dropEquipment*", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;hasAnyEnchantmentsWith(Lnet/minecraft/item/ItemStack;Lnet/minecraft/component/ComponentType;)Z"))
    private boolean shouldNotDrop(boolean original, @Local ItemStack itemStack) {
        return original && !hasEnchantment(this.getWorld(), Enchantments.SOUL_BINDING, itemStack);
    }

}
