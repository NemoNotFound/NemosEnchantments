package com.nemonotfound.nemos.enchantments.platform;

import com.nemonotfound.nemos.enchantments.platform.services.IRegistryHelper;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.nemonotfound.nemos.enchantments.Constants.MOD_ID;

public class FabricRegistryHelper implements IRegistryHelper {

    @Override
    public Supplier<Block> registerBlock(String id, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties properties) {
        return registerSupplierWithResourceKey(BuiltInRegistries.BLOCK, id, key -> function.apply(properties.setId(key)));
    }

    @Override
    public Supplier<Block> registerBlock(String id, Function<BlockBehaviour.Properties, Block> function, Supplier<BlockBehaviour.Properties> properties) {
        return registerSupplierWithResourceKey(BuiltInRegistries.BLOCK, id, key -> function.apply(properties.get().setId(key)));
    }

    @Override
    public Supplier<Item> registerItem(String id, Function<Item.Properties, Item> function, Item.Properties properties) {
        return registerSupplierWithResourceKey(BuiltInRegistries.ITEM, id, key -> function.apply(properties.setId(key)));
    }

    @Override
    public Supplier<Item> registerItem(String id, Function<Item.Properties, Item> function, Supplier<Item.Properties> properties) {
        return  registerSupplierWithResourceKey(BuiltInRegistries.ITEM, id, key -> function.apply(properties.get().setId(key)));
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> registerEntity(String id, Supplier<EntityType<T>> entity) {
        return registerSupplier(BuiltInRegistries.ENTITY_TYPE, id, entity);
    }

    @Override
    public <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(String id, Supplier<T> creativeModeTab) {
        return registerSupplier(BuiltInRegistries.CREATIVE_MODE_TAB, id, creativeModeTab);
    }

    @Override
    public CreativeModeTab.Builder createCreativeModeTab() {
        return CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0);
    }

    @Override
    public Supplier<KeyMapping> registerKeyMapping(KeyMapping keyMapping) {
        var registeredKeyMapping = KeyMappingHelper.registerKeyMapping(keyMapping);

        return () -> registeredKeyMapping;
    }

    @Override
    public Supplier<Holder<Attribute>> registerAttribute(String id, Attribute attribute) {
        var attributeReference = Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, Identifier.fromNamespaceAndPath(MOD_ID, id), attribute);

        return () -> attributeReference;
    }

    private static <T, R extends Registry<? super T>> Supplier<T> registerSupplier(R registry, String id, Supplier<T> object) {
        final var identifier = Identifier.fromNamespaceAndPath(MOD_ID, id);
        final var registeredObject = Registry.register((Registry<T>) registry, identifier, object.get());

        return () -> registeredObject;
    }

    private static <T, R extends Registry<T>> Supplier<T> registerSupplierWithResourceKey(R registry, String id, Function<ResourceKey<T>, T> object) {
        final var identifier = Identifier.fromNamespaceAndPath(MOD_ID, id);
        final var registeredObject = Registry.register(registry, identifier, object.apply(ResourceKey.create(registry.key(), identifier)));

        return () -> registeredObject;
    }
}
