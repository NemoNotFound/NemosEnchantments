package com.devnemo.nemos.enchantments.utils;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

public class EnchantmentUtils {

    private EnchantmentUtils() {

    }

    public static boolean hasEnchantment(Level level, ResourceKey<Enchantment> enchantment, ItemStack itemStack) {
        return EnchantmentHelper.getItemEnchantmentLevel(getEnchantmentRegistryEntry(level, enchantment), itemStack) > 0;
    }

    public static int getEnchantmentLevel(Level level, ResourceKey<Enchantment> enchantment, ItemStack itemStack) {
        return EnchantmentHelper.getItemEnchantmentLevel(getEnchantmentRegistryEntry(level, enchantment), itemStack);
    }

    private static Holder<Enchantment> getEnchantmentRegistryEntry(Level level, ResourceKey<Enchantment> enchantmentRegistryKey) {
        return level.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .getOrThrow(enchantmentRegistryKey);
    }
}
