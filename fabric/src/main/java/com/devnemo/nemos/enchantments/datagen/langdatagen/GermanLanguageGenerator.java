package com.devnemo.nemos.enchantments.datagen.langdatagen;

import com.devnemo.nemos.enchantments.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

import static com.devnemo.nemos.enchantments.datagen.langdatagen.EnglishLanguageGenerator.getEnchantmentTranslationKey;

public class GermanLanguageGenerator extends FabricLanguageProvider {

    public GermanLanguageGenerator(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(dataOutput, "de_de", completableFuture);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(getEnchantmentTranslationKey(ModEnchantments.SOUL_BINDING), "Seelenbindung");
        translationBuilder.add(getEnchantmentTranslationKey(ModEnchantments.CLIMBER), "Kletterer");
        translationBuilder.add(getEnchantmentTranslationKey(ModEnchantments.SPRINTER), "Sprinter");
        translationBuilder.add(getEnchantmentTranslationKey(ModEnchantments.REAPER), "Mäher");
        translationBuilder.add(getEnchantmentTranslationKey(ModEnchantments.FARMERS_KNOWLEDGE), "Wissen des Bauern");
        translationBuilder.add(getEnchantmentTranslationKey(ModEnchantments.REPLANTING), "Wiederbepflanzung");
        translationBuilder.add(getEnchantmentTranslationKey(ModEnchantments.MAGMA_WALKER), "Magmaläufer");
        translationBuilder.add("attribute.name.climbing_efficiency", "Klettereffizienz");
    }
}
