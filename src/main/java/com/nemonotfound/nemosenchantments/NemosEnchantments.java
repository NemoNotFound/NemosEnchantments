package com.nemonotfound.nemosenchantments;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NemosEnchantments implements ModInitializer {

	public static final String MOD_ID = "nemos-enchantments";
	public static final Logger log = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		log.info("Thank you for using Nemo's Enchantments!");
	}
}