package net.dofmine.minedofmod.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dofmine.minedofmod.job.ExtendedEntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;

public class ManaBar extends Gui {

    public ManaBar(Minecraft minecraft, PoseStack matrixStack) {
        super(minecraft);
        ExtendedEntityPlayer extendedEntityPlayer = ExtendedEntityPlayer.get();
        drawString(matrixStack, minecraft.font, String.format("%d/%d", extendedEntityPlayer.mana, extendedEntityPlayer.maxMana), 0, 0, Integer.parseInt("FFAA00", 16));
    }

}

