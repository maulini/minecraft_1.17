package net.dofmine.minedofmod.tileentity;

import net.dofmine.minedofmod.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CraftingTableTile extends BlockEntity {

    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public CraftingTableTile(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    public CraftingTableTile(BlockPos blockPos, BlockState blockState) {
        super(ModTileEntity.CRAFTING_TABLE_TILE.get(), blockPos, blockState);
    }

    @Override
    public void load(CompoundTag p_155245_) {
        itemHandler.deserializeNBT(p_155245_.getCompound("inv"));
        super.load(p_155245_);
    }

    @Override
    public CompoundTag save(CompoundTag p_58888_) {
        p_58888_.put("inv", itemHandler.serializeNBT());
        return super.save(p_58888_);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(17) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                switch (slot) {
                    case 0: return stack.getItem() == Items.GLASS_PANE;
                    case 1: return stack.getItem() == ModItems.TITANUIM_INGOT.get() || stack.getItem() == ModItems.TITANUIM_NUGGET.get();

                    default: return false;
                }
            }

            @Override
            public int getSlotLimit(int slot) {
                return 1;
            }

            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (!isItemValid(slot, stack)) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }

        return super.getCapability(cap, side);
    }

    public void lightningHasStruck() {
        boolean hasFocusInFirstSlot = this.itemHandler.getStackInSlot(0).getCount() > 0 && itemHandler.getStackInSlot(0).getItem() == Items.GLASS_PANE;
        boolean hasTitaniumInSecondSlot = itemHandler.getStackInSlot(1).getCount() > 0 && itemHandler.getStackInSlot(1).getItem() == ModItems.TITANUIM_INGOT.get();

        if (hasFocusInFirstSlot && hasTitaniumInSecondSlot) {
            itemHandler.getStackInSlot(0).shrink(1);
            itemHandler.getStackInSlot(1).shrink(1);

            itemHandler.insertItem(1, new ItemStack(ModItems.TITANUIM_NUGGET.get()), false);
        }
    }
}
