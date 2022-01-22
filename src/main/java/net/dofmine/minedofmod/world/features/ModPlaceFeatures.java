package net.dofmine.minedofmod.world.features;

import net.dofmine.minedofmod.world.gen.OreType;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPlaceFeatures {

    public static final PlacedFeature REDWOOD_PLACED = PlacementUtils.register("redwood_placed",
            ModConfiguredFeatures.REDWOOD_TREE_CHECKED.placed(VegetationPlacements.treePlacement(
                    PlacementUtils.countExtra(1, 0.1f, 2))));

    public static final PlacedFeature ORCHID_PLACED = PlacementUtils.register("orchid_placed",
            ModConfiguredFeatures.ORCHID_CONFIG.placed(RarityFilter.onAverageOnceEvery(16),
                    InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));

    public static final PlacedFeature RUBY_ORE = PlacementUtils.register("ruby_ore_placed",
            ModConfiguredFeatures.RUBY_ORE.placed(commonOrePlacement(7, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(OreType.RUBY_ORE.getMinHeight()), VerticalAnchor.aboveBottom(OreType.RUBY_ORE.getMaxHeight())))));

    public static final PlacedFeature LAPIS_ORE = PlacementUtils.register("lapis_ore_placed",
            ModConfiguredFeatures.LAPIS_ORE.placed(commonOrePlacement(7, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(OreType.LAPIS_ORE.getMinHeight()), VerticalAnchor.aboveBottom(OreType.LAPIS_ORE.getMaxHeight())))));

    public static final PlacedFeature DARK_ORE = PlacementUtils.register("dark_ore_placed",
            ModConfiguredFeatures.DARK_ORE.placed(rareOrePlacement(5, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(OreType.DARK_ORE.getMinHeight()), VerticalAnchor.aboveBottom(OreType.DARK_ORE.getMaxHeight())))));

    public static final PlacedFeature GOD_ORE = PlacementUtils.register("god_ore_placed",
            ModConfiguredFeatures.GOD_ORE.placed(rareOrePlacement(5, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(OreType.GODS_ORE.getMinHeight()), VerticalAnchor.aboveBottom(OreType.GODS_ORE.getMaxHeight())))));

    public static final PlacedFeature STYX = PlacementUtils.register("styx_placed", ModConfiguredFeatures.STYX.placed(RarityFilter.onAverageOnceEvery(200), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP_WORLD_SURFACE, BiomeFilter.biome()));


    private static List<PlacementModifier> orePlacement(PlacementModifier p_195347_, PlacementModifier p_195348_) {
        return List.of(p_195347_, InSquarePlacement.spread(), p_195348_, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int p_195344_, PlacementModifier p_195345_) {
        return orePlacement(CountPlacement.of(p_195344_), p_195345_);
    }

    private static List<PlacementModifier> rareOrePlacement(int p_195350_, PlacementModifier p_195351_) {
        return orePlacement(RarityFilter.onAverageOnceEvery(p_195350_), p_195351_);
    }
}