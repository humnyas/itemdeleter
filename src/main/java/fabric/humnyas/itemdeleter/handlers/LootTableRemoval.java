package fabric.humnyas.itemdeleter.handlers;

import com.mojang.datafixers.util.Either;
import fabric.humnyas.itemdeleter.ItemDeleter;
import fabric.humnyas.itemdeleter.compat.modmenu.ModConfigData;
import fabric.humnyas.itemdeleter.mixin.accessors.*;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.Item;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.*;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.stream.Collectors;

public class LootTableRemoval {
    // ENTRY
    public static void initialise(ModConfigData config) {
        long start = System.nanoTime();
        LootTableEvents.REPLACE.register((key, original, source, registries) -> {
            if (!shouldRunClass(config, key.getValue())) return original;

            Set<Identifier> identifiersToRemove = ModConfigData.HANDLER.instance().deletedItems.stream()
                    .map(Identifier::of)
                    .collect(Collectors.toSet());

            List<LootPool> pools = ((LootTableAccessor) original).getPools();
            List<LootPool> newPools = new ArrayList<>();

            for (LootPool pool : pools) {
                LootPool filteredPool = filterPoolByRemovingItems(pool, identifiersToRemove, registries);

                newPools.add(filteredPool);
            }

            if (newPools.equals(pools)) return original;

            LootTable.Builder newBuilder = LootTable.builder();
            for (LootPool pool : newPools) {
                newBuilder.pool(pool);
            }

            return newBuilder.build();
        });
        long end = System.nanoTime();
        ItemDeleter.LOGGER.info("Loot table processing took {} µs", (end - start) / 1000);
    }


    // METHODS FOR FILTERING LOOT POOL
    public static LootPool filterPoolByRemovingItems(
            LootPool pool, Set<Identifier> itemsToRemove, RegistryWrapper.WrapperLookup registries
    ) {
        LootPoolAccessor poolAccessor = (LootPoolAccessor) pool;

        LootPool.Builder builder = LootPool.builder()
                .rolls(poolAccessor.getRolls())
                .bonusRolls(poolAccessor.getBonusRolls());

        for (LootCondition condition : poolAccessor.getConditions()) {
            builder.conditionally(condition);
        }

        for (LootFunction function : poolAccessor.getFunctions()) {
            builder.apply(function);
        }

        for (LootPoolEntry entry : poolAccessor.getEntries()) {
            Set<Item> entryItems = new HashSet<>();

            collectItemsFromLootPool(entry, entryItems, registries);

            boolean containsRemovedItem = entryItems.stream()
                    .map(Registries.ITEM::getId)
                    .anyMatch(itemsToRemove::contains);

            if (!containsRemovedItem) builder.with(entry);
        }

        return builder.build();
    }
    public static void collectItemsFromLootPool(
            LootPoolEntry entry, Set<Item> items, RegistryWrapper.WrapperLookup registries
    ) {
        if (entry instanceof ItemEntry itemEntry) {
            collectFromItemEntry(itemEntry, items);

        } else if (entry instanceof AlternativeEntry || entry instanceof GroupEntry) {
            collectFromCombinedEntry((CombinedEntryAccessor) entry, items, registries);

        } else if (entry instanceof LootTableEntry tableEntry) {
            collectFromLootTableEntry((LootTableEntryAccessor) tableEntry, items, registries);

        } else if (entry instanceof TagEntry tagEntry) {
            collectFromTagEntry(tagEntry, items);

        } else {
            ItemDeleter.LOGGER.warn("Unsupported LootPoolEntry type: {}", entry.getClass().getSimpleName());
        }
    }


    // HELPER METHODS FOR COLLECTING FROM LOOT POOL
    private static void collectFromItemEntry(
            ItemEntry itemEntry, Set<Item> items
    ) {
        RegistryEntry<Item> registryEntry = ((ItemEntryAccessor) itemEntry).getItem();
        items.add(registryEntry.value());
    }
    private static void collectFromCombinedEntry(
            CombinedEntryAccessor entry, Set<Item> items, RegistryWrapper.WrapperLookup registries
    ) {
        for (LootPoolEntry child : entry.getChildren()) {
            collectItemsFromLootPool(child, items, registries);
        }
    }
    private static void collectFromLootTable(
            LootTable table, Set<Item> items, RegistryWrapper.WrapperLookup registries
    ) {
        for (LootPool pool : ((LootTableAccessor) table).getPools()) {
            for (LootPoolEntry entry : ((LootPoolAccessor) pool).getEntries()) {
                collectItemsFromLootPool(entry, items, registries);
            }
        }
    }
    private static void collectFromTagEntry(
            TagEntry tagEntry, Set<Item> items
    ) {
        TagKey<Item> tag = ((TagEntryAccessor) tagEntry).getTag();
        Registry<Item> itemRegistry = Registries.ITEM;
        itemRegistry.stream()
                .filter(item -> itemRegistry.getEntry(item).isIn(tag))
                .forEach(items::add);
    }
    private static void collectFromLootTableEntry(
            LootTableEntryAccessor accessor, Set<Item> items, RegistryWrapper.WrapperLookup registries
    ) {
        Either<RegistryKey<LootTable>, LootTable> value = accessor.getValue();
        value.ifLeft(key -> {
            Optional<LootTable> nested = registries
                    .getOptionalWrapper(RegistryKeys.LOOT_TABLE)
                    .flatMap(wrapper -> wrapper.getOptional(key))
                    .map(RegistryEntry::value);
            nested.ifPresent(table -> collectFromLootTable(table, items, registries));
        });
        value.ifRight(lootTable -> collectFromLootTable(lootTable, items, registries));
    }


    // CONFIG RELATED METHODS
    public enum LootTableType {
        ENTITY, BLOCK, CHEST, UNKNOWN
    }

    public static LootTableType getLootTableType(Identifier id) {
        String path = id.getPath();

        if (path.startsWith("entities/")) return LootTableType.ENTITY;
        if (path.startsWith("blocks/")) return LootTableType.BLOCK;
        if (path.startsWith("chests/")) return LootTableType.CHEST;

        return LootTableType.UNKNOWN;
    }
    public static boolean shouldRunClass(ModConfigData configData, Identifier id) {
        LootTableType type = getLootTableType(id);

        if (type == LootTableType.BLOCK) {
            return configData.tileDropsDeleted;
        } else if (type == LootTableType.CHEST) {
            return configData.lootTablesDeleted;
        } else if (type == LootTableType.ENTITY) {
            return configData.mobDropsDeleted;
        }

        return false;
    }
}

