package org.betterx.bclib.items.boat;

import org.betterx.bclib.interfaces.ItemModelProvider;

import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.BoatItem;

public class BaseBoatItem extends BoatItem implements CustomBoatTypeOverride, ItemModelProvider {
    BoatTypeOverride customType;

    public BaseBoatItem(boolean bl, BoatTypeOverride type, Properties properties) {
        super(bl ? EntityTypes.OAK_CHEST_BOAT : EntityTypes.OAK_BOAT, properties);
        bcl_setCustomType(type);
    }

    @Override
    public void bcl_setCustomType(BoatTypeOverride type) {
        customType = type;
    }

    @Override
    public BoatTypeOverride bcl_getCustomType() {
        return customType;
    }
}
