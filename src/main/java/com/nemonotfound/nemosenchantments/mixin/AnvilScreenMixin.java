package com.nemonotfound.nemosenchantments.mixin;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {

	@Expression("? >= 40")
	@ModifyExpressionValue(method = "drawForeground", at = @At("MIXINEXTRAS:EXPRESSION"))
	private boolean removeTooExpensive(boolean original) {
		return false;
	}
}