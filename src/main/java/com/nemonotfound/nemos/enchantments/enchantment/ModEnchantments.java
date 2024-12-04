package com.nemonotfound.nemos.enchantments.enchantment;

import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

import static com.nemonotfound.nemos.enchantments.NemosEnchantments.MOD_ID;
import static com.nemonotfound.nemos.enchantments.NemosEnchantments.log;

public class ModEnchantments {

    public static final RegistryKey<Enchantment> SOUL_BINDING = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "soul_binding"));

    public static void bootstrap(Registerable<Enchantment> registerable) {
        log.info("Registering enchantments");

        RegistryEntryLookup<Enchantment> enchantmentRegistryEntryLookup = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        RegistryEntryLookup<Item> itemRegistryEntryLookup = registerable.getRegistryLookup(RegistryKeys.ITEM);

        register(registerable, SOUL_BINDING, Enchantment.builder(Enchantment.definition(
                        itemRegistryEntryLookup.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                        1,
                        1,
                        Enchantment.leveledCost(25, 25),
                        Enchantment.leveledCost(75, 25),
                        60,
                        AttributeModifierSlot.ANY))
                .addEffect(EnchantmentEffectComponentTypes.PREVENT_EQUIPMENT_DROP));
    }


    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }
}
