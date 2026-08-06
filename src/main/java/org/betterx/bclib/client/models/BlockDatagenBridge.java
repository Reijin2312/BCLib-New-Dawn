package org.betterx.bclib.client.models;

import org.betterx.bclib.blocks.BasePathBlock;
import org.betterx.wover.block.api.model.WoverBlockModelGenerators;

import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;

public final class BlockDatagenBridge {
    private BlockDatagenBridge() {}

    public static void provideBasePathBlockModels(
            Object modelGenerator,
            BasePathBlock pathBlock,
            Block baseBlock
    ) {
        WoverBlockModelGenerators generator = (WoverBlockModelGenerators) modelGenerator;
        Material sideMaterial = TextureMapping.getBlockTexture(pathBlock, "_side");
        Identifier side = sideMaterial.sprite();
        side = Identifier.fromNamespaceAndPath(side.getNamespace(), side
                .getPath()
                .replace("_path", ""));

        TextureMapping mapping = new TextureMapping()
                .put(TextureSlot.SIDE, new Material(side, sideMaterial.forceTranslucent()))
                .put(TextureSlot.TOP, TextureMapping.getBlockTexture(pathBlock, "_top"))
                .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(baseBlock));
        Identifier location = BCLModels.PATH.create(pathBlock, mapping, generator.modelOutput());

        generator.acceptBlockState(generator.randomTopModelVariant(pathBlock, location));
        generator.delegateItemModel(pathBlock, location);
    }
}
