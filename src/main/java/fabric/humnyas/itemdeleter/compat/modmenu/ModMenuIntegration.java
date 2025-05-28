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
            RECIPES_NAME = Text.translatable("itemdeleter.config.option.recipes.name"),
            LOOT_TABLES_NAME = Text.translatable("itemdeleter.config.option.loot_tables.name"),
            MOB_DROPS_NAME = Text.translatable("itemdeleter.config.option.mob_drops.name"),
            TILE_DROPS_NAME = Text.translatable("itemdeleter.config.option.tile_drops.name"),
            RECIPE_VIEWER_NAME = Text.translatable("itemdeleter.config.option.recipe_viewer.name"),
            CREATIVE_MENU_NAME = Text.translatable("itemdeleter.config.option.creative_menu.name"),
            TAG_ATTENDANCE_NAME = Text.translatable("itemdeleter.config.option.tag_attendance.name");

    private static final OptionDescription
            RECIPES_DESC = OptionDescription.of(Text.translatable("itemdeleter.config.option.recipes.description")),
            LOOT_TABLES_DESC = OptionDescription.of(Text.translatable("itemdeleter.config.option.loot_tables.description")),
            MOB_DROPS_DESC = OptionDescription.of(Text.translatable("itemdeleter.config.option.mob_drops.description")),
            TILE_DROPS_DESC = OptionDescription.of(Text.translatable("itemdeleter.config.option.tile_drops.description")),
            RECIPE_VIEWER_DESC = OptionDescription.of(Text.translatable("itemdeleter.config.option.recipe_viewer.description")),
            CREATIVE_MENU_DESC = OptionDescription.of(Text.translatable("itemdeleter.config.option.creative_menu.description")),
            TAG_ATTENDANCE_DESC = OptionDescription.of(Text.translatable("itemdeleter.config.option.tag_attendance.description"));


    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> {
            ModConfigData itemDeleterConfig = ModConfigData.HANDLER.instance();

            // Boolean option
            OptionGroup booleanOptionBuilder = OptionGroup.createBuilder()
                    .name(BOOLEAN_OPTION_NAME)
                    .description(BOOLEAN_OPTION_DESCRIPTION)
                    .option(createBoolOption(RECIPES_NAME, RECIPES_DESC, () -> itemDeleterConfig.recipesDeleted, val -> itemDeleterConfig.recipesDeleted = val))
                    .option(createBoolOption(LOOT_TABLES_NAME, LOOT_TABLES_DESC, () -> itemDeleterConfig.lootTablesDeleted, val -> itemDeleterConfig.lootTablesDeleted = val))
                    .option(createBoolOption(MOB_DROPS_NAME, MOB_DROPS_DESC, () -> itemDeleterConfig.mobDropsDeleted, val -> itemDeleterConfig.mobDropsDeleted = val))
                    .option(createBoolOption(TILE_DROPS_NAME, TILE_DROPS_DESC, () -> itemDeleterConfig.tileDropsDeleted, val -> itemDeleterConfig.tileDropsDeleted = val))
                    .option(createBoolOption(RECIPE_VIEWER_NAME, RECIPE_VIEWER_DESC, () -> itemDeleterConfig.recipeViewerDeleted, val -> itemDeleterConfig.recipeViewerDeleted = val))
                    .option(createBoolOption(CREATIVE_MENU_NAME, CREATIVE_MENU_DESC, () -> itemDeleterConfig.creativeMenuDeleted, val -> itemDeleterConfig.creativeMenuDeleted = val))
                    .option(createBoolOption(TAG_ATTENDANCE_NAME, TAG_ATTENDANCE_DESC, () -> itemDeleterConfig.tagAttendanceDeleted, val -> itemDeleterConfig.tagAttendanceDeleted = val))
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
