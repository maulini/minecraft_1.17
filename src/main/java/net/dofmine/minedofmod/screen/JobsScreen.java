package net.dofmine.minedofmod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.job.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class JobsScreen extends Screen {

    private final ResourceLocation JOBS = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/jobs_gui.png");
    private final ResourceLocation XP_BARRE = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/custom_bar/xp_barre.png");
    private final int IMAGE_WIDTH = 75;
    private final int IMAGE_HEIGHT = 6;
    private static final int WHITE_COLOR = 0xFFFFFFFF;

    public JobsScreen(Component p_96550_) {
        super(p_96550_);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0, JOBS);
        blit(stack, width / 4, 0,0,0,252,250);
        ExtendedHunterJobsEntityPlayer hunter = ExtendedHunterJobsEntityPlayer.get();
        ExtendedMinerJobsEntityPlayer miner = ExtendedMinerJobsEntityPlayer.get();
        ExtendedFarmerJobsEntityPlayer farmer = ExtendedFarmerJobsEntityPlayer.get();
        ExtendedWizardJobsEntityPlayer wizard = ExtendedWizardJobsEntityPlayer.get();
        ExtendedLocksmithJobsEntityPlayer locksmith = ExtendedLocksmithJobsEntityPlayer.get();
        Minecraft.getInstance().font.draw(stack, String.valueOf(hunter.level), width - 165, 110, WHITE_COLOR);
        Minecraft.getInstance().font.draw(stack, String.valueOf(miner.level), width / 2 - 70, 110, WHITE_COLOR);
        Minecraft.getInstance().font.draw(stack, String.valueOf(farmer.level), width - 165, 235, WHITE_COLOR);
        Minecraft.getInstance().font.draw(stack, String.valueOf(wizard.level), width / 2 - 70, 235, WHITE_COLOR);
        Minecraft.getInstance().font.draw(stack, String.valueOf(locksmith.level), width / 2, 200, WHITE_COLOR);
        addXpFarmer(stack, farmer);
        addXpHunter(stack, hunter);
        addXpMiner(stack, miner);
        addXpWizard(stack, wizard);
        if (locksmith.level == 20) {
            addXpLocksmith(stack);
        }
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    private void addXpLocksmith(PoseStack stack) {
        RenderSystem.setShaderTexture(0, XP_BARRE);
        blit(stack, width / 2 - 32, 188, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
    }

    private void addXpWizard(PoseStack stack, ExtendedWizardJobsEntityPlayer wizard) {
        RenderSystem.setShaderTexture(0, XP_BARRE);
        blit(stack, width / 2 - 105, 223, 0, 0, getImageWidth(wizard.xp, wizard.maxXp), IMAGE_HEIGHT);
    }

    private void addXpMiner(PoseStack stack, ExtendedMinerJobsEntityPlayer miner) {
        RenderSystem.setShaderTexture(0, XP_BARRE);
        blit(stack, width / 2 - 104, 100, 0, 0, getImageWidth(miner.xp, miner.maxXp), IMAGE_HEIGHT);
    }

    private void addXpHunter(PoseStack stack, ExtendedHunterJobsEntityPlayer hunter) {
        RenderSystem.setShaderTexture(0, XP_BARRE);
        blit(stack, width - 199, 100, 0, 0, getImageWidth(hunter.xp, hunter.maxXp), IMAGE_HEIGHT);
    }

    private void addXpFarmer(PoseStack stack, ExtendedFarmerJobsEntityPlayer farmer) {
        RenderSystem.setShaderTexture(0, XP_BARRE);
        blit(stack, width - 198, 223, 0, 0, getImageWidth(farmer.xp, farmer.maxXp), IMAGE_HEIGHT);
    }

    private int getImageWidth(long xp, long maxXp) {
        int percentXp = (int) (xp * 100 / maxXp);
        int value = (percentXp * IMAGE_WIDTH) / 100;
        return Math.min(value, IMAGE_WIDTH);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
