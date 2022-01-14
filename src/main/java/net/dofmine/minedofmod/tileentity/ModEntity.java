package net.dofmine.minedofmod.tileentity;

import net.dofmine.minedofmod.MinedofMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntity {
    public static DeferredRegister<EntityType<?>> ENTITY = DeferredRegister.create(ForgeRegistries.ENTITIES, MinedofMod.MODS_ID);
    public static final RegistryObject<EntityType<MjollnirEntity>> MJOLLNIR_PROJECTILE = ENTITY.register("mjollnir",
            () -> EntityType.Builder.<MjollnirEntity>of(MjollnirEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).build(new ResourceLocation(MinedofMod.MODS_ID, "mjollnir").toString()));

    public static void register(IEventBus event) {
        ENTITY.register(event);
    }
    
}
