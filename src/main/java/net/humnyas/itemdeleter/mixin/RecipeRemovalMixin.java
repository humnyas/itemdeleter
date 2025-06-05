package net.humnyas.itemdeleter.mixin;

import net.humnyas.itemdeleter.ItemDeleter;
import net.humnyas.itemdeleter.compat.modmenu.ModConfigData;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(RecipeManager.class)
public class RecipeRemovalMixin {
    @Inject(method = "apply", at = @At("HEAD"))
    private static void removeRecipesFromList(
            Map<Identifier, Recipe<?>> recipes, ResourceManager resourceManager, Profiler profiler, CallbackInfo info)
    {
        ModConfigData config = ModConfigData.HANDLER.instance();
        if (!config.recipesDeleted) return;

        List<String> recipesToRemoveList = ModConfigData.HANDLER.instance().deletedItems;
        Set<String> recipesToRemove = new HashSet<>(recipesToRemoveList);

        for (String string : recipesToRemove) {
            Identifier id = new Identifier(string);

            if (recipes.containsKey(id)) {
                recipes.remove(id);
                ItemDeleter.LOGGER.info("Removed recipe for item: {}", id);
            }
        }
    }
}
