package com.nemonotfound.nemos.enchantments.datagen;

import net.minecraft.advancements.predicates.DataComponentMatchers;
import net.minecraft.advancements.predicates.EnchantmentPredicate;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.advancements.predicates.MinMaxBounds;
import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.registries.Registries;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.FillPlayerHead;
import net.minecraft.world.level.storage.loot.functions.SetCustomDataFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static com.nemonotfound.nemos.enchantments.enchantment.NemosEnchantments.SOUL_TOUCH;
import static com.nemonotfound.nemos.enchantments.utils.HeadHunterUtils.HEAD_HUNTER_LOOT_TABLE;

public class EnchantmentLootTableProvider extends LootTableProvider {

    private static final String SOUL_TOUCH_MARKER = "nemos_enchantments:soul_touch";

    public EnchantmentLootTableProvider(
            FabricPackOutput output,
            CompletableFuture<HolderLookup.Provider> registries
    ) {
        super(
                output,
                Set.of(HEAD_HUNTER_LOOT_TABLE, Blocks.SPAWNER.getLootTable().orElseThrow()),
                List.of(new SubProviderEntry(
                        lookup -> consumer -> generate(lookup, consumer),
                        LootContextParamSets.ALL_PARAMS
                )),
                registries
        );
    }

    private static void generate(
            HolderLookup.Provider registries,
            BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output
    ) {
        generateHeadHunterLootTable(registries, output);
        generateSoulTouchLootTable(registries, output);
    }

    private static void generateHeadHunterLootTable(
            HolderLookup.Provider registries,
            BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output
    ) {
        HolderGetter<EntityType<?>> entityTypes = registries.lookupOrThrow(Registries.ENTITY_TYPE);

        output.accept(
                HEAD_HUNTER_LOOT_TABLE,
                LootTable.lootTable().withPool(
                        LootPool.lootPool().add(
                                AlternativesEntry.alternatives(
                                        playerHead(entityTypes),
                                        head(Items.SKELETON_SKULL, entityTypes, EntityTypes.SKELETON),
                                        head(Items.SKELETON_SKULL, entityTypes, EntityTypes.STRAY),
                                        head(Items.SKELETON_SKULL, entityTypes, EntityTypes.BOGGED),
                                        head(Items.ZOMBIE_HEAD, entityTypes, EntityTypes.ZOMBIE),
                                        head(Items.ZOMBIE_HEAD, entityTypes, EntityTypes.HUSK),
                                        head(Items.ZOMBIE_HEAD, entityTypes, EntityTypes.DROWNED),
                                        head(Items.ZOMBIE_HEAD, entityTypes, EntityTypes.ZOMBIE_VILLAGER),
                                        head(Items.ZOMBIE_HEAD, entityTypes, EntityTypes.ZOMBIFIED_PIGLIN),
                                        head(Items.CREEPER_HEAD, entityTypes, EntityTypes.CREEPER),
                                        head(Items.PIGLIN_HEAD, entityTypes, EntityTypes.PIGLIN),
                                        head(Items.PIGLIN_HEAD, entityTypes, EntityTypes.PIGLIN_BRUTE),
                                        head(Items.DRAGON_HEAD, entityTypes, EntityTypes.ENDER_DRAGON)
                                )
                        )
                )
        );
    }

    private static void generateSoulTouchLootTable(
            HolderLookup.Provider registries,
            BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output
    ) {
        var soulTouch = registries.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(SOUL_TOUCH);
        var enchantedWithSoulTouch = ItemPredicate.Builder.item().withComponents(
                DataComponentMatchers.Builder.components()
                        .partial(
                                DataComponentPredicates.ENCHANTMENTS,
                                EnchantmentsPredicate.enchantments(List.of(
                                        new EnchantmentPredicate(soulTouch, MinMaxBounds.Ints.atLeast(1))
                                ))
                        )
                        .build()
        );
        CompoundTag soulTouchMarker = new CompoundTag();
        soulTouchMarker.putBoolean(SOUL_TOUCH_MARKER, true);

        output.accept(
                Blocks.SPAWNER.getLootTable().orElseThrow(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().add(
                                LootItem.lootTableItem(Blocks.SPAWNER)
                                        .when(MatchTool.toolMatches(enchantedWithSoulTouch))
                                        .apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
                                                .include(DataComponents.BLOCK_ENTITY_DATA))
                                        .apply(SetCustomDataFunction.setCustomData(soulTouchMarker))
                        )
                )
        );
    }

    private static LootPoolEntryContainer.Builder<?> playerHead(HolderGetter<EntityType<?>> entityTypes) {
        return LootItem.lootTableItem(Items.PLAYER_HEAD)
                .apply(FillPlayerHead.fillPlayerHead(LootContext.EntityTarget.THIS))
                .when(isEntityType(entityTypes, EntityTypes.PLAYER));
    }

    private static LootPoolEntryContainer.Builder<?> head(
            Item head,
            HolderGetter<EntityType<?>> entityTypes,
            EntityType<?> entityType
    ) {
        return LootItem.lootTableItem(head).when(isEntityType(entityTypes, entityType));
    }

    private static LootItemEntityPropertyCondition.Builder isEntityType(
            HolderGetter<EntityType<?>> entityTypes,
            EntityType<?> entityType
    ) {
        return LootItemEntityPropertyCondition.hasProperties(
                LootContext.EntityTarget.THIS,
                EntityPredicate.Builder.entity().of(entityTypes, entityType)
        );
    }
}
