package net.dofmine.minedofmod.screen;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dofmine.minedofmod.MinedofMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class BlockList extends ContainerObjectSelectionList<BlockList.Entry> {

    private final ResourceLocation GUI = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/teleport_gui.png");
    final ChunkFinderScreen controlsScreen;

    public BlockList(ChunkFinderScreen p_97399_, Minecraft p_97400_) {
        super(p_97400_, p_97399_.width - 110, 167, 58, 190, 16);
        this.controlsScreen = p_97399_;
        int y = 0;
        for(Map.Entry<ResourceLocation, Integer> entry : p_97399_.itemInChunk.entrySet()) {
            this.addEntry(new BlockList.ImageEntry(entry.getKey(), new TextComponent("x " + entry.getValue()), y));
            y += 16;
        }
        setRenderBackground(false);
        setRenderTopAndBottom(false);
        setScrollAmount(5);
    }

    @Override
    protected void renderBackground(PoseStack stack) {
        RenderSystem.setShaderTexture(0, GUI);
        blit(stack, controlsScreen.width / 4 + 25, controlsScreen.height / 4 - 25, 0, 0, 176, 167);
    }

    public int getRowWidth() {
        return 10;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract static class Entry extends ContainerObjectSelectionList.Entry<BlockList.Entry> {
    }

    @OnlyIn(Dist.CLIENT)
    public class ImageEntry extends BlockList.Entry {
        private final ResourceLocation imageLocation;
        private final Component name;
        private final ImageButton imageButton;
        private final int y;

        ImageEntry(final ResourceLocation p_97451_, final Component p_97452_, int y) {
            this.imageLocation = p_97451_;
            this.name = p_97452_;
            this.y = y;
            this.imageButton = new ImageButton(controlsScreen.width / 4 + 37, y, 16, 16, 0, 0, 0, imageLocation, 16, 16, (p_97479_) -> {
            });
        }

        public void render(PoseStack stack, int p_97464_, int p_97465_, int p_97466_, int p_97467_, int p_97468_, int p_97469_, int p_97470_, boolean p_97471_, float p_97472_) {
            Minecraft.getInstance().font.draw(stack, name.getString(), controlsScreen.width / 4 + 57, p_97465_ + 3, 0xFFFFFFFF);
            this.imageButton.x = controlsScreen.width / 4 + 37;
            this.imageButton.y = p_97465_;
            imageButton.render(stack, p_97469_, p_97470_, p_97472_);
        }

        public List<? extends GuiEventListener> children() {
            return ImmutableList.of(this.imageButton);
        }

        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(this.imageButton);
        }
    }
}
