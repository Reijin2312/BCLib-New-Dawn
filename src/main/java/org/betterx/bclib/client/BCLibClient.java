package org.betterx.bclib.client;

import org.betterx.bclib.api.v2.ModIntegrationAPI;
import org.betterx.bclib.api.v2.PostInitAPI;
import org.betterx.bclib.api.v2.dataexchange.DataExchangeAPI;
import org.betterx.bclib.client.models.CustomModelBakery;
import org.betterx.bclib.client.textures.AtlasSetManager;
import org.betterx.bclib.client.textures.SpriteLister;
import org.betterx.bclib.registry.BaseBlockEntityRenders;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.color.block.BlockTintSource;

import java.util.List;
import java.util.stream.IntStream;

public class BCLibClient implements ClientModInitializer {
    private static final int LEGACY_TINT_LAYER_COUNT = 32;
    private static CustomModelBakery modelBakery;

    public static CustomModelBakery lazyModelbakery() {
        if (modelBakery == null) {
            modelBakery = new CustomModelBakery();
        }
        return modelBakery;
    }

    public static List<BlockTintSource> createBlockTintSources(
            org.betterx.bclib.interfaces.CustomColorProvider provider
    ) {
        return IntStream.range(0, LEGACY_TINT_LAYER_COUNT)
                        .mapToObj(tintIndex -> (BlockTintSource) new BlockTintSource() {
                            @Override
                            public int color(net.minecraft.world.level.block.state.BlockState state) {
                                return provider.getProvider().getColor(state, null, null, tintIndex);
                            }

                            @Override
                            public int colorInWorld(
                                    net.minecraft.world.level.block.state.BlockState state,
                                    net.minecraft.client.renderer.block.BlockAndTintGetter level,
                                    net.minecraft.core.BlockPos pos
                            ) {
                                return provider.getProvider().getColor(state, level, pos, tintIndex);
                            }
                        })
                        .toList();
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
