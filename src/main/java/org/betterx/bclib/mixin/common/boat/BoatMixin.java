package org.betterx.bclib.mixin.common.boat;

import org.betterx.bclib.items.boat.BoatTypeOverride;
import org.betterx.bclib.items.boat.CustomBoatTypeOverride;
import org.betterx.bclib.util.BCLAttachments;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.entity.vehicle.boat.AbstractChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBoat.class)
public abstract class BoatMixin implements CustomBoatTypeOverride {
    @Unique private BoatTypeOverride bcl_type;

    @Override
    public void bcl_setCustomType(BoatTypeOverride type) {
        bcl_type = type;
        AttachmentTarget target = (AttachmentTarget) this;
        if (type == null) target.removeAttached(BCLAttachments.BOAT_CUSTOM_TYPE);
        else target.setAttached(BCLAttachments.BOAT_CUSTOM_TYPE, type.name());
    }

    @Override
    public BoatTypeOverride bcl_getCustomType() {
        String name = ((AttachmentTarget) this).getAttached(BCLAttachments.BOAT_CUSTOM_TYPE);
        if (name == null || name.isEmpty()) return bcl_type = null;
        if (bcl_type == null || !name.equals(bcl_type.name())) bcl_type = BoatTypeOverride.byName(name);
        return bcl_type;
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void bclib_readLegacyType(ValueInput input, CallbackInfo ci) {
        if (((AttachmentTarget) this).getAttached(BCLAttachments.BOAT_CUSTOM_TYPE) == null) {
            input.getString("cType").ifPresent(name -> bcl_setCustomType(BoatTypeOverride.byName(name)));
        }
    }

    @Inject(method = "getDropItem", at = @At("HEAD"), cancellable = true)
    private void bclib_getDropItem(CallbackInfoReturnable<Item> cir) {
        BoatTypeOverride type = bcl_getCustomType();
        if (type == null) return;
        Item item = (Object) this instanceof AbstractChestBoat ? type.getChestBoatItem() : type.getBoatItem();
        if (item != null) cir.setReturnValue(item);
    }

    @Inject(method = "getPickResult", at = @At("HEAD"), cancellable = true)
    private void bclib_getPickResult(CallbackInfoReturnable<ItemStack> cir) {
        BoatTypeOverride type = bcl_getCustomType();
        if (type == null) return;
        Item item = (Object) this instanceof AbstractChestBoat ? type.getChestBoatItem() : type.getBoatItem();
        if (item != null) cir.setReturnValue(new ItemStack(item));
    }

}
