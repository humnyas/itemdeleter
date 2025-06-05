package net.humnyas.itemdeleter.mixin.accessors;

import net.minecraft.item.Item;
import net.minecraft.loot.entry.TagEntry;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TagEntry.class)
public interface TagEntryAccessor {
    @Accessor("name")
    TagKey<Item> getTag();
}
