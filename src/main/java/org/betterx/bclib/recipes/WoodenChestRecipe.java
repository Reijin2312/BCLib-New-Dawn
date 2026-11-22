package org.betterx.bclib.recipes;

import org.betterx.bclib.BCLib;
import org.betterx.bclib.blocks.BaseChestBlock;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.NotNull;

/**
 * Replaces vanilla's broad chest recipe with an equivalent recipe that preserves the wood
 * variant when all eight planks belong to a BCLib chest material.
 */
public final class WoodenChestRecipe extends CustomRecipe {
    public static final SimpleCraftingRecipeSerializer<WoodenChestRecipe> SERIALIZER =
            new SimpleCraftingRecipeSerializer<>(WoodenChestRecipe::new);

    public WoodenChestRecipe(CraftingBookCategory category) {
        super(category);
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
        if (input.width() != 3 || input.height() != 3 || input.ingredientCount() != 8) return null;

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
