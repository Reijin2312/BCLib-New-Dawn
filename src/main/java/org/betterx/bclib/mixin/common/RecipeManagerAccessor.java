package org.betterx.bclib.mixin.common;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeMap;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RecipeManager.class)
public interface RecipeManagerAccessor {
    @Accessor("recipes")
    RecipeMap bclib_getRecipes();
}
