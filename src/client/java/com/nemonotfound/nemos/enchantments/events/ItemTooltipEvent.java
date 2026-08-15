package com.nemonotfound.nemos.enchantments.events;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemTooltipEvent {

    public static void register() {
        ItemTooltipCallback.EVENT.register((stack, _, _, tooltip) ->
                addEnchantmentDescriptionTooltips(stack, tooltip));
    }

    private static void addEnchantmentDescriptionTooltips(ItemStack stack, List<Component> tooltip) {
        var enchantmentDescriptions = getEnchantmentDescriptions(stack, tooltip);

        if (enchantmentDescriptions.isEmpty()) {
            return;
        }

        if (!Minecraft.getInstance().hasShiftDown()) {
            tooltip.add(Component.empty());
            tooltip.add(Component.translatable("tooltip.nemos_enchantments.hold_shift")
                    .withStyle(ChatFormatting.DARK_GRAY));

            return;
        }

        enchantmentDescriptions.entrySet().stream()
                .sorted(Comparator.<Map.Entry<Integer, Component>>comparingInt(Map.Entry::getKey).reversed())
                .forEach(description -> tooltip.add(description.getKey(), description.getValue()));
    }

    private static Map<Integer, Component> getEnchantmentDescriptions(ItemStack stack, List<Component> tooltip) {
        var enchantmentDescriptions = new HashMap<Integer, Component>();

        for (var entry : EnchantmentHelper.getEnchantmentsForCrafting(stack).entrySet()) {
            var enchantment = entry.getKey();
            var enchantmentKey = enchantment.unwrapKey().orElse(null);
            var index = tooltip.indexOf(Enchantment.getFullname(enchantment, entry.getIntValue()));

            if (enchantmentKey == null || index <= 0) {
                continue;
            }

            var descriptionKey = enchantmentKey.identifier().toLanguageKey("enchantment") + ".description";

            if (Language.getInstance().has(descriptionKey)) {
                enchantmentDescriptions.put(index + 1, Component.translatable(descriptionKey).withStyle(ChatFormatting.DARK_GRAY));
            }
        }

        return enchantmentDescriptions;
    }
}
