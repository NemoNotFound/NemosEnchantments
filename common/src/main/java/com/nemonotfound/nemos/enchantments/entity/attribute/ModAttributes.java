package com.nemonotfound.nemos.enchantments.entity.attribute;

import com.nemonotfound.nemos.enchantments.platform.Services;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import java.util.function.Supplier;

public class ModAttributes {

    public static final Supplier<Holder<Attribute>> CLIMBING_EFFICIENCY = register(
            "climbing_efficiency", new RangedAttribute("attribute.name.climbing_efficiency", 0.2, 0.2, 1.0).setSyncable(true)
    );

    public static void init() {}

    private static Supplier<Holder<Attribute>> register(String id, Attribute attribute) {
        return Services.REGISTRY_HELPER.registerAttribute(id, attribute);
    }
}
