package org.betterx.bclib.mixin.common.signs;

import org.betterx.bclib.blocks.signs.BaseHangingSignBlock;
import org.betterx.bclib.blocks.signs.BaseSignBlock;
import org.betterx.bclib.blocks.signs.BaseWallHangingSignBlock;
import org.betterx.bclib.blocks.signs.BaseWallSignBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BlockEntityType.class)
public class BlockEntityTypeMixin {
    @Redirect(
            method = "isValid",
            at = @At(value = "INVOKE", target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z")
    )
    private boolean bcl_isValid(Set<Block> validBlocks, Object candidate) {
        if (validBlocks.contains(candidate)) {
            return true;
        }

        final BlockEntityType self = (BlockEntityType) (Object) this;
        final Block block = (Block) candidate;
        if (self == BlockEntityType.SIGN) {
            return block instanceof BaseSignBlock
                    || block instanceof BaseWallSignBlock
                    || block instanceof StandingSignBlock
                    || block instanceof WallSignBlock;
        } else if (self == BlockEntityType.HANGING_SIGN) {
            return block instanceof BaseHangingSignBlock
                    || block instanceof BaseWallHangingSignBlock
                    || block instanceof CeilingHangingSignBlock
                    || block instanceof WallHangingSignBlock;
        }

        return self == BlockEntityType.CHISELED_BOOKSHELF && block instanceof ChiseledBookShelfBlock;
    }
}



