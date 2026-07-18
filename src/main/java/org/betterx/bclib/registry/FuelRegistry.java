package org.betterx.bclib.registry;

import net.fabricmc.fabric.api.registry.FuelRegistryEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

import java.util.IdentityHashMap;
import java.util.Map;

/** Fabric adapter for the dynamic fuel values introduced in 1.21.11. */
public final class FuelRegistry {
    public static final FuelRegistry INSTANCE = new FuelRegistry();

    private final Map<Item, Integer> fuels = new IdentityHashMap<>();

    private FuelRegistry() {
        FuelRegistryEvents.BUILD.register((builder, context) ->
                fuels.forEach(builder::add));
    }

    public void add(ItemLike item, int burnTime) {
        fuels.put(item.asItem(), burnTime);
    }
}
