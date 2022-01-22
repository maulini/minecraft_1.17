package net.dofmine.minedofmod.inventory;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.items.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Container;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class BackPackInventory implements Container, Nameable {

    public ItemStack[] content;
    public int size;

    public BackPackInventory(ItemStack container, int size) {
        this.size = size;
        this.content = new ItemStack[size+1];
        for (int i = 0; i < size; i++) {
            content[i] = Items.AIR.getDefaultInstance();
        }
        if (!container.hasTag()) container.setTag(new CompoundTag());
        this.readFromNBT(container.getTag());
    }

    /**
     * This methods reads the content of the NBTTagCompound inside the container
     *
     * @param comp
     *            the container NBTTagCompound
     */
    public void readFromNBT(CompoundTag comp) {
        ListTag nbtlist = comp.getList("Inventory", Tag.TAG_COMPOUND);
        for (int i = 0; i < nbtlist.size(); i++) {
            CompoundTag comp1 = nbtlist.getCompound(i);
            int slot = comp1.getInt("Slot");
            this.content[slot] = ItemStack.of(comp1);
        }
    }

    /**
     * This methods saves the content inside the container
     *
     * @param comp
     *            the NBTTagCompound to write in
     */
    public void writeToNBT(CompoundTag comp) {
        ListTag nbtlist = new ListTag();

        for (int i = 0; i < this.size; i++) {
            if (this.content[i] != null) {
                CompoundTag comp1 = new CompoundTag();
                comp1.putInt("Slot", i);
                this.content[i].save(comp1);
                nbtlist.add(comp1);
            }
        }
        comp.put("Inventory", nbtlist);
    }

    @Override
    public int getContainerSize() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return content.length > 0;
    }

    @Override
    public ItemStack getItem(int index) {
        ItemStack stack = this.content[index];
        if (stack == null || (stack != null && stack.getItem() == null)) {
            stack = Items.AIR.getDefaultInstance();
        }
        return stack;
    }

    @Override
    public ItemStack removeItem(int index, int amount) {
        ItemStack stack = getItem(index);
        if (stack != null) {
            if (stack.getCount() > amount) {
                stack = stack.split(amount);
                if (stack.getCount() == 0) this.content[index] = null;
            } else {
                this.content[index] = null;
            }
        }
        return stack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack stack = getItem(index);
        if (stack != null) this.content[index] = null;
        return stack;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (!stack.getItem().equals(ModItems.BACK_PACK.get())) {
            this.content[index] = stack;
        }
    }

    @Override
    public Component getName() {
        return new TextComponent(MinedofMod.MODS_ID + ".container.backpack");
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void setChanged() {
    }

    @Override
    public void startOpen(Player p_18955_) {
    }

    @Override
    public void stopOpen(Player p_18954_) {
    }

    /**
     * Prevents backpack-ception
     */
    @Override
    public boolean stillValid(Player player) {
        return !player.getMainHandItem().getItem().equals(ModItems.BACK_PACK.get());
    }

    @Override
    public void clearContent() {

    }
}
