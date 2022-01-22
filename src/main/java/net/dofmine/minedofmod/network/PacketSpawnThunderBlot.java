package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.tileentity.Spells;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSpawnThunderBlot {

    private BlockPos playerPos;
    private boolean creative;

    public PacketSpawnThunderBlot(BlockPos playerPos, boolean creative) {
        this.playerPos = playerPos;
        this.creative = creative;
    }

    public static boolean handle(PacketSpawnThunderBlot packetSpawnThunderBlot, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Spells.thunderBolt(packetSpawnThunderBlot.playerPos, packetSpawnThunderBlot.creative);
        });
        return true;
    }

    public static void encode(PacketSpawnThunderBlot packetSpawnThunderBlot, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBlockPos(packetSpawnThunderBlot.playerPos);
        friendlyByteBuf.writeBoolean(packetSpawnThunderBlot.creative);
    }

    public static PacketSpawnThunderBlot decode(FriendlyByteBuf friendlyByteBuf) {
        return new PacketSpawnThunderBlot(friendlyByteBuf.readBlockPos(), friendlyByteBuf.readBoolean());
    }
}
