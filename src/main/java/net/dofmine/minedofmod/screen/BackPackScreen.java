package net.dofmine.minedofmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.container.BackPackContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BackPackScreen extends AbstractContainerScreen<BackPackContainer> {

    private final ResourceLocation GUI = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack.png");

    public BackPackScreen(BackPackContainer backPackContainer, Inventory inventory, Component component) {
        super(backPackContainer, inventory, component);
    }

    @Override
    protected void renderBg(PoseStack poseStack, float p_97788_, int p_97789_, int p_97790_) {
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0, GUI);
        int i = this.getGuiLeft();
        int j = this.getGuiTop();
        blit(poseStack, i, j, 0, 0, getXSize(), getYSize());
    }
}
