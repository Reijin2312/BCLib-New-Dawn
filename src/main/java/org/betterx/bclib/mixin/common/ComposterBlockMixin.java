package org.betterx.bclib.mixin.common;

import org.betterx.bclib.api.v2.ComposterAPI;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ComposterBlock.class)
public class ComposterBlockMixin {
    @WrapOperation(method = "useItemOn", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
    ))
    private Object bclib_compostableOnUse(
            ItemStack stack,
            DataComponentType<?> componentType,
            Operation<Object> original
    ) {
        return bclib_compostableOr(stack, componentType, original);
    }

    @WrapOperation(method = "insertItem", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;get(Lnet/minecraft/core/component/DataComponentType;)Ljava/lang/Object;"
    ))
    private static Object bclib_compostableOnInsert(
            ItemStack stack,
            DataComponentType<?> componentType,
            Operation<Object> original
    ) {
        return bclib_compostableOr(stack, componentType, original);
    }

    @Unique
    private static Object bclib_compostableOr(
            ItemStack stack,
            DataComponentType<?> componentType,
            Operation<Object> original
    ) {
        final Object vanilla = original.call(stack, componentType);
        return vanilla != null ? vanilla : ComposterAPI.runtimeCompostable(stack.getItem());
    }

    @WrapOperation(method = "addLayer", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/storage/loot/providers/number/ints/ResolvableInt;get(Lnet/minecraft/world/level/storage/loot/LootContext;I)I"
    ))
    private static int bclib_runtimeLayerCount(
            ResolvableInt layers,
            LootContext context,
            int fallback,
            Operation<Integer> original
    ) {
        final int vanilla = original.call(layers, context, fallback);
        if (vanilla != 0) return vanilla;
        final int runtime = ComposterAPI.runtimeLayerCount(layers, context);
        return runtime >= 0 ? runtime : vanilla;
    }
}
