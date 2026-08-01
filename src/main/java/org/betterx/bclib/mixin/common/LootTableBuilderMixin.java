package org.betterx.bclib.mixin.common;

import org.betterx.bclib.interfaces.LootPoolAccessor;
import org.betterx.bclib.interfaces.LootTableBuilderAccessor;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;

import com.google.common.collect.ImmutableList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayList;
import java.util.List;

@Mixin(LootTable.Builder.class)
public abstract class LootTableBuilderMixin implements LootTableBuilderAccessor {
    @Mutable
    @Shadow
    @Final
    private ImmutableList.Builder<LootPool> pools;

    @Override
    public boolean bcl_addToPool(int index, List<LootPoolEntryContainer> newEntries) {
        final List<LootPool> currentPools = new ArrayList<>(pools.build());
        if (index < 0 || index >= currentPools.size()) {
            return false;
        }

        final LootPool pool = currentPools.get(index);
        final LootPool mergedPool = ((LootPoolAccessor) pool).bcl_mergeEntries(newEntries);
        currentPools.set(index, mergedPool);

        pools = ImmutableList.builder();
        pools.addAll(currentPools);
        return true;
    }
}
