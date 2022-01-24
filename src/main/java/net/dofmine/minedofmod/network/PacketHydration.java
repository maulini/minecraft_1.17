package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.job.HydrationEntityPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketHydration {
    private int max;
    private int hydration;
    private float exhaustionLevel;
    private int tickTimer;

    public PacketHydration(int max, int hydration, float exhaustionLevel, int tickTimer) {
        this.max = max;
        this.hydration = hydration;
        this.exhaustionLevel = exhaustionLevel;
        this.tickTimer = tickTimer;
    }

    public PacketHydration() {
    }

    public static boolean handle(PacketHydration packetHydration, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            HydrationEntityPlayer props = HydrationEntityPlayer
                    .get();
            packetHydration.max = props.maxHydration;
            packetHydration.hydration = props.actualHydration;
            packetHydration.exhaustionLevel = props.exhaustionLevel;
            packetHydration.tickTimer = props.tickTimer;
        });
        return true;
    }

    public static void encode(PacketHydration packetHydration, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(packetHydration.max);
        friendlyByteBuf.writeInt(packetHydration.hydration);
        friendlyByteBuf.writeFloat(packetHydration.exhaustionLevel);
        friendlyByteBuf.writeInt(packetHydration.tickTimer);
    }

    public static PacketHydration decode(FriendlyByteBuf friendlyByteBuf) {
        return new PacketHydration(friendlyByteBuf.readInt(), friendlyByteBuf.readInt(), friendlyByteBuf.readFloat(), friendlyByteBuf.readInt());
    }
}
