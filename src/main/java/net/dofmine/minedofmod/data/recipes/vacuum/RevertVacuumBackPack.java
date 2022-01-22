package net.dofmine.minedofmod.data.recipes.vacuum;

import net.dofmine.minedofmod.data.recipes.ModRecipeType;
import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.setup.ClientSetup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CraftingTableBlock;

public class RevertVacuumBackPack extends CustomRecipe {

    public static ResourceLocation resourceLocation;

    public RevertVacuumBackPack(ResourceLocation res) {
        super(res);
        RevertVacuumBackPack.resourceLocation = res;
    }

    @Override
    public boolean matches(CraftingContainer inv, Level worldIn) {
        boolean flag = false;
        boolean flag2 = false;
        int emptyItem = 0;

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && stack.is(ModItems.VACUUM_BACK_PACK.get())) {
                flag = true;
            } if (stack.isEmpty()) {
                emptyItem++;
            }
        }
        flag2 = emptyItem == 3 || emptyItem == 8;

        return flag && flag2;
    }

    @Override
    public ItemStack assemble(CraftingContainer inv) {
        ItemStack output = null;
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (!stack.isEmpty() && stack.is(ModItems.VACUUM_BACK_PACK.get())) {
                output = net.dofmine.minedofmod.items.backpack.VacuumBackPack.itemContent(stack);
            }
        }
        return output;
    }

    public boolean canCraftInDimensions(int p_44489_, int p_44490_) {
        return p_44489_ >= 2 && p_44490_ >= 2;
    }

    public RecipeSerializer<?> getSerializer() {
        return ModRecipeType.REVERT_VACUUM_SERIALIZER_RECIPE.get();
    }

    public static class RevertVacuumRecipeType implements RecipeType<RevertVacuumBackPack> {
        @Override
        public String toString() {
            return RevertVacuumBackPack.resourceLocation.toString();
        }
    }
}
