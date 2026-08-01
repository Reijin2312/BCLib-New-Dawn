package org.betterx.bclib.interfaces;

import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

import java.util.List;

public interface LootTableBuilderAccessor {
    boolean bcl_addToPool(int index, List<LootPoolEntryContainer> newEntries);
}
