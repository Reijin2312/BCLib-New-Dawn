package org.betterx.bclib.integration.obe;

import org.betterx.bclib.registry.BaseBlockEntities;

import fr.madu59.obe.client.api.registry.RegistryApi;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

/** Client-only integration for Optimised Block Entities. */
public final class OBEIntegration {
    private OBEIntegration() {
    }

    public static void register() {
        RegistryApi.registerBlockEntityType(BaseBlockEntities.CHEST, "chest");
        RegistryApi.registerMaterialProvider(BaseBlockEntities.CHEST, OBEIntegration::getChestTexture);
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
