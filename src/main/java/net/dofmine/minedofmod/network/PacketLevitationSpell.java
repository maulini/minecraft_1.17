package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.tileentity.Spells;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketLevitationSpell {

    private final boolean creative;
    private BlockPos playerPos;

    public PacketLevitationSpell(BlockPos playerPos, boolean creative) {
        this.playerPos = playerPos;
        this.creative = creative;
    }

    public static boolean handle(PacketLevitationSpell packetLevitationSpell, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Spells.levitation(packetLevitationSpell.playerPos, packetLevitationSpell.creative);
        });
        return true;
    }

    public static void encode(PacketLevitationSpell packetLevitationSpell, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBlockPos(packetLevitationSpell.playerPos);
        friendlyByteBuf.writeBoolean(packetLevitationSpell.creative);
    }

    public static PacketLevitationSpell decode(FriendlyByteBuf friendlyByteBuf) {
        return new PacketLevitationSpell(friendlyByteBuf.readBlockPos(), friendlyByteBuf.readBoolean());
    }
}
