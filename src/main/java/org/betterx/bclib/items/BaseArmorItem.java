package org.betterx.bclib.items;

import org.betterx.bclib.interfaces.ItemModelProvider;
import org.betterx.wover.item.api.ItemTagProvider;
import org.betterx.wover.tag.api.event.context.ItemTagBootstrapContext;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

public class BaseArmorItem extends Item implements ItemModelProvider, ItemTagProvider {
    private final ArmorType armorType;

    public BaseArmorItem(Holder<ArmorMaterial> material, ArmorType type, Properties settings) {
        this(material.value(), type, settings, null);
    }

    public BaseArmorItem(
            Holder<ArmorMaterial> material,
            ArmorType type,
            Properties settings,
            ItemAttributeModifiers customAttributes
    ) {
        this(material.value(), type, settings, customAttributes);
    }

    public BaseArmorItem(ArmorMaterial material, ArmorType type, Properties settings) {
        this(material, type, settings, null);
    }

    public BaseArmorItem(
            ArmorMaterial material,
            ArmorType type,
            Properties settings,
            ItemAttributeModifiers customAttributes
    ) {
        super(applyArmorSettings(settings, material, type, customAttributes));
        this.armorType = type;
    }

    private static Properties applyArmorSettings(
            Properties settings,
            ArmorMaterial material,
            ArmorType type,
            ItemAttributeModifiers customAttributes
    ) {
        settings.humanoidArmor(material, type);
        if (customAttributes != null) {
            settings.attributes(customAttributes);
        }
        return settings;
    }

    @Override
    public void registerItemTags(Identifier location, ItemTagBootstrapContext context) {
        if (armorType.getSlot() == EquipmentSlot.HEAD) {
            context.add(this, ItemTags.HEAD_ARMOR);
        } else if (armorType.getSlot() == EquipmentSlot.CHEST) {
            context.add(this, ItemTags.CHEST_ARMOR);
        } else if (armorType.getSlot() == EquipmentSlot.LEGS) {
            context.add(this, ItemTags.LEG_ARMOR);
        } else if (armorType.getSlot() == EquipmentSlot.FEET) {
            context.add(this, ItemTags.FOOT_ARMOR);
        }
    }
}
