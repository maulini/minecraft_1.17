package net.dofmine.minedofmod.tileentity;

import net.dofmine.minedofmod.job.ExtendedEntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class Spells {

    public static ServerLevel serverLevel;

    public static void thunderBolt() {
        List<? extends Entity> entities = serverLevel.getEntitiesOfClass(Entity.class, Minecraft.getInstance().player.getBoundingBox().expandTowards(5.0d, 5.0d, 5.0d), entity -> !(entity instanceof Player));
        if (!entities.isEmpty() && spendMana(10)) {
            for (Entity entity: entities) {
                EntityType.LIGHTNING_BOLT.spawn(serverLevel, null, null, entity.blockPosition(), MobSpawnType.TRIGGERED, true, true);
            }
        }else {
            entities = serverLevel.getEntitiesOfClass(Entity.class, Minecraft.getInstance().player.getBoundingBox().expandTowards(-5.0d, -5.0d, -5.0d), entity -> !(entity instanceof Player));
            if (!entities.isEmpty() && spendMana(10)) {
                for (Entity entity : entities) {
                    EntityType.LIGHTNING_BOLT.spawn(serverLevel, null, null, entity.blockPosition(), MobSpawnType.TRIGGERED, true, true);
                }
            }
        }
    }

    public static void zombie() {
        if (spendMana(20)) {
            EntityType.ZOMBIE.spawn(serverLevel, null, null, Minecraft.getInstance().player.eyeBlockPosition(), MobSpawnType.TRIGGERED, true, true);
        }
    }

    public static void skeleton() {
        if (spendMana(20)) {
            EntityType.SKELETON.spawn(serverLevel, null, null, Minecraft.getInstance().player.eyeBlockPosition(), MobSpawnType.TRIGGERED, true, true);
        }
    }

    public static void witch() {
        if (spendMana(30)) {
            EntityType.WITCH.spawn(serverLevel, null, null, Minecraft.getInstance().player.eyeBlockPosition(), MobSpawnType.TRIGGERED, true, true);
        }
    }

    public static void levitation() {
        List<? extends Entity> entities = serverLevel.getEntitiesOfClass(Entity.class, Minecraft.getInstance().player.getBoundingBox().expandTowards(5.0d, 5.0d, 5.0d), entity -> !(entity instanceof Player));
        if (!entities.isEmpty() && spendMana(15)) {
            for (Entity entity: entities) {
                entity.lerpMotion(0.0d, 1.0d, 0.0d);
            }
        }else {
            entities = serverLevel.getEntitiesOfClass(Entity.class, Minecraft.getInstance().player.getBoundingBox().expandTowards(-5.0d, -5.0d, -5.0d), entity -> !(entity instanceof Player));
            if (!entities.isEmpty() && spendMana(10)) {
                for (Entity entity : entities) {
                    entity.lerpMotion(0.0d, 2.0d, 0.0d);
                }
            }
        }
    }

    private static boolean spendMana(int mana) {
        if (!Minecraft.getInstance().player.isCreative()) {
            return ExtendedEntityPlayer.get().spend(mana);
        }
        return true;
    }

}
