package net.dofmine.minedofmod.data.recipes.vacuum;

import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.setup.ClientSetup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class VacuumBackPack extends CustomRecipe {

    public VacuumBackPack(ResourceLocation res) {
        super(res);
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        boolean flag = false;
        boolean flag2 = false;
        int emptyItem = 0;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && stack.getItem() == ModItems.BACK_PACK.get()) {
                flag = true;
            } if (stack.isEmpty()) {
               emptyItem++;
            }
        }
        flag2 = emptyItem == 2 || emptyItem == 7;

        return flag && flag2;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        ItemStack output = new ItemStack(ModItems.VACUUM_BACK_PACK.get(), 1);
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && !stack.is(ModItems.BACK_PACK.get())) {
                net.dofmine.minedofmod.items.backpack.VacuumBackPack.saveInventoryContent(output, stack);
            }
        }
        return output;
    }

    public boolean canCraftInDimensions(int p_44489_, int p_44490_) {
        return p_44489_ >= 2 && p_44490_ >= 2;
    }

    public RecipeSerializer<?> getSerializer() {
        return ClientSetup.VACUUM_CRAFTING;
    }

}
