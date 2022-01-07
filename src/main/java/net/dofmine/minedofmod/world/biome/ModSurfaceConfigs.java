package net.dofmine.minedofmod.world.biome;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.block.ModBlocks;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.level.levelgen.surfacebuilders.SurfaceBuilderBaseConfiguration;

public class ModSurfaceConfigs {
    public static final ConfiguredSurfaceBuilder<SurfaceBuilderBaseConfiguration> LAVA_SURFACE_BUILDER =
            register("lava_surface", SurfaceBuilder.DEFAULT.configured(new SurfaceBuilderBaseConfiguration(
                    ModBlocks.GRASS_DARK.get().defaultBlockState(),
                    ModBlocks.DIRT_DARK.get().defaultBlockState(),
                    ModBlocks.STONE_DARK.get().defaultBlockState())));

    private static <T extends SurfaceBuilderBaseConfiguration> ConfiguredSurfaceBuilder<T> register(String name,
                                                                                                    ConfiguredSurfaceBuilder<T> surfaceBuilder) {
        return Registry.register(BuiltinRegistries.CONFIGURED_SURFACE_BUILDER,
                new ResourceLocation(MinedofMod.MODS_ID, name), surfaceBuilder);
    }
}
