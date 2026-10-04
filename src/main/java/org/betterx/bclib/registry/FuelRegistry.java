package org.betterx.bclib.registry;

import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;

import java.util.IdentityHashMap;
import java.util.Map;

/** Fabric adapter for the dynamic fuel values introduced in 1.21.11. */
public final class FuelRegistry {
    public static final FuelRegistry INSTANCE = new FuelRegistry();

    private final Map<Item, Integer> fuels = new IdentityHashMap<>();

    private FuelRegistry() {
        DefaultItemComponentEvents.MODIFY.register(context -> fuels.forEach((item, burnTime) ->
                context.modify(item, builder -> builder.set(
                        DataComponents.COOKING_FUEL,
                        new CookingFuel(
                                new ResolvableInt.Constant(burnTime),
                                new ResolvableFloat.Constant(1.0F)
                        )
                ))));
    }

    public void add(ItemLike item, int burnTime) {
        fuels.put(item.asItem(), burnTime);
    }
}
