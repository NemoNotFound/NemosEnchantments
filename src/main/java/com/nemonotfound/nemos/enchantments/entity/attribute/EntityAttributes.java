package com.nemonotfound.nemos.enchantments.entity.attribute;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import static com.nemonotfound.nemos.enchantments.NemosEnchantments.MOD_ID;

public class EntityAttributes {

    public static final RegistryEntry<EntityAttribute> CLIMBING_EFFICIENCY = register(
            "climbing_efficiency", new ClampedEntityAttribute("attribute.name.climbing_efficiency", 0.2, 0.2, 1.0).setTracked(true)
    );

    public static void init() {}

    private static RegistryEntry<EntityAttribute> register(String id, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(MOD_ID, id), attribute);
    }
}
