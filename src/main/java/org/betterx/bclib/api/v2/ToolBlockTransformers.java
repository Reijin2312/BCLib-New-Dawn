package org.betterx.bclib.api.v2;

import org.betterx.bclib.interfaces.tools.AxeCanStrip;

import net.minecraft.core.Direction;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.component.BlockTransformers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.CopyPropertiesProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;

import net.fabricmc.fabric.api.item.v1.BlockTransformerEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Adds BCLib's dynamic axe and shovel conversions to the 26.3 block-transformer registry. */
public final class ToolBlockTransformers {
    private ToolBlockTransformers() {
    }

    public static void register() {
        BlockTransformerEvents.MODIFY.register((key, transforms, source, lookup) -> {
            if (key.equals(BlockTransformers.AXE)) {
                transforms.addAll(axeTransforms());
            } else if (key.equals(BlockTransformers.SHOVEL)) {
                transforms.addAll(shovelTransforms());
            }
        });
    }

    private static List<BlockTransformer.BlockTransformData> axeTransforms() {
        final List<BlockTransformer.BlockTransformData> result = new ArrayList<>();
        for (Block block : net.minecraft.core.registries.BuiltInRegistries.BLOCK) {
            if (!(block instanceof AxeCanStrip stripable)) continue;

            final BlockState stripped = stripable.strippedState(block.defaultBlockState());
            if (stripped == null) continue;

            result.add(BlockTransformer.BlockTransformData
                    .builder(RuleBasedStateProvider.ifTrueThenProvide(
                            BlockPredicate.matchesBlocks(block),
                            new CopyPropertiesProvider(stripped.getBlock())
                    ))
                    .sound(SoundEvents.AXE_STRIP)
                    .build());
        }
        return result;
    }

    private static List<BlockTransformer.BlockTransformData> shovelTransforms() {
        final List<BlockTransformer.BlockTransformData> result = new ArrayList<>();
        for (Map.Entry<Block, BlockState> entry : ShovelAPI.flattenables().entrySet()) {
            result.add(BlockTransformer.BlockTransformData
                    .builder(RuleBasedStateProvider.ifTrueThenProvide(
                            BlockPredicate.allOf(
                                    BlockPredicate.matchesBlocks(entry.getKey()),
                                    BlockPredicate.matchesTag(Direction.UP, BlockTags.AIR)
                            ),
                            BlockStateProvider.of(entry.getValue())
                    ))
                    .sound(SoundEvents.SHOVEL_FLATTEN)
                    .disallowedFaces(List.of(Direction.DOWN))
                    .build());
        }
        return result;
    }
}
