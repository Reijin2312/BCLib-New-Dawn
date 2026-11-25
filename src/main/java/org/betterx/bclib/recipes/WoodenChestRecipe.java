package org.betterx.bclib.recipes;

import net.minecraft.world.item.crafting.*;
import org.betterx.bclib.BCLib;
import org.betterx.bclib.blocks.BaseChestBlock;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Replaces vanilla's broad chest recipe with an equivalent recipe that preserves the wood
 * variant when all eight planks belong to a BCLib chest material.
 */
public final class WoodenChestRecipe extends ShapedRecipe {
    public static final SimpleCraftingRecipeSerializer<WoodenChestRecipe> SERIALIZER =
            new SimpleCraftingRecipeSerializer<>(WoodenChestRecipe::new);

    public WoodenChestRecipe(CraftingBookCategory category) {
        super("", category, createPattern(), new ItemStack(Items.CHEST), true);
    }

    private static ShapedRecipePattern createPattern(){
        return ShapedRecipePattern.of(
                Map.of('#', Ingredient.of(ItemTags.PLANKS)),
                "###",
                "# #",
                "###"
        );
    }

    public static void register() {
        BCLRecipeManager.registerSerializer(BCLib.MOD_ID, "wooden_chest", SERIALIZER);
    }

    @Override
    public boolean matches(@NotNull CraftingInput input, @NotNull Level level) {
        return getPlank(input) != null;
    }

    @Override
    public @NotNull ItemStack assemble(
            @NotNull CraftingInput input,
            @NotNull HolderLookup.Provider registries
    ) {
        Item plank = getPlank(input);
        if (plank == null) return ItemStack.EMPTY;
        if (plank == Items.AIR) return new ItemStack(Items.CHEST);

        for (Block block : BuiltInRegistries.BLOCK) {
            if (block instanceof BaseChestBlock chest && chest.getMaterialBlock().asItem() == plank) {
                return new ItemStack(chest);
            }
        }
        return new ItemStack(Items.CHEST);
    }

    /** Returns AIR for a valid mixed-plank recipe and null for an invalid shape. */
    private static Item getPlank(CraftingInput input) {
        Item plank = null;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                ItemStack stack = input.getItem(x, y);
                if (x == 1 && y == 1) {
                    if (!stack.isEmpty()) return null;
                    continue;
                }
                if (!stack.is(ItemTags.PLANKS)) return null;
                if (plank == null) plank = stack.getItem();
                else if (plank != stack.getItem()) plank = Items.AIR;
            }
        }
        return plank;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width >= 3 && height >= 3;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }
}
