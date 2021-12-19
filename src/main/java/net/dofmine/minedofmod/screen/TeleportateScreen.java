package net.dofmine.minedofmod.screen;

import com.google.common.util.concurrent.AtomicDouble;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.block.custom.SpecialDoorTeleportate;
import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.items.key.TeleportateKey;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.OptionsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SkinCustomizationScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.SelectorComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.TeleportCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityTeleportEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class TeleportateScreen extends Screen {

    private final ResourceLocation GUI = new ResourceLocation(MinedofMod.MODS_ID, "textures/gui/teleport_gui.png");
    private final TeleportateKey telePortateKey;
    private final Player player;
    private final Level level;
    protected int imageWidth = 176;
    protected int imageHeight = 166;

    public TeleportateScreen(TeleportateKey teleportateKey, Player player, Level level) {
        super(new TranslatableComponent("teleporte.title"));
        this.telePortateKey = teleportateKey;
        minecraft = Minecraft.getInstance();
        this.player = player;
        this.level = level;

    }

    @Override
    protected void init() {
        int i = this.width / 2 - 155;
        int j = this.height / 6 + 48 - 6;
        for (SpecialDoorTeleportate specialDoorTeleportate : telePortateKey.getDoorTeleportates()) {
            this.addRenderableWidget(new Button(i, j, 150, 20, new TextComponent("X : " + specialDoorTeleportate.getBlockPos().getX() + " / Y : " + specialDoorTeleportate.getBlockPos().getY() + " / Z : " + specialDoorTeleportate.getBlockPos().getZ()), button -> {
                this.minecraft.popGuiLayer();
                int nbItemToRemove = 0;
                if (player.getOnPos().getX() - specialDoorTeleportate.getBlockPos().getX() > 10 && player.getOnPos().getX() - specialDoorTeleportate.getBlockPos().getX() < 50||
                        player.getOnPos().getY() - specialDoorTeleportate.getBlockPos().getY() > 10 && player.getOnPos().getY() - specialDoorTeleportate.getBlockPos().getY() < 50||
                        player.getOnPos().getZ() - specialDoorTeleportate.getBlockPos().getZ() > 10 && player.getOnPos().getZ() - specialDoorTeleportate.getBlockPos().getZ() < 50) {
                    nbItemToRemove = 1;
                }else if (player.getOnPos().getX() - specialDoorTeleportate.getBlockPos().getX() > 50 && player.getOnPos().getX() - specialDoorTeleportate.getBlockPos().getX() < 150||
                        player.getOnPos().getY() - specialDoorTeleportate.getBlockPos().getY() > 50 && player.getOnPos().getY() - specialDoorTeleportate.getBlockPos().getY() < 150||
                        player.getOnPos().getZ() - specialDoorTeleportate.getBlockPos().getZ() > 50 && player.getOnPos().getZ() - specialDoorTeleportate.getBlockPos().getZ() < 150) {
                    nbItemToRemove = 2;
                }else if (player.getOnPos().getX() - specialDoorTeleportate.getBlockPos().getX() > 150 && player.getOnPos().getX() - specialDoorTeleportate.getBlockPos().getX() < 250||
                        player.getOnPos().getY() - specialDoorTeleportate.getBlockPos().getY() > 150 && player.getOnPos().getY() - specialDoorTeleportate.getBlockPos().getY() < 250||
                        player.getOnPos().getZ() - specialDoorTeleportate.getBlockPos().getZ() > 150 && player.getOnPos().getZ() - specialDoorTeleportate.getBlockPos().getZ() < 250) {
                    nbItemToRemove = 4;
                }else if (player.getOnPos().getX() - specialDoorTeleportate.getBlockPos().getX() > 250 && player.getOnPos().getX() - specialDoorTeleportate.getBlockPos().getX() < 350||
                        player.getOnPos().getY() - specialDoorTeleportate.getBlockPos().getY() > 250 && player.getOnPos().getY() - specialDoorTeleportate.getBlockPos().getY() < 350||
                        player.getOnPos().getZ() - specialDoorTeleportate.getBlockPos().getZ() > 250 && player.getOnPos().getZ() - specialDoorTeleportate.getBlockPos().getZ() < 350) {
                    nbItemToRemove = 6;
                }else if (player.getOnPos().getX() - specialDoorTeleportate.getBlockPos().getX() > 350 ||
                        player.getOnPos().getY() - specialDoorTeleportate.getBlockPos().getY() > 350 ||
                        player.getOnPos().getZ() - specialDoorTeleportate.getBlockPos().getZ() > 350) {
                    nbItemToRemove = 10;
                }
                if (player.getInventory().countItem(ModItems.COINS.get()) != 0 && player.getInventory().countItem(ModItems.COINS.get()) >= nbItemToRemove) {
                    ItemStack itemStack = player.getInventory().items.stream().filter(itemStackw -> itemStackw.getItem().equals(ModItems.COINS.get())).findFirst().get();
                    itemStack.setCount(itemStack.getCount() - nbItemToRemove);
                    player.moveTo(specialDoorTeleportate.getBlockPos(), 0f, 0f);
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
