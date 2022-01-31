package net.dofmine.minedofmod.tileentity;

import net.dofmine.minedofmod.job.client.ExtendedEntityPlayer;
import net.dofmine.minedofmod.job.server.ExtendedEntityPlayerServer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class Spells {

    public static ServerPlayer serverPlayer;

    public static void thunderBolt(BlockPos blockPos, boolean creative) {
        List<? extends Entity> entities = serverPlayer.level.getEntitiesOfClass(Entity.class, new AABB(blockPos).expandTowards(5.0d, 5.0d, 5.0d), entity -> !(entity instanceof Player));
        if (!entities.isEmpty() && spendMana(10, creative)) {
            for (Entity entity: entities) {
                EntityType.LIGHTNING_BOLT.spawn((ServerLevel) serverPlayer.level, null, null, entity.blockPosition(), MobSpawnType.TRIGGERED, true, true);
            }
        }else {
            entities = serverPlayer.level.getEntitiesOfClass(Entity.class, new AABB(blockPos).expandTowards(-5.0d, -5.0d, -5.0d), entity -> !(entity instanceof Player));
            if (!entities.isEmpty() && spendMana(10, creative)) {
                for (Entity entity : entities) {
                    EntityType.LIGHTNING_BOLT.spawn((ServerLevel) serverPlayer.level, null, null, entity.blockPosition(), MobSpawnType.TRIGGERED, true, true);
                }
            }
        }
    }

    public static void zombie(BlockPos blockPos, boolean creative) {
        if (spendMana(20, creative)) {
            EntityType.ZOMBIE.spawn((ServerLevel) serverPlayer.level, null, null, blockPos, MobSpawnType.TRIGGERED, true, true);
        }
    }

    public static void skeleton(BlockPos blockPos, boolean creative) {
        if (spendMana(20, creative)) {
            EntityType.SKELETON.spawn((ServerLevel) serverPlayer.level, null, null, blockPos, MobSpawnType.TRIGGERED, true, true);
        }
    }

    public static void witch(BlockPos blockPos, boolean creative) {
        if (spendMana(30, creative)) {
            EntityType.WITCH.spawn((ServerLevel) serverPlayer.level, null, null, blockPos, MobSpawnType.TRIGGERED, true, true);
        }
    }

    public static void levitation(BlockPos blockPos, boolean creative) {
        List<? extends Entity> entities = serverPlayer.level.getEntitiesOfClass(Entity.class, new AABB(blockPos).expandTowards(5.0d, 5.0d, 5.0d), entity -> !(entity instanceof Player));
        if (!entities.isEmpty() && spendMana(15, creative)) {
            for (Entity entity: entities) {
                entity.lerpMotion(0.0d, 1.0d, 0.0d);
            }
        }else {
            entities = serverPlayer.level.getEntitiesOfClass(Entity.class, new AABB(blockPos).expandTowards(-5.0d, -5.0d, -5.0d), entity -> !(entity instanceof Player));
            if (!entities.isEmpty() && spendMana(10, creative)) {
                for (Entity entity : entities) {
                    entity.lerpMotion(0.0d, 2.0d, 0.0d);
                }
            }
        }
    }

    private static boolean spendMana(int mana, boolean creative) {
        if (!creative) {
            return ExtendedEntityPlayerServer.get(serverPlayer).spend(mana);
        }
        return true;
    }

}
