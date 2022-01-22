package net.dofmine.minedofmod.world;

import net.dofmine.minedofmod.MinedofMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;

public class ModWorldType {

    public static final ResourceKey<NoiseGeneratorSettings> DARK = ResourceKey.create(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY, new ResourceLocation(MinedofMod.MODS_ID, "dark/darkdim_world"));

}
