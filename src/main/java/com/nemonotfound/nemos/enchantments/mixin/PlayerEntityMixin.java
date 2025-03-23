package com.nemonotfound.nemos.enchantments.mixin;

import com.nemonotfound.nemos.enchantments.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

import static com.nemonotfound.nemos.enchantments.utils.EnchantmentUtils.hasEnchantment;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity {

    @Shadow @Final
    PlayerInventory inventory;
    @Unique
    private final Map<Integer, ItemStack> itemStackMap = new HashMap<>();

    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "dropInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;vanishCursedItems()V"))
    private void dropInventory(CallbackInfo ci) {
        itemStackMap.clear();

        for (int i = 0; i < this.inventory.size(); i++) {
            ItemStack itemStack = this.inventory.getStack(i);
            if (!itemStack.isEmpty() && hasEnchantment(this.getWorld(), Enchantments.SOUL_BINDING, itemStack)) {
                itemStackMap.put(i, itemStack);
            }
        }
    }

    @Inject(method = "dropInventory", at = @At(value = "TAIL"))
    private void dropInventoryTail(CallbackInfo ci) {
        for (Map.Entry<Integer, ItemStack> entry : itemStackMap.entrySet()) {
            this.inventory.setStack(entry.getKey(), entry.getValue());
        }
    }
}
