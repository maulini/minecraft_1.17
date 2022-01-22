package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.job.HydrationEntityPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketHydration {
    private static int max;
    private static int hydration;

    public PacketHydration(int max, int hydration) {
        this.max = max;
        this.hydration = hydration;
    }

    public PacketHydration() {
    }

    public static boolean handle(PacketHydration packetHydration, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            HydrationEntityPlayer props = HydrationEntityPlayer
                    .get();
            packetHydration.max = props.maxHydration;
            packetHydration.hydration = props.actualHydration;
        });
        return true;
    }

    public static void encode(PacketHydration packetHydration, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(packetHydration.max);
        friendlyByteBuf.writeInt(packetHydration.hydration);
    }

    public static PacketHydration decode(FriendlyByteBuf friendlyByteBuf) {
        return new PacketHydration(friendlyByteBuf.readInt(), friendlyByteBuf.readInt());
    }
}
