package net.dofmine.minedofmod.screen;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class TeleportateList extends ContainerObjectSelectionList<TeleportateList.Entry> {

    private final ResourceLocation GUI = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/teleport_gui_extended.png");
    @OnlyIn(Dist.CLIENT)
    final TeleportateScreen teleportateScreen;

    public TeleportateList(TeleportateScreen p_97399_, Minecraft p_97400_, Player player) {
        super(p_97400_, p_97399_.width - 45, 167, 90, 190, 40);
        this.teleportateScreen = p_97399_;
        int x = p_97399_.width / 2 - 155;
        int y = p_97399_.height / 6 + 42;
        for (BlockPos blockPos : p_97399_.blockPosList) {
            int nbItemToRemove = 0;
            if (player.getOnPos().getX() - blockPos.getX() > 10 && player.getOnPos().getX() - blockPos.getX() < 50||
                    player.getOnPos().getY() - blockPos.getY() > 10 && player.getOnPos().getY() - blockPos.getY() < 50||
                    player.getOnPos().getZ() - blockPos.getZ() > 10 && player.getOnPos().getZ() - blockPos.getZ() < 50) {
                nbItemToRemove = 1;
            }else if (player.getOnPos().getX() - blockPos.getX() > 50 && player.getOnPos().getX() - blockPos.getX() < 150||
                    player.getOnPos().getY() - blockPos.getY() > 50 && player.getOnPos().getY() - blockPos.getY() < 150||
                    player.getOnPos().getZ() - blockPos.getZ() > 50 && player.getOnPos().getZ() - blockPos.getZ() < 150) {
                nbItemToRemove = 2;
            }else if (player.getOnPos().getX() - blockPos.getX() > 150 && player.getOnPos().getX() - blockPos.getX() < 250||
                    player.getOnPos().getY() - blockPos.getY() > 150 && player.getOnPos().getY() - blockPos.getY() < 250||
                    player.getOnPos().getZ() - blockPos.getZ() > 150 && player.getOnPos().getZ() - blockPos.getZ() < 250) {
                nbItemToRemove = 4;
            }else if (player.getOnPos().getX() - blockPos.getX() > 250 && player.getOnPos().getX() - blockPos.getX() < 350||
                    player.getOnPos().getY() - blockPos.getY() > 250 && player.getOnPos().getY() - blockPos.getY() < 350||
                    player.getOnPos().getZ() - blockPos.getZ() > 250 && player.getOnPos().getZ() - blockPos.getZ() < 350) {
                nbItemToRemove = 6;
            }else if (player.getOnPos().getX() - blockPos.getX() > 350 ||
                    player.getOnPos().getY() - blockPos.getY() > 350 ||
                    player.getOnPos().getZ() - blockPos.getZ() > 350) {
                nbItemToRemove = 10;
            }
            this.addEntry(new TeleportateList.TeleportateEntry(x, y, blockPos, nbItemToRemove, player));
            y += 60;
        }
        setRenderBackground(false);
        setRenderTopAndBottom(false);
    }

    @Override
    protected void renderBackground(PoseStack stack) {
        RenderSystem.setShaderTexture(0, GUI);
        blit(stack, teleportateScreen.width / 4, teleportateScreen.height / 4 - 25, 0, 0, 239, 167);
    }

    public int getRowWidth() {
        return 10;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract static class Entry extends ContainerObjectSelectionList.Entry<TeleportateList.Entry> {
    }

    @OnlyIn(Dist.CLIENT)
    public class TeleportateEntry extends TeleportateList.Entry {
        private final Button button;
        private final int y;

        TeleportateEntry(int x, int y, BlockPos blockPos, int nbItemToRemove, Player player) {
            this.y = y;
            button = new Button(x, y, 200, 20, new TextComponent("X : " + blockPos.getX() + " / Y : " + blockPos.getY() + " / Z : " + blockPos.getZ()), button -> {
                if ((player.getInventory().countItem(ModItems.COINS.get()) != 0 && player.getInventory().countItem(ModItems.COINS.get()) >= nbItemToRemove) || player.isCreative()) {
                    if (!player.isCreative()) {
                        ItemStack itemStack = player.getInventory().items.stream().filter(itemStackw -> itemStackw.getItem().equals(ModItems.COINS.get())).findFirst().get();
                        itemStack.setCount(itemStack.getCount() - nbItemToRemove);
                    }
                    player.moveTo(blockPos, 0f, 0f);
                    player.fallDistance = 0.0f;
                    Minecraft.getInstance().popGuiLayer();
                }
            }, (button, stack, p_93755_, p_93756_) -> {
                Minecraft.getInstance().font.draw(stack, "Coast " + nbItemToRemove, p_93755_, p_93756_, 0);
            });
        }

        public void render(PoseStack stack, int p_97464_, int p_97465_, int p_97466_, int p_97467_, int p_97468_, int p_97469_, int p_97470_, boolean p_97471_, float p_97472_) {
            this.button.x = teleportateScreen.width / 4 + 15;
            this.button.y = p_97465_;
            button.render(stack, p_97469_, p_97470_, p_97472_);
        }

        public List<? extends GuiEventListener> children() {
            return ImmutableList.of(this.button);
        }

        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.of(this.button);
        }
    }
}
