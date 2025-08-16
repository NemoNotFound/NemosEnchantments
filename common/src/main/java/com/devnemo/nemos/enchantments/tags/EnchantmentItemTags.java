package com.devnemo.nemos.enchantments.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.devnemo.nemos.enchantments.Constants.MOD_ID;

public class EnchantmentItemTags {

    public static final TagKey<Item> WISDOM_ENCHANTABLE = bind("enchantable/wisdom");

    private static TagKey<Item> bind(String name) {
        return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(MOD_ID, name));
    }
}
