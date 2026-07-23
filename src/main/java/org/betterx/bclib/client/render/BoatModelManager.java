package org.betterx.bclib.client.render;

import org.betterx.bclib.items.boat.BoatTypeOverride;

import net.minecraft.client.model.object.boat.BoatModel;
import net.minecraft.client.model.object.boat.RaftModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import java.util.IdentityHashMap;
import java.util.Map;

public final class BoatModelManager {
    private static final String DEFAULT_LAYER = "main";
    private static final Map<BoatTypeOverride, Object> BOAT_MODELS = new IdentityHashMap<>();
    private static final Map<BoatTypeOverride, Object> CHEST_BOAT_MODELS = new IdentityHashMap<>();

    private BoatModelManager() {
    }

    public static ModelLayerLocation boatLayer(BoatTypeOverride type) {
        return new ModelLayerLocation(type.boatModelName, DEFAULT_LAYER);
    }

    public static ModelLayerLocation chestBoatLayer(BoatTypeOverride type) {
        return new ModelLayerLocation(type.chestBoatModelName, DEFAULT_LAYER);
    }

    public static Object getBoatModel(BoatTypeOverride type, boolean chest) {
        return chest ? CHEST_BOAT_MODELS.get(type) : BOAT_MODELS.get(type);
    }

    public static void createBoatModels(EntityRendererProvider.Context context, BoatTypeOverride type) {
        BOAT_MODELS.computeIfAbsent(type, key -> key.isRaft
                ? new RaftModel(context.bakeLayer(boatLayer(key)))
                : new BoatModel(context.bakeLayer(boatLayer(key))));
        CHEST_BOAT_MODELS.computeIfAbsent(type, key -> key.isRaft
                ? new RaftModel(context.bakeLayer(chestBoatLayer(key)))
                : new BoatModel(context.bakeLayer(chestBoatLayer(key))));
    }
}
