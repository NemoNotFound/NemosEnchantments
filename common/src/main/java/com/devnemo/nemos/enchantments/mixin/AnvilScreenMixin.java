package com.devnemo.nemos.enchantments.mixin;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {

	@Expression("? >= 40")
	@ModifyExpressionValue(method = "renderLabels", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean removeTooExpensive(boolean original) {
		return false;
	}
}