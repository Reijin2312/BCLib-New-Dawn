package org.betterx.bclib.recipes;

import org.betterx.bclib.util.BCLDataComponents;
import org.betterx.wover.recipe.api.BaseRecipeBuilder;
import org.betterx.wover.recipe.impl.BaseRecipeBuilderImpl;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.triggers.RecipeUnlockedTrigger;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public abstract class BCLBaseRecipeBuilder<I extends BaseRecipeBuilder<I>, R extends Recipe<? extends RecipeInput>> extends BaseRecipeBuilderImpl<I> {
    public interface RecipeOutputConsumer extends Consumer<CompoundTag> {
    }

    protected final Advancement.Builder advancement;
    protected Ingredient primaryInput;
    protected Ingredient secondaryInput;
    protected RecipeOutputConsumer outputTagConsumer;

    private final boolean dualInput;

    protected BCLBaseRecipeBuilder(
            @NotNull Identifier id,
            @NotNull ItemLike output,
            boolean dualInput
    ) {
        super(id, output);
        this.advancement = Advancement.Builder.advancement();
        this.dualInput = dualInput;
        this.group("");
    }

    protected BCLBaseRecipeBuilder(@NotNull Identifier id, @NotNull ItemStack output, boolean dualInput) {
        super(id, output);
        this.advancement = Advancement.Builder.advancement();
        this.dualInput = dualInput;
        this.group("");
    }

    protected BCLBaseRecipeBuilder(@NotNull Identifier id, @NotNull ItemStackTemplate output, boolean dualInput) {
        super(id, output);
        this.advancement = Advancement.Builder.advancement();
        this.dualInput = dualInput;
        this.group("");
    }

    @Override
    protected void validate() {
        super.validate();
        if (primaryInput == null || primaryInput.isEmpty()) {
            throwIllegalStateException(
                    "Primary input for Recipe can't be 'null', recipe {} will be ignored!"
            );
        }
        if (secondaryInput == null && this.dualInput) {
            throwIllegalStateException(
                    "Secondary input for Recipe can't be 'null', recipe {} will be ignored!"
            );
        }
    }

    @Override
    public final void build(RecipeOutput ctx) {
        validate();

        setupAdvancementForResult();
        final AdvancementHolder advancementHolder = advancement.build(createAdvancementId());

        final R recipe = createRecipe(id);
        ctx.accept(recipeKey(id), recipe, advancementHolder);
    }

    protected abstract R createRecipe(Identifier id);

    @Override
    protected ItemStackTemplate outputTemplate() {
        final ItemStackTemplate template = super.outputTemplate();
        if (this.outputTagConsumer == null) {
            return template;
        }

        final DataComponentPatch.Builder builder = DataComponentPatch.builder();
        for (var entry : template.components().entrySet()) {
            applyPatchEntry(builder, entry.getKey(), entry.getValue());
        }

        final CompoundTag tag = new CompoundTag();
        this.outputTagConsumer.accept(tag);
        if (!tag.isEmpty()) {
            builder.set(BCLDataComponents.ANVIL_ENTITY_DATA, CustomData.of(tag));
        }
        return new ItemStackTemplate(template.item(), template.count(), builder.build());
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void applyPatchEntry(DataComponentPatch.Builder builder, DataComponentType type, Optional<?> value) {
        if (value.isPresent()) {
            builder.set(type, value.get());
        } else {
            builder.remove(type);
        }
    }

    protected ItemStack outputStack() {
        return this.outputTemplate().create();
    }

    @SuppressWarnings("removal")
    protected void setupAdvancementForResult() {
        advancement
                .parent(RecipeBuilder.ROOT_RECIPE_ADVANCEMENT)//automatically at root level
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeKey(id)))
                .rewards(net.minecraft.advancements.AdvancementRewards.Builder.recipe(recipeKey(id)))
                .requirements(AdvancementRequirements.Strategy.OR);
    }

    protected Identifier createAdvancementId() {
        return id.withPrefix("recipes/" + category.getFolderName() + "/");
    }

    public I setPrimaryInput(ItemLike... inputs) {
        this.primaryInput = Ingredient.of(inputs);
        return (I) this;
    }

    public I setPrimaryInput(TagKey<Item> input) {
        this.primaryInput = ingredientOf(input);
        return (I) this;
    }

    public I setPrimaryInputAndUnlock(TagKey<Item> input) {
        this.setPrimaryInput(input);
        this.unlockedBy(input);
        return (I) this;
    }

    public I setPrimaryInputAndUnlock(ItemLike... inputs) {
        setPrimaryInput(inputs);
        for (ItemLike item : inputs) unlockedBy(item);

        return (I) this;
    }

    public I setSecondaryInput(ItemLike... inputs) {
        this.secondaryInput = Ingredient.of(inputs);
        return (I) this;
    }

    public I setSecondaryInput(TagKey<Item> input) {
        this.secondaryInput = ingredientOf(input);
        return (I) this;
    }

    public I setSecondaryInputAndUnlock(TagKey<Item> input) {
        setSecondaryInput(input);
        this.unlockedBy(input);
        return (I) this;
    }

    public I setSecondaryInputAndUnlock(ItemLike... inputs) {
        setSecondaryInput(inputs);
        for (ItemLike item : inputs) unlockedBy(item);

        return (I) this;
    }

    public I setOutputTag(CompoundTag tag) {
        this.outputTagConsumer = (itemTag) -> {
            for (String k : tag.keySet()) {
                itemTag.put(k, tag.get(k));
            }
        };
        return (I) this;
    }

    public I setOutputTag(RecipeOutputConsumer consumer) {
        this.outputTagConsumer = consumer;
        return (I) this;
    }

}
