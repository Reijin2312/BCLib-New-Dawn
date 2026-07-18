package org.betterx.bclib.interfaces;

import net.minecraft.resources.Identifier;

public interface ItemModelProvider {
    default Object getItemModel(Identifier resourceLocation) {
        try {
            Class<?> modelsHelper = Class.forName("org.betterx.bclib.client.models.ModelsHelper");
            return modelsHelper
                    .getMethod("createItemModel", Identifier.class)
                    .invoke(null, resourceLocation);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Failed to create item model for " + resourceLocation, ex);
        }
    }
}
