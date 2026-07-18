package org.betterx.bclib.registry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

/** Loader-neutral helper for registering flammable blocks. */
public final class FlammableBlockRegistry {
    private static final FlammableBlockRegistry INSTANCE = new FlammableBlockRegistry();

    private FlammableBlockRegistry() {
    }

    public static FlammableBlockRegistry getDefaultInstance() {
        return INSTANCE;
    }

    public void add(Block block, int encouragement, int flammability) {
        ((FireBlock) Blocks.FIRE).setFlammable(block, encouragement, flammability);
    }
}
