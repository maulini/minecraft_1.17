package net.dofmine.minedofmod.tileentity;

import net.dofmine.minedofmod.data.recipes.ModRecipeType;
import net.dofmine.minedofmod.data.recipes.crafting.CraftingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class CraftingTableTile extends BlockEntity {

    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);
    private boolean isCraft;
    private boolean deleteOutPut;

    public CraftingTableTile(BlockPos blockPos, BlockState blockState) {
        super(ModTileEntity.CRAFTING_TABLE_TILE.get(), blockPos, blockState);
    }

    @Override
    public void load(CompoundTag p_155245_) {
        itemHandler.deserializeNBT(p_155245_.getCompound("inv"));
        super.load(p_155245_);
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        p_187471_.put("inv", itemHandler.serializeNBT());
        super.saveAdditional(p_187471_);
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler(17) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return true;
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

    public void craft() {
        RecipeWrapper recipeWrapper = new RecipeWrapper(new ItemStackHandler(17));
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            recipeWrapper.setItem(i, itemHandler.getStackInSlot(i));
        }

        Optional<CraftingRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(ModRecipeType.CRAFTING_RECIPE, recipeWrapper, level);
        recipe.ifPresentOrElse(iRecipe -> {
            ItemStack output = iRecipe.getResultItem();
            if (!isCraft)
            craftTheItem(output);
            setChanged();
        }, this::deleteOutPut);

        if (itemHandler.getStackInSlot(16).getItem().equals(Items.AIR) && isCraft && !deleteOutPut) {
            deleteIngredient();
            isCraft = false;
        }
    }

    private void craftTheItem(ItemStack output) {
        isCraft = true;
        deleteOutPut = false;
        itemHandler.insertItem(16, output, false);
    }

    private void deleteOutPut() {
        deleteOutPut = true;
        isCraft = false;
        itemHandler.extractItem(16, 1, false);
    }

    private void deleteIngredient() {
        for (int i = 0; i < 16; i++) {
            itemHandler.extractItem(i, 64, false);
        }
    }

}
