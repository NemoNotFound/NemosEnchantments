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

    public static final RegistryKey<Enchantment> SOUL_BINDING = createRegistryKey("soul_binding");
    public static final RegistryKey<Enchantment> CLIMBER = createRegistryKey("climber");
    public static final RegistryKey<Enchantment> SPRINTER = createRegistryKey("sprinter");
    public static final RegistryKey<Enchantment> FARMERS_KNOWLEDGE = createRegistryKey("farmers_knowledge");
    public static final RegistryKey<Enchantment> REAPER = createRegistryKey("reaper");
    public static final RegistryKey<Enchantment> REPLANTING = createRegistryKey("replanting");

    public static void bootstrap(Registerable<Enchantment> registerable) {
        log.info("Registering enchantments");

        RegistryEntryLookup<Enchantment> enchantmentRegistryEntryLookup = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        RegistryEntryLookup<Item> itemRegistryEntryLookup = registerable.getRegistryLookup(RegistryKeys.ITEM);

        register(
                registerable,
                SOUL_BINDING,
                Enchantment.builder(
                                Enchantment.definition(
                                        itemRegistryEntryLookup.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                                        1,
                                        1,
                                        Enchantment.leveledCost(25, 25),
                                        Enchantment.leveledCost(75, 25),
                                        60,
                                        AttributeModifierSlot.ANY
                                )
                        )
                        .addEffect(EnchantmentEffectComponentTypes.PREVENT_EQUIPMENT_DROP)
        );

        register(
                registerable,
                CLIMBER,
                Enchantment.builder(
                                Enchantment.definition(
                                        itemRegistryEntryLookup.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                                        2,
                                        3,
                                        Enchantment.leveledCost(10, 10),
                                        Enchantment.leveledCost(25, 10),
                                        4,
                                        AttributeModifierSlot.FEET
                                )
                        )
                        .exclusiveSet(enchantmentRegistryEntryLookup.getOrThrow(EnchantmentTags.BOOTS_EXCLUSIVE_SET))
                        .addEffect(
                                EnchantmentEffectComponentTypes.ATTRIBUTES,
                                new AttributeEnchantmentEffect(
                                        Identifier.of(MOD_ID, "enchantment.climber"),
                                        EntityAttributes.CLIMBING_EFFICIENCY,
                                        EnchantmentLevelBasedValue.linear(0.05F),
                                        EntityAttributeModifier.Operation.ADD_VALUE
                                )
                        )
        );

        register(
                registerable,
                SPRINTER,
                Enchantment.builder(
                                Enchantment.definition(
                                        itemRegistryEntryLookup.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                                        2,
                                        3,
                                        Enchantment.leveledCost(10, 10),
                                        Enchantment.leveledCost(25, 10),
                                        4,
                                        AttributeModifierSlot.FEET
                                )
                        )
                        .exclusiveSet(enchantmentRegistryEntryLookup.getOrThrow(EnchantmentTags.BOOTS_EXCLUSIVE_SET))
                        .addEffect(
                                EnchantmentEffectComponentTypes.ATTRIBUTES,
                                new AttributeEnchantmentEffect(
                                        Identifier.of(MOD_ID, "enchantment.sprinter"),
                                        net.minecraft.entity.attribute.EntityAttributes.MOVEMENT_SPEED,
                                        EnchantmentLevelBasedValue.linear(0.02F),
                                        EntityAttributeModifier.Operation.ADD_VALUE
                                )
                        )
        );

        register(
                registerable,
                FARMERS_KNOWLEDGE,
                Enchantment.builder(
                        Enchantment.definition(
                                itemRegistryEntryLookup.getOrThrow(ItemTags.HOES),
                                1,
                                1,
                                Enchantment.constantCost(15),
                                Enchantment.constantCost(65),
                                8,
                                AttributeModifierSlot.MAINHAND
                        )
                )
        );

        register(
                registerable,
                REPLANTING,
                Enchantment.builder(
                        Enchantment.definition(
                                itemRegistryEntryLookup.getOrThrow(ItemTags.HOES),
                                1,
                                1,
                                Enchantment.constantCost(15),
                                Enchantment.constantCost(65),
                                8,
                                AttributeModifierSlot.MAINHAND
                        )
                )
        );

        register(
                registerable,
                REAPER,
                Enchantment.builder(
                        Enchantment.definition(
                                itemRegistryEntryLookup.getOrThrow(ItemTags.HOES),
                                2,
                                3,
                                Enchantment.leveledCost(15, 9),
                                Enchantment.leveledCost(65, 9),
                                4,
                                AttributeModifierSlot.MAINHAND
                        )
                )
        );
    }

    private static RegistryKey<Enchantment> createRegistryKey(String id) {
        return RegistryKey.of(RegistryKeys.ENCHANTMENT, Identifier.of(MOD_ID, id));
    }

    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }
}
