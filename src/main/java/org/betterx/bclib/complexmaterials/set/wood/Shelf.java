package org.betterx.bclib.complexmaterials.set.wood;

import org.betterx.bclib.complexmaterials.ComplexMaterial;
import org.betterx.bclib.complexmaterials.WoodenComplexMaterial;
import org.betterx.bclib.complexmaterials.entry.BlockEntry;
import org.betterx.bclib.complexmaterials.entry.SimpleMaterialSlot;
import org.betterx.wover.recipe.api.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ShelfBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Shelf extends SimpleMaterialSlot<WoodenComplexMaterial> {
    public Shelf() { super("shelf"); }
    @Override
    protected @NotNull Block createBlock(WoodenComplexMaterial material, BlockBehaviour.Properties ignored) {
        return new ShelfBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SHELF).mapColor(material.planksColor));
    }
    @Override
    protected void modifyBlockEntry(WoodenComplexMaterial material, @NotNull BlockEntry entry) {
        entry.setBlockTags(BlockTags.WOODEN_SHELVES).setItemTags(ItemTags.WOODEN_SHELVES);
    }
    @Override
    protected @Nullable void makeRecipe(RecipeOutput context, ComplexMaterial material, Identifier id) {
        RecipeBuilder.crafting(id, material.getBlock(suffix))
                     .outputCount(6)
                     .shape("###", "   ", "###")
                     .addMaterial('#', material.getBlock(WoodSlots.STRIPPED_LOG))
                     .group("shelf")
                     .category(RecipeCategory.MISC)
                     .build(context);
    }
}
