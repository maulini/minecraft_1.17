package net.dofmine.minedofmod.tileentity;

import net.dofmine.minedofmod.items.ModItems;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BackPackEntity extends ItemEntity {
    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public BackPackEntity(Level level) {
        super((EntityType<? extends ItemEntity>) ModTileEntity.BACk_PACK_ENTITY.get(), level);
    }

    public BackPackEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        NonNullList<ItemStack> nonNullList = NonNullList.withSize(27, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag, nonNullList);
        for (int i = 0; i < nonNullList.size(); i++) {
            itemHandler.insertItem(i, nonNullList.get(i), false);
        }
    }

    @Override
    public boolean save(CompoundTag compoundTag) {
        NonNullList<ItemStack> nonNullList = NonNullList.create();
        for (int i = 0; i < 28; i++) {
            nonNullList.add(itemHandler.getStackInSlot(i));
        }
        ContainerHelper.saveAllItems(compoundTag, nonNullList);
        return true;
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(27) {
            @Override
            protected void onContentsChanged(int slot) {
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return !stack.getItem().equals(ModItems.BACK_PACK.get());
            }

            @Override
            public int getSlotLimit(int slot) {
                return 64;
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

}
