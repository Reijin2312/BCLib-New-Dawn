package org.betterx.bclib.client.render;

import org.betterx.bclib.BCLib;
import org.betterx.bclib.config.Configs;
import org.betterx.bclib.util.BackgroundInfo;
import org.betterx.bclib.util.MHelper;
import org.betterx.wover.biome.api.BiomeManager;
import org.betterx.wover.biome.api.data.BiomeData;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import net.fabricmc.loader.api.FabricLoader;

public class CustomFogRenderer {
    private static final MutableBlockPos LAST_POS = new MutableBlockPos(0, -100, 0);
    private static final MutableBlockPos MUT_POS = new MutableBlockPos();
    private static final float[] FOG_DENSITY = new float[8];
    private static final int GRID_SIZE = 32;
    private static final float MIN_END_FACTOR = 0.5F;
    private static final float START_FACTOR = 0.25F;
    private static final float THIN_START_FACTOR = 0.7F;
    private static boolean atmosphericApplied;
    private static float atmosphericDensity = 1.0F;

    private static boolean hasDistantHorizons() {
        return BCLib.RUNS_DISTANT_HORIZONS || FabricLoader.getInstance().isModLoaded("distanthorizons");
    }

    public static void applyAtmosphericFog(
            FogData fogData,
            Camera camera,
            ClientLevel level,
            float renderDistance
    ) {
        if (hasDistantHorizons()) {
            return;
        }

        if (!Configs.CLIENT_CONFIG.renderCustomFog()) {
            return;
        }

        float configDensity = Configs.CLIENT_CONFIG.fogDensity();
        if (!(configDensity > 0.0F)) {
            return;
        }

        Vec3 pos = camera.position();
        if (!isForcedDimension(level) && shouldIgnoreArea(
                level,
                MHelper.floor(pos.x),
                MHelper.floor(pos.y),
                MHelper.floor(pos.z)
        )) {
            return;
        }

        float density = getFogDensity(level, pos.x, pos.y, pos.z) * configDensity;
        atmosphericApplied = true;
        atmosphericDensity = density;
        blendFogColorToSkyHorizon(fogData, level, density);

        float vanillaStart = fogData.environmentalStart;
        float vanillaEnd = fogData.environmentalEnd;
        if (level.dimension() == Level.NETHER && !Configs.CLIENT_CONFIG.netherThickFog()) {
            vanillaStart = 0.0F;
            vanillaEnd = Float.MAX_VALUE;
        }

        float fogEnd = Math.min(vanillaEnd, renderDistance) * endFactor(density);
        if (fogEnd >= vanillaEnd) {
            return;
        }

        float fogStart = fogEnd * startFactor(level, density);
        fogStart = vanillaStart > 0.0F ? Math.min(fogStart, vanillaStart) : fogStart + vanillaStart;

        fogData.environmentalStart = fogStart;
        fogData.environmentalEnd = fogEnd;
        fogData.skyEnd = Math.min(fogData.skyEnd, fogEnd);
        fogData.cloudEnd = Math.min(fogData.cloudEnd, fogEnd);
    }

    private static void blendFogColorToSkyHorizon(FogData fog, Level level, float density) {
        if (level.dimension() != Level.END || !BackgroundInfo.hasSkyHorizonColor) return;
        final float t = 1.0F - Mth.clamp(density - 1.0F, 0.0F, 1.0F);
        if (t <= 0.0F) return;
        fog.color.set(
                Mth.lerp(t, fog.color.x(), BackgroundInfo.skyHorizonRed),
                Mth.lerp(t, fog.color.y(), BackgroundInfo.skyHorizonGreen),
                Mth.lerp(t, fog.color.z(), BackgroundInfo.skyHorizonBlue),
                fog.color.w()
        );
    }

    private static float startFactor(Level level, float density) {
        if (level.dimension() != Level.END) return START_FACTOR;
        return Mth.lerp(Mth.clamp(density - 1.0F, 0.0F, 1.0F), THIN_START_FACTOR, START_FACTOR);
    }

