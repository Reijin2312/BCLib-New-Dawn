package org.betterx.bclib.mixin.common.boat;

import org.betterx.bclib.items.boat.BoatTypeOverride;
import org.betterx.bclib.items.boat.CustomBoatTypeOverride;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.boat.AbstractChestBoat;
import net.minecraft.world.item.Item;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class BoatNameMixin {
    @Inject(method = "getTypeName", at = @At("HEAD"), cancellable = true)
    private void bclib_getCustomBoatTypeName(CallbackInfoReturnable<Component> cir) {
        if (!((Object) this instanceof CustomBoatTypeOverride customBoat)) return;

        BoatTypeOverride type = customBoat.bcl_getCustomType();
        if (type == null) return;

        Item item = (Object) this instanceof AbstractChestBoat ? type.getChestBoatItem() : type.getBoatItem();
        if (item != null) {
            cir.setReturnValue(Component.translatable(item.getDescriptionId()));
        }
    }
}
