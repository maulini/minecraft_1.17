package net.dofmine.minedofmod.tileentity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.tileentity.MjollnirEntity;
import net.dofmine.minedofmod.tileentity.MjollnirModel;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.ThrownTridentRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class MjollnirEntityRenderer extends EntityRenderer<MjollnirEntity> {

    private final MjollnirModel model;

    public MjollnirEntityRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
        this.model = new MjollnirModel(MjollnirModel.createBodyLayer().bakeRoot());
    }

    @Override
    public void render(MjollnirEntity mjollnirEntity, float yaw, float tickDelta, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        poseStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(tickDelta, mjollnirEntity.yRotO, mjollnirEntity.getYRot()) - 90.0F));
        poseStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(tickDelta, mjollnirEntity.xRotO, mjollnirEntity.getXRot()) + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(bufferSource, this.model.renderType(this.getTextureLocation(mjollnirEntity)), false, false);
        this.model.renderToBuffer(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(mjollnirEntity, yaw, tickDelta, poseStack, bufferSource, light);
    }

    @Override
    public ResourceLocation getTextureLocation(MjollnirEntity p_114482_) {
        return new ResourceLocation(MinedofMod.MODS_ID, "textures/entity/mjollnir.png");
    }
}
