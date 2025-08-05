package com.devnemo.nemos.enchantments.datagen.langdatagen;

import com.devnemo.nemos.enchantments.enchantment.NemosEnchantments;
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
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.SOUL_BINDING), "Seelenbindung");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.CLIMBER), "Kletterer");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.SPRINTER), "Sprinter");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.REAPER), "Mäher");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.FARMERS_KNOWLEDGE), "Wissen des Bauern");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.REPLANTING), "Wiederbepflanzung");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.MAGMA_WALKER), "Magmaläufer");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.SNOW_WALKER), "Schneeläufer");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.COLLECTOR), "Sammler");
        translationBuilder.add("attribute.name.climbing_efficiency", "Klettereffizienz");
    }
}
