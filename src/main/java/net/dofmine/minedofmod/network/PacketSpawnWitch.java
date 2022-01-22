package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.tileentity.Spells;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSpawnWitch {

    private BlockPos playerPos;
    private boolean creative;

    public PacketSpawnWitch(BlockPos playerPos, boolean creative) {
        this.playerPos = playerPos;
        this.creative = creative;
    }

    public static boolean handle(PacketSpawnWitch packetSpawnWitch, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Spells.witch(packetSpawnWitch.playerPos, packetSpawnWitch.creative);
        });
        return true;
    }

    public static void encode(PacketSpawnWitch packetSpawnWitch, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBlockPos(packetSpawnWitch.playerPos);
        friendlyByteBuf.writeBoolean(packetSpawnWitch.creative);
    }

    public static PacketSpawnWitch decode(FriendlyByteBuf friendlyByteBuf) {
        return new PacketSpawnWitch(friendlyByteBuf.readBlockPos(), friendlyByteBuf.readBoolean());
    }
}
