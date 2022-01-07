package net.dofmine.minedofmod.slot;

import net.dofmine.minedofmod.items.backpack.BackPackItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class BackPackSlot extends Slot {

    public BackPackSlot(Container p_40223_, int p_40224_, int p_40225_, int p_40226_) {
        super(p_40223_, p_40224_, p_40225_, p_40226_);
    }

    /**
     * Method used to prevent backpack-ception (backpacks inside backpacks)
     */
    @Override
    public boolean mayPlace(ItemStack stack) {
        return !(stack.getItem() instanceof BackPackItem);
    }

}
