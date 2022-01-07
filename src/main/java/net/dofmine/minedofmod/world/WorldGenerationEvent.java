package net.dofmine.minedofmod.world;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.world.gen.ModFlowerGeneration;
import net.dofmine.minedofmod.world.gen.ModOreGeneration;
import net.dofmine.minedofmod.world.gen.ModTreeGeneration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MinedofMod.MODS_ID)
public class WorldGenerationEvent {
    @SubscribeEvent
    public static void ModWorldGeneration(final BiomeLoadingEvent event) {
        ModOreGeneration.generateOres(event);

        ModTreeGeneration.generateTrees(event);
        ModFlowerGeneration.generateFlowers(event);
    }
}
