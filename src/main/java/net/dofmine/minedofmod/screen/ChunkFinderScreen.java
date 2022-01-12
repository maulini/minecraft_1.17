package net.dofmine.minedofmod.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class ChunkFinderScreen extends Screen {

    public final Map<ResourceLocation, Integer> itemInChunk;
    private BlockList blockList;

    public ChunkFinderScreen(Map<ResourceLocation, Integer> itemInChunk) {
        super(new TranslatableComponent("chunk.finder.screen"));
        this.itemInChunk = itemInChunk;
    }

    @Override
    protected void init() {
        this.blockList = new BlockList(this, Minecraft.getInstance());
        addWidget(blockList);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        blockList.render(stack, mouseX, mouseY, partialTicks);
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
