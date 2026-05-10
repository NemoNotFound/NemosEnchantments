package com.nemonotfound.nemos.enchantments.datagen.langdatagen;

import com.nemonotfound.nemos.enchantments.enchantment.NemosEnchantments;
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
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.SOUL_BINDING), "Soul Binding");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.CLIMBER), "Climber");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.SPRINTER), "Sprinter");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.REAPER), "Reaper");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.FARMERS_KNOWLEDGE), "Farmer's Knowledge");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.REPLANTING), "Replanting");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.MAGMA_WALKER), "Magma Walker");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.SNOW_WALKER), "Snow Walker");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.COLLECTOR), "Collector");
        translationBuilder.add(getEnchantmentTranslationKey(NemosEnchantments.WISDOM), "Wisdom");
        translationBuilder.add("attribute.name.climbing_efficiency", "Climbing Efficiency");
        translationBuilder.add("attribute.name.experience_bonus", "Experience Bonus");
    }

    public static String getEnchantmentTranslationKey(ResourceKey<Enchantment> enchantmentRegistryKey) {
        return enchantmentRegistryKey.identifier().toLanguageKey("enchantment");
    }
}
