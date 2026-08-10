package org.betterx.bclib.complexmaterials.set.wood;

import org.betterx.bclib.complexmaterials.ComplexMaterial;
import org.betterx.bclib.complexmaterials.WoodenComplexMaterial;
import org.betterx.bclib.complexmaterials.entry.SimpleMaterialSlot;
import org.betterx.wover.recipe.api.RecipeBuilder;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A vanilla chiseled bookshelf backed by the wood material's planks and slab. */
public class ChiseledBookshelf extends SimpleMaterialSlot<WoodenComplexMaterial> {
    public ChiseledBookshelf() {
        super("chiseled_bookshelf");
    }

    @Override
    protected @NotNull Block createBlock(WoodenComplexMaterial material, BlockBehaviour.Properties ignored) {
        return new ChiseledBookShelfBlock(
                BlockBehaviour.Properties.ofFullCopy(Blocks.CHISELED_BOOKSHELF).mapColor(material.planksColor)
        );
    }

    @Override
    protected @Nullable void makeRecipe(RecipeOutput context, ComplexMaterial material, Identifier id) {
        RecipeBuilder.crafting(id, material.getBlock(suffix))
                     .shape("###", "SSS", "###")
                     .addMaterial('#', material.getBlock(WoodSlots.PLANKS))
                     .addMaterial('S', material.getBlock(WoodSlots.SLAB))
                     .group("chiseled_bookshelf")
                     .category(RecipeCategory.BUILDING_BLOCKS)
                     .build(context);
    }
}
