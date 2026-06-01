package org.betterx.bclib.client.models;

import com.mojang.blaze3d.platform.Transparency;

import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;

import net.neoforged.neoforge.client.model.pipeline.QuadBakingVertexConsumer;

import org.joml.Matrix4fc;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class UnbakedQuad {
    private static final Vector4f POS = new Vector4f();
    private final float[] data = new float[20]; // 4 * (xyz + uv)
    private Direction dir = Direction.UP;
    private boolean useShading = false;
    private int spriteIndex;

    public void addData(int index, float value) {
        data[index] = value;
    }

    public void setSpriteIndex(int index) {
        spriteIndex = index;
    }

    public int getSpriteIndex() {
        return spriteIndex;
    }

    public void setDirection(Direction dir) {
        this.dir = dir;
    }

    public void setShading(boolean useShading) {
        this.useShading = useShading;
    }

    public Vector3f getPos(int index, Vector3f result) {
        int dataIndex = index * 5;
        result.set(data[dataIndex], data[dataIndex + 1], data[dataIndex + 2]);
        return result;
    }

    public BakedQuad bake(Material.Baked[] materials, ModelState modelState) {
        Matrix4fc matrix = modelState.transformation().getMatrix();
        Material.Baked material = materials[spriteIndex];
        TextureAtlasSprite sprite = material.sprite();
        Transparency transparency = material.forceTranslucent()
                ? sprite.transparency().or(Transparency.TRANSLUCENT)
                : sprite.transparency();

        QuadBakingVertexConsumer quadBaker = new QuadBakingVertexConsumer();
        quadBaker.setTintIndex(-1);
        quadBaker.setDirection(dir);
        quadBaker.setSprite(material, transparency);
        quadBaker.setShade(useShading);
        quadBaker.setAmbientOcclusion(true);

        for (int i = 0; i < 4; i++) {
            int dataIndex = i * 5;
            float x = data[dataIndex++];
            float y = data[dataIndex++];
            float z = data[dataIndex++];
            float u = data[dataIndex++];
            float v = data[dataIndex];

            POS.set(x, y, z, 1.0F);
            POS.mul(matrix);

            quadBaker.addVertex(POS.x(), POS.y(), POS.z());
            quadBaker.setColor(255, 255, 255, 255);
            quadBaker.setUv(sprite.getU(u), sprite.getV(v));
        }
        return quadBaker.bakeQuad();
    }
}
