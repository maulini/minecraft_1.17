package net.dofmine.minedofmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.container.LightningChannelerContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.FurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;

public class LightningChannelerScreen extends AbstractContainerScreen<LightningChannelerContainer> {

    private final ResourceLocation GUI = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/lightning_channeler_gui.png");

    public LightningChannelerScreen(LightningChannelerContainer screenContainer, Inventory inventory, Component component) {
        super(screenContainer, inventory, component);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void renderBg(PoseStack p_97787_, float p_97788_, int p_97789_, int p_97790_) {
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0, GUI);
        int i = this.getGuiLeft();
        int j = this.getGuiTop();
        blit(p_97787_, i, j, 0, 0, getXSize(), getYSize());

        if (menu.isLightningStorm()) {
            blit(p_97787_, i + 82, j + 9, 176, 0, 13, 17);
        }
    }
}
