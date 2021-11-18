package net.dofmine.minedofmod.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.dofmine.minedofmod.block.ModBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class LightningChannelerRecipe implements ILightningChannelerRecipe {

    public enum Weather {
        CLEAR,
        RAIN,
        THUNDERING;

        public static Weather getWeatherByString(String name) {
            return Stream.of(Weather.values()).filter(s -> s.toString().equalsIgnoreCase(name)).findFirst().orElseGet(() -> CLEAR);
        }
    }

    private final ResourceLocation id;
    private final ItemStack output;
    private final NonNullList<Ingredient> recipeItem;
    private final Weather weather;

    public LightningChannelerRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> recipeItem, Weather weather) {
        this.id = id;
        this.output = output;
        this.recipeItem = recipeItem;
        this.weather = weather;
    }

    @Override
    public boolean matches(RecipeWrapper recipeWrapper, Level level) {
        if (recipeItem.get(0).test(recipeWrapper.getItem(0))) {
            return recipeItem.get(1).test(recipeWrapper.getItem(1));
        }
        return false;
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

    public Weather getWeather() {
        return weather;
    }

    public ItemStack getIcon() {
        return new ItemStack(ModBlocks.LIGHTNING_CHANNELER.get());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeType.LIGHTNING_SERIALIZER_RECIPE.get();
    }

    public static class CraftingRecipeType implements RecipeType<LightningChannelerRecipe> {
        @Override
        public String toString() {
            return LightningChannelerRecipe.TYPE_ID.toString();
        }
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<LightningChannelerRecipe> {

        @Override
        public LightningChannelerRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            ItemStack output = ShapedRecipe.itemStackFromJson(jsonObject.getAsJsonObject("output"));
            String weather = jsonObject.get("weather").getAsString();

            JsonArray ingredients = jsonObject.getAsJsonArray("ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(2, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i)));
            }

            return new LightningChannelerRecipe(resourceLocation, output,
                    inputs, Weather.getWeatherByString(weather));
        }

        @Nullable
        @Override
        public LightningChannelerRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf byteBuf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(2, Ingredient.EMPTY);

            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(byteBuf));
            }

            ItemStack output = byteBuf.readItem();
            return new LightningChannelerRecipe(resourceLocation, output,
                    inputs, null);
        }

        @Override
        public void toNetwork(FriendlyByteBuf byteBuf, LightningChannelerRecipe lightningChannelerRecipe) {
            byteBuf.writeInt(lightningChannelerRecipe.getIngredients().size());
            for (Ingredient ing : lightningChannelerRecipe.getIngredients()) {
                ing.toNetwork(byteBuf);
            }
            byteBuf.writeItemStack(lightningChannelerRecipe.getResultItem(), false);
        }
    }
}
