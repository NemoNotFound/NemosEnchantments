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
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.SOUL_BINDING), "Soul-bound items will not drop after death.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.CLIMBER), "Climber");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.CLIMBER), "Lets you climb ladders and scaffolding faster.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.SPRINTER), "Sprinter");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.SPRINTER), "Increases your movement speed.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.REAPER), "Reaper");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.REAPER), "Harvests multiple nearby crops at once.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.FARMERS_KNOWLEDGE), "Farmer's Knowledge");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.FARMERS_KNOWLEDGE), "Prevents you from harvesting crops before they're fully grown.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.REPLANTING), "Replanting");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.REPLANTING), "Replants harvested crops.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.MAGMA_WALKER), "Magma Walker");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.MAGMA_WALKER), "Turns nearby lava into magma while you walk.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.SNOW_WALKER), "Snow Walker");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.SNOW_WALKER), "Allows you to walk over powder snow.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.COLLECTOR), "Collector");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.COLLECTOR), "Places mined blocks directly into your inventory.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.WISDOM), "Wisdom");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.WISDOM), "Increases experience dropped by blocks and mobs.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.FELLING), "Felling");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.FELLING), "Fells an entire tree by breaking one log.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.HEAD_HUNTER), "Head-Hunter");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.HEAD_HUNTER), "Killed mobs have a chance to drop their heads.");
        translationBuilder.add(getEnchantmentTranslationKey(Enchantments.SOUL_TOUCH), "Soul Touch");
        translationBuilder.add(getEnchantmentDescriptionTranslationKey(Enchantments.SOUL_TOUCH), "Allows you to collect a monster spawner.");

        translationBuilder.add("tooltip.nemos_enchantments.hold_shift", "Hold Shift for enchantment descriptions");
        translationBuilder.add("attribute.name.climbing_efficiency", "Climbing Efficiency");
    }

    public static String getEnchantmentTranslationKey(ResourceKey<Enchantment> enchantmentRegistryKey) {
        return enchantmentRegistryKey.identifier().toLanguageKey("enchantment");
    }

    public static String getEnchantmentDescriptionTranslationKey(ResourceKey<Enchantment> enchantmentRegistryKey) {
        return getEnchantmentTranslationKey(enchantmentRegistryKey) + ".description";
    }
}
