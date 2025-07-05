package com.devnemo.nemos.enchantments.enchantment;

import com.devnemo.nemos.enchantments.Constants;
import com.devnemo.nemos.enchantments.entity.attribute.ModAttributes;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.DamageImmunity;
import net.minecraft.world.item.enchantment.effects.EnchantmentAttributeEffect;
import net.minecraft.world.item.enchantment.effects.ReplaceDisk;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.AllOfCondition;
import net.minecraft.world.level.storage.loot.predicates.DamageSourceCondition;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

import java.util.Optional;

import static com.devnemo.nemos.enchantments.Constants.MOD_ID;

public class ModEnchantments {

    public static final ResourceKey<Enchantment> SOUL_BINDING = createResourceKey("soul_binding");
    public static final ResourceKey<Enchantment> CLIMBER = createResourceKey("climber");
    public static final ResourceKey<Enchantment> SPRINTER = createResourceKey("sprinter");
    public static final ResourceKey<Enchantment> FARMERS_KNOWLEDGE = createResourceKey("farmers_knowledge");
    public static final ResourceKey<Enchantment> REAPER = createResourceKey("reaper");
    public static final ResourceKey<Enchantment> REPLANTING = createResourceKey("replanting");
    public static final ResourceKey<Enchantment> MAGMA_WALKER = createResourceKey("magma_walker");

    public static void bootstrap(BootstrapContext<Enchantment> context) {
        Constants.LOG.info("Registering enchantments");

        var enchantmentLookup = context.lookup(Registries.ENCHANTMENT);
        var itemLookup = context.lookup(Registries.ITEM);

        register(
                context,
                SOUL_BINDING,
                Enchantment.enchantment(
                                Enchantment.definition(
                                        itemLookup.getOrThrow(ItemTags.DURABILITY_ENCHANTABLE),
                                        1,
                                        1,
                                        Enchantment.dynamicCost(25, 25),
                                        Enchantment.dynamicCost(75, 25),
                                        60,
                                        EquipmentSlotGroup.ANY
                                )
                        )
                        .exclusiveWith(enchantmentLookup.getOrThrow(EnchantmentTags.CURSE))
                        .withEffect(EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)
        );

        register(
                context,
                CLIMBER,
                Enchantment.enchantment(
                                Enchantment.definition(
                                        itemLookup.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                                        2,
                                        3,
                                        Enchantment.dynamicCost(10, 10),
                                        Enchantment.dynamicCost(25, 10),
                                        4,
                                        EquipmentSlotGroup.FEET
                                )
                        )
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
                context,
                SPRINTER,
                Enchantment.enchantment(
                                Enchantment.definition(
                                        itemLookup.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                                        2,
                                        3,
                                        Enchantment.dynamicCost(10, 10),
                                        Enchantment.dynamicCost(25, 10),
                                        4,
                                        EquipmentSlotGroup.FEET
                                )
                        )
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
                context,
                FARMERS_KNOWLEDGE,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemLookup.getOrThrow(ItemTags.HOES),
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
                context,
                REPLANTING,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemLookup.getOrThrow(ItemTags.HOES),
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
                context,
                REAPER,
                Enchantment.enchantment(
                        Enchantment.definition(
                                itemLookup.getOrThrow(ItemTags.HOES),
                                2,
                                3,
                                Enchantment.dynamicCost(15, 9),
                                Enchantment.dynamicCost(65, 9),
                                4,
                                EquipmentSlotGroup.MAINHAND
                        )
                )
        );

        register(
                context,
                MAGMA_WALKER,
                Enchantment.enchantment(
                                Enchantment.definition(
                                        itemLookup.getOrThrow(ItemTags.FOOT_ARMOR_ENCHANTABLE),
                                        2,
                                        1,
                                        Enchantment.dynamicCost(10, 10),
                                        Enchantment.dynamicCost(25, 10),
                                        4,
                                        EquipmentSlotGroup.FEET
                                )
                        )
                        .exclusiveWith(enchantmentLookup.getOrThrow(EnchantmentTags.BOOTS_EXCLUSIVE))
                        .withEffect(
                                EnchantmentEffectComponents.DAMAGE_IMMUNITY,
                                DamageImmunity.INSTANCE,
                                DamageSourceCondition.hasDamageSource(
                                        DamageSourcePredicate.Builder.damageType()
                                                .tag(TagPredicate.is(DamageTypeTags.BURN_FROM_STEPPING))
                                                .tag(TagPredicate.isNot(DamageTypeTags.BYPASSES_INVULNERABILITY))
                                )
                        )
                        .withEffect(
                                EnchantmentEffectComponents.LOCATION_CHANGED,
                                new ReplaceDisk(
                                        new LevelBasedValue.Clamped(LevelBasedValue.perLevel(3.0F, 1.0F), 0.0F, 16.0F),
                                        LevelBasedValue.constant(1.0F),
                                        new Vec3i(0, -1, 0),
                                        Optional.of(
                                                BlockPredicate.allOf(
                                                        BlockPredicate.matchesTag(new Vec3i(0, 1, 0), BlockTags.AIR),
                                                        BlockPredicate.matchesBlocks(Blocks.LAVA),
                                                        BlockPredicate.matchesFluids(Fluids.LAVA),
                                                        BlockPredicate.unobstructed()
                                                )
                                        ),
                                        BlockStateProvider.simple(Blocks.MAGMA_BLOCK),
                                        Optional.of(GameEvent.BLOCK_PLACE)
                                ),
                                AllOfCondition.allOf(
                                        LootItemEntityPropertyCondition.hasProperties(
                                                LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnGround(true))
                                        ),
                                        InvertedLootItemCondition.invert(
                                                LootItemEntityPropertyCondition.hasProperties(
                                                        LootContext.EntityTarget.THIS, EntityPredicate.Builder.entity().vehicle(EntityPredicate.Builder.entity())
                                                )
                                        )
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
