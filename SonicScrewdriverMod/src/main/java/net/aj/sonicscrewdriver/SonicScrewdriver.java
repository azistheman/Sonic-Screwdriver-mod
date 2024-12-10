package net.aj.sonicscrewdriver;

import net.aj.sonicscrewdriver.item.ModsItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SonicScrewdriver implements ModInitializer {
	public static final String MOD_ID = "sonicscrewdriver";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModsItems.registerModItems();

	}
}