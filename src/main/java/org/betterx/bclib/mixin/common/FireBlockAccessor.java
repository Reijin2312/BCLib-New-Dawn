package org.betterx.bclib.mixin.common;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FireBlock.class)
public interface FireBlockAccessor {
    @Invoker("setFlammable")
    void bclib_setFlammable(Block block, int igniteOdds, int burnOdds);
}
