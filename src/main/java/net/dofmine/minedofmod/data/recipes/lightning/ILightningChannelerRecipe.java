package net.dofmine.minedofmod.data.recipes.lightning;

import net.dofmine.minedofmod.MinedofMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public interface ILightningChannelerRecipe extends Recipe<RecipeWrapper> {

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
