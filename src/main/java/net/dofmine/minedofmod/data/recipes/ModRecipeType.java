package net.dofmine.minedofmod.data.recipes;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.data.recipes.crafting.CraftingRecipe;
import net.dofmine.minedofmod.data.recipes.lightning.LightningChannelerRecipe;
import net.dofmine.minedofmod.data.recipes.vacuum.RevertVacuumBackPack;
import net.dofmine.minedofmod.data.recipes.vacuum.VacuumBackPack;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeType {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MinedofMod.MODS_ID);

    public static final RegistryObject<LightningChannelerRecipe.Serializer> LIGHTNING_SERIALIZER_RECIPE
            = RECIPE_SERIALIZER.register("lightning", LightningChannelerRecipe.Serializer::new);

    public static final RegistryObject<CraftingRecipe.Serializer> CRAFTING_SERIALIZER_RECIPE
            = RECIPE_SERIALIZER.register("craft", CraftingRecipe.Serializer::new);

    public static final RegistryObject<SimpleRecipeSerializer<RevertVacuumBackPack>> VACUUM_SERIALIZER_RECIPE
            = RECIPE_SERIALIZER.register("vacuum_recipe", () -> new SimpleRecipeSerializer<>(RevertVacuumBackPack::new));

    public static final RegistryObject<SimpleRecipeSerializer<VacuumBackPack>> REVERT_VACUUM_SERIALIZER_RECIPE
            = RECIPE_SERIALIZER.register("revert_vacuum_recipe", () -> new SimpleRecipeSerializer<>(VacuumBackPack::new));

    public static RecipeType<LightningChannelerRecipe> LIGHTNING_RECIPE
            = new LightningChannelerRecipe.CraftingRecipeType();

    public static RecipeType<CraftingRecipe> CRAFTING_RECIPE
            = new CraftingRecipe.CraftingRecipeType();

    public static RecipeType<VacuumBackPack> VACUUM_RECIPE
            = new VacuumBackPack.VacuumRecipeType();

    public static RecipeType<RevertVacuumBackPack> REVERT_VACUUM_RECIPE
            = new RevertVacuumBackPack.RevertVacuumRecipeType();


    public static void register(IEventBus eventBus) {
        RECIPE_SERIALIZER.register(eventBus);

        Registry.register(Registry.RECIPE_TYPE, LightningChannelerRecipe.TYPE_ID, LIGHTNING_RECIPE);
        Registry.register(Registry.RECIPE_TYPE, CraftingRecipe.TYPE_ID, CRAFTING_RECIPE);
        Registry.register(Registry.RECIPE_TYPE, VacuumBackPack.resourceLocation, VACUUM_RECIPE);
        Registry.register(Registry.RECIPE_TYPE, RevertVacuumBackPack.resourceLocation, REVERT_VACUUM_RECIPE);
    }
}
