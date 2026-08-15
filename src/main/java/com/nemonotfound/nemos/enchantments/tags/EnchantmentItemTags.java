package com.nemonotfound.nemos.enchantments.tags;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import static com.nemonotfound.nemos.enchantments.NemosEnchantments.MOD_ID;

public class EnchantmentItemTags {

    public static final TagKey<Item> WISDOM_ENCHANTABLE = bind("enchantable/wisdom");
    public static final TagKey<Item> HEAD_HUNTER_ENCHANTABLE = bind("enchantable/head_hunter");

    private static TagKey<Item> bind(String name) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(MOD_ID, name));
    }
}
