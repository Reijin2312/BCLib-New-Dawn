package org.betterx.bclib.registry;

import org.betterx.bclib.BCLib;

import net.minecraft.world.item.Item;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * NeoForge fuel registry helper.
 */
public final class FuelRegistry {
    public static final FuelRegistry INSTANCE = new FuelRegistry();

    private final Map<Item, Integer> fuels = new IdentityHashMap<>();

    private FuelRegistry() {
    }

    public static void register(IEventBus modBus) {
        modBus.addListener(FuelRegistry::modifyDefaultComponents);
    }

    public void add(ItemLike item, int burnTime) {
        fuels.put(item.asItem(), burnTime);
    }

    private static void modifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        for (Map.Entry<Item, Integer> entry : INSTANCE.fuels.entrySet()) {
            event.modify(entry.getKey(), (builder, context, item) -> builder.set(
                    DataComponents.COOKING_FUEL,
                    new CookingFuel(
                            new ResolvableInt.Constant(entry.getValue()),
                            new ResolvableFloat.Constant(1.0F)
                    )
            ));
        }
    }
}
