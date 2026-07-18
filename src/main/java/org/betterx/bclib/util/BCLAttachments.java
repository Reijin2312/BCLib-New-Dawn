package org.betterx.bclib.util;

import org.betterx.bclib.BCLib;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.minecraft.network.codec.ByteBufCodecs;

public final class BCLAttachments {
    public static final AttachmentType<String> BOAT_CUSTOM_TYPE = AttachmentRegistry
            .<String>builder()
            .persistent(Codec.STRING)
            .syncWith(ByteBufCodecs.STRING_UTF8, AttachmentSyncPredicate.all())
            .buildAndRegister(BCLib.makeID("boat_custom_type"));

    private BCLAttachments() {
    }

    public static void ensureStaticInitialization() {
    }
}
