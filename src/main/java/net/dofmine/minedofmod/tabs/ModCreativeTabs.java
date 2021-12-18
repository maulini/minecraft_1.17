package net.dofmine.minedofmod.tabs;

import net.dofmine.minedofmod.items.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeTabs {

    public static final CreativeModeTab MODS_TABS = new CreativeModeTab("modsTabs") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.TITANIUM_INGOT.get());
        }
    };

    public static final CreativeModeTab TOOLS_TABS = new CreativeModeTab("toolsTabs") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.TITANIUM_SWORD.get());
        }
    };

}
