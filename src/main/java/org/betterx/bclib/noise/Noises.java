package org.betterx.bclib.noise;

import org.betterx.bclib.BCLib;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.HashMap;
import java.util.Map;

public class Noises {
    private static final Map<ResourceKey<NormalNoise>, NormalNoise> noiseInstances = new HashMap<>();
    public static final ResourceKey<NormalNoise> ROUGHNESS_NOISE = createKey(BCLib.makeID(
            "roughness_noise"));

    public static ResourceKey<NormalNoise> createKey(Identifier loc) {
        return ResourceKey.create(Registries.NOISE, loc);
    }

    public static NormalNoise createNoise(
            Registry<NormalNoise> registry,
            RandomSource randomSource,
            ResourceKey<NormalNoise> resourceKey
    ) {
        return registry.getOrThrow(resourceKey).value();
    }

    public static NormalNoise getOrCreateNoise(
            RegistryAccess registryAccess,
            RandomSource randomSource,
            ResourceKey<NormalNoise> noise
    ) {
        final Registry<NormalNoise> registry = registryAccess.lookupOrThrow(Registries.NOISE);
        return noiseInstances.computeIfAbsent(noise, key -> createNoise(registry, randomSource, key));
    }
}
