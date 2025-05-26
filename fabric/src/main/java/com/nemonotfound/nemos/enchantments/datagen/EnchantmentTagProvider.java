package com.nemonotfound.nemos.enchantments.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;

import java.util.concurrent.CompletableFuture;

import static com.nemonotfound.nemos.enchantments.enchantment.ModEnchantments.*;

public class EnchantmentTagProvider extends FabricTagProvider.EnchantmentTagProvider {

    public EnchantmentTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        var enchantmentRegistryWrapper = provider.lookupOrThrow(Registries.ENCHANTMENT);

        getOrCreateTagBuilder(EnchantmentTags.IN_ENCHANTING_TABLE)
                .add(enchantmentRegistryWrapper.getOrThrow(CLIMBER).key())
                .add(enchantmentRegistryWrapper.getOrThrow(SPRINTER).key())
                .add(enchantmentRegistryWrapper.getOrThrow(FARMERS_KNOWLEDGE).key())
                .add(enchantmentRegistryWrapper.getOrThrow(REAPER).key())
                .add(enchantmentRegistryWrapper.getOrThrow(REPLANTING).key());

        getOrCreateTagBuilder(EnchantmentTags.ON_RANDOM_LOOT)
                .add(enchantmentRegistryWrapper.getOrThrow(SOUL_BINDING).key());

        getOrCreateTagBuilder(EnchantmentTags.NON_TREASURE)
                .add(enchantmentRegistryWrapper.getOrThrow(CLIMBER).key())
                .add(enchantmentRegistryWrapper.getOrThrow(SPRINTER).key())
                .add(enchantmentRegistryWrapper.getOrThrow(FARMERS_KNOWLEDGE).key())
                .add(enchantmentRegistryWrapper.getOrThrow(REAPER).key())
                .add(enchantmentRegistryWrapper.getOrThrow(REPLANTING).key());

        getOrCreateTagBuilder(EnchantmentTags.TREASURE)
                .add(enchantmentRegistryWrapper.getOrThrow(SOUL_BINDING).key());

        getOrCreateTagBuilder(EnchantmentTags.TRADEABLE)
                .add(enchantmentRegistryWrapper.getOrThrow(SOUL_BINDING).key())
                .add(enchantmentRegistryWrapper.getOrThrow(CLIMBER).key())
                .add(enchantmentRegistryWrapper.getOrThrow(SPRINTER).key())
                .add(enchantmentRegistryWrapper.getOrThrow(FARMERS_KNOWLEDGE).key())
                .add(enchantmentRegistryWrapper.getOrThrow(REAPER).key())
                .add(enchantmentRegistryWrapper.getOrThrow(REPLANTING).key());
    }
}
