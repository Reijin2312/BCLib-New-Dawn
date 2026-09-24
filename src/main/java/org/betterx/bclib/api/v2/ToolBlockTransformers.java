package org.betterx.bclib.api.v2;

import org.betterx.bclib.interfaces.tools.AxeCanStrip;

import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.CopyPropertiesProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ToolBlockTransformers {
    private ToolBlockTransformers() {
    }

    public static void register(IEventBus modBus) {
        modBus.addListener(ToolBlockTransformers::modifyDefaultComponents);
    }

    private static void modifyDefaultComponents(ModifyDefaultComponentsEvent event) {
        final List<BlockTransformer.BlockTransformData> axeTransforms = axeTransforms();
        if (!axeTransforms.isEmpty()) {
            event.modifyMatching(
                    (item, components) -> carriesAllOf(
                            components.get(DataComponents.BLOCK_TRANSFORMER),
                            Items.DIAMOND_AXE.components().get(DataComponents.BLOCK_TRANSFORMER)
                    ),
                    (builder, context, item) -> append(builder, axeTransforms)
            );
        }

        final List<BlockTransformer.BlockTransformData> shovelTransforms = shovelTransforms();
        if (!shovelTransforms.isEmpty()) {
            event.modifyMatching(
                    (item, components) -> carriesAllOf(
                            components.get(DataComponents.BLOCK_TRANSFORMER),
                            Items.DIAMOND_SHOVEL.components().get(DataComponents.BLOCK_TRANSFORMER)
                    ),
                    (builder, context, item) -> append(builder, shovelTransforms)
            );
        }
    }

    private static void append(
            net.minecraft.core.component.DataComponentMap.Builder builder,
            List<BlockTransformer.BlockTransformData> extra
    ) {
        final Holder<BlockTransformer> current = builder.get(DataComponents.BLOCK_TRANSFORMER);
        if (current == null) return;

        final List<BlockTransformer.BlockTransformData> merged = new ArrayList<>(current.value().transforms());
        merged.addAll(extra);
        builder.set(DataComponents.BLOCK_TRANSFORMER, Holder.direct(new BlockTransformer(merged)));
    }

    private static List<BlockTransformer.BlockTransformData> axeTransforms() {
        final List<BlockTransformer.BlockTransformData> result = new ArrayList<>();
        for (Block block : BuiltInRegistries.BLOCK) {
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
                    .builder(
                            RuleBasedStateProvider.ifTrueThenProvide(
                                    BlockPredicate.allOf(
                                            BlockPredicate.matchesBlocks(entry.getKey()),
                                            BlockPredicate.matchesTag(Direction.UP, BlockTags.AIR)
                                    ),
                                    BlockStateProvider.of(entry.getValue())
                            )
                    )
                    .sound(SoundEvents.SHOVEL_FLATTEN)
                    .disallowedFaces(List.of(Direction.DOWN))
                    .build());
        }
        return result;
    }

    private static boolean carriesAllOf(Holder<BlockTransformer> transformer, Holder<BlockTransformer> reference) {
        return transformer != null && reference != null
                && transformer.value().transforms().containsAll(reference.value().transforms());
    }
}
