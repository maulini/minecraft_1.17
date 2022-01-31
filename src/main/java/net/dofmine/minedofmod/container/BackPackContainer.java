package net.dofmine.minedofmod.container;


import net.dofmine.minedofmod.inventory.BackPackInventory;
import net.dofmine.minedofmod.items.backpack.BackPackItem;
import net.dofmine.minedofmod.job.client.ExtendedHunterJobsEntityPlayer;
import net.dofmine.minedofmod.slot.BackPackSlot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class BackPackContainer extends AbstractContainerMenu {
    private final Player player;
    private final IItemHandler playerInventory;
    private final BackPackInventory backPackInventory;

    public BackPackContainer(int windowId, Player player, BackPackInventory backPackInventory) {
        super(ModContainer.BACK_PACK_CONTAINER.get(), windowId);
        this.player = player;
        this.playerInventory = new InvWrapper(player.getInventory());
        this.backPackInventory = backPackInventory;
        layoutPlayerInventorySlots(7, 86);
        ExtendedHunterJobsEntityPlayer hunter = ExtendedHunterJobsEntityPlayer.get(player);
        addSlotByLevel(hunter.level, backPackInventory);
    }

    private void addSlotByLevel(int level, BackPackInventory backPackInventory) {
        int x = 7;
        int y = 19;
        int max = level == 1 ? 1 : level == 2 ? 2 : level == 3 ? 3 : level == 4 ? 4 : level == 5 ? 6 : level == 6 ? 7 : level == 7 ? 8 : level == 8 ? 9 : level == 9 ? 10 : level == 10 ? 12 : level == 11 ? 13 : level == 12 ? 14 : level == 13 ? 15 : level == 14 ? 16 : level == 15 ? 19 : level == 16 ? 20 : level == 17 ? 21 : level == 18 ? 22 : level == 19 ? 23 : 27;
        for (int i = 0; i < max; i++) {
            if (i == 0) {
                addSlot(new BackPackSlot(backPackInventory, i, x, y));
            }else {
                x += 18;
                if (i == 9 || i == 18) {
                    y += 18;
                    x = 7;
                }
                addSlot(new BackPackSlot(backPackInventory, i, x, y));
            }
            if (i == 5 || i == 10) {
                i++;
                x += 18;
                if (i == 9 || i == 18) {
                    y += 18;
                    x = 7;
                }
                addSlot(new BackPackSlot(backPackInventory, i, x, y));
            }
            if (i == 15 || i == 20) {
                i++;
                x += 18;
                if (i == 9 || i == 18) {
                    y += 18;
                    x = 7;
                }
                addSlot(new BackPackSlot(backPackInventory, i, x, y));
            }
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    public void writeToNBT(ItemStack stack) {
        if (!stack.hasTag()) stack.setTag(new CompoundTag());
        backPackInventory.writeToNBT(stack.getTag());
    }

    public void saveContent(Player player) {
        writeToNBT(player.getMainHandItem());
    }

    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0; i < amount; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }

        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0; j < verAmount; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }

        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = null;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.getItem() != null) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            // Prevents backpack-ception (backpack inside backpack) with shift-click
            if (itemstack.getItem() instanceof BackPackItem) return null;

            if (index < this.backPackInventory.getContainerSize()) {
                if (!this.moveItemStackTo(itemstack1, this.backPackInventory.getContainerSize(), this.slots.size(), true)) return null;
            } else if (!this.moveItemStackTo(itemstack1, 0, this.backPackInventory.getContainerSize(), false)) { return null; }

            if (itemstack1.getCount() == 0) {
                slot.set(Items.AIR.getDefaultInstance());
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    /**
     * @param buttonPressed left click, right click, wheel click, etc.
     * @param flag category (e.g.: hotbar keys)
     */
    @Override
    public void clicked(int slotIndex, int buttonPressed, ClickType flag, Player player) {
        // Prevents from removing current backpack
        if (flag == ClickType.PICKUP_ALL && buttonPressed == player.getInventory().selected) return;
        if (slotIndex - this.backPackInventory.getContainerSize() - 27 == player.getInventory().getContainerSize()) return;
        super.clicked(slotIndex, buttonPressed, flag, player);
    }

}
