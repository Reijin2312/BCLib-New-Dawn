package org.betterx.bclib.api.v3.bonemeal;

import org.betterx.bclib.api.v3.tag.BCLBlockTags;
import org.betterx.wover.feature.api.FeatureUtils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NetherForestVegetationConfig;

import org.jetbrains.annotations.Nullable;

//adapted from NyliumBlock
public interface BonemealNyliumLike extends BonemealableBlock {
    Block getHostBlock(); //this
    @Nullable
    Holder<? extends ConfiguredFeature<?, ? extends Feature<?>>> getCoverFeature();

    default boolean isValidBonemealTarget(
            LevelReader blockGetter,
            BlockPos blockPos,
            BlockState blockState
    ) {
        return blockGetter.getBlockState(blockPos.above()).isAir();
    }

    default boolean isBonemealSuccess(
            Level level,
            RandomSource randomSource,
            BlockPos blockPos,
            BlockState blockState
    ) {
        return true;
    }

    @Override
    default BonemealableBlock.Type getType() {
        return BonemealableBlock.Type.NEIGHBOR_SPREADER;
    }

    default void performBonemeal(
            ServerLevel serverLevel,
            RandomSource randomSource,
            BlockPos blockPos,
            BlockState blockState
    ) {
        final BlockState currentState = serverLevel.getBlockState(blockPos);
        if (currentState.is(getHostBlock())) {
            Holder<? extends ConfiguredFeature<?, ?>> feature = getCoverFeature();
            if (feature != null) {
                if (!placeNetherrackVegetation(feature.value(), serverLevel, blockPos, currentState, randomSource)) {
                    FeatureUtils.placeInWorld(feature.value(), serverLevel, blockPos.above(), randomSource, false);
                }
            }
        }
    }

    default boolean placeNetherrackVegetation(
            ConfiguredFeature<?, ?> configuredFeature,
            ServerLevel serverLevel,
            BlockPos blockPos,
            BlockState spreadState,
            RandomSource randomSource
    ) {
        if (
                configuredFeature.feature() != Feature.NETHER_FOREST_VEGETATION ||
                        !(configuredFeature.config() instanceof NetherForestVegetationConfig config) ||
                        !spreadState.is(BCLBlockTags.BONEMEAL_SOURCE_NETHERRACK)
        ) {
            return false;
        }

        BlockPos origin = blockPos.above();
        if (!serverLevel.getBlockState(origin.below()).is(BlockTags.NYLIUM)) {
            return true;
        }

        int originY = origin.getY();
        if (originY < serverLevel.getMinY() + 1 || originY + 1 >= serverLevel.getMaxY()) {
            return true;
        }

        for (int i = 0; i < config.spreadWidth * config.spreadWidth; i++) {
            BlockPos targetPos = origin.offset(
                    randomSource.nextInt(config.spreadWidth) - randomSource.nextInt(config.spreadWidth),
                    randomSource.nextInt(config.spreadHeight) - randomSource.nextInt(config.spreadHeight),
                    randomSource.nextInt(config.spreadWidth) - randomSource.nextInt(config.spreadWidth)
            );
            if (!serverLevel.isEmptyBlock(targetPos) || targetPos.getY() <= serverLevel.getMinY()) {
                continue;
            }

            BlockPos substratePos = targetPos.below();
            BlockState substrateState = serverLevel.getBlockState(substratePos);
            boolean convertedSubstrate = false;
            if (
                    substrateState.is(Blocks.NETHERRACK) &&
                            serverLevel.getBlockState(substratePos.above()).isAir()
            ) {
                serverLevel.setBlock(substratePos, spreadState, 3);
                convertedSubstrate = true;
            }

            BlockState plantState = config.stateProvider.getState(serverLevel, randomSource, targetPos);
            if (plantState.canSurvive(serverLevel, targetPos)) {
                serverLevel.setBlock(targetPos, plantState, 2);
            } else if (convertedSubstrate) {
                serverLevel.setBlock(substratePos, substrateState, 3);
            }
        }

        return true;
    }
}
