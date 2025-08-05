package com.devnemo.nemos.enchantments.datagen.langdatagen;

import com.devnemo.nemos.enchantments.enchantment.NemosEnchantments;
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
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.SOUL_BINDING), "Soul Binding");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.CLIMBER), "Climber");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.SPRINTER), "Sprinter");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.REAPER), "Reaper");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.FARMERS_KNOWLEDGE), "Farmer's Knowledge");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.REPLANTING), "Replanting");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.MAGMA_WALKER), "Magma Walker");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.SNOW_WALKER), "Snow Walker");
        translationBuilder.add("attribute.name.climbing_efficiency", "Climbing Efficiency");
    }

    public static String getEnchantmentTranslationKey(ResourceKey<Enchantment> enchantmentRegistryKey) {
        return enchantmentRegistryKey.location().toLanguageKey("enchantment");
    }
}
