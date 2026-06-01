package org.betterx.bclib.client;

import org.betterx.bclib.api.v2.ModIntegrationAPI;
import org.betterx.bclib.api.v2.PostInitAPI;
import org.betterx.bclib.api.v2.dataexchange.DataExchangeAPI;
import org.betterx.bclib.BCLib;
import org.betterx.bclib.client.models.CustomModelBakery;
import org.betterx.bclib.client.textures.AtlasSetManager;
import org.betterx.bclib.client.textures.SpriteLister;
import org.betterx.bclib.integration.modmenu.ModMenuEntryPoint;
import org.betterx.bclib.interfaces.CustomColorProvider;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.world.level.block.Block;

import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.List;
import java.util.stream.IntStream;

@EventBusSubscriber(modid = BCLib.MOD_ID, value = Dist.CLIENT)
public class BCLibClient {
    private static final int LEGACY_TINT_LAYER_COUNT = 32;
    private static CustomModelBakery modelBakery;

    public static CustomModelBakery lazyModelbakery() {
        if (modelBakery == null) {
            modelBakery = new CustomModelBakery();
        }
        return modelBakery;
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        modelBakery = new CustomModelBakery();

        ModIntegrationAPI.registerAll();
        DataExchangeAPI.prepareClientside();
        PostInitAPI.postInit(true);
        ModMenuEntryPoint.register();

        AtlasSetManager.addSource(AtlasSetManager.VANILLA_BLOCKS, SpriteLister.of("entity/chest"));
        AtlasSetManager.addSource(AtlasSetManager.VANILLA_BLOCKS, SpriteLister.of("blocks"));
    }

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.BlockTintSources event) {
        for (Block block : BuiltInRegistries.BLOCK) {
            if (block instanceof CustomColorProvider provider) {
                event.register(createBlockTintSources(provider), block);
            }
        }
    }

    public static List<BlockTintSource> createBlockTintSources(CustomColorProvider provider) {
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
}
