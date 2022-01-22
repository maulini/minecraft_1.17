package net.dofmine.minedofmod.container;

import net.dofmine.minedofmod.items.drink.FilterItem;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.UUID;

public class WaterCollectorContainer extends AbstractContainerMenu {
    private final Container waterCollector;
    private final ContainerData waterCollectorData;
    private final Slot ingredientSlot;
    private final Slot filterSlot;

    public WaterCollectorContainer(int p_39090_, Inventory p_39091_) {
        this(p_39090_, p_39091_, new SimpleContainer(3), new SimpleContainerData(4));
    }

    public WaterCollectorContainer(int p_39093_, Inventory p_39094_, Container p_39095_, ContainerData p_39096_) {
        super(ModContainer.WATER_COLLECTOR_CONTAINER.get(), p_39093_);
        checkContainerSize(p_39095_, 3);
        checkContainerDataCount(p_39096_, 3);
        this.waterCollector = p_39095_;
        this.waterCollectorData = p_39096_;
        this.filterSlot = this.addSlot(new WaterCollectorContainer.FilterSlot(p_39095_, 0, 56, 17));
        this.ingredientSlot = this.addSlot(new WaterCollectorContainer.IngredientsSlot(p_39095_, 1, 56, 53));
        this.addSlot(new WaterCollectorContainer.ResultSlot(p_39095_, 2, 116, 35));
        this.addDataSlots(p_39096_);

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(p_39094_, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(p_39094_, k, 8 + k * 18, 142));
        }

    }

    public boolean stillValid(Player p_39098_) {
        return this.waterCollector.stillValid(p_39098_);
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
    private static final int TE_INVENTORY_SLOT_COUNT = 3;  // must match TileEntityInventoryBasic.NUMBER_OF_SLOTS

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
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

    public int getActualWater() {
        return waterCollectorData.get(0);
    }

    public int getFilterProgress() {
        return waterCollectorData.get(1);
    }

    public boolean canFilter() {
        return !filterSlot.getItem().isEmpty() && !ingredientSlot.getItem().isEmpty() && getActualWater() == 100;
    }

    static class IngredientsSlot extends Slot {
        public IngredientsSlot(Container p_39115_, int p_39116_, int p_39117_, int p_39118_) {
            super(p_39115_, p_39116_, p_39117_, p_39118_);
        }

        public boolean mayPlace(ItemStack p_39121_) {
            return p_39121_.is(Items.GLASS_BOTTLE);
        }

        public int getMaxStackSize() {
            return 64;
        }
    }

    static class FilterSlot extends Slot {
        public FilterSlot(Container p_39123_, int p_39124_, int p_39125_, int p_39126_) {
            super(p_39123_, p_39124_, p_39125_, p_39126_);
        }

        public boolean mayPlace(ItemStack p_39132_) {
            return mayPlaceItem(p_39132_);
        }

        public int getMaxStackSize() {
            return 1;
        }

        public static boolean mayPlaceItem(ItemStack p_39134_) {
            return p_39134_.getItem() instanceof FilterItem;
        }
    }

    static class ResultSlot extends Slot {
        public ResultSlot(Container p_39123_, int p_39124_, int p_39125_, int p_39126_) {
            super(p_39123_, p_39124_, p_39125_, p_39126_);
        }

        public boolean mayPlace(ItemStack p_39132_) {
            return false;
        }

        public int getMaxStackSize() {
            return 64;
        }

    }
}
