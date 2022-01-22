package net.dofmine.minedofmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.job.ExtendedEntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;

@OnlyIn(Dist.CLIENT)
public class ManaBar extends Gui {

    private final ResourceLocation MANA_BAR = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/custom_bar/mana_bar.png");

    public ManaBar(Minecraft minecraft, PoseStack matrixStack) {
        super(minecraft);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0, MANA_BAR);
        ExtendedEntityPlayer extendedEntityPlayer = ExtendedEntityPlayer.get();
        matrixStack.pushPose();
        matrixStack.scale(0.7f, 0.7f, 0.7f);
        blit(matrixStack, 0, 0, 405, 0, 30, 250);
        if (extendedEntityPlayer.mana >= 67) {
            blit(matrixStack, 12, 7, 370, 0, 12, 250);
        } else if (extendedEntityPlayer.mana >= 34) {
            blit(matrixStack, 12, 7, 380, 0, 12, 250);
        } else if (extendedEntityPlayer.mana >= 0) {
            blit(matrixStack, 12, 7, 390, 0, 12, 250);
        }
        matrixStack.popPose();
    }

}

