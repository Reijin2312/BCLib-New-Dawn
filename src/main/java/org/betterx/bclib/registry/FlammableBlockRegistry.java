package org.betterx.bclib.registry;


import org.betterx.bclib.mixin.common.FireBlockAccessor;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

/**
 * NeoForge helper for flammable blocks.
 */
public final class FlammableBlockRegistry {
    private static final FlammableBlockRegistry INSTANCE = new FlammableBlockRegistry();

    private FlammableBlockRegistry() {
    }

    public static FlammableBlockRegistry getDefaultInstance() {
        return INSTANCE;
    }

    public void add(Block block, int encouragement, int flammability) {
        FireBlock fire = (FireBlock) Blocks.FIRE;
        ((FireBlockAccessor) fire).bclib_setFlammable(block, encouragement, flammability);
    }
}
