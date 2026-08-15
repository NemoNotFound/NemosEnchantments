package com.nemonotfound.nemos.enchantments.entity.attribute;

import com.nemonotfound.nemos.enchantments.NemosEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class NemosAttributes {

    public static final Holder<Attribute> CLIMBING_EFFICIENCY = register(
            "climbing_efficiency", new RangedAttribute("attribute.name.climbing_efficiency", 0.2, 0.2, 1.0).setSyncable(true)
    );
    public static void init() {}

    private static Holder<Attribute> register(String id, Attribute attribute) {
        return Registry.registerForHolder(BuiltInRegistries.ATTRIBUTE, NemosEnchantments.modIdentifier(id), attribute);
    }
}
