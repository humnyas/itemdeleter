package fabric.humnyas.itemdeleter.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    private static final Text TITLE = Text.translatable("itemdeleter.config.title");
    private static final Text CATEGORY_NAME = Text.translatable("itemdeleter.config.category.name");
    private static final Text CATEGORY_TOOLTIP = Text.translatable("itemdeleter.config.category.tooltip");

    private static final Text BOOLEAN_OPTION_NAME = Text.translatable("itemdeleter.config.boolean_option.name");
    private static final OptionDescription BOOLEAN_OPTION_DESCRIPTION = OptionDescription.of(Text.translatable("itemdeleter.config.boolean_option.description"));
    private static final Text LIST_OPTION_NAME = Text.translatable("itemdeleter.config.list_option.name");
    private static final OptionDescription LIST_OPTION_DESCRIPTION = OptionDescription.of(Text.translatable("itemdeleter.config.list_option.description"));

    private static final Text
            RECIPES_DELETED_NAME = Text.translatable("itemdeleter.config.option.recipes_deleted.name"),
            LOOT_TABLES_DELETED_NAME = Text.translatable("itemdeleter.config.option.loot_tables_deleted.name"),
            MOB_DROPS_DELETED_NAME = Text.translatable("itemdeleter.config.option.mob_drops_deleted.name"),
            TILE_DROPS_DELETED_NAME = Text.translatable("itemdeleter.config.option.tile_drops_deleted.name"),
            RECIPE_VIEWER_DELETED_NAME = Text.translatable("itemdeleter.config.option.recipe_viewer_deleted.name"),
            CREATIVE_MENU_DELETED_NAME = Text.translatable("itemdeleter.config.option.creative_menu_deleted.name");

    private static final OptionDescription
            RECIPES_DELETED_DESC = OptionDescription.of(Text.translatable("itemdeleter.config.option.recipes_deleted.description")),
            LOOT_TABLES_DELETED_DESC = OptionDescription.of(Text.translatable("itemdeleter.config.option.loot_tables_deleted.description")),
            MOB_DROPS_DELETED_DESC = OptionDescription.of(Text.translatable("itemdeleter.config.option.mob_drops_deleted.description")),
            TILE_DROPS_DELETED_DESC = OptionDescription.of(Text.translatable("itemdeleter.config.option.tile_drops_deleted.description")),
            RECIPE_VIEWER_DELETED_DESC = OptionDescription.of(Text.translatable("itemdeleter.config.option.recipe_viewer_deleted.description")),
            CREATIVE_MENU_DELETED_DESC = OptionDescription.of(Text.translatable("itemdeleter.config.option.creative_menu_deleted.description"));


    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> {
            ModConfigData itemDeleterConfig = ModConfigData.HANDLER.instance();

            // Boolean option
            OptionGroup booleanOptionBuilder = OptionGroup.createBuilder()
                    .name(BOOLEAN_OPTION_NAME)
                    .description(BOOLEAN_OPTION_DESCRIPTION)
                    .option(createBoolOption(RECIPES_DELETED_NAME, RECIPES_DELETED_DESC, () -> itemDeleterConfig.recipesDeleted, val -> itemDeleterConfig.recipesDeleted = val))
                    .option(createBoolOption(LOOT_TABLES_DELETED_NAME, LOOT_TABLES_DELETED_DESC, () -> itemDeleterConfig.lootTablesDeleted, val -> itemDeleterConfig.lootTablesDeleted = val))
                    .option(createBoolOption(MOB_DROPS_DELETED_NAME, MOB_DROPS_DELETED_DESC, () -> itemDeleterConfig.mobDropsDeleted, val -> itemDeleterConfig.mobDropsDeleted = val))
                    .option(createBoolOption(TILE_DROPS_DELETED_NAME, TILE_DROPS_DELETED_DESC, () -> itemDeleterConfig.tileDropsDeleted, val -> itemDeleterConfig.tileDropsDeleted = val))
                    .option(createBoolOption(RECIPE_VIEWER_DELETED_NAME, RECIPE_VIEWER_DELETED_DESC, () -> itemDeleterConfig.recipeViewerDeleted, val -> itemDeleterConfig.recipeViewerDeleted = val))
                    .option(createBoolOption(CREATIVE_MENU_DELETED_NAME, CREATIVE_MENU_DELETED_DESC, () -> itemDeleterConfig.creativeMenuDeleted, val -> itemDeleterConfig.creativeMenuDeleted = val))
                    .build();


            // List option
            ListOption.Builder<String> listOptionBuilder = ListOption.<String>createBuilder()
                    .name(LIST_OPTION_NAME)
                    .description(LIST_OPTION_DESCRIPTION)
                    .controller(StringControllerBuilder::create)
                    .binding(
                            itemDeleterConfig.deletedItems,
                            () -> itemDeleterConfig.deletedItems,
                            val -> itemDeleterConfig.deletedItems = val
                    )
                    .initial("");


            // Building category
            ConfigCategory.Builder categoryBuilder = ConfigCategory.createBuilder()
                    .name(CATEGORY_NAME)
                    .tooltip(CATEGORY_TOOLTIP)
                    .group(booleanOptionBuilder)
                    .group(listOptionBuilder.build());


            return YetAnotherConfigLib.createBuilder()
                    .title(TITLE)
                    .save(ModConfigData.HANDLER::save)
                    .category(categoryBuilder.build())
                    .build()
                    .generateScreen(screen);
        };
    }

    private Option<Boolean> createBoolOption(
            Text name, OptionDescription description, Supplier<Boolean> getter, Consumer<Boolean> setter
    ) {
        return Option.<Boolean>createBuilder()
                .name(name)
                .description(description)
                .binding(true, getter, setter)
                .controller(TickBoxControllerBuilder::create)
                .build();
    }
}
