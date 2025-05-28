package fabric.humnyas.itemdeleter.handlers;

import fabric.humnyas.itemdeleter.compat.modmenu.ModConfigData;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public class TileDropRemoval {
    public static void initialise(MinecraftServer server) {
        List<String> deletedItems = ModConfigData.HANDLER.instance().deletedItems;


    }
}
