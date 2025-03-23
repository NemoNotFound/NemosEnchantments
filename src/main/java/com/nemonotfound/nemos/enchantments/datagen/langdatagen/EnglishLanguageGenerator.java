package com.nemonotfound.nemos.enchantments.datagen.langdatagen;

import com.nemonotfound.nemos.enchantments.enchantment.Enchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class EnglishLanguageGenerator extends FabricLanguageProvider {

    public EnglishLanguageGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.SOUL_BINDING), "Soul Binding");
    }

    public static String getEnchantmentTranslationKey(RegistryKey<Enchantment> enchantmentRegistryKey) {
        return enchantmentRegistryKey.getValue().toTranslationKey("enchantment");
    }
}
