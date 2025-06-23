package com.nemonotfound.nemos.enchantments.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.EnchantmentTags;

import java.util.concurrent.CompletableFuture;

import static com.nemonotfound.nemos.enchantments.enchantment.ModEnchantments.*;

public class EnchantmentTagProvider extends FabricTagProvider.EnchantmentTagProvider {

    public EnchantmentTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(EnchantmentTags.IN_ENCHANTING_TABLE)
                .add(CLIMBER)
                .add(SPRINTER)
                .add(FARMERS_KNOWLEDGE)
                .add(REAPER)
                .add(REPLANTING);

        getOrCreateTagBuilder(EnchantmentTags.ON_RANDOM_LOOT)
                .add(SOUL_BINDING)
                .add(MAGMA_WALKER);

        getOrCreateTagBuilder(EnchantmentTags.NON_TREASURE)
                .add(CLIMBER)
                .add(SPRINTER)
                .add(FARMERS_KNOWLEDGE)
                .add(REAPER)
                .add(REPLANTING);

        getOrCreateTagBuilder(EnchantmentTags.TREASURE)
                .add(SOUL_BINDING)
                .add(MAGMA_WALKER);

        getOrCreateTagBuilder(EnchantmentTags.TRADEABLE)
                .add(SOUL_BINDING)
                .add(CLIMBER)
                .add(SPRINTER)
                .add(FARMERS_KNOWLEDGE)
                .add(REAPER)
                .add(REPLANTING)
                .add(MAGMA_WALKER);

        getOrCreateTagBuilder(EnchantmentTags.BOOTS_EXCLUSIVE)
                .add(MAGMA_WALKER);
    }
}
