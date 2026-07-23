package org.betterx.bclib.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class HumanoidArmorRenderer {
    public interface CopyExtraState {
        void copyPropertiesFrom(HumanoidModel<?> parentModel);
    }

    public void render(
            PoseStack pose,
            SubmitNodeCollector submitNodeCollector,
            ItemStack stack,
            @Nullable LivingEntity entity,
            EquipmentSlot slot,
            int light,
            HumanoidRenderState renderState,
            HumanoidModel<?> parentModel
    ) {
        HumanoidModel<?> model = getModelForSlot(entity, slot);
        if (model == null) {
            return;
        }

        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        if (equippable == null || equippable.slot() != slot) {
            return;
        }

        copyHumanoidPose(parentModel, model);
        if (model instanceof CopyExtraState extraState) {
            extraState.copyPropertiesFrom(parentModel);
        }

        setPartVisibility(model, slot);
        setupAnim(model, renderState);
        renderModel(pose, submitNodeCollector, light, model, renderState, getTextureForSlot(slot, usesInnerModel(slot)), 0xFFFFFFFF);

        if (stack.hasFoil()) {
            renderGlint(pose, submitNodeCollector, light, model, renderState);
        }
    }

    @NotNull
    protected abstract Identifier getTextureForSlot(EquipmentSlot slot, boolean innerLayer);

    protected abstract @Nullable HumanoidModel<?> getModelForSlot(@Nullable LivingEntity entity, EquipmentSlot slot);

    protected boolean usesInnerModel(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.LEGS;
    }

    protected void renderModel(
            PoseStack pose,
            SubmitNodeCollector submitNodeCollector,
            int light,
            HumanoidModel<?> humanoidModel,
            HumanoidRenderState renderState,
            Identifier texture,
            int color
    ) {
        submitModel(submitNodeCollector, humanoidModel, renderState, pose, RenderTypes.armorCutoutNoCull(texture), light, color);
    }

    protected void renderGlint(
            PoseStack pose,
            SubmitNodeCollector submitNodeCollector,
            int light,
            HumanoidModel<?> humanoidModel,
            HumanoidRenderState renderState
    ) {
        submitModel(submitNodeCollector, humanoidModel, renderState, pose, RenderTypes.armorEntityGlint(), light, 0xFFFFFFFF);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void submitModel(
            SubmitNodeCollector collector,
            HumanoidModel<?> model,
            HumanoidRenderState state,
            PoseStack pose,
            net.minecraft.client.renderer.rendertype.RenderType renderType,
            int light,
            int color
    ) {
        collector.submitModel(
                (HumanoidModel) model,
                state,
                pose,
                renderType,
                light,
                OverlayTexture.NO_OVERLAY,
                color,
                null,
                0,
                null
        );
    }

    protected void setPartVisibility(HumanoidModel<?> humanoidModel, EquipmentSlot equipmentSlot) {
        humanoidModel.head.visible = false;
        humanoidModel.hat.visible = false;
        humanoidModel.body.visible = false;
        humanoidModel.rightArm.visible = false;
        humanoidModel.leftArm.visible = false;
        humanoidModel.rightLeg.visible = false;
        humanoidModel.leftLeg.visible = false;
        switch (equipmentSlot) {
            case HEAD -> {
                humanoidModel.head.visible = true;
                humanoidModel.hat.visible = true;
            }
            case CHEST -> {
                humanoidModel.body.visible = true;
                humanoidModel.rightArm.visible = true;
                humanoidModel.leftArm.visible = true;
            }
            case LEGS -> {
                humanoidModel.body.visible = true;
                humanoidModel.rightLeg.visible = true;
                humanoidModel.leftLeg.visible = true;
            }
            case FEET -> {
                humanoidModel.rightLeg.visible = true;
                humanoidModel.leftLeg.visible = true;
            }
            default -> {
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void setupAnim(HumanoidModel<?> model, HumanoidRenderState renderState) {
        ((HumanoidModel) model).setupAnim(renderState);
    }

    private static void copyHumanoidPose(HumanoidModel<?> from, HumanoidModel<?> to) {
        copyPart(from.head, to.head);
        copyPart(from.hat, to.hat);
        copyPart(from.body, to.body);
        copyPart(from.rightArm, to.rightArm);
        copyPart(from.leftArm, to.leftArm);
        copyPart(from.rightLeg, to.rightLeg);
        copyPart(from.leftLeg, to.leftLeg);
    }

    private static void copyPart(ModelPart from, ModelPart to) {
        to.x = from.x;
        to.y = from.y;
        to.z = from.z;
        to.xRot = from.xRot;
        to.yRot = from.yRot;
        to.zRot = from.zRot;
        to.xScale = from.xScale;
        to.yScale = from.yScale;
        to.zScale = from.zScale;
        to.visible = from.visible;
        to.skipDraw = from.skipDraw;
    }
}
