package com.devnemo.nemos.enchantments.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerEntityMixin extends Player {

    @Shadow public abstract @NotNull Level level();

    public ServerPlayerEntityMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(method = "restoreFrom", at = @At(value = "TAIL"))
    private void restoreFrom(ServerPlayer oldPlayer, boolean alive, CallbackInfo ci) {
        var serverLevel = (ServerLevel) this.level();

        if (!alive && !(serverLevel.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) || oldPlayer.isSpectator())) {
            this.getInventory().replaceWith(oldPlayer.getInventory());
        }
    }
}
