package net.dofmine.minedofmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.container.WaterCollectorContainer;
import net.dofmine.minedofmod.tileentity.WaterCollectorTile;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class WaterCollectorScreen extends AbstractContainerScreen<WaterCollectorContainer> {

    private final ResourceLocation GUI = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/container/water_collector.png");
    private final int IMAGE_HEIGHT = 16;
    private final int IMAGE_WIDTH = 22;

    public WaterCollectorScreen(WaterCollectorContainer screenContainer, Inventory inventory, Component component) {
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

        blit(p_97787_, i + 60, j + 35, 179, 0, 8, menu.getActualWater());
        if (menu.canFilter()) {
            blit(p_97787_, i + 80, j + 35, 177, 16, getImageWidth(), 15);
        }
    }

    private int getImageHeight() {
        int percentWater = (int) (menu.getActualWater() * 100 / WaterCollectorTile.MAX_WATER);
        int value = (percentWater * IMAGE_HEIGHT) / 100;
        return Math.min(value, IMAGE_HEIGHT);
    }

    private int getImageWidth() {
        int percentWater = (int) (menu.getFilterProgress() * 100 / WaterCollectorTile.FILTER_DURATION);
        int value = (percentWater * IMAGE_WIDTH) / 100;
        return Math.abs(Math.min(value, IMAGE_WIDTH) - IMAGE_WIDTH);
    }
}
