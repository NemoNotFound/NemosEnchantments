package com.nemonotfound.nemos.enchantments.datagen.langdatagen;

import com.nemonotfound.nemos.enchantments.enchantment.Enchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

import static com.nemonotfound.nemos.enchantments.datagen.langdatagen.EnglishLanguageGenerator.getEnchantmentTranslationKey;

public class GermanLanguageGenerator extends FabricLanguageProvider {

    public GermanLanguageGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, "de_de", completableFuture);
    }

    @Override
    public void generateTranslations(HolderLookup.@NonNull Provider registryLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.SOUL_BINDING), "Seelenbindung");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.CLIMBER), "Kletterer");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.SPRINTER), "Sprinter");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.REAPER), "Mäher");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.FARMERS_KNOWLEDGE), "Wissen des Bauern");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.REPLANTING), "Wiederbepflanzung");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.MAGMA_WALKER), "Magmaläufer");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.SNOW_WALKER), "Schneeläufer");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.COLLECTOR), "Sammler");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.WISDOM), "Weisheit");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.FELLING), "Baumfäller");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.HEAD_HUNTER), "Kopfjäger");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.SOUL_TOUCH), "Seelenbehutsamkeit");
        translationBuilder.add("attribute.name.climbing_efficiency", "Klettereffizienz");
    }
}
