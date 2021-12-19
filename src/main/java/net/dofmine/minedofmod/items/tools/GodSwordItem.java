package net.dofmine.minedofmod.items.tools;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.ForgeTier;

import java.util.Random;

public class GodSwordItem extends SwordItem {

    public GodSwordItem(Tier tier, int p_43270_, float p_43271_, Properties properties) {
        super(tier, p_43270_, p_43271_, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity livingEntity, LivingEntity livingEntity1) {
        if (livingEntity1 instanceof ServerPlayer serverPlayer) {
            if (!serverPlayer.level.isClientSide) {
                Random random = serverPlayer.getRandom();
                double rand = random.nextDouble();
                if (rand >= 0d && rand <= 0.25d) {
                    EntityType.LIGHTNING_BOLT.spawn((ServerLevel) serverPlayer.level, null, null, new BlockPos(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ()), MobSpawnType.TRIGGERED, true, true);
                }
            }
        }
        return super.hurtEnemy(itemStack, livingEntity, livingEntity1);
    }
}
