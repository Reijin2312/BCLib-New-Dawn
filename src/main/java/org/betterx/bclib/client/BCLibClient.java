package org.betterx.bclib.client;

import org.betterx.bclib.api.v2.ModIntegrationAPI;
import org.betterx.bclib.api.v2.PostInitAPI;
import org.betterx.bclib.api.v2.dataexchange.DataExchangeAPI;
import org.betterx.bclib.client.models.CustomModelBakery;
import org.betterx.bclib.client.textures.AtlasSetManager;
import org.betterx.bclib.client.textures.SpriteLister;
import org.betterx.bclib.registry.BaseBlockEntityRenders;

import net.fabricmc.api.ClientModInitializer;

public class BCLibClient implements ClientModInitializer {
    private static CustomModelBakery modelBakery;

    public static CustomModelBakery lazyModelbakery() {
        if (modelBakery == null) {
            modelBakery = new CustomModelBakery();
        }
        return modelBakery;
    }

    @Override
    public void onInitializeClient() {
        modelBakery = new CustomModelBakery();

        ModIntegrationAPI.registerAll();
        BaseBlockEntityRenders.register();
        DataExchangeAPI.prepareClientside();
        PostInitAPI.postInit(true);
        AtlasSetManager.addSource(AtlasSetManager.VANILLA_BLOCKS, SpriteLister.of("entity/chest"));
        AtlasSetManager.addSource(AtlasSetManager.VANILLA_BLOCKS, SpriteLister.of("blocks"));
    }
//    @Override
//    public @Nullable UnbakedModel loadModelResource(
//            Identifier resourceId,
//            ModelProviderContext context
//    ) throws ModelProviderException {
//        return modelBakery.getBlockModel(resourceId);
//    }
//
//    @Override
//    public @Nullable UnbakedModel loadModelVariant(
//            ModelIdentifier modelId,
//            ModelProviderContext context
//    ) throws ModelProviderException {
//        return modelId.getVariant().equals("inventory")
//                ? modelBakery.getItemModel(modelId)
//                : modelBakery.getBlockModel(modelId);
//    }


}
