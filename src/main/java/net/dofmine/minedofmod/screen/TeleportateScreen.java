package net.dofmine.minedofmod.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.items.key.TeleportateKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TeleportateScreen extends Screen {

    private final ResourceLocation GUI = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/teleport_gui.png");
    private final TeleportateKey teleportateKey;
    private final Player player;
    private final Level level;
    private final ItemStack teleportateKeyStack;
    protected int imageWidth = 176;
    protected int imageHeight = 166;

    public TeleportateScreen(ItemStack teleportateKey, Player player, Level level) {
        super(new TranslatableComponent("teleporte.title"));
        this.teleportateKeyStack = teleportateKey;
        this.teleportateKey = (TeleportateKey) teleportateKey.getItem();
        minecraft = Minecraft.getInstance();
        this.player = player;
        this.level = level;

    }

    @Override
    protected void init() {
        int i = this.width / 2 - 155;
        int j = this.height / 6 + 48 - 6;
        for (BlockPos blockPos : teleportateKey.getBlockPositions(teleportateKeyStack)) {
            this.addRenderableWidget(new Button(i, j, 150, 20, new TextComponent("X : " + blockPos.getX() + " / Y : " + blockPos.getY() + " / Z : " + blockPos.getZ()), button -> {
                this.minecraft.popGuiLayer();
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
                if ((player.getInventory().countItem(ModItems.COINS.get()) != 0 && player.getInventory().countItem(ModItems.COINS.get()) >= nbItemToRemove) || player.isCreative()) {
                    if (!player.isCreative()) {
                        ItemStack itemStack = player.getInventory().items.stream().filter(itemStackw -> itemStackw.getItem().equals(ModItems.COINS.get())).findFirst().get();
                        itemStack.setCount(itemStack.getCount() - nbItemToRemove);
                    }
                    player.moveTo(blockPos, 0f, 0f);
                    player.fallDistance = 0.0f;
                }
            }));
            j += 60;
        }
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
