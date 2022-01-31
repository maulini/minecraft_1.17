package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.job.client.ExtendedEntityPlayer;
import net.dofmine.minedofmod.job.client.ExtendedFarmerJobsEntityPlayer;
import net.dofmine.minedofmod.job.server.ExtendedEntityPlayerServer;
import net.dofmine.minedofmod.job.server.HydrationEntityPlayerServer;
import net.dofmine.minedofmod.utils.JobsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketMana {

    private int max;
    private int mana;

    public PacketMana(int max, int mana) {
        this.max = max;
        this.mana = mana;
    }

    public PacketMana() {
    }

    public static boolean handle(PacketMana packetMana, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() != null) {
                ExtendedEntityPlayerServer props = (ExtendedEntityPlayerServer) JobsUtil.getAllCapabilities(ctx.get().getSender()).stream().filter(iCapabilityProvider -> iCapabilityProvider instanceof ExtendedEntityPlayerServer).findFirst().get();
                props.maxMana = packetMana.max;
                props.mana = packetMana.mana;
            }else {
                getLocalPlayer(packetMana);
            }
        });
        ctx.get().setPacketHandled(true);
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    private static void getLocalPlayer(PacketMana packetMana) {
        ExtendedEntityPlayer props = ExtendedEntityPlayer.get(Minecraft.getInstance().player);
        props.maxMana = packetMana.max;
        props.mana = packetMana.mana;
    }

    public static void encode(PacketMana packetMana, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(packetMana.max);
        friendlyByteBuf.writeInt(packetMana.mana);
    }

    public static PacketMana decode(FriendlyByteBuf friendlyByteBuf) {
        return new PacketMana(friendlyByteBuf.readInt(), friendlyByteBuf.readInt());
    }
}
