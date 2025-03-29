package com.nemonotfound.nemos.enchantments.datagen.langdatagen;

import com.nemonotfound.nemos.enchantments.enchantment.Enchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

import static com.nemonotfound.nemos.enchantments.datagen.langdatagen.EnglishLanguageGenerator.getEnchantmentTranslationKey;

public class GermanLanguageGenerator extends FabricLanguageProvider {

    public GermanLanguageGenerator(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, "de_de", registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.SOUL_BINDING), "Seelenbindung");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.CLIMBER), "Kletterer");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.SPRINTER), "Sprinter");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.REAPER), "Mäher");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.FARMERS_KNOWLEDGE), "Wissen des Bauern");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.REPLANTING), "Wiederbepflanzung");
        translationBuilder.add("attribute.name.climbing_efficiency", "Klettereffizienz");
    }
}
