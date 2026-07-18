package org.betterx.bclib.recipes;

import org.betterx.bclib.BCLib;
import org.betterx.wover.config.api.DatapackConfigs;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashSet;
import java.util.ArrayList;
import java.util.Map;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class BCLRecipeManager {
    public static final Identifier RECIPES_CONFIG_FILE = BCLib.C.id("recipes.json");

    public static <C extends RecipeInput, S extends RecipeSerializer<T>, T extends Recipe<C>> S registerSerializer(
            String modID,
            String id,
            S serializer
    ) {
        return Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, modID + ":" + id, serializer);
    }

    public static <C extends RecipeInput, T extends Recipe<C>> RecipeType<T> registerType(String modID, String type) {
        Identifier recipeTypeId = Identifier.fromNamespaceAndPath(modID, type);
        return Registry.register(BuiltInRegistries.RECIPE_TYPE, recipeTypeId, new RecipeType<T>() {
            public String toString() {
                return type;
            }
        });
    }

    public static boolean exists(ItemLike item) {
        if (item instanceof Block) {
            return BuiltInRegistries.BLOCK.getKey((Block) item) != BuiltInRegistries.BLOCK.getDefaultKey();
        } else {
            return item != Items.AIR && BuiltInRegistries.ITEM.getKey(item.asItem()) != BuiltInRegistries.ITEM.getDefaultKey();
        }
    }

    private final static HashSet<Identifier> disabledRecipes = new HashSet<>();

    private static void clearRecipeConfig() {
        disabledRecipes.clear();
    }

    private static void processRecipeConfig(@NotNull Identifier sourceId, @NotNull JsonObject root) {
        if (root.has("disable")) {
            root
                    .getAsJsonArray("disable")
                    .asList()
                    .stream()
                    .map(el -> Identifier.tryParse(el.getAsString()))
                    .filter(id -> id != null)
                    .forEach(disabledRecipes::add);
        }
    }

    @ApiStatus.Internal
    public static void removeDisabledRecipes(ResourceManager manager, Map<Identifier, JsonElement> map) {
        clearRecipeConfig();
        DatapackConfigs
                .instance()
                .runForResource(manager, RECIPES_CONFIG_FILE, BCLRecipeManager::processRecipeConfig);

        for (Identifier id : disabledRecipes) {
            BCLib.LOGGER.verbose("Disabling Recipe: {}", id);

            map.remove(id);
        }
    }

    @ApiStatus.Internal
    public static RecipeMap removeDisabledRecipes(ResourceManager manager, RecipeMap recipeMap) {
        clearRecipeConfig();
        DatapackConfigs.instance().runForResource(manager, RECIPES_CONFIG_FILE, BCLRecipeManager::processRecipeConfig);
        if (disabledRecipes.isEmpty()) return recipeMap;

        ArrayList<RecipeHolder<?>> filtered = new ArrayList<>();
        for (RecipeHolder<?> holder : recipeMap.values()) {
            if (!disabledRecipes.contains(holder.id().identifier())) filtered.add(holder);
        }
        return RecipeMap.create(filtered);
    }
}
