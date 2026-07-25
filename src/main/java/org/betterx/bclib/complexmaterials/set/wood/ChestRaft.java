package org.betterx.bclib.complexmaterials.set.wood;

import org.betterx.bclib.complexmaterials.WoodenComplexMaterial;

import net.minecraft.world.item.Item;

public class ChestRaft extends ChestBoat {
    public ChestRaft() {
        super("chest_raft");
    }

    @Override
    protected Item getBaseBoat(WoodenComplexMaterial parentMaterial) {
        return parentMaterial.getItem(WoodSlots.RAFT);
    }
}
