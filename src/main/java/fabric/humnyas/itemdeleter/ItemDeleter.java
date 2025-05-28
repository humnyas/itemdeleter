package fabric.humnyas.itemdeleter;

import fabric.humnyas.itemdeleter.compat.modmenu.ModConfigData;
import fabric.humnyas.itemdeleter.handlers.DeleterEntryPoint;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static fabric.humnyas.itemdeleter.utils.Constants.*;

public class ItemDeleter implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModConfigData.HANDLER.load();
		DeleterEntryPoint.initialise();
	}

}