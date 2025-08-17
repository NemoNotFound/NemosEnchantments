package com.devnemo.nemos.enchantments.entity.attribute;

import com.devnemo.nemos.enchantments.platform.Services;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;

import java.util.function.Supplier;

public class NemosAttributes {

    public static final Supplier<Holder<Attribute>> CLIMBING_EFFICIENCY = register(
            "climbing_efficiency", new RangedAttribute("attribute.name.climbing_efficiency", 0.2, 0.2, 1.0).setSyncable(true)
    );
    public static final Supplier<Holder<Attribute>> EXPERIENCE_BONUS = register(
            "experience_bonus", new RangedAttribute("attribute.name.experience_bonus", 1, 1, 3).setSyncable(true)
    );

    public static void init() {}

    private static Supplier<Holder<Attribute>> register(String id, Attribute attribute) {
        return Services.REGISTRY_HELPER.registerAttribute(id, attribute);
    }
}
