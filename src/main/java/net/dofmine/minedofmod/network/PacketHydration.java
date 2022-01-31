package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.job.client.ExtendedHunterJobsEntityPlayer;
import net.dofmine.minedofmod.job.client.HydrationEntityPlayer;
import net.dofmine.minedofmod.job.server.ExtendedFarmerJobsEntityPlayerServer;
import net.dofmine.minedofmod.job.server.HydrationEntityPlayerServer;
import net.dofmine.minedofmod.utils.JobsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
            if (ctx.get().getSender() != null) {
                HydrationEntityPlayerServer props = (HydrationEntityPlayerServer) JobsUtil.getAllCapabilities(ctx.get().getSender()).stream().filter(iCapabilityProvider -> iCapabilityProvider instanceof HydrationEntityPlayerServer).findFirst().get();
                packetHydration.max = props.maxHydration;
                packetHydration.hydration = props.actualHydration;
                packetHydration.exhaustionLevel = props.exhaustionLevel;
                packetHydration.tickTimer = props.tickTimer;
            } else {
                getLocalPlayer(packetHydration);
            }
        });
        ctx.get().setPacketHandled(true);
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    private static void getLocalPlayer(PacketHydration packetHydration) {
        HydrationEntityPlayer props = HydrationEntityPlayer
                .get(Minecraft.getInstance().player);
        packetHydration.max = props.maxHydration;
        packetHydration.hydration = props.actualHydration;
        packetHydration.exhaustionLevel = props.exhaustionLevel;
        packetHydration.tickTimer = props.tickTimer;
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
