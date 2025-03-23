package com.nemonotfound.nemos.enchantments.enchantment;

import com.nemonotfound.nemos.enchantments.entity.attribute.EntityAttributes;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.AttributeEnchantmentEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;

import static com.nemonotfound.nemos.enchantments.NemosEnchantments.MOD_ID;
import static com.nemonotfound.nemos.enchantments.NemosEnchantments.log;

public class Enchantments {

    public static final RegistryKey<Enchantment> SOUL_BINDING = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "soul_binding"));
    public static final RegistryKey<Enchantment> CLIMBER = RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, "climber"));

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

        register(registerable, CLIMBER, Enchantment.builder(Enchantment.definition(
                        itemRegistryEntryLookup.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                        2,
                        3,
                        Enchantment.leveledCost(10, 10),
                        Enchantment.leveledCost(25, 10),
                        60,
                        AttributeModifierSlot.FEET))
                .exclusiveSet(enchantmentRegistryEntryLookup.getOrThrow(EnchantmentTags.BOOTS_EXCLUSIVE_SET))
                .addEffect(
                        EnchantmentEffectComponentTypes.ATTRIBUTES,
                        new AttributeEnchantmentEffect(
                                Identifier.of(MOD_ID, "enchantment.climber"),
                                EntityAttributes.CLIMBING_EFFICIENCY,
                                EnchantmentLevelBasedValue.linear(0.05F),
                                EntityAttributeModifier.Operation.ADD_VALUE
                        )));
    }

    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }
}
