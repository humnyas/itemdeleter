package net.humnyas.itemdeleter.handlers;

import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.humnyas.itemdeleter.compat.modmenu.ModConfigData;
import net.humnyas.itemdeleter.mixin.accessors.*;
import net.minecraft.item.Item;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.*;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.stream.Collectors;

public class LootTableRemoval {
    // ENTRY
    public static void initialise(ModConfigData config) {
        LootTableEvents.REPLACE.register((resourceManager, lootManager, id, original, source) -> {
            if (!shouldRunClass(config, id)) return original;

            Set<Identifier> identifiersToRemove = ModConfigData.HANDLER.instance().deletedItems.stream()
                    .map(Identifier::new)
                    .collect(Collectors.toSet());

            LootPool[] pools = ((LootTableAccessor) original).getPools();
            LootPool[] newPools = new LootPool[pools.length];

            int i = 0;
            for (LootPool pool : pools) {
                LootPool filteredPool = filterPoolByRemovingItems(pool, identifiersToRemove, lootManager);

                newPools[i] = filteredPool;
                i++;
            }

            if (Arrays.equals(newPools, pools)) return original;

            LootTable.Builder newBuilder = LootTable.builder();
            for (LootPool pool : newPools) {
                newBuilder.pool(pool);
            }

            return newBuilder.build();
        });
    }


    // METHODS FOR FILTERING LOOT POOL
    public static LootPool filterPoolByRemovingItems(
            LootPool pool, Set<Identifier> itemsToRemove, LootManager lootManager
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

            collectItemsFromLootPool(entry, entryItems, lootManager);

            boolean containsRemovedItem = entryItems.stream()
                    .map(Registries.ITEM::getId)
                    .anyMatch(itemsToRemove::contains);

            if (!containsRemovedItem) builder.with(entry);
        }

        return builder.build();
    }
    public static void collectItemsFromLootPool(
            LootPoolEntry entry, Set<Item> items, LootManager lootManager
    ) {
        if (entry instanceof ItemEntry itemEntry) {
            collectFromItemEntry(itemEntry, items);

        } else if (entry instanceof AlternativeEntry || entry instanceof GroupEntry) {
            collectFromCombinedEntry((CombinedEntryAccessor) entry, items, lootManager);

        } else if (entry instanceof LootTableEntry tableEntry) {
            collectFromLootTableEntry((LootTableEntryAccessor) tableEntry, items, lootManager);

        } else if (entry instanceof TagEntry tagEntry) {
            collectFromTagEntry(tagEntry, items);

        }
    }


    // HELPER METHODS FOR COLLECTING FROM LOOT POOL
    private static void collectFromItemEntry(ItemEntry itemEntry, Set<Item> items) {
        items.add( ((ItemEntryAccessor) itemEntry).getItem());
    }
    private static void collectFromCombinedEntry(CombinedEntryAccessor entry, Set<Item> items, LootManager lootManager) {
        for (LootPoolEntry child : entry.getChildren()) {
            collectItemsFromLootPool(child, items, lootManager);
        }
    }
    private static void collectFromLootTable(LootTable table, Set<Item> items, LootManager lootManager) {
        for (LootPool pool : ((LootTableAccessor) table).getPools()) {
            for (LootPoolEntry entry : ((LootPoolAccessor) pool).getEntries()) {
                collectItemsFromLootPool(entry, items, lootManager);
            }
        }
    }
    private static void collectFromTagEntry(TagEntry tagEntry, Set<Item> items) {
        TagKey<Item> tag = ((TagEntryAccessor) tagEntry).getTag();
        Registry<Item> itemRegistry = Registries.ITEM;
        itemRegistry.stream()
                .filter(item -> itemRegistry.getEntry(item).isIn(tag))
                .forEach(items::add);
    }
    private static void collectFromLootTableEntry(LootTableEntryAccessor accessor, Set<Item> items, LootManager lootManager) {
        Identifier id = accessor.getId();
        LootTable lootTable = lootManager.getLootTable(id);
        collectFromLootTable(lootTable, items, lootManager);
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

