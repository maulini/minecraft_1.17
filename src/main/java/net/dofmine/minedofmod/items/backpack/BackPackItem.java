package net.dofmine.minedofmod.items.backpack;

import net.dofmine.minedofmod.container.BackPackContainer;
import net.dofmine.minedofmod.inventory.BackPackInventory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;

public class BackPackItem extends Item {

    public BackPackItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openGui(serverPlayer, createMenuProvider(player.getItemInHand(interactionHand)));
            return InteractionResultHolder.success(player.getItemInHand(interactionHand));
        }
        return InteractionResultHolder.fail(player.getItemInHand(interactionHand));
    }

    private MenuProvider createMenuProvider(ItemStack itemStack) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return new TranslatableComponent("screen.mods.back_pack");
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return new BackPackContainer(i, player, new BackPackInventory(itemStack, 27));
            }
        };
    }

}
