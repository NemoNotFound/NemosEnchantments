package com.nemonotfound.nemos.enchantments.enchantment;

import com.nemonotfound.nemos.enchantments.Constants;
import com.nemonotfound.nemos.enchantments.entity.attribute.ModAttributes;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;

import static com.nemonotfound.nemos.enchantments.Constants.MOD_ID;

public class ModEnchantments {

    public static final ResourceKey<Enchantment> SOUL_BINDING = createResourceKey("soul_binding");
    public static final ResourceKey<Enchantment> CLIMBER = createResourceKey("climber");
    public static final ResourceKey<Enchantment> SPRINTER = createResourceKey("sprinter");
    public static final ResourceKey<Enchantment> FARMERS_KNOWLEDGE = createResourceKey("farmers_knowledge");
    public static final ResourceKey<Enchantment> REAPER = createResourceKey("reaper");
    public static final ResourceKey<Enchantment> REPLANTING = createResourceKey("replanting");

    public static void bootstrap(BootstrapContext<Enchantment> registerable) {
        Constants.LOG.info("Registering enchantments");

        HolderGetter<Enchantment> enchantmentRegistryEntryLookup = registerable.lookup(Registries.ENCHANTMENT);
        HolderGetter<Item> itemRegistryEntryLookup = registerable.lookup(Registries.ITEM);

        register(
                registerable,
                SOUL_BINDING,
                Enchantment.enchantment(
                                Enchantment.definition(
                                        itemRegistryEntryLookup.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                                        1,
                                        1,
                                        Enchantment.dynamicCost(25, 25),
                                        Enchantment.dynamicCost(75, 25),
                                        60,
                                        EquipmentSlotGroup.ANY
                                )
                        )
                        .withEffect(EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)
        );

        register(
                registerable,
                CLIMBER,
                Enchantment.enchantment(
                                Enchantment.definition(
                                        itemRegistryEntryLookup.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                                        2,
                                        3,
                                        Enchantment.dynamicCost(10, 10),
                                        Enchantment.dynamicCost(25, 10),
                                        4,
                                        EquipmentSlotGroup.FEET
                                )
                        )
                        .exclusiveWith(enchantmentRegistryEntryLookup.getOrThrow(EnchantmentTags.BOOTS_EXCLUSIVE))
                        .withEffect(
                                EnchantmentEffectComponents.ATTRIBUTES,
                                new EnchantmentAttributeEffect(
                                        ResourceLocation.fromNamespaceAndPath(MOD_ID, "enchantment.climber"),
                                        ModAttributes.CLIMBING_EFFICIENCY.get(),
                                        LevelBasedValue.perLevel(0.05F),
                                        AttributeModifier.Operation.ADD_VALUE
                                )
                        )
        );

        register(
                registerable,
                SPRINTER,
                Enchantment.enchantment(
                                Enchantment.definition(
                                        itemRegistryEntryLookup.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                                        2,
                                        3,
                                        Enchantment.dynamicCost(10, 10),
                                        Enchantment.dynamicCost(25, 10),
                                        4,
                                        EquipmentSlotGroup.FEET
                                )
                        )
                        .exclusiveWith(enchantmentRegistryEntryLookup.getOrThrow(EnchantmentTags.BOOTS_EXCLUSIVE))
                        .withEffect(
                                EnchantmentEffectComponents.ATTRIBUTES,
                                new EnchantmentAttributeEffect(
                                        ResourceLocation.fromNamespaceAndPath(MOD_ID, "enchantment.sprinter"),
                                        Attributes.MOVEMENT_SPEED,
                                        LevelBasedValue.perLevel(0.02F),
                                        AttributeModifier.Operation.ADD_VALUE
                                )
                        )
        );

        register(
                registerable,
                FARMERS_KNOWLEDGE,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemRegistryEntryLookup.getOrThrow(ItemTags.HOES),
                                1,
                                1,
                                Enchantment.constantCost(15),
                                Enchantment.constantCost(65),
                                8,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );

        register(
                registerable,
                REPLANTING,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemRegistryEntryLookup.getOrThrow(ItemTags.HOES),
                                1,
                                1,
                                Enchantment.constantCost(15),
                                Enchantment.constantCost(65),
                                8,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );

        register(
                registerable,
                REAPER,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemRegistryEntryLookup.getOrThrow(ItemTags.HOES),
                                2,
                                3,
                                Enchantment.dynamicCost(15, 9),
                                Enchantment.dynamicCost(65, 9),
                                4,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );
    }

    private static ResourceKey<Enchantment> createResourceKey(String id) {
        return ResourceKey.create(Registries.ENCHANTMENT, ResourceLocation.fromNamespaceAndPath(MOD_ID, id));
    }

    private static void register(BootstrapContext<Enchantment> registry, ResourceKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.location()));
    }
}
