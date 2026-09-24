package org.betterx.bclib.items.tool;

import org.betterx.bclib.client.models.ModelsHelper;
import org.betterx.bclib.interfaces.ItemModelProvider;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;


public class BaseHoeItem extends Item implements ItemModelProvider {
    public BaseHoeItem(ToolMaterial material, int attackDamage, float attackSpeed, Item.Properties settings) {
        super(settings.hoe(material, attackDamage, attackSpeed));
    }

    public BaseHoeItem(ToolMaterial material, Item.Properties settings) {
        this(material, 0, 0.0F, settings);
    }

    @Override
    public Object getItemModel(Identifier resourceLocation) {
        return ModelsHelper.createHandheldItem(resourceLocation);
    }
}
