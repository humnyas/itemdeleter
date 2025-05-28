package fabric.humnyas.itemdeleter.compat.emi;

import dev.emi.emi.api.EmiInitRegistry;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.stack.EmiStack;
import fabric.humnyas.itemdeleter.compat.modmenu.ModConfigData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.util.Set;
import java.util.stream.Collectors;

public class EMIIntegration implements EmiPlugin  {
    @Override
    public void initialize(EmiInitRegistry registry) {
        EmiPlugin.super.initialize(registry);
    }

    @Override
    public void register(EmiRegistry registry) {
        ModConfigData configData = ModConfigData.HANDLER.instance();
        if (!configData.recipeViewerDeleted) return;

        Set<Identifier> removedItems = configData.deletedItems.stream()
                .map(Identifier::of)
                .collect(Collectors.toSet());

        Registry<Item> itemRegistry = Registries.ITEM;

        for (Identifier id : removedItems) {
            Item item = itemRegistry.get(id);
            registry.removeEmiStacks(EmiStack.of(new ItemStack(item)));
        }
    }
}
