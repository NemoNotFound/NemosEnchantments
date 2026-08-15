package com.nemonotfound.nemos.enchantments.client;

import com.nemonotfound.nemos.enchantments.events.ItemTooltipEvent;
import net.fabricmc.api.ClientModInitializer;

public class NemosEnchantmentsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ItemTooltipEvent.register();
    }
}
