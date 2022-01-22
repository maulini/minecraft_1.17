package net.dofmine.minedofmod.world.features;

import net.dofmine.minedofmod.block.ModBlocks;
import net.dofmine.minedofmod.world.features.tree.StraightTrunkPlacerCustom;
import net.dofmine.minedofmod.world.gen.OreType;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ConfiguredFeature<?, ?> REDWOOD = register("redwood", Feature.TREE.configured(
            new TreeConfiguration.TreeConfigurationBuilder(
                    BlockStateProvider.simple(ModBlocks.REDWOOD_LOG.get()),
                    new StraightTrunkPlacerCustom(8, 4, 3),
                    BlockStateProvider.simple(ModBlocks.REDWOOD_LEAVES.get()),
                    new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                    new TwoLayersFeatureSize(1, 0, 1)).dirt(BlockStateProvider.simple(ModBlocks.DIRT_DARK.get())).build()));

    public static final ConfiguredFeature<RandomFeatureConfiguration, ?> REDWOOD_TREE_CHECKED =
            register("redwood_feature",
                    Feature.RANDOM_SELECTOR.configured(new RandomFeatureConfiguration(List.of(new WeightedPlacedFeature(
                            REDWOOD.filteredByBlockSurvival(ModBlocks.REDWOOD_SAPLING.get()), 0.1f)),
                            REDWOOD.filteredByBlockSurvival(ModBlocks.REDWOOD_SAPLING.get()))));

    public static final ConfiguredFeature<?, ?> ORCHID_CONFIG = register("flower_orchid",
            Feature.FLOWER.configured(new RandomPatchConfiguration(32, 6, 2,
                    () -> Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.ORCHID.get())))
                            .onlyWhenEmpty())));

    public static final ConfiguredFeature<?, ?> RUBY_ORE = register("ruby_ore", Feature.ORE.configured(registerOre(OreType.RUBY_ORE)));

    public static final ConfiguredFeature<?, ?> LAPIS_ORE = register("lapis_ore", Feature.ORE.configured(registerOre(OreType.LAPIS_ORE)));

    public static final ConfiguredFeature<?, ?> DARK_ORE = register("dark_ore", Feature.ORE.configured(registerOre(OreType.DARK_ORE)));

    public static final ConfiguredFeature<?, ?> GOD_ORE = register("god_ore", Feature.ORE.configured(registerOre(OreType.GODS_ORE)));

    public static final ConfiguredFeature<?, ?> STYX = register("styx", Feature.LAKE.configured(new LakeFeature.Configuration(BlockStateProvider.simple(ModBlocks.STYX.get().defaultBlockState()), BlockStateProvider.simple(ModBlocks.STONE_DARK.get().defaultBlockState()))));

    private static <FC extends FeatureConfiguration>ConfiguredFeature<FC, ?> register(String name,
                                                                                      ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, name, configuredFeature);
    }

    private static OreConfiguration registerOre(OreType oreType) {
        return new OreConfiguration(new BlockMatchTest(ModBlocks.STONE_DARK.get()), oreType.getBlock().get().defaultBlockState(), oreType.getMaxVeinSize());
    }

}
