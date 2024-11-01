package com.nemonotfound.nemosenchantments.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.EnchantmentTags;

import java.util.concurrent.CompletableFuture;

import static com.nemonotfound.nemosenchantments.enchantment.ModEnchantments.SOUL_BINDING;

public class EnchantmentTagProvider extends FabricTagProvider.EnchantmentTagProvider {

    public EnchantmentTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        RegistryWrapper.Impl<Enchantment> enchantmentRegistryWrapper = wrapperLookup.getOrThrow(RegistryKeys.ENCHANTMENT);

        getOrCreateTagBuilder(EnchantmentTags.ON_RANDOM_LOOT)
                .add(enchantmentRegistryWrapper.getOrThrow(SOUL_BINDING).registryKey());

        getOrCreateTagBuilder(EnchantmentTags.TREASURE)
                .add(enchantmentRegistryWrapper.getOrThrow(SOUL_BINDING).registryKey());

        getOrCreateTagBuilder(EnchantmentTags.TRADEABLE)
                .add(enchantmentRegistryWrapper.getOrThrow(SOUL_BINDING).registryKey());
    }
}
