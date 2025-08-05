package com.devnemo.nemos.enchantments.mixin;

import com.devnemo.nemos.enchantments.enchantment.NemosEnchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

import static com.devnemo.nemos.enchantments.utils.EnchantmentUtils.hasEnchantment;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends Entity {

    @Shadow @Final
    Inventory inventory;
    @Unique
    private final Map<Integer, ItemStack> nemosEnchantments$itemStackMap = new HashMap<>();

    public PlayerEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "dropEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;destroyVanishingCursedItems()V"))
    private void dropInventory(CallbackInfo ci) {
        nemosEnchantments$itemStackMap.clear();

        for (int i = 0; i < this.inventory.getContainerSize(); i++) {
            ItemStack itemStack = this.inventory.getItem(i);
            if (!itemStack.isEmpty() && hasEnchantment(this.level(), NemosEnchantments.SOUL_BINDING, itemStack)) {
                nemosEnchantments$itemStackMap.put(i, itemStack);
            }
        }
    }

    @Inject(method = "dropEquipment", at = @At(value = "TAIL"))
    private void dropInventoryTail(CallbackInfo ci) {
        for (Map.Entry<Integer, ItemStack> entry : nemosEnchantments$itemStackMap.entrySet()) {
            this.inventory.setItem(entry.getKey(), entry.getValue());
        }
    }
}
