package com.nemonotfound.nemos.enchantments.entity.attribute;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import static com.nemonotfound.nemos.enchantments.Constants.MOD_ID;

public class ModAttributes {

    public static final Holder<Attribute> CLIMBING_EFFICIENCY = register(
            "climbing_efficiency", new RangedAttribute("attribute.name.climbing_efficiency", 0.2, 0.2, 1.0).setSyncable(true)
    );

    public static void init() {}

    //TODO: Refactor for Forge and NeoForge
    private static Holder<Attribute> register(String id, Attribute attribute) {
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, ResourceLocation.fromNamespaceAndPath(MOD_ID, id), attribute);
    }
}
