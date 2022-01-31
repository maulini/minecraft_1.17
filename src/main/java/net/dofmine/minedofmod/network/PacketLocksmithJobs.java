package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.job.client.ExtendedHunterJobsEntityPlayer;
import net.dofmine.minedofmod.job.client.ExtendedLocksmithJobsEntityPlayer;
import net.dofmine.minedofmod.job.server.ExtendedHunterJobsEntityPlayerServer;
import net.dofmine.minedofmod.job.server.ExtendedLocksmithJobsEntityPlayerServer;
import net.dofmine.minedofmod.utils.JobsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketLocksmithJobs {

    private int level;

    public PacketLocksmithJobs(int level) {
        this.level = level;
    }

    public PacketLocksmithJobs() {
    }

    public static boolean handle(PacketLocksmithJobs packetJobs, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() != null) {
                ExtendedLocksmithJobsEntityPlayerServer props = (ExtendedLocksmithJobsEntityPlayerServer) JobsUtil.getAllCapabilities(ctx.get().getSender()).stream().filter(iCapabilityProvider -> iCapabilityProvider instanceof ExtendedLocksmithJobsEntityPlayerServer).findFirst().get();
                props.level = packetJobs.level;
            } else {
                getLocalPlayer(packetJobs);
            }
        });
        ctx.get().setPacketHandled(true);
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    private static void getLocalPlayer(PacketLocksmithJobs packetJobs) {
        ExtendedLocksmithJobsEntityPlayer props = ExtendedLocksmithJobsEntityPlayer.get(Minecraft.getInstance().player);
        props.level = packetJobs.level;
    }

    public static void encode(PacketLocksmithJobs packetHunterJobs, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(packetHunterJobs.level);
    }

    public static PacketLocksmithJobs decode(FriendlyByteBuf friendlyByteBuf) {
        return new PacketLocksmithJobs(friendlyByteBuf.readInt());
    }
}
