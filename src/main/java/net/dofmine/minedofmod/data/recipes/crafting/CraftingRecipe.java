package net.dofmine.minedofmod.data.recipes.crafting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.dofmine.minedofmod.data.recipes.ModRecipeType;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.*;

public class CraftingRecipe implements ICraftingRecipe {

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItem;
    private final Pattern pattern;

    public CraftingRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItem, Pattern pattern) {
        this.id = id;
        this.output = output;
        this.recipeItem = recipeItem;
        this.pattern = pattern;
    }

    @Override
    public boolean matches(RecipeWrapper recipeWrapper, Level level) {
        return pattern.verify(recipeWrapper);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipeItem;
    }

    @Override
    public ItemStack assemble(RecipeWrapper recipeWrapper) {
        return output;
    }

    @Override
    public ItemStack getResultItem() {
        return output.copy();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeType.LIGHTNING_SERIALIZER_RECIPE.get();
    }

    public static class CraftingRecipeType implements RecipeType<CraftingRecipe> {
        @Override
        public String toString() {
            return CraftingRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<CraftingRecipe> {

        @Override
        public CraftingRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            ItemStack output = ShapedRecipe.itemStackFromJson(jsonObject.getAsJsonObject("result"));

            Pattern pattern = new Pattern(jsonObject.getAsJsonArray("pattern"), jsonObject.getAsJsonObject("key"));

            NonNullList<Ingredient> inputs = NonNullList.withSize(pattern.getIngredientMap().size(), Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, pattern.getIngredientMap().get(pattern.getSingleElement().get(i)));
            }

            return new CraftingRecipe(resourceLocation, output,
                    inputs, pattern);
        }

        @Nullable
        @Override
        public CraftingRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf byteBuf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(17, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(byteBuf));
            }

            ItemStack output = byteBuf.readItem();
            return new CraftingRecipe(resourceLocation, output,
                    inputs, null);
        }

        @Override
        public void toNetwork(FriendlyByteBuf byteBuf, CraftingRecipe craftingRecipe) {
            byteBuf.writeInt(craftingRecipe.getIngredients().size());
            for (Ingredient ing : craftingRecipe.getIngredients()) {
                ing.toNetwork(byteBuf);
            }
            byteBuf.writeItemStack(craftingRecipe.getResultItem(), false);
        }
    }

    private static class Pattern {

        private List<String> singleElement = new ArrayList<>();
        private Map<String, Ingredient> ingredientMap = new HashMap<>();
        private RecipeWrapper ingredientInSlot = new RecipeWrapper(new ItemStackHandler(16));

        public Pattern(JsonArray pattern, JsonObject ingredient) {
            registerPattern(pattern, ingredient);
        }

        private void registerPattern(JsonArray pattern, JsonObject ingredient) {
            int slot = 0;
            for (int i = 0; i < pattern.size(); i++) {
                String globalKey = pattern.get(i).getAsString();
                for (int j = 0; j < globalKey.length(); j++) {
                    Character key = globalKey.charAt(j);
                    if (!singleElement.contains(key) && !key.toString().isBlank()) {
                        singleElement.add(key.toString());
                        Ingredient value = Ingredient.fromJson(ingredient.get(key.toString()));
                        ingredientMap.put(key.toString(), value);
                        ingredientInSlot.setItem(slot, value.getItems()[0]);
                    }else {
                        ingredientInSlot.setItem(slot, ItemStack.EMPTY);
                    }
                    slot++;
                }
                if (globalKey.length() < 4) {
                    for (int j = 0; j < 4 - globalKey.length(); j++) {
                        ingredientInSlot.setItem(slot, ItemStack.EMPTY);
                        slot++;
                    }
                }
            }
        }

        public List<String> getSingleElement() {
            return singleElement;
        }

        public Map<String, Ingredient> getIngredientMap() {
            return ingredientMap;
        }

        public boolean verify(RecipeWrapper recipeWrapper) {
            for (int i = 0; i < ingredientInSlot.getContainerSize(); i++) {
                if (!recipeWrapper.getItem(i).getItem().equals(ingredientInSlot.getItem(i).getItem())) {
                    return false;
                }
            }
            return true;
        }
    }
}
