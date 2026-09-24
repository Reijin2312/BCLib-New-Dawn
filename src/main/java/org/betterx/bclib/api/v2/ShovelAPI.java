package org.betterx.bclib.api.v2;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShovelAPI {
    private static final Map<Block, BlockState> FLATTENABLES = new LinkedHashMap<>();

    /**
     * Will add left-click behaviour to shovel: when it is targeting cetrain {@link Block} it will be converting to new
     * {@link BlockState} on usage. Example: grass converting to path.
     *
     * @param target  {@link Block} that will be converted.
     * @param convert {@link BlockState} to convert block into.
     */
    public static void addShovelBehaviour(Block target, BlockState convert) {
        FLATTENABLES.put(target, convert);
    }

    static Map<Block, BlockState> flattenables() {
        return Collections.unmodifiableMap(FLATTENABLES);
    }
}
