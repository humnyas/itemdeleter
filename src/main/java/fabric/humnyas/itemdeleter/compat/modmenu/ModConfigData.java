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

    @SerialEntry
    public boolean recipesDeleted;
    @SerialEntry
    public boolean lootTablesDeleted;
    @SerialEntry
    public boolean mobDropsDeleted;
    @SerialEntry
    public boolean tileDropsDeleted;
    @SerialEntry
    public boolean recipeViewerDeleted;
    @SerialEntry
    public boolean creativeMenuDeleted;

    @SerialEntry
    public List<String> deletedItems = new ArrayList<>();
}
