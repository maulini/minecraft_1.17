package net.dofmine.minedofmod.tileentity;

import net.dofmine.minedofmod.block.thirst.WaterCollector;
import net.dofmine.minedofmod.container.WaterCollectorContainer;
import net.dofmine.minedofmod.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import javax.annotation.Nullable;

public class WaterCollectorTile  extends BaseContainerBlockEntity implements WorldlyContainer {

    public static final int FILTER_DURATION = 400;
    public static final int MAX_WATER = 100;
    public static final int TICK_TO_FILL_WATER_MAX = 250;
    private static final int[] SLOTS_FOR_UP = new int[]{1};
    private static final int[] SLOTS_FOR_DOWN = new int[]{2, 0};
    private static final int[] SLOTS_FOR_SIDES = new int[]{0};
    private NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);
    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
            net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
    int actualWater;
    int filterProgress;
    int nbFilter;
    int tickToFillWater;
    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int p_58431_) {
            switch(p_58431_) {
                case 0:
                    return WaterCollectorTile.this.actualWater;
                case 1:
                    return WaterCollectorTile.this.filterProgress;
                case 2:
                    return WaterCollectorTile.this.nbFilter;
                case 3:
                    return WaterCollectorTile.this.tickToFillWater;
                default:
                    return 0;
            }
        }

        public void set(int p_58433_, int p_58434_) {
            switch(p_58433_) {
                case 0:
                    WaterCollectorTile.this.actualWater = p_58434_;
                    break;
                case 1:
                    WaterCollectorTile.this.filterProgress = p_58434_;
                    break;
                case 2:
                    WaterCollectorTile.this.nbFilter = p_58434_;
                    break;
                case 3:
                    WaterCollectorTile.this.tickToFillWater = p_58434_;
                    break;
            }

        }

        public int getCount() {
            return 4;
        }
    };

    public WaterCollectorTile(BlockPos blockPos, BlockState blockState) {
        super(ModTileEntity.WATER_COLLECTOR_TILE.get(), blockPos, blockState);
    }

    public boolean canFilter() {
        return hasFilter() && actualWater == 100;
    }

    public boolean hasFilter() {
        return getBlockState().getValue(WaterCollector.FILTER_PRESENT);
    }

    public boolean isFull() {
        return getBlockState().getValue(WaterCollector.FULL);
    }

    public int getActualWater() {
        return dataAccess.get(0);
    }

    public void setNbFilter(int nbFilter) {
        dataAccess.set(2, nbFilter);
    }

    public int getNbFilter() {
        return dataAccess.get(2);
    }

    @Override
    public void load(CompoundTag p_155245_) {
        items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(p_155245_, this.items);
        actualWater = p_155245_.getInt("actualWater");
        filterProgress = p_155245_.getInt("filterProgress");
        nbFilter = p_155245_.getInt("nbFilter");
        tickToFillWater = p_155245_.getInt("tickToFill");
        super.load(p_155245_);
    }

    @Override
    protected void saveAdditional(CompoundTag p_187461_) {
        ContainerHelper.saveAllItems(p_187461_, this.items);
        p_187461_.putInt("actualWater", actualWater);
        p_187461_.putInt("filterProgress", filterProgress);
        p_187461_.putInt("nbFilter", nbFilter);
        p_187461_.putInt("tickToFill", tickToFillWater);
        super.saveAdditional(p_187461_);
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("container.water_collector");
    }

    @Override
    protected AbstractContainerMenu createMenu(int p_58627_, Inventory p_58628_) {
        return new WaterCollectorContainer(p_58627_, p_58628_, this, dataAccess);
    }

    public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, WaterCollectorTile waterCollectorTile) {
        if (waterCollectorTile.canFilter()) {
            ItemStack itemstack = waterCollectorTile.items.get(0);
            if (waterCollectorTile.nbFilter <= 0 && itemstack.is(ModItems.FILTER_WATER.get())) {
                itemstack.shrink(1);
                level.setBlockAndUpdate(blockPos, blockState.setValue(WaterCollector.FILTER_PRESENT, false));
                setChanged(level, blockPos, blockState);
            }

            boolean flag = isFilterable(waterCollectorTile.items, waterCollectorTile);
            boolean flag1 = waterCollectorTile.filterProgress > 0;
            ItemStack itemstack1 = waterCollectorTile.items.get(1);
            if (flag1) {
                --waterCollectorTile.filterProgress;
                boolean flag2 = waterCollectorTile.filterProgress == 0;
                if (flag2 && flag) {
                    doFilter(level, blockPos, waterCollectorTile.items, waterCollectorTile);
                    setChanged(level, blockPos, blockState);
                } else if (!flag || !itemstack1.is(Items.GLASS_BOTTLE)) {
                    waterCollectorTile.filterProgress = 0;
                    setChanged(level, blockPos, blockState);
                }
            } else if (flag && waterCollectorTile.nbFilter > 0) {
                --waterCollectorTile.nbFilter;
                waterCollectorTile.filterProgress = FILTER_DURATION;
                setChanged(level, blockPos, blockState);
            }
        }else {
            BlockPos xPlusOne = new BlockPos(blockPos.getX() + 1, blockPos.getY(), blockPos.getZ());
            BlockPos xMinusOne = new BlockPos(blockPos.getX() - 1, blockPos.getY(), blockPos.getZ());
            BlockPos zPlusOne = new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() + 1);
            BlockPos zMinusOne = new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ() - 1);
            if (level.getBlockState(xPlusOne).is(Blocks.WATER) && ((LiquidBlock)level.getBlockState(xPlusOne).getBlock()).getFluid().isSame(Fluids.FLOWING_WATER)
            || level.getBlockState(xMinusOne).is(Blocks.WATER) && ((LiquidBlock)level.getBlockState(xMinusOne).getBlock()).getFluid().isSame(Fluids.FLOWING_WATER)
            || level.getBlockState(zPlusOne).is(Blocks.WATER) && ((LiquidBlock)level.getBlockState(zPlusOne).getBlock()).getFluid().isSame(Fluids.FLOWING_WATER)
            || level.getBlockState(zMinusOne).is(Blocks.WATER) && ((LiquidBlock)level.getBlockState(zMinusOne).getBlock()).getFluid().isSame(Fluids.FLOWING_WATER)) {
                if (++waterCollectorTile.tickToFillWater >= TICK_TO_FILL_WATER_MAX && waterCollectorTile.actualWater < MAX_WATER) {
                    waterCollectorTile.dataAccess.set(0, ++waterCollectorTile.actualWater);
                    waterCollectorTile.tickToFillWater = 0;
                }
            }
        }
    }

    private static void doFilter(Level level, BlockPos blockPos, NonNullList<ItemStack> items, WaterCollectorTile waterCollectorTile) {
        ItemStack itemstack = items.get(1);

        itemstack.shrink(1);

        if (waterCollectorTile.getItem(2).is(ModItems.WATER_BOTTLE.get())) {
            waterCollectorTile.getItem(2).setCount(waterCollectorTile.getItem(2).getCount() + 1);
            waterCollectorTile.setItem(2, waterCollectorTile.getItem(2));
        }else {
            waterCollectorTile.setItem(2, new ItemStack(ModItems.WATER_BOTTLE.get()));
        }

        level.levelEvent(1035, blockPos, 0);
    }

    private static boolean isFilterable(NonNullList<ItemStack> items, WaterCollectorTile waterCollectorTile) {
        return !items.get(0).isEmpty() && !items.get(1).isEmpty() && waterCollectorTile.actualWater == 100;
    }

    @Override
    public int[] getSlotsForFace(Direction p_19238_) {
        if (p_19238_ == Direction.UP) {
            return SLOTS_FOR_UP;
        } else {
            return p_19238_ == Direction.DOWN ? SLOTS_FOR_DOWN : SLOTS_FOR_SIDES;
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int p_19235_, ItemStack p_19236_, @Nullable Direction p_19237_) {
        if (p_19235_ == 0) {
            return p_19236_.is(ModItems.FILTER_WATER.get());
        }else if (p_19235_ == 1) {
            return p_19236_.is(Items.GLASS_BOTTLE);
        }
        return false;
    }

    @Override
    public boolean canTakeItemThroughFace(int p_19239_, ItemStack p_19240_, Direction p_19241_) {
        return p_19239_ != 3 || p_19240_.is(Items.GLASS_BOTTLE);
    }

    @Override
    public int getContainerSize() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        return i >= 0 && i < this.items.size() ? this.items.get(i) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int p_18942_, int p_18943_) {
        if (p_18942_ == 0) {
            setNbFilter(0);
            level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(WaterCollector.FILTER_PRESENT, false));
        } else if (p_18942_ == 2) {
            level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(WaterCollector.FULL, false));
        }
        return ContainerHelper.removeItem(this.items, p_18942_, p_18943_);
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_18951_) {
        return ContainerHelper.takeItem(this.items, p_18951_);
    }

    @Override
    public void setItem(int p_18944_, ItemStack p_18945_) {
        if (p_18944_ >= 0 && p_18944_ < this.items.size()) {
            this.items.set(p_18944_, p_18945_);
        }
        if (p_18944_ == 0) {
            setNbFilter(p_18945_.getMaxDamage());
            level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(WaterCollector.FILTER_PRESENT, !p_18945_.isEmpty()));
        } else if (p_18944_ == 2) {
            level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(WaterCollector.FULL, !p_18945_.isEmpty()));
        }
    }

    @Override
    public boolean stillValid(Player p_18946_) {
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return !(p_18946_.distanceToSqr((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D) > 64.0D);
        }
    }

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.remove && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.UP)
                return handlers[0].cast();
            else if (facing == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[2].cast();
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        for (int x = 0; x < handlers.length; x++)
            handlers[x].invalidate();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        this.handlers = net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
    }
}
