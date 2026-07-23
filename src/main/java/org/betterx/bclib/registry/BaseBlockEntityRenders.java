package org.betterx.bclib.registry;

import org.betterx.bclib.client.render.BaseChestBlockEntityRenderer;
import org.betterx.bclib.client.render.BoatModelManager;
import org.betterx.bclib.furniture.renderer.RenderChair;
import org.betterx.bclib.items.boat.BoatTypeOverride;

import net.minecraft.client.model.object.boat.BoatModel;
import net.minecraft.client.model.object.boat.RaftModel;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

@Environment(EnvType.CLIENT)
public class BaseBlockEntityRenders {
    public static void register() {
        BlockEntityRendererRegistry.register(BaseBlockEntities.CHEST, BaseChestBlockEntityRenderer::new);

        LayerDefinition boatModel = BoatModel.createBoatModel();
        LayerDefinition chestBoatModel = BoatModel.createChestBoatModel();
        LayerDefinition raftModel = RaftModel.createRaftModel();
        LayerDefinition chestRaftModel = RaftModel.createChestRaftModel();

        BoatTypeOverride.values().forEach(type -> {
            ModelLayerRegistry.registerModelLayer(BoatModelManager.boatLayer(type), () -> type.isRaft ? raftModel : boatModel);
            ModelLayerRegistry.registerModelLayer(
                    BoatModelManager.chestBoatLayer(type),
                    () -> type.isRaft ? chestRaftModel : chestBoatModel
            );
        });

        EntityRendererRegistry.register(BaseBlockEntities.CHAIR, RenderChair::new);
    }
}