    private static float endFactor(float density) {
        if (density <= 1.0F) {
            return 1.0F;
        }
        return MIN_END_FACTOR + (1.0F - MIN_END_FACTOR) / density;
    }

    public static void updateBackgroundInfo(Camera camera) {
        BackgroundInfo.fogDensity = atmosphericApplied ? atmosphericDensity : 1.0F;
        atmosphericApplied = false;
        BackgroundInfo.blindness = 0.0F;

        if (camera.entity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) camera.entity();
            MobEffectInstance effect = livingEntity.getEffect(MobEffects.BLINDNESS);
            if (effect != null) {
                int duration = effect.getDuration();
                if (duration > 20) {
                    BackgroundInfo.blindness = 1;
                } else {
                    BackgroundInfo.blindness = (float) duration / 20.0F;
                }
            }
        }
    }

    private static boolean isForcedDimension(Level level) {
        return level.dimension() == Level.END || level.dimension() == Level.NETHER;
    }

    private static boolean shouldIgnoreArea(Level level, int x, int y, int z) {
        for (int i = -8; i <= 8; i += 8) {
            for (int j = -8; j <= 8; j += 8) {
                if (!shouldIgnore(level, x + i, y, z + j)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean shouldIgnore(Level level, int x, int y, int z) {
        Holder<Biome> biome = level.getBiome(MUT_POS.set(x, y, z));
        return BiomeManager.biomeDataForHolder(biome) != null;
    }

    private static float getFogDensityI(Level level, int x, int y, int z) {
        Holder<Biome> biome = level.getBiome(MUT_POS.set(x, y, z));
        BiomeData renderBiome = BiomeManager.biomeDataForHolder(biome);
        if (renderBiome == null) return 1.0f;
        return renderBiome.fogDensity;
    }

    private static float getFogDensity(Level level, double x, double y, double z) {
        int x1 = MHelper.floor(x / GRID_SIZE) * GRID_SIZE;
        int y1 = MHelper.floor(y / GRID_SIZE) * GRID_SIZE;
        int z1 = MHelper.floor(z / GRID_SIZE) * GRID_SIZE;
        float dx = (float) (x - x1) / GRID_SIZE;
        float dy = (float) (y - y1) / GRID_SIZE;
        float dz = (float) (z - z1) / GRID_SIZE;

        if (LAST_POS.getX() != x1 || LAST_POS.getY() != y1 || LAST_POS.getZ() != z1) {
            int x2 = x1 + GRID_SIZE;
            int y2 = y1 + GRID_SIZE;
            int z2 = z1 + GRID_SIZE;
            LAST_POS.set(x1, y1, z1);
            FOG_DENSITY[0] = getFogDensityI(level, x1, y1, z1);
            FOG_DENSITY[1] = getFogDensityI(level, x2, y1, z1);
            FOG_DENSITY[2] = getFogDensityI(level, x1, y2, z1);
            FOG_DENSITY[3] = getFogDensityI(level, x2, y2, z1);
            FOG_DENSITY[4] = getFogDensityI(level, x1, y1, z2);
            FOG_DENSITY[5] = getFogDensityI(level, x2, y1, z2);
            FOG_DENSITY[6] = getFogDensityI(level, x1, y2, z2);
            FOG_DENSITY[7] = getFogDensityI(level, x2, y2, z2);
        }

        float a = Mth.lerp(dx, FOG_DENSITY[0], FOG_DENSITY[1]);
        float b = Mth.lerp(dx, FOG_DENSITY[2], FOG_DENSITY[3]);
        float c = Mth.lerp(dx, FOG_DENSITY[4], FOG_DENSITY[5]);
        float d = Mth.lerp(dx, FOG_DENSITY[6], FOG_DENSITY[7]);

        a = Mth.lerp(dy, a, b);
        b = Mth.lerp(dy, c, d);

        return Mth.lerp(dz, a, b);
    }
}
