package com.nemonotfound.nemos.enchantments.datagen;

import com.nemonotfound.nemos.enchantments.tags.EnchantmentItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends FabricTagsProvider.ItemTagsProvider {

    public ItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        valueLookupBuilder(EnchantmentItemTags.WISDOM_ENCHANTABLE)
                .forceAddTag(ItemTags.MINING_ENCHANTABLE)
                .forceAddTag(ItemTags.BOW_ENCHANTABLE)
                .forceAddTag(ItemTags.CROSSBOW_ENCHANTABLE)
                .forceAddTag(ItemTags.WEAPON_ENCHANTABLE);
    }
}
