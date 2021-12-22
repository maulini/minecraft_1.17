package net.dofmine.minedofmod.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.MobSpawnType;

public class Spells {

    private static ServerLevel serverLevel;

    public Spells(ServerLevel serverLevel) {
        this.serverLevel = serverLevel;
    }

    public static void thunderBolt() {
        EntityType.LIGHTNING_BOLT.spawn(serverLevel, null, null, Minecraft.getInstance().player.eyeBlockPosition(), MobSpawnType.TRIGGERED, true, true);
    }

}
