package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.job.client.ExtendedFarmerJobsEntityPlayer;
import net.dofmine.minedofmod.job.client.ExtendedHunterJobsEntityPlayer;
import net.dofmine.minedofmod.job.server.ExtendedEntityPlayerServer;
import net.dofmine.minedofmod.job.server.ExtendedFarmerJobsEntityPlayerServer;
import net.dofmine.minedofmod.job.server.ExtendedHunterJobsEntityPlayerServer;
import net.dofmine.minedofmod.utils.JobsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketHunterJobs {

    private long xp;
    private int level;
    private long maxXp;

    public PacketHunterJobs(long xp, int level, long maxXp) {
        this.xp = xp;
        this.level = level;
        this.maxXp = maxXp;
    }

    public PacketHunterJobs() {
    }

    public static boolean handle(PacketHunterJobs packetJobs, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() != null) {
                ExtendedHunterJobsEntityPlayerServer props = (ExtendedHunterJobsEntityPlayerServer) JobsUtil.getAllCapabilities(ctx.get().getSender()).stream().filter(iCapabilityProvider -> iCapabilityProvider instanceof ExtendedHunterJobsEntityPlayerServer).findFirst().get();
                props.xp = packetJobs.xp;
                props.level = packetJobs.level;
                props.maxXp = packetJobs.maxXp;
            } else {
                getLocalPlayer(packetJobs);
            }
        });
        ctx.get().setPacketHandled(true);
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    private static void getLocalPlayer(PacketHunterJobs packetJobs) {
        ExtendedHunterJobsEntityPlayer props = ExtendedHunterJobsEntityPlayer.get(Minecraft.getInstance().player);
        props.xp = packetJobs.xp;
        props.level = packetJobs.level;
        props.maxXp = packetJobs.maxXp;
    }

    public static void encode(PacketHunterJobs packetHunterJobs, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeLong(packetHunterJobs.xp);
        friendlyByteBuf.writeInt(packetHunterJobs.level);
        friendlyByteBuf.writeLong(packetHunterJobs.maxXp);
    }

    public static PacketHunterJobs decode(FriendlyByteBuf friendlyByteBuf) {
        return new PacketHunterJobs(friendlyByteBuf.readLong(), friendlyByteBuf.readInt(), friendlyByteBuf.readLong());
    }
}
