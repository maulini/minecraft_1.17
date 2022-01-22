package net.dofmine.minedofmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.effects.ModEffect;
import net.dofmine.minedofmod.job.HydrationEntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HydrationBar extends Gui {

    private final ResourceLocation HYDRATION = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/custom_bar/thirst.png");

    public HydrationBar(Minecraft minecraft, PoseStack matrixStack) {
        super(minecraft);
        RenderSystem.setShaderTexture(0, HYDRATION);
        int yScreenPosition = 215;
        if (minecraft.player.isUnderWater()) {
            yScreenPosition = 205;
        }
        renderHydrationBar(matrixStack, yScreenPosition);
        HydrationEntityPlayer hydrationEntityPlayer = HydrationEntityPlayer.get();
        renderHydrationByLevel(hydrationEntityPlayer.actualHydration, matrixStack, minecraft.player, yScreenPosition + 1);
    }

    private void renderHydrationBar(PoseStack poseStack, int yScreenPosition) {
        int xPos = 250;
        for (int i = 0; i < 10; i++) {
            blit(poseStack, xPos, yScreenPosition, 1, 0, 7, 9);
            xPos += 8;
        }
    }
    private void renderHydrationByLevel(int actualHydration, PoseStack poseStack, Player player, int yScreenPosition) {
        int xPosition = 2;
        int yPosition = 10;
        int xPositionMiddle = 11;
        int yPositionMiddle = 10;
        if (player.hasEffect(ModEffect.THIRST.get())) {
            xPosition = 20;
            yPosition = 10;
            xPositionMiddle = 29;
            yPositionMiddle = 10;
        }
        switch (actualHydration) {
            case 20: renderWater(10, xPosition, yPosition, poseStack, yScreenPosition);
                break;
            case 19: renderWater(9, xPosition, yPosition, poseStack, yScreenPosition);
                    blit(poseStack, 251, yScreenPosition, xPositionMiddle, yPositionMiddle, 5, 7);
                break;
            case 18: renderWater(9, xPosition, yPosition, poseStack, yScreenPosition);
                break;
            case 17: renderWater(8, xPosition, yPosition, poseStack, yScreenPosition);
                    blit(poseStack, 259, yScreenPosition, xPositionMiddle, yPositionMiddle, 5, 7);
                break;
            case 16: renderWater(8, xPosition, yPosition, poseStack, yScreenPosition);
                break;
            case 15: renderWater(7, xPosition, yPosition, poseStack, yScreenPosition);
                    blit(poseStack, 267, yScreenPosition, xPositionMiddle, yPositionMiddle, 5, 7);
                break;
            case 14: renderWater(7, xPosition, yPosition, poseStack, yScreenPosition);
                break;
            case 13: renderWater(6, xPosition, yPosition, poseStack, yScreenPosition);
                blit(poseStack, 275, yScreenPosition, xPositionMiddle, yPositionMiddle, 5, 7);
                break;
            case 12: renderWater(6, xPosition, yPosition, poseStack, yScreenPosition);
                break;
            case 11: renderWater(5, xPosition, yPosition, poseStack, yScreenPosition);
                    blit(poseStack, 283, yScreenPosition, xPositionMiddle, yPositionMiddle, 5, 7);
                break;
            case 10: renderWater(5, xPosition, yPosition, poseStack, yScreenPosition);
                break;
            case 9: renderWater(4, xPosition, yPosition, poseStack, yScreenPosition);
                    blit(poseStack, 291, yScreenPosition, xPositionMiddle, yPositionMiddle, 5, 7);
                break;
            case 8: renderWater(4, xPosition, yPosition, poseStack, yScreenPosition);
                break;
            case 7: renderWater(3, xPosition, yPosition, poseStack, yScreenPosition);
                    blit(poseStack, 299, yScreenPosition, xPositionMiddle, yPositionMiddle, 5, 7);
                break;
            case 6: renderWater(3, xPosition, yPosition, poseStack, yScreenPosition);
                break;
            case 5: renderWater(2, xPosition, yPosition, poseStack, yScreenPosition);
                    blit(poseStack, 307, yScreenPosition, xPositionMiddle, yPositionMiddle, 5, 7);
                break;
            case 4: renderWater(2, xPosition, yPosition, poseStack, yScreenPosition);
                break;
            case 3: renderWater(1, xPosition, yPosition, poseStack, yScreenPosition);
                    blit(poseStack, 315, yScreenPosition, xPositionMiddle, yPositionMiddle, 5, 7);
                break;
            case 2: renderWater(1, xPosition, yPosition, poseStack, yScreenPosition);
                break;
            case 1: blit(poseStack, 323, yScreenPosition, xPositionMiddle, yPositionMiddle, 5, 7);
                break;
            default:
                break;
        }
    }

    private void renderWater(int nbRender, int xPosition, int yPosition, PoseStack poseStack, int yScreenPosition) {
        int positionOnScreen = 323;
        for (int i = 0; i < nbRender; i++) {
            blit(poseStack, positionOnScreen, yScreenPosition, xPosition, yPosition, 5, 7);
            positionOnScreen -= 8;
        }
    }
}

