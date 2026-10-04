package org.betterx.bclib.api.v2;

import org.betterx.bclib.BCLib;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Compostable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/** Runtime compost registrations backed by the 26.3 compostable item component. */
public final class ComposterAPI {
    private static final String RUNTIME_PATH_PREFIX = "compostable/runtime/";
    private static final Map<Item, Float> REGISTERED = new ConcurrentHashMap<>();
    private static final Map<Float, Compostable> CHANCE_TO_MARKER = new ConcurrentHashMap<>();
    private static final Map<ResourceKey<ContextIntProvider>, Float> KEY_TO_CHANCE = new ConcurrentHashMap<>();

    private ComposterAPI() {
    }

    public static Block allowCompost(float chance, Block block) {
        if (block != null) allowCompost(chance, block.asItem());
        return block;
    }

    public static Item allowCompost(float chance, Item item) {
        if (item != null && item != Items.AIR && chance > 0.0F) REGISTERED.put(item, chance);
        return item;
    }

    @ApiStatus.Internal
    @Nullable
    public static Compostable runtimeCompostable(Item item) {
        final Float registered = REGISTERED.get(item);
        if (registered == null) return null;

        final float chance = Math.round(registered * 1000.0F) / 1000.0F;
        return CHANCE_TO_MARKER.computeIfAbsent(chance, value -> {
            final ResourceKey<ContextIntProvider> key = ResourceKey.create(
                    Registries.CONTEXT_INT_PROVIDER,
                    BCLib.C.id(RUNTIME_PATH_PREFIX + Math.round(value * 1000.0F))
            );
            KEY_TO_CHANCE.put(key, value);
            return new Compostable(key);
        });
    }

    @ApiStatus.Internal
    public static int runtimeLayerCount(ResolvableInt layers, LootContext context) {
        if (!(layers instanceof ResolvableInt.Reference reference)) return -1;
        final Float chance = KEY_TO_CHANCE.get(reference.key());
        if (chance == null) return -1;

        final BlockState state = context.getOptional(LootContextParams.BLOCK_STATE);
        final int fill = state != null && state.hasProperty(ComposterBlock.LEVEL)
                ? state.getValue(ComposterBlock.LEVEL)
                : 0;
        return fill == 0 || context.getRandom().nextDouble() < chance ? 1 : 0;
    }
}
