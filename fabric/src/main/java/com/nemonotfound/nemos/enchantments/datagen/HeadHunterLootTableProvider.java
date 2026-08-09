package com.nemonotfound.nemos.enchantments.datagen;

import net.minecraft.advancements.predicates.entity.EntityPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.FillPlayerHead;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;

import java.util.function.BiConsumer;

import static com.nemonotfound.nemos.enchantments.utils.HeadHunterUtils.HEAD_HUNTER_LOOT_TABLE;

public class HeadHunterLootTableProvider implements LootTableSubProvider {

    private final HolderLookup.Provider registries;

    public HeadHunterLootTableProvider(HolderLookup.Provider registries) {
        this.registries = registries;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
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
