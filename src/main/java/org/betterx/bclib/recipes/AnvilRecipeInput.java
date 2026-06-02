package org.betterx.bclib.recipes;

import org.betterx.wover.tag.api.predefined.CommonItemTags;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class AnvilRecipeInput implements RecipeInput {
    public final int HAMMER_SLOT = 1;
    public final int INGREDIENT_SLOT = 0;
    private final ItemStack hammer;
    private final ItemStack ingredient;

    public AnvilRecipeInput(ItemStack first, ItemStack second, TagKey<Item> allowedTools) {
        if (isHammer(first, allowedTools)) {
            this.hammer = first;
            this.ingredient = second;
        } else if (isHammer(second, allowedTools)) {
            this.hammer = second;
            this.ingredient = first;
        } else {
            this.hammer = ItemStack.EMPTY;
            this.ingredient = first;
        }
    }

    private static boolean isHammer(ItemStack stack, TagKey<Item> allowedTools) {
        if (stack.isEmpty()) return false;
        return allowedTools == null ? stack.is(CommonItemTags.HAMMERS) : stack.is(allowedTools);
    }

    public boolean hasHammer() {
        return !hammer.isEmpty();
    }

    public boolean hasIngerdient() {
        return !ingredient.isEmpty();
    }

    public ItemStack getHammer() {
        return hammer;
    }

    public ItemStack getIngredient() {
        return ingredient;
    }

    @Override
    public ItemStack getItem(int i) {
        if (i == HAMMER_SLOT) {
            return hammer;
        } else if (i == INGREDIENT_SLOT) {
            return ingredient;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public int size() {
        if (hasHammer() && hasIngerdient()) {
            return 2;
        } else if (hasIngerdient() || hasHammer()) {
            return 1;
        } else {
            return 0;
        }
    }
}
