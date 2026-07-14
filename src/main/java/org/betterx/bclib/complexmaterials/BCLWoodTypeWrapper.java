package org.betterx.bclib.complexmaterials;

import org.betterx.wover.core.api.ModCore;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.MapColor;

import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Objects;

public final class BCLWoodTypeWrapper {
    public final ResourceLocation id;
    public final WoodType type;
    public final MapColor color;
    public final boolean flammable;

    protected BCLWoodTypeWrapper(ResourceLocation id, WoodType type, MapColor color, boolean flammable) {
        this.id = id;
        this.type = type;
        this.color = color;
        this.flammable = flammable;
    }

    public static Builder create(ModCore modCore, String string) {
        return new Builder(modCore.mk(string));
    }

    public static Builder create(ResourceLocation id) {
        return new Builder(id);
    }

    public BlockSetType setType() {
        return type.setType();
    }

    public ResourceLocation id() {
        return id;
    }

    public WoodType type() {
        return type;
    }

    public MapColor color() {
        return color;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BCLWoodTypeWrapper) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.type, that.type) &&
                Objects.equals(this.color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, color);
    }

    @Override
    public String toString() {
        return "BCLWoodTypeWrapper[" +
                "id=" + id + ", " +
                "type=" + type + ", " +
                "color=" + color + ']';
    }


    public static class Builder {
        private final ResourceLocation id;
        private BlockSetType setType;
        private MapColor color;
        private boolean flammable;

        public Builder(ResourceLocation id) {
            this.id = id;
            this.color = MapColor.WOOD;
            this.flammable = true;
        }

        public Builder setBlockSetType(BlockSetType setType) {
            this.setType = setType;
            return this;
        }

        public Builder setColor(MapColor color) {
            this.color = color;
            return this;
        }

        public Builder setFlammable(boolean flammable) {
            this.flammable = flammable;
            return this;
        }

        public BCLWoodTypeWrapper build() {
            WoodType existing = findExistingWoodType(id);
            if (existing != null) {
                return new BCLWoodTypeWrapper(id, existing, color, flammable);
            }
            if (setType == null) setType = new BlockSetTypeBuilder().register(id);

            final WoodType type = new WoodTypeBuilder().register(id, setType);
            return new BCLWoodTypeWrapper(id, type, color, flammable);
        }
    }

    private static WoodType findExistingWoodType(ResourceLocation id) {
        try {
            for (Field field : WoodType.class.getDeclaredFields()) {
                if (!Modifier.isStatic(field.getModifiers()) || !Map.class.isAssignableFrom(field.getType())) continue;
                field.setAccessible(true);
                Object raw = field.get(null);
                if (!(raw instanceof Map<?, ?> map) || map.isEmpty()) continue;

                WoodType found = findWoodTypeInMap(map, id);
                if (found != null) return found;
            }
        } catch (ReflectiveOperationException ignored) {
            // Fall through to normal registration path.
        }
        return null;
    }

    private static WoodType findWoodTypeInMap(Map<?, ?> map, ResourceLocation id) {
        Object value = map.get(id);
        if (value instanceof WoodType type) return type;
        value = map.get(id.getPath());
        if (value instanceof WoodType type) return type;
        value = map.get(id.toString());
        if (value instanceof WoodType type) return type;

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = entry.getKey();
            Object entryValue = entry.getValue();
            if (!(entryValue instanceof WoodType type)) continue;
            if (key instanceof ResourceLocation rl && rl.getPath().equals(id.getPath())) return type;
            if (key instanceof String name && name.equals(id.getPath())) return type;
        }
        return null;
    }
}
