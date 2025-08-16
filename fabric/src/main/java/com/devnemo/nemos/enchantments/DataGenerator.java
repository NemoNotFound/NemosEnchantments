package com.devnemo.nemos.enchantments;

import com.devnemo.nemos.enchantments.datagen.EnchantmentTagProvider;
import com.devnemo.nemos.enchantments.datagen.ItemTagProvider;
import com.devnemo.nemos.enchantments.datagen.RegistryProvider;
import com.devnemo.nemos.enchantments.datagen.langdatagen.EnglishLanguageGenerator;
import com.devnemo.nemos.enchantments.datagen.langdatagen.GermanLanguageGenerator;
import com.devnemo.nemos.enchantments.enchantment.NemosEnchantments;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

public class DataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(RegistryProvider::new);
        pack.addProvider(EnchantmentTagProvider::new);
        pack.addProvider(EnglishLanguageGenerator::new);
        pack.addProvider(GermanLanguageGenerator::new);
        pack.addProvider(ItemTagProvider::new);
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.ENCHANTMENT, NemosEnchantments::bootstrap);
    }
}
