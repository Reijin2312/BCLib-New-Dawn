package org.betterx.bclib.blocks;

import org.betterx.bclib.behaviours.interfaces.BehaviourWood;
import org.betterx.bclib.interfaces.tools.AxeCanStrip;
import org.betterx.wover.block.api.BlockTagProvider;
import org.betterx.wover.item.api.ItemTagProvider;
import org.betterx.wover.tag.api.event.context.ItemTagBootstrapContext;
import org.betterx.wover.tag.api.event.context.TagBootstrapContext;

import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;


public abstract class BaseStripableBarkBlock extends BaseBarkBlock implements AxeCanStrip {
    private final Block strippedBlock;

    protected BaseStripableBarkBlock(Block strippedBlock, Properties settings) {
        super(settings);
        this.strippedBlock = strippedBlock;
    }


    @Override
    public BlockState strippedState(BlockState state) {
        return strippedBlock.withPropertiesOf(state);
    }

    @Override
    public @Nullable BlockState getToolModifiedState(
            BlockState state,
            UseOnContext context,
            ItemAbility itemAbility,
            boolean simulate
    ) {
        if (itemAbility == ItemAbilities.AXE_STRIP) {
            return strippedState(state);
        }
        return super.getToolModifiedState(state, context, itemAbility, simulate);
    }

    public static class Wood extends BaseStripableBarkBlock implements BehaviourWood, BlockTagProvider, ItemTagProvider {
        private final boolean flammable;

        public Wood(MapColor color, Block strippedBlock, boolean flammable) {
            super(
                    strippedBlock,
                    (flammable
                            ? Properties.ofFullCopy(strippedBlock).ignitedByLava()
                            : Properties.ofFullCopy(strippedBlock)).mapColor(color)
            );
            this.flammable = flammable;
        }

        @Override
        public void registerBlockTags(Identifier location, TagBootstrapContext<Block> context) {
            context.add(this, BlockTags.LOGS);
            if (flammable) {
                context.add(this, BlockTags.LOGS_THAT_BURN);
            }
        }

        @Override
        public void registerItemTags(Identifier location, ItemTagBootstrapContext context) {
            context.add(this, ItemTags.LOGS);
            if (flammable) {
                context.add(this, ItemTags.LOGS_THAT_BURN);
            }
        }
    }
}
