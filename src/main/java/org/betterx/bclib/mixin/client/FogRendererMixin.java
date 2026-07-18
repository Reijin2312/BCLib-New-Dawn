package org.betterx.bclib.mixin.client;

import org.betterx.bclib.client.render.CustomFogRenderer;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @ModifyVariable(
            method = "setupFog",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;getDevice()Lcom/mojang/blaze3d/systems/GpuDevice;"
            ),
            ordinal = 0
    )
    private FogData bclib_customFog(FogData fogData, Camera camera, int renderDistance) {
        CustomFogRenderer.applyFogDensity(camera, renderDistance * 16.0F, fogData);
        return fogData;
    }
}
