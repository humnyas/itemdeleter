package fabric.humnyas.itemdeleter.handlers;

import fabric.humnyas.itemdeleter.compat.modmenu.ModConfigData;

public class DeleterEntryPoint {
    public static void initialise() {
        ModConfigData config = ModConfigData.HANDLER.instance();

        // Recipes are in a mixin
        LootTableRemoval.initialise(config); // Handles chest loot, mob drops and tile drops
        // Recipe viewers work based on the mods entry points
        if (config.creativeMenuDeleted) CreativeMenuRemoval.initialise(); // TODO
        if (config.tagAttendanceDeleted) TagAttendanceRemoval.initialise(); // TODO
    }
}