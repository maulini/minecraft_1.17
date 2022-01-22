package net.dofmine.minedofmod.world.biome;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.world.features.ModPlaceFeatures;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBiomes {
    public static final DeferredRegister<Biome> BIOMES =
            DeferredRegister.create(ForgeRegistries.BIOMES, MinedofMod.MODS_ID);

    public static final RegistryObject<Biome> LAVA_LAND = BIOMES.register("lava_land",
            ModBiomes::createLavaLand);
    public static final ResourceKey<Biome> LAVA_LAND_R = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MinedofMod.MODS_ID, "lava_land"));

    private static Biome createLavaLand() {
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();

        BiomeGenerationSettings.Builder generationSettings = new BiomeGenerationSettings.Builder();

        generationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlaceFeatures.RUBY_ORE);
        generationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlaceFeatures.GOD_ORE);
        generationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlaceFeatures.DARK_ORE);
        generationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlaceFeatures.LAPIS_ORE);
        generationSettings.addFeature(GenerationStep.Decoration.LAKES, ModPlaceFeatures.STYX);

        generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,  ModPlaceFeatures.REDWOOD_PLACED);
        generationSettings.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION,  ModPlaceFeatures.ORCHID_PLACED);

        return (new Biome.BiomeBuilder()).precipitation(Biome.Precipitation.NONE).biomeCategory(Biome.BiomeCategory.NONE)
                .biomeCategory(Biome.BiomeCategory.NETHER).temperature(0.8F).downfall(0.4F)
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
