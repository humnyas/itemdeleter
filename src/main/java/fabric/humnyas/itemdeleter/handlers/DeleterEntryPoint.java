package fabric.humnyas.itemdeleter.handlers;

import fabric.humnyas.itemdeleter.compat.modmenu.ModConfigData;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class DeleterEntryPoint {
    public static void initialise() {
        ServerLifecycleEvents.SERVER_STARTED.register(DeleterEntryPoint::deleteItems);

        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) -> {
            if (success) deleteItems(server);
        });
    }

    public static void deleteItems(MinecraftServer server) {
        ModConfigData config = ModConfigData.HANDLER.instance();

        // Recipes are in a mixin
        LootTableRemoval.initialise(config); // Handles chest loot, mob drops and tile drops
        if (config.recipeViewerDeleted) RecipeViewerRemoval.initialise(server); //TODO
        if (config.creativeMenuDeleted) CreativeMenuRemoval.initialise(server); //TODO
    }
}