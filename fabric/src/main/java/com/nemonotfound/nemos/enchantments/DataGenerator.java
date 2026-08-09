package com.nemonotfound.nemos.enchantments;

import com.nemonotfound.nemos.enchantments.datagen.EnchantmentTagProvider;
import com.nemonotfound.nemos.enchantments.datagen.HeadHunterLootTableProvider;
import com.nemonotfound.nemos.enchantments.datagen.ItemTagProvider;
import com.nemonotfound.nemos.enchantments.datagen.RegistryProvider;
import com.nemonotfound.nemos.enchantments.datagen.langdatagen.EnglishLanguageGenerator;
import com.nemonotfound.nemos.enchantments.datagen.langdatagen.GermanLanguageGenerator;
import com.nemonotfound.nemos.enchantments.enchantment.NemosEnchantments;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

import static com.nemonotfound.nemos.enchantments.utils.HeadHunterUtils.HEAD_HUNTER_LOOT_TABLE;

public class DataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(RegistryProvider::new);
        pack.addProvider(EnchantmentTagProvider::new);
        pack.addProvider(EnglishLanguageGenerator::new);
        pack.addProvider(GermanLanguageGenerator::new);
        pack.addProvider(ItemTagProvider::new);
        pack.addProvider((output, registries) -> new LootTableProvider(
                output,
                Set.of(HEAD_HUNTER_LOOT_TABLE),
                List.of(new LootTableProvider.SubProviderEntry(HeadHunterLootTableProvider::new, LootContextParamSets.ENTITY)),
                registries
        ));
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        registryBuilder.add(Registries.ENCHANTMENT, NemosEnchantments::bootstrap);
    }
}
