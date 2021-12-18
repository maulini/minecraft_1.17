package net.dofmine.minedofmod.tileentity;

import net.dofmine.minedofmod.data.recipes.lightning.LightningChannelerRecipe;
import net.dofmine.minedofmod.data.recipes.ModRecipeType;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fmllegacy.TextComponentMessageFormatHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class LightningChannelerTile extends BlockEntity implements Tickable  {

    private final ItemStackHandler itemHandler = createHandler();
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> itemHandler);

    public LightningChannelerTile(BlockPos blockPos, BlockState blockState) {
        super(ModTileEntity.LIGHTNING_CHANNELER_TILE.get(), blockPos, blockState);
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
        return new ItemStackHandler(2) {
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

    private void strikeLightning() {
        if (!level.isClientSide) {
            EntityType.LIGHTNING_BOLT.spawn((ServerLevel) level, null, null, getBlockPos(), MobSpawnType.TRIGGERED, true, true);
        }
    }

    public void craft() {
        RecipeWrapper recipeWrapper = new RecipeWrapper(new ItemStackHandler(2));
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            recipeWrapper.setItem(i, itemHandler.getStackInSlot(i));
        }

        Optional<LightningChannelerRecipe> recipe = level.getRecipeManager()
                .getRecipeFor(ModRecipeType.LIGHTNING_RECIPE, recipeWrapper, level);

        recipe.ifPresent(iRecipe -> {
            ItemStack output = iRecipe.getResultItem();

            if(iRecipe.getWeather().equals(LightningChannelerRecipe.Weather.CLEAR) &&
                    !level.isRaining()) {
                strikeLightning();
                craftTheItem(output);
            }

            if(iRecipe.getWeather().equals(LightningChannelerRecipe.Weather.RAIN) &&
                    level.isRaining()) {
                strikeLightning();
                craftTheItem(output);
            }

            if(iRecipe.getWeather().equals(LightningChannelerRecipe.Weather.THUNDERING) &&
                    level.isThundering()) {
                strikeLightning();
                craftTheItem(output);
            }

            setChanged();
        });
    }

    private void craftTheItem(ItemStack output) {
        itemHandler.extractItem(0, 1, false);
        itemHandler.extractItem(1, 1, false);
        itemHandler.insertItem(1, output, false);
    }

    @Override
    public void tick() {
        if(level.isClientSide) {
            return;
        }
        craft();
    }
}
