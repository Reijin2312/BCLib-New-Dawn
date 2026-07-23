package org.betterx.bclib.api.v2.levelgen.structures.templatesystem;

import org.betterx.bclib.util.BlocksHelper;
import org.betterx.bclib.util.MHelper;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

public class DestructionStructureProcessor implements StructureProcessor {
    public static final MapCodec<DestructionStructureProcessor> CODEC = Codec.intRange(1, Integer.MAX_VALUE)
            .optionalFieldOf("chance", 4)
            .xmap(DestructionStructureProcessor::new, processor -> processor.chance);

    private int chance = 4;

    public DestructionStructureProcessor() {
    }

    public DestructionStructureProcessor(int chance) {
        setChance(chance);
    }

    public void setChance(int chance) {
        if (chance < 1) throw new IllegalArgumentException("chance must be positive");
        this.chance = chance;
    }

    @Override
    public StructureBlockInfo processBlock(
            LevelReader worldView,
            BlockPos pos,
            BlockPos blockPos,
            BlockPos templateRelativePos,
            StructureBlockInfo structureBlockInfo2,
            StructurePlaceSettings structurePlacementData
    ) {
        if (!BlocksHelper.isInvulnerable(
                structureBlockInfo2.state(),
                worldView,
                structureBlockInfo2.pos()
        ) && MHelper.RANDOM.nextInt(chance) == 0) {
            return null;
        }
        return structureBlockInfo2;
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return CODEC;
    }
}
