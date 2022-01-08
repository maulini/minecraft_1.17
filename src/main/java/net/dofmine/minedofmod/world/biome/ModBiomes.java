package net.dofmine.minedofmod.world.biome;
import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.world.features.ModConfiguredFeatures;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.Features;
import net.minecraft.data.worldgen.biome.Biomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.NetherVines;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;
import net.minecraft.world.level.levelgen.placement.FrequencyWithExtraChanceDecoratorConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBiomes {
    public static final DeferredRegister<Biome> BIOMES =
            DeferredRegister.create(ForgeRegistries.BIOMES, MinedofMod.MODS_ID);

    public static final RegistryObject<Biome> LAVA_LAND = BIOMES.register("lava_land",
            ModBiomes::createLavaLand);
    public static final ResourceKey<Biome> LAVA_LAND_R = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MinedofMod.MODS_ID, "lava_land"));

    private static Biome createLavaLand() {
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder generationSettings = new BiomeGenerationSettings.Builder();
        generationSettings.surfaceBuilder(ModSurfaceConfigs.LAVA_SURFACE_BUILDER);
        generationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModConfiguredFeatures.RUBY_ORE);
        generationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModConfiguredFeatures.GOD_ORE);
        generationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModConfiguredFeatures.DARK_ORE);
        generationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModConfiguredFeatures.LAPIS_ORE);
        generationSettings.addFeature(GenerationStep.Decoration.LAKES, ModConfiguredFeatures.STYX);

        generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,  ModConfiguredFeatures.REDWOOD
                .decorated(Features.Decorators.HEIGHTMAP_WITH_TREE_THRESHOLD_SQUARED)
                .decorated(FeatureDecorator.COUNT_EXTRA
                        .configured(new FrequencyWithExtraChanceDecoratorConfiguration(
                                2, 0.1f, 1))));
        generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,  ModConfiguredFeatures.ORCHID_CONFIG
                .decorated(FeatureDecorator.COUNT_EXTRA
                        .configured(new FrequencyWithExtraChanceDecoratorConfiguration(
                                2, 0.1f, 1))));

        return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE).biomeCategory(Biome.BiomeCategory.NONE)
                .depth(0.125F).scale(0.05F).temperature(0.8F).downfall(0.4F)
                .specialEffects((new BiomeSpecialEffects.Builder())
                        .waterColor(0x3f76e4).waterFogColor(0x050533)
                        .fogColor(0xc0d8ff).skyColor(0x77adff)
                        .build()).mobSpawnSettings(spawnSettings.build())
                .generationSettings(generationSettings.build()).build();
    }

    public static void register(IEventBus eventBus) {
        BIOMES.register(eventBus);
    }
}
