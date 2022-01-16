package net.dofmine.minedofmod.effects;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.effects.custom.ThirstEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEffect {

    public static final DeferredRegister<MobEffect> EFFECT = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MinedofMod.MODS_ID);

    public static final RegistryObject<MobEffect> THIRST = EFFECT.register("thirst_effect", () -> new ThirstEffect(MobEffectCategory.HARMFUL, 5797459));

    public static void register(IEventBus event) {
        EFFECT.register(event);
    }
}
