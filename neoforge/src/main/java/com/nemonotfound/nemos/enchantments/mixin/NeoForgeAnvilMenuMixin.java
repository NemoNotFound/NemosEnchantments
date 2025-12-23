package com.nemonotfound.nemos.enchantments.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilMenu.class)
public class NeoForgeAnvilMenuMixin {

    @Definition(id = "cost", field = "Lnet/minecraft/world/inventory/AnvilMenu;cost:Lnet/minecraft/world/inventory/DataSlot;")
    @Definition(id = "get", method = "Lnet/minecraft/world/inventory/DataSlot;get()I")
    @Expression("this.cost.get() >= 40")
    @ModifyExpressionValue(method = "createResultInternal", at = @At(value = "MIXINEXTRAS:EXPRESSION"))
    private boolean updateResult(boolean original) {
        return false;
    }
}