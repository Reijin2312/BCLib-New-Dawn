package org.betterx.bclib.blocks;

import org.betterx.bclib.behaviours.BehaviourBuilders;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class FeatureHangingSaplingBlock extends FeatureSaplingBlock {

    private static final VoxelShape SHAPE = Block.box(4, 2, 4, 12, 16, 12);

    public FeatureHangingSaplingBlock(FeatureSupplier featureSupplier) {
        this(featureSupplier, 0);
    }

    public FeatureHangingSaplingBlock(FeatureSupplier featureSupplier, int light) {
        this(
                BehaviourBuilders.createPlant()
                        .randomTicks()
                        .noCollision()
                        .lightLevel(state -> light)
                        .sound(SoundType.GRASS),
                featureSupplier
        );
    }

    public FeatureHangingSaplingBlock(
            BlockBehaviour.Properties properties,
            FeatureSupplier featureSupplier
    ) {
        super(properties, featureSupplier, true);
    }

}
