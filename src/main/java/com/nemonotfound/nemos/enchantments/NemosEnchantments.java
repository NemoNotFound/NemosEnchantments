package com.nemonotfound.nemos.enchantments;

import com.nemonotfound.nemos.enchantments.entity.attribute.NemosAttributes;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NemosEnchantments implements ModInitializer {

	public static final String MOD_ID = "nemos_enchantments";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("You want more enchantments? What are you? A wizard?");

		NemosAttributes.init();
	}

	public static Identifier modIdentifier(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
