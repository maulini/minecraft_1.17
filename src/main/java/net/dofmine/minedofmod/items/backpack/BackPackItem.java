package net.dofmine.minedofmod.items.backpack;

import net.dofmine.minedofmod.container.BackPackContainer;
import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.tileentity.BackPackEntity;
import net.dofmine.minedofmod.tileentity.ModTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;

public class BackPackItem extends Item {

    private BackPackEntity backPackEntity;

    public BackPackItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openGui(serverPlayer, createMenuProvider(player.getItemInHand(interactionHand)));
        }
        return super.use(level, player, interactionHand);
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
                itemStack.setEntityRepresentation(ModTileEntity.BACk_PACK_ENTITY.get().create(player.level));
                return new BackPackContainer(i, player, itemStack, inventory);
            }
        };
    }

}
