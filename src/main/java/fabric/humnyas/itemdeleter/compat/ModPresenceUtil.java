package fabric.humnyas.itemdeleter.compat;

import net.fabricmc.loader.api.FabricLoader;

public class ModPresenceUtil {
    public static boolean isEMIPresent() {
        return FabricLoader.getInstance().isModLoaded("emi");
    }

    public static boolean isREIPresent() {
        return FabricLoader.getInstance().isModLoaded("rei");
    }
}
