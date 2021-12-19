package net.dofmine.minedofmod.container;


import net.dofmine.minedofmod.items.ModItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class BackPackContainer extends AbstractContainerMenu {
    private final Player player;
    private final IItemHandler playerInventory;

    public BackPackContainer(int windowId, Player player, ItemStack itemStack, Inventory playerInventory) {
        super(ModContainer.BACK_PACK_CONTAINER.get(), windowId);

        this.player = player;
        this.playerInventory = new InvWrapper(playerInventory);
        layoutPlayerInventorySlots(7, 86);
        if (itemStack != null) {
            itemStack.getEntityRepresentation().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
                addSlot(new SlotItemHandler(h, 0, 7, 19));
                addSlot(new SlotItemHandler(h, 1, 25, 19));
                addSlot(new SlotItemHandler(h, 2, 43, 19));
                addSlot(new SlotItemHandler(h, 3, 61, 19));
                addSlot(new SlotItemHandler(h, 4, 79, 19));
                addSlot(new SlotItemHandler(h, 5, 97, 19));
                addSlot(new SlotItemHandler(h, 6, 115, 19));
                addSlot(new SlotItemHandler(h, 7, 133, 19));
                addSlot(new SlotItemHandler(h, 8, 151, 19));
                addSlot(new SlotItemHandler(h, 9, 7, 37));
                addSlot(new SlotItemHandler(h, 10, 25, 37));
                addSlot(new SlotItemHandler(h, 11, 43, 37));
                addSlot(new SlotItemHandler(h, 12, 61, 37));
                addSlot(new SlotItemHandler(h, 13, 79, 37));
                addSlot(new SlotItemHandler(h, 14, 97, 37));
                addSlot(new SlotItemHandler(h, 15, 115, 37));
                addSlot(new SlotItemHandler(h, 16, 133, 37));
                addSlot(new SlotItemHandler(h, 17, 151, 37));
                addSlot(new SlotItemHandler(h, 18, 7, 55));
                addSlot(new SlotItemHandler(h, 19, 25, 55));
                addSlot(new SlotItemHandler(h, 20, 43, 55));
                addSlot(new SlotItemHandler(h, 21, 61, 55));
                addSlot(new SlotItemHandler(h, 22, 79, 55));
                addSlot(new SlotItemHandler(h, 23, 97, 55));
                addSlot(new SlotItemHandler(h, 24, 115, 55));
                addSlot(new SlotItemHandler(h, 25, 133, 55));
                addSlot(new SlotItemHandler(h, 26, 151, 55));
            });
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
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

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 17;  // must match TileEntityInventoryBasic.NUMBER_OF_SLOTS

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || sourceSlot.getItem() == null) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.safeInsert(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

}
