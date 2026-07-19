package org.betterx.bclib.particles;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public final class ParticleFactoryRegistry {
    public interface PendingParticleFactory<T extends ParticleOptions> {
        ParticleProvider<T> create(SpriteSet sprites);
    }

    private static final ParticleFactoryRegistry INSTANCE = new ParticleFactoryRegistry();

    private ParticleFactoryRegistry() {
    }

    public static ParticleFactoryRegistry getInstance() {
        return INSTANCE;
    }

    public <T extends ParticleOptions> void register(ParticleType<T> type, PendingParticleFactory<T> factory) {
        net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry.getInstance()
                .register(type, factory::create);
    }
}
