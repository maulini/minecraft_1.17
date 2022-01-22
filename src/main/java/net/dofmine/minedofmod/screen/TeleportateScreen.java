package net.dofmine.minedofmod.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dofmine.minedofmod.items.key.TeleportateKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class TeleportateScreen extends Screen {

    public final TeleportateKey teleportateKey;
    private final Player player;
    public final ItemStack teleportateKeyStack;
    public final List<BlockPos> blockPosList;
    private TeleportateList teleportateList;

    public TeleportateScreen(ItemStack teleportateKey, Player player) {
        super(new TranslatableComponent("teleporte.title"));
        this.teleportateKeyStack = teleportateKey;
        this.teleportateKey = (TeleportateKey) teleportateKey.getItem();
        minecraft = Minecraft.getInstance();
        this.player = player;
        this.blockPosList = this.teleportateKey.getBlockPositions(teleportateKey);
    }

    @Override
    protected void init() {
        this.teleportateList = new TeleportateList(this, minecraft, player);
        addWidget(teleportateList);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        teleportateList.render(stack, mouseX, mouseY, partialTicks);
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
