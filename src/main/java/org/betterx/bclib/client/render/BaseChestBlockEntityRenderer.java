package org.betterx.bclib.client.render;

import org.betterx.bclib.blockentities.BaseChestBlockEntity;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.blockentity.state.ChestRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;


import com.google.common.collect.Maps;
import org.jspecify.annotations.Nullable;

import java.util.Map;

public class BaseChestBlockEntityRenderer extends ChestRenderer<BaseChestBlockEntity> {
    private static final int ID_NORMAL = 0;
    private static final int ID_LEFT = 1;
    private static final int ID_RIGHT = 2;
    private static final Map<Block, Material[]> CUSTOM_MATERIALS = Maps.newHashMap();

    public BaseChestBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        super(ctx);
    }

    public static class BCLChestRenderState extends ChestRenderState {
        public @Nullable Material customMaterial;
    }

    @Override
    public ChestRenderState createRenderState() {
        return new BCLChestRenderState();
    }

    @Override
    public void extractRenderState(
            BaseChestBlockEntity blockEntity,
            ChestRenderState renderState,
            float partialTick,
            Vec3 cameraPosition,
            ModelFeatureRenderer.CrumblingOverlay crumblingOverlay
    ) {
        super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, crumblingOverlay);
        ((BCLChestRenderState) renderState).customMaterial = getCustomMaterial(blockEntity, renderState);
    }

    private static @Nullable Material getCustomMaterial(BaseChestBlockEntity blockEntity, ChestRenderState renderState) {
        Material[] materials = CUSTOM_MATERIALS.get(blockEntity.getBlockState().getBlock());
        if (materials == null) {
            return null;
        }
        return switch (renderState.type) {
            case LEFT -> materials[ID_LEFT];
            case RIGHT -> materials[ID_RIGHT];
            default -> materials[ID_NORMAL];
        };
    }

    public static void registerRenderLayer(Block block) {
        Identifier blockId = BuiltInRegistries.BLOCK.getKey(block);
        String modId = blockId.getNamespace();
        String path = blockId.getPath();
        CUSTOM_MATERIALS.put(
                block,
                new Material[]{
                        chestMaterial(modId, path),
                        chestMaterial(modId, path + "_left"),
                        chestMaterial(modId, path + "_right")
                }
        );
    }

    private static Material chestMaterial(String modId, String path) {
        return new Material(Sheets.CHEST_SHEET, Identifier.fromNamespaceAndPath(modId, "entity/chest/" + path));
    }
}
