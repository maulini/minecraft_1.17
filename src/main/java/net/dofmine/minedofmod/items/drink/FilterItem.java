package net.dofmine.minedofmod.items.drink;

import net.dofmine.minedofmod.tabs.ModCreativeTabs;
import net.minecraft.world.item.Item;

public class FilterItem extends Item {

    public FilterItem(int durability) {
        super(new Properties().stacksTo(1).tab(ModCreativeTabs.MODS_TABS).durability(durability).setNoRepair());
    }

}
