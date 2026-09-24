package org.betterx.bclib.blocks;

import org.betterx.bclib.behaviours.BehaviourBuilders;
import org.betterx.bclib.interfaces.SurvivesOnBlocks;
import org.betterx.bclib.util.BlocksHelper;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealSource;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Passive growth ({@link #performBonemeal} on a 1-in-8 chance) runs from {@link #randomTick}, so a block
 * registered with this class needs random-ticking {@code Properties} to ever grow on its own. The convenience
 * constructor supplies those properties; callers of the protected constructor remain responsible for them.
 */
public class BaseCropBlock extends BasePlantBlock implements SurvivesOnBlocks {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);
    private static final VoxelShape SHAPE = box(2, 0, 2, 14, 14, 14);

    private final List<Block> terrain;
    private final Item drop;

    /** Compatibility constructor retained for BCLib's pre-26.3 block API. */
    public BaseCropBlock(Item drop, Block... terrain) {
        this(
                BehaviourBuilders.createPlant()
                        .randomTicks()
                        .sound(SoundType.CROP)
                        .offsetType(BlockBehaviour.OffsetType.XZ),
                drop,
                terrain
        );
    }

    protected BaseCropBlock(BlockBehaviour.Properties properties, Item drop, Block... terrain) {
        super(properties);
        this.drop = drop;
        this.terrain = List.of(terrain);
        this.registerDefaultState(defaultBlockState().setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(AGE);
    }

    @Override
    public void performBonemeal(
            ServerLevel level,
            RandomSource random,
            BlockPos pos,
            BlockState state,
            BonemealSource bonemealSource
    ) {
        int age = state.getValue(AGE);
        if (age < 3) {
            BlocksHelper.setWithUpdate(level, pos, state.setValue(AGE, age + 1));
        }
    }

    @Override
    public boolean isValidBonemealTarget(
            LevelReader world,
            BlockPos pos,
            BlockState state,
            BonemealSource bonemealSource
    ) {
        return state.getValue(AGE) < 3;
    }

    @Override
    public boolean isBonemealSuccess(
            Level level,
            RandomSource random,
            BlockPos pos,
            BlockState state,
            BonemealSource bonemealSource
    ) {
        return state.getValue(AGE) < 3;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        super.randomTick(state, world, pos, random);
        if (isBonemealSuccess(world, random, pos, state, BonemealSource.INTERACTION) && random.nextInt(8) == 0) {
            performBonemeal(world, random, pos, state, BonemealSource.INTERACTION);
        }
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ePos) {
        return SHAPE;
    }

    @Override
    public boolean isTerrain(BlockState state) {
        return SurvivesOnBlocks.super.isTerrain(state);
    }

    @Override
    public List<Block> getSurvivableBlocks() {
        return terrain;
    }
}
