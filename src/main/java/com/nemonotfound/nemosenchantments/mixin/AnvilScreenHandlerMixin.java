package com.nemonotfound.nemosenchantments.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
public class AnvilScreenHandlerMixin {

    @Definition(id = "levelCost", field = "Lnet/minecraft/screen/AnvilScreenHandler;levelCost:Lnet/minecraft/screen/Property;")
    @Definition(id = "get", method = "Lnet/minecraft/screen/Property;get()I")
    @Expression("this.levelCost.get() >= 40")
    @ModifyExpressionValue(method = "updateResult", at = @At(value = "MIXINEXTRAS:EXPRESSION", ordinal = 1))
    private boolean updateResult(boolean original) {
        return false;
    }
}