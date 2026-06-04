package org.betterx.bclib.mixin.common;

import org.betterx.bclib.api.v3.bonemeal.BonemealAPI;
import org.betterx.bclib.blocks.FeatureSaplingBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BoneMealItem.class)
public class BoneMealItemMixin {
    private static void bclib_showBonemealEffect(Level level, BlockPos blockPos) {
        level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, blockPos, 15);
    }

    @Inject(method = "useOn", at = @At("HEAD"), cancellable = true)
    private void bclib_onUse(UseOnContext context, CallbackInfoReturnable<InteractionResult> info) {
        Level level = context.getLevel();
        final BlockPos blockPos = context.getClickedPos();

        if (context.getPlayer().isCreative()) {
            if (BonemealAPI.INSTANCE.runSpreaders(context.getItemInHand(), level, blockPos, true)) {
                bclib_showBonemealEffect(level, blockPos);
                info.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
            }

            final BlockState blockState = level.getBlockState(blockPos);
            if (blockState.getBlock() instanceof BonemealableBlock bblock
                    && level instanceof ServerLevel server
                    && blockState.getBlock() instanceof FeatureSaplingBlock<?, ?>
            ) {
                bblock.performBonemeal(server, context.getLevel().getRandom(), blockPos, blockState);
                bclib_showBonemealEffect(level, blockPos);
                info.setReturnValue(InteractionResult.sidedSuccess(level.isClientSide));
            }
        }
    }

    @Inject(remap = false, method = "applyBonemeal", at = @At("HEAD"), cancellable = true)
    private static void bcl_applyBonemeal(
            ItemStack itemStack,
            Level level,
            BlockPos blockPos,
            Player player,
            CallbackInfoReturnable<Boolean> cir
    ) {
        boolean forceBonemeal = player != null && player.isCreative();
        if (BonemealAPI.INSTANCE.runSpreaders(itemStack, level, blockPos, forceBonemeal)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "growCrop", at = @At("HEAD"), cancellable = true)
    private static void bcl_growCrop(
            ItemStack itemStack,
            Level level,
            BlockPos blockPos,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (BonemealAPI.INSTANCE.runSpreaders(itemStack, level, blockPos, false)) {
            cir.setReturnValue(true);
        }
    }
}



