package com.nemonotfound.nemos.enchantments.datagen.langdatagen;

import com.nemonotfound.nemos.enchantments.enchantment.ModEnchantments;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.concurrent.CompletableFuture;

public class EnglishLanguageGenerator extends FabricLanguageProvider {

    public EnglishLanguageGenerator(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> completableFuture) {
        super(dataOutput, completableFuture);
    }

    @Override
    public void generateTranslations(HolderLookup.Provider provider, TranslationBuilder translationBuilder) {
        translationBuilder.add(getEnchantmentTranslationKey(ModEnchantments.SOUL_BINDING), "Soul Binding");
        translationBuilder.add(getEnchantmentTranslationKey(ModEnchantments.CLIMBER), "Climber");
        translationBuilder.add(getEnchantmentTranslationKey(ModEnchantments.SPRINTER), "Sprinter");
        translationBuilder.add(getEnchantmentTranslationKey(ModEnchantments.REAPER), "Reaper");
        translationBuilder.add(getEnchantmentTranslationKey(ModEnchantments.FARMERS_KNOWLEDGE), "Farmer's Knowledge");
        translationBuilder.add(getEnchantmentTranslationKey(ModEnchantments.REPLANTING), "Replanting");
        translationBuilder.add("attribute.name.climbing_efficiency", "Climbing Efficiency");
    }

    public static String getEnchantmentTranslationKey(ResourceKey<Enchantment> enchantmentRegistryKey) {
        return enchantmentRegistryKey.location().toLanguageKey("enchantment");
    }
}
