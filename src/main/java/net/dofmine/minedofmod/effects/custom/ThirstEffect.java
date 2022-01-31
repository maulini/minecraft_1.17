package net.dofmine.minedofmod.effects.custom;

import net.dofmine.minedofmod.effects.ModEffect;
import net.dofmine.minedofmod.job.client.HydrationEntityPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ThirstEffect extends MobEffect {

    public ThirstEffect(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public boolean isDurationEffectTick(int p_19455_, int p_19456_) {
        return this == ModEffect.THIRST.get();
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int p_19468_) {
        if (this == ModEffect.THIRST.get() && livingEntity instanceof Player player) {
            if (!player.isCreative()) {
                HydrationEntityPlayer.get(player).addExhaustion(0.005F * (float)(p_19468_ + 1));
            }
        }
    }
}
