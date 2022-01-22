package net.dofmine.minedofmod.world.dimension;

import net.dofmine.minedofmod.MinedofMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class ModDimension {

    public static ResourceKey<Level> DARKDim = ResourceKey.create(Registry.DIMENSION_REGISTRY,
            new ResourceLocation(MinedofMod.MODS_ID, "dark/darkdim"));

    public static void register() {
    }

}
