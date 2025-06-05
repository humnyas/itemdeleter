package net.humnyas.itemdeleter.mixin.accessors;

import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.provider.number.LootNumberProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LootPool.class)
public interface LootPoolAccessor {
    @Accessor("entries")
    LootPoolEntry[] getEntries();

    @Accessor("conditions")
    LootCondition[] getConditions();

    @Accessor("functions")
    LootFunction[] getFunctions();

    @Accessor("rolls")
    LootNumberProvider getRolls();

    @Accessor("bonusRolls")
    LootNumberProvider getBonusRolls();
}
