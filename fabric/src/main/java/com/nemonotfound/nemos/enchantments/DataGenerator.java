package com.nemonotfound.nemos.enchantments;

import com.nemonotfound.nemos.enchantments.datagen.EnchantmentTagProvider;
import com.nemonotfound.nemos.enchantments.datagen.RegistryProvider;
import com.nemonotfound.nemos.enchantments.datagen.langdatagen.EnglishLanguageGenerator;
import com.nemonotfound.nemos.enchantments.datagen.langdatagen.GermanLanguageGenerator;
import com.nemonotfound.nemos.enchantments.enchantment.ModEnchantments;
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
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.ENCHANTMENT, ModEnchantments::bootstrap);
    }
}
