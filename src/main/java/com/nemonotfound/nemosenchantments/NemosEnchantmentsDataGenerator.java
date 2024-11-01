package com.nemonotfound.nemosenchantments;

import com.nemonotfound.nemosenchantments.datagen.EnchantmentTagProvider;
import com.nemonotfound.nemosenchantments.datagen.RegistryProvider;
import com.nemonotfound.nemosenchantments.datagen.langdatagen.EnglishLanguageGenerator;
import com.nemonotfound.nemosenchantments.datagen.langdatagen.GermanLanguageGenerator;
import com.nemonotfound.nemosenchantments.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class NemosEnchantmentsDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(RegistryProvider::new);
		pack.addProvider(EnchantmentTagProvider::new);
		pack.addProvider(EnglishLanguageGenerator::new);
		pack.addProvider(GermanLanguageGenerator::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT, ModEnchantments::bootstrap);
	}
}
