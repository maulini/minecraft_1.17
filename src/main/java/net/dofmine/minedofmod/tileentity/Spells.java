package net.dofmine.minedofmod.tileentity;

import net.dofmine.minedofmod.job.ExtendedEntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class Spells {

    public static ServerLevel serverLevel;

    public static void thunderBolt(BlockPos blockPos, boolean creative) {
        List<? extends Entity> entities = serverLevel.getEntitiesOfClass(Entity.class, new AABB(blockPos).expandTowards(5.0d, 5.0d, 5.0d), entity -> !(entity instanceof Player));
        if (!entities.isEmpty() && spendMana(10, creative)) {
            for (Entity entity: entities) {
                EntityType.LIGHTNING_BOLT.spawn(serverLevel, null, null, entity.blockPosition(), MobSpawnType.TRIGGERED, true, true);
            }
        }else {
            entities = serverLevel.getEntitiesOfClass(Entity.class, new AABB(blockPos).expandTowards(-5.0d, -5.0d, -5.0d), entity -> !(entity instanceof Player));
            if (!entities.isEmpty() && spendMana(10, creative)) {
                for (Entity entity : entities) {
                    EntityType.LIGHTNING_BOLT.spawn(serverLevel, null, null, entity.blockPosition(), MobSpawnType.TRIGGERED, true, true);
                }
            }
        }
    }

    public static void zombie(BlockPos blockPos, boolean creative) {
        if (spendMana(20, creative)) {
            EntityType.ZOMBIE.spawn(serverLevel, null, null, blockPos, MobSpawnType.TRIGGERED, true, true);
        }
    }

    public static void skeleton(BlockPos blockPos, boolean creative) {
        if (spendMana(20, creative)) {
            EntityType.SKELETON.spawn(serverLevel, null, null, blockPos, MobSpawnType.TRIGGERED, true, true);
        }
    }

    public static void witch(BlockPos blockPos, boolean creative) {
        if (spendMana(30, creative)) {
            EntityType.WITCH.spawn(serverLevel, null, null, blockPos, MobSpawnType.TRIGGERED, true, true);
        }
    }

    public static void levitation(BlockPos blockPos, boolean creative) {
        List<? extends Entity> entities = serverLevel.getEntitiesOfClass(Entity.class, new AABB(blockPos).expandTowards(5.0d, 5.0d, 5.0d), entity -> !(entity instanceof Player));
        if (!entities.isEmpty() && spendMana(15, creative)) {
            for (Entity entity: entities) {
                entity.lerpMotion(0.0d, 1.0d, 0.0d);
            }
        }else {
            entities = serverLevel.getEntitiesOfClass(Entity.class, new AABB(blockPos).expandTowards(-5.0d, -5.0d, -5.0d), entity -> !(entity instanceof Player));
            if (!entities.isEmpty() && spendMana(10, creative)) {
                for (Entity entity : entities) {
                    entity.lerpMotion(0.0d, 2.0d, 0.0d);
                }
            }
        }
    }

    private static boolean spendMana(int mana, boolean creative) {
        if (!creative) {
            return ExtendedEntityPlayer.get().spend(mana);
        }
        return true;
    }

}
