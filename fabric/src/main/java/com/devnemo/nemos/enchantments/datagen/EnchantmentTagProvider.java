package com.devnemo.nemos.enchantments.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.concurrent.CompletableFuture;

import static com.devnemo.nemos.enchantments.enchantment.ModEnchantments.*;

public class EnchantmentTagProvider extends FabricTagProvider<Enchantment> {

    public EnchantmentTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.ENCHANTMENT, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        builder(EnchantmentTags.IN_ENCHANTING_TABLE)
                .add(CLIMBER)
                .add(SPRINTER)
                .add(FARMERS_KNOWLEDGE)
                .add(REAPER)
                .add(REPLANTING);

        builder(EnchantmentTags.ON_RANDOM_LOOT)
                .add(SOUL_BINDING)
                .add(MAGMA_WALKER);

        builder(EnchantmentTags.NON_TREASURE)
                .add(CLIMBER)
                .add(SPRINTER)
                .add(FARMERS_KNOWLEDGE)
                .add(REAPER)
                .add(REPLANTING);

        builder(EnchantmentTags.TREASURE)
                .add(SOUL_BINDING)
                .add(MAGMA_WALKER);

        builder(EnchantmentTags.TRADEABLE)
                .add(SOUL_BINDING)
                .add(CLIMBER)
                .add(SPRINTER)
                .add(FARMERS_KNOWLEDGE)
                .add(REAPER)
                .add(REPLANTING)
                .add(MAGMA_WALKER);

        builder(EnchantmentTags.BOOTS_EXCLUSIVE)
                .add(MAGMA_WALKER);
    }
}
