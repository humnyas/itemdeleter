package net.humnyas.itemdeleter;

import net.humnyas.itemdeleter.compat.modmenu.ModConfigData;
import net.humnyas.itemdeleter.handlers.DeleterEntryPoint;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.humnyas.itemdeleter.utils.Constants.*;

public class ItemDeleter implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModConfigData.HANDLER.load();
		DeleterEntryPoint.initialise();
	}

}