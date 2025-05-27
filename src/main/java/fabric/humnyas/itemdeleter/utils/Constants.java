package fabric.humnyas.itemdeleter.utils;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.nio.file.Path;

public interface Constants {
    String MOD_ID = "itemdeleter";

    Path CONFIG_PATH = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("itemdeleter.txt");

    Identifier CONFIG_IDENTIFIER = Identifier.of(MOD_ID);

}
