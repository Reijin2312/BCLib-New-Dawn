package org.betterx.bclib.mixin.client;

import org.betterx.bclib.client.render.BaseChestBlockEntityRenderer.BCLChestRenderState;

import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.resources.model.sprite.SpriteId;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChestRenderer.class)
public abstract class ChestRendererMixin {
    @ModifyVariable(
            method = "submit(Lnet/minecraft/client/renderer/blockentity/state/ChestRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V",
            at = @At(value = "STORE"),
            ordinal = 0
    )
    private SpriteId bclib_useCustomChestSprite(SpriteId vanillaSprite, ChestRenderState renderState) {
        if (renderState instanceof BCLChestRenderState customState && customState.customSprite != null) {
            return customState.customSprite;
        }
        return vanillaSprite;
    }
}
