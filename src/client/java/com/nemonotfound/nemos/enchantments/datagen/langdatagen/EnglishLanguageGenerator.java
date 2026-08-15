package com.nemonotfound.nemos.enchantments.datagen.langdatagen;

import com.nemonotfound.nemos.enchantments.enchantment.Enchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class EnglishLanguageGenerator extends FabricLanguageProvider {

    public EnglishLanguageGenerator(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    public void generateTranslations(HolderLookup.@NotNull Provider provider, TranslationBuilder translationBuilder) {
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.SOUL_BINDING), "Soul Binding");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.CLIMBER), "Climber");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.SPRINTER), "Sprinter");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.REAPER), "Reaper");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.FARMERS_KNOWLEDGE), "Farmer's Knowledge");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.REPLANTING), "Replanting");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.MAGMA_WALKER), "Magma Walker");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.SNOW_WALKER), "Snow Walker");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.COLLECTOR), "Collector");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.WISDOM), "Wisdom");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.FELLING), "Felling");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.HEAD_HUNTER), "Head-Hunter");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.SOUL_TOUCH), "Soul Touch");
        translationBuilder.add("attribute.name.climbing_efficiency", "Climbing Efficiency");
    }

    public static String getEnchantmentTranslationKey(ResourceKey<Enchantment> enchantmentRegistryKey) {
        return enchantmentRegistryKey.identifier().toLanguageKey("enchantment");
    }
}
