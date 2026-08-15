package com.nemonotfound.nemos.enchantments.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockItem.class)
public class BlockItemMixin {

    @Unique
    private static final CompoundTag NEMOS_ENCHANTMENTS$SOUL_TOUCH_MARKER = nemosEnchantments$createSoulTouchMarker();

    @ModifyExpressionValue(
            method = "updateCustomBlockEntityTag(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BlockEntityType;onlyOpCanSetNbt()Z")
    )
    private static boolean nemosEnchantments$allowSoulTouchSpawnerData(boolean operatorOnly, @Local(argsOnly = true, name = "itemStack") ItemStack stack) {
        return operatorOnly && !nemosEnchantments$isSoulTouchSpawner(stack);
    }

    @ModifyExpressionValue(
            method = "shouldPrintOpWarning(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/player/Player;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BlockEntityType;onlyOpCanSetNbt()Z")
    )
    private boolean nemosEnchantments$hideSoulTouchSpawnerWarning(boolean operatorOnly, @Local(argsOnly = true, name = "stack") ItemStack stack) {
        return operatorOnly && !nemosEnchantments$isSoulTouchSpawner(stack);
    }

    @Unique
    private static boolean nemosEnchantments$isSoulTouchSpawner(ItemStack stack) {
        if (!stack.is(Items.SPAWNER)) {
            return false;
        }

        var customData = stack.get(DataComponents.CUSTOM_DATA);
        return customData != null && customData.matchedBy(NEMOS_ENCHANTMENTS$SOUL_TOUCH_MARKER);
    }

    @Unique
    private static CompoundTag nemosEnchantments$createSoulTouchMarker() {
        CompoundTag marker = new CompoundTag();
        marker.putBoolean("nemos_enchantments:soul_touch", true);
        return marker;
    }
}
