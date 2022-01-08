package net.dofmine.minedofmod.world.features;

import net.dofmine.minedofmod.block.ModBlocks;
import net.dofmine.minedofmod.world.features.tree.StraightTrunkPlacerCustom;
import net.dofmine.minedofmod.world.gen.OreType;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Features;
import net.minecraft.data.worldgen.biome.Biomes;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.blockplacers.SimpleBlockPlacer;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

public class ModConfiguredFeatures {
    public static final ConfiguredFeature<?, ?> REDWOOD = register("redwood", Feature.TREE.configured(
            new TreeConfiguration.TreeConfigurationBuilder(
                    new SimpleStateProvider(ModBlocks.REDWOOD_LOG.get().defaultBlockState()),
                    new StraightTrunkPlacerCustom(8, 4, 3),
                    new SimpleStateProvider(ModBlocks.REDWOOD_LEAVES.get().defaultBlockState()),
                    new SimpleStateProvider(ModBlocks.REDWOOD_SAPLING.get().defaultBlockState()),
                    new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
                    new TwoLayersFeatureSize(1, 0, 1)).dirt(new SimpleStateProvider(ModBlocks.DIRT_DARK.get().defaultBlockState())).build()));

    public static final ConfiguredFeature<?, ?> ORCHID_CONFIG = Feature.FLOWER.configured((
                    new RandomPatchConfiguration.GrassConfigurationBuilder(
                            new SimpleStateProvider(ModBlocks.ORCHID.get().defaultBlockState()),
                            SimpleBlockPlacer.INSTANCE)).tries(12).build())
            .decorated(Features.Decorators.HEIGHTMAP_WITH_TREE_THRESHOLD_SQUARED).count(1);

    public static final ConfiguredFeature<?, ?> RUBY_ORE = register("ruby_ore", Feature.ORE.configured(registerOre(OreType.RUBY_ORE))
            .range(rangeConfiguration(OreType.RUBY_ORE)).squared().count(OreType.RUBY_ORE.getVeinsPerChunk()));

    public static final ConfiguredFeature<?, ?> LAPIS_ORE = register("lapis_ore", Feature.ORE.configured(registerOre(OreType.LAPIS_ORE))
            .range(rangeConfiguration(OreType.LAPIS_ORE)).squared().count(OreType.LAPIS_ORE.getVeinsPerChunk()));

    public static final ConfiguredFeature<?, ?> DARK_ORE = register("dark_ore", Feature.ORE.configured(registerOre(OreType.DARK_ORE))
            .range(rangeConfiguration(OreType.DARK_ORE)).squared().count(OreType.DARK_ORE.getVeinsPerChunk()));

    public static final ConfiguredFeature<?, ?> GOD_ORE = register("god_ore", Feature.ORE.configured(registerOre(OreType.GODS_ORE))
            .range(rangeConfiguration(OreType.GODS_ORE)).squared().count(OreType.GODS_ORE.getVeinsPerChunk()));

    public static final ConfiguredFeature<?, ?> STYX = register("styx", Feature.LAKE.configured(new BlockStateConfiguration(ModBlocks.STYX.get().defaultBlockState())).range(Features.Decorators.FULL_RANGE).squared().rarity(4));

    private static <FC extends FeatureConfiguration>ConfiguredFeature<FC, ?> register(String name,
                                                                                      ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, name, configuredFeature);
    }

    private static OreConfiguration registerOre(OreType oreType) {
        return new OreConfiguration(new BlockMatchTest(ModBlocks.STONE_DARK.get()), oreType.getBlock().get().defaultBlockState(), oreType.getMaxVeinSize());
    }

    private static RangeDecoratorConfiguration rangeConfiguration(OreType ore) {
        return new RangeDecoratorConfiguration(UniformHeight.of(VerticalAnchor.absolute(ore.getMinHeight()),
                VerticalAnchor.absolute(ore.getMaxHeight())));
    }
}
