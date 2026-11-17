package org.betterx.bclib.mixin.common;

import org.betterx.wover.tag.api.predefined.CommonBlockTags;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour.StatePredicate;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

/** Lets tagged blocks count as Nether portal frame without conflicting redirect mixins. */
@Mixin(PortalShape.class)
public class PortalShapeMixin {
    @Unique
    private static final String FRAME_TEST = "Lnet/minecraft/world/level/block/state/BlockBehaviour$StatePredicate;test(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z";

    @WrapOperation(method = "getDistanceUntilEdgeAboveFrame", at = @At(value = "INVOKE", target = FRAME_TEST), remap = false)
    private static boolean be_getDistanceUntilEdgeAboveFrame(StatePredicate predicate, BlockState state, BlockGetter level, BlockPos pos, Operation<Boolean> original) {
        return be_FRAME(predicate, state, level, pos, original);
    }

    @WrapOperation(method = "hasTopFrame", at = @At(value = "INVOKE", target = FRAME_TEST), remap = false)
    private static boolean be_hasTopFrame(StatePredicate predicate, BlockState state, BlockGetter level, BlockPos pos, Operation<Boolean> original) {
        return be_FRAME(predicate, state, level, pos, original);
    }

    @WrapOperation(method = "getDistanceUntilTop", at = @At(value = "INVOKE", target = FRAME_TEST), remap = false)
    private static boolean be_getDistanceUntilTop(StatePredicate predicate, BlockState state, BlockGetter level, BlockPos pos, Operation<Boolean> original) {
        return be_FRAME(predicate, state, level, pos, original);
    }

    @Unique
    private static boolean be_FRAME(StatePredicate predicate, BlockState state, BlockGetter level, BlockPos pos, Operation<Boolean> original) {
        return state.is(CommonBlockTags.NETHER_PORTAL_FRAME) || original.call(predicate, state, level, pos);
    }
}
