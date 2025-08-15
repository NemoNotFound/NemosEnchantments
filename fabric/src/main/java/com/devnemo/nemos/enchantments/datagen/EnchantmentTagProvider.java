package com.devnemo.nemos.enchantments.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.concurrent.CompletableFuture;

import static com.devnemo.nemos.enchantments.enchantment.NemosEnchantments.*;

public class EnchantmentTagProvider extends FabricTagProvider<Enchantment> {

    public EnchantmentTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ENCHANTMENT, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        getOrCreateTagBuilder(EnchantmentTags.IN_ENCHANTING_TABLE)
                .add(CLIMBER)
                .add(SPRINTER)
                .add(FARMERS_KNOWLEDGE)
                .add(REAPER)
                .add(REPLANTING)
                .add(SNOW_WALKER)
                .add(COLLECTOR);

        getOrCreateTagBuilder(EnchantmentTags.ON_RANDOM_LOOT)
                .add(SOUL_BINDING)
                .add(MAGMA_WALKER)
                .add(SNOW_WALKER)
                .add(COLLECTOR);

        getOrCreateTagBuilder(EnchantmentTags.NON_TREASURE)
                .add(CLIMBER)
                .add(SPRINTER)
                .add(FARMERS_KNOWLEDGE)
                .add(REAPER)
                .add(REPLANTING)
                .add(SNOW_WALKER)
                .add(COLLECTOR);

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
                .add(MAGMA_WALKER)
                .add(SNOW_WALKER)
                .add(COLLECTOR);
    }
}
