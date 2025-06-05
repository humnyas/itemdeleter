package net.humnyas.itemdeleter.compat.rei;


import net.humnyas.itemdeleter.compat.modmenu.ModConfigData;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Set;
import java.util.stream.Collectors;

public class REIIntegration implements REIClientPlugin {

    @Override public void registerEntries(EntryRegistry registry) {
        ModConfigData configData = ModConfigData.HANDLER.instance();
        if (!configData.recipeViewerDeleted) return;

        Set<Identifier> removedItems = configData.deletedItems.stream()
                .map(Identifier::new)
                .collect(Collectors.toSet());

        for (Identifier id : removedItems) {
            Item item = Registries.ITEM.get(id);
            registry.removeEntry(EntryStacks.of(item));
        }
    }

    @Override public double getPriority() {
        return 1000;
    }
}