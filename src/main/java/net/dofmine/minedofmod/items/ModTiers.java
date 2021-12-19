package net.dofmine.minedofmod.items;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;

public class ModTiers {

    public static final ForgeTier TITANIUM = new ForgeTier(1, 1500, 1f,
            3f, 10, Tags.Blocks.NEEDS_GOLD_TOOL,
            () -> Ingredient.of(ModItems.TITANIUM_INGOT.get()));

    public static final ForgeTier DARK = new ForgeTier(4, 3500, 5f,
            8.5f, 10, Tags.Blocks.NEEDS_NETHERITE_TOOL,
            () -> Ingredient.of(ModItems.DARK_INGOT.get()));

    public static final ForgeTier RUBIS = new ForgeTier(3, 2500, 3f,
            5f, 10, Tags.Blocks.NEEDS_GOLD_TOOL,
            () -> Ingredient.of(ModItems.RUBIS_INGOT.get()));

    public static final ForgeTier GOD = new ForgeTier(5, 5000, 7f,
            10f, 10, Tags.Blocks.NEEDS_NETHERITE_TOOL,
            () -> Ingredient.of(ModItems.GOD_INGOT.get()));

    public static final ForgeTier LAPIS = new ForgeTier(2, 2000, 2f,
            6f, 10, Tags.Blocks.NEEDS_GOLD_TOOL,
            () -> Ingredient.of(ModItems.LAPIS_INGOT.get()));

}
