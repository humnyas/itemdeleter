package fabric.humnyas.itemdeleter.compat.modmenu;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static fabric.humnyas.itemdeleter.utils.Constants.*;
import static me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.*;

@Config(name = "item")
public class ModConfigData implements ConfigData {
    public static ConfigClassHandler<ModConfigData> HANDLER = ConfigClassHandler.createBuilder(ModConfigData.class)
            .id(CONFIG_IDENTIFIER)
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(CONFIG_PATH)
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting)
                    .setJson5(true)
                    .build())
            .build();

    @RequiresRestart @SerialEntry public boolean recipesDeleted;
    @RequiresRestart @SerialEntry public boolean lootTablesDeleted;
    @RequiresRestart @SerialEntry public boolean mobDropsDeleted;
    @RequiresRestart @SerialEntry public boolean tileDropsDeleted;
    @RequiresRestart @SerialEntry public boolean recipeViewerDeleted;
    @RequiresRestart @SerialEntry public boolean creativeMenuDeleted;
    @RequiresRestart @SerialEntry public boolean tagAttendanceDeleted;

    @RequiresRestart @SerialEntry
    public List<String> deletedItems = new ArrayList<>();
}
