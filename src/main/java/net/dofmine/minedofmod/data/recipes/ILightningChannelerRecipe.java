package net.dofmine.minedofmod.data.recipes;

import net.dofmine.minedofmod.MinedofMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.crafting.IShapedRecipe;

public interface ILightningChannelerRecipe extends IShapedRecipe<Inventory> {

    ResourceLocation TYPE_ID = new ResourceLocation(MinedofMod.MODS_ID, "lightning");

    @Override
    default RecipeType<?> getType() {
        return Registry.RECIPE_TYPE.getOptional(TYPE_ID).get();
    }

    @Override
    default boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return true;
    }



}
