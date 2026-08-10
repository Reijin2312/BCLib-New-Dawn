package org.betterx.bclib.integration.obe;

import org.betterx.bclib.registry.BaseBlockEntities;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

/** Client-only integration for Optimised Block Entities. */
public final class OBEIntegration {
    private OBEIntegration() {
    }

    public static void register() {
        try {
            Class<?> registryApi = Class.forName("fr.madu59.obe.client.api.registry.RegistryApi");
            Method registerBlockEntityType = registryApi.getMethod(
                    "registerBlockEntityType",
                    BlockEntityType.class,
                    String.class
            );
            Method registerMaterialProvider = registryApi.getMethod(
                    "registerMaterialProvider",
                    BlockEntityType.class,
                    Function.class
            );

            registerBlockEntityType.invoke(null, BaseBlockEntities.CHEST, "chest");
            registerMaterialProvider.invoke(
                    null,
                    BaseBlockEntities.CHEST,
                    (Function<BlockState, ResourceLocation>) OBEIntegration::getChestTexture
            );
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException error) {
            throw new IllegalStateException("Installed OBE exposes an incompatible registry API", error);
        }
    }

    private static ResourceLocation getChestTexture(BlockState state) {
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        ChestType chestType = state.hasProperty(ChestBlock.TYPE)
                ? state.getValue(ChestBlock.TYPE)
                : ChestType.SINGLE;
        String suffix = switch (chestType) {
            case LEFT -> "_left";
            case RIGHT -> "_right";
            case SINGLE -> "";
        };

        return ResourceLocation.fromNamespaceAndPath(
                blockId.getNamespace(),
                "entity/chest/" + blockId.getPath() + suffix
        );
    }
}
