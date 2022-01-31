package net.dofmine.minedofmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.job.client.ExtendedEntityPlayer;
import net.dofmine.minedofmod.job.client.ExtendedHunterJobsEntityPlayer;
import net.dofmine.minedofmod.utils.JobsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ManaBar extends Gui {

    private final ResourceLocation MANA_BAR = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/custom_bar/mana_bar.png");

    public ManaBar(Minecraft minecraft, PoseStack matrixStack) {
        super(minecraft);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0, MANA_BAR);
        ExtendedEntityPlayer wizard = (ExtendedEntityPlayer) JobsUtil.getAllCapabilitiesForLocalPlayer(Minecraft.getInstance().player).stream().filter(iCapabilityProvider -> iCapabilityProvider instanceof ExtendedEntityPlayer).findFirst().get();
        if (wizard != null) {
            matrixStack.pushPose();
            matrixStack.scale(0.7f, 0.7f, 0.7f);
            blit(matrixStack, 0, 0, 405, 0, 30, 250);
            if (wizard.mana >= 67) {
                blit(matrixStack, 12, 7, 370, 0, 12, 250);
            } else if (wizard.mana >= 34) {
                blit(matrixStack, 12, 7, 380, 0, 12, 250);
            } else if (wizard.mana >= 0) {
                blit(matrixStack, 12, 7, 390, 0, 12, 250);
            }
            matrixStack.popPose();
        }
    }

}

