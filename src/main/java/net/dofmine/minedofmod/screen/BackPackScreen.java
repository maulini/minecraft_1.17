package net.dofmine.minedofmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.container.BackPackContainer;
import net.dofmine.minedofmod.job.client.ExtendedHunterJobsEntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.lang.reflect.Field;

@OnlyIn(Dist.CLIENT)
public class BackPackScreen extends AbstractContainerScreen<BackPackContainer> {

    private final ResourceLocation GUI1 = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack/back_pack1.png");
    private final ResourceLocation GUI2 = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack/back_pack2.png");
    private final ResourceLocation GUI3 = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack/back_pack3.png");
    private final ResourceLocation GUI4 = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack/back_pack4.png");
    private final ResourceLocation GUI5 = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack/back_pack5.png");
    private final ResourceLocation GUI6 = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack/back_pack6.png");
    private final ResourceLocation GUI7 = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack/back_pack7.png");
    private final ResourceLocation GUI8 = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack/back_pack8.png");
    private final ResourceLocation GUI9 = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack/back_pack9.png");
    private final ResourceLocation GUI10 = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack/back_pack10.png");
    private final ResourceLocation GUI11 = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack/back_pack11.png");
    private final ResourceLocation GUI12 = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack/back_pack12.png");
    private final ResourceLocation GUI13 = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack/back_pack13.png");
    private final ResourceLocation GUI14 = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack/back_pack14.png");
    private final ResourceLocation GUI15 = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack/back_pack15.png");
    private final ResourceLocation GUI16 = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack/back_pack16.png");
    private final ResourceLocation GUI17 = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack/back_pack17.png");
    private final ResourceLocation GUI18 = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack/back_pack18.png");
    private final ResourceLocation GUI19 = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack/back_pack19.png");
    private final ResourceLocation GUI20 = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/back_pack/back_pack20.png");
    private final int level;

    public BackPackScreen(BackPackContainer backPackContainer, Inventory inventory, Component component) {
        super(backPackContainer, inventory, component);
        this.level = ExtendedHunterJobsEntityPlayer.get(Minecraft.getInstance().player).level;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float p_97788_, int p_97789_, int p_97790_) {
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        Field field = null;
        try {
            field = getClass().getDeclaredField("GUI" + level);
            RenderSystem.setShaderTexture(0, (ResourceLocation) field.get(this));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        int i = this.getGuiLeft();
        int j = this.getGuiTop();
        blit(poseStack, i, j, 0, 0, getXSize(), getYSize());
    }
}
