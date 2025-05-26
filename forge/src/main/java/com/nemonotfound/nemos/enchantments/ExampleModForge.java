package com.nemonotfound.nemos.enchantments;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;

import static com.nemonotfound.nemos.enchantments.Constants.MOD_ID;

@Mod(MOD_ID)
public class ExampleModForge {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(Registries.ENTITY_TYPE, MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public ExampleModForge(FMLJavaModLoadingContext context) {
        final var modEventBus = context.getModEventBus();
        CommonClass.init();

        BLOCKS.register(modEventBus);
        ENTITIES.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
    }
}