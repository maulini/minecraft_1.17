package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.job.client.ExtendedHunterJobsEntityPlayer;
import net.dofmine.minedofmod.job.client.ExtendedMinerJobsEntityPlayer;
import net.dofmine.minedofmod.job.server.ExtendedHunterJobsEntityPlayerServer;
import net.dofmine.minedofmod.job.server.ExtendedLocksmithJobsEntityPlayerServer;
import net.dofmine.minedofmod.utils.JobsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketMinerJobs {

    private long xp;
    private int level;
    private long maxXp;

    public PacketMinerJobs(long xp, int level, long maxXp) {
        this.xp = xp;
        this.level = level;
        this.maxXp = maxXp;
    }

    public PacketMinerJobs() {
    }

    public static boolean handle(PacketMinerJobs packetJobs, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() != null) {
                ExtendedMinerJobsEntityPlayer props = (ExtendedMinerJobsEntityPlayer) JobsUtil.getAllCapabilities(ctx.get().getSender()).stream().filter(iCapabilityProvider -> iCapabilityProvider instanceof ExtendedMinerJobsEntityPlayer).findFirst().get();
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
    private static void getLocalPlayer(PacketMinerJobs packetJobs) {
        ExtendedMinerJobsEntityPlayer props = ExtendedMinerJobsEntityPlayer.get(Minecraft.getInstance().player);
        props.xp = packetJobs.xp;
        props.level = packetJobs.level;
        props.maxXp = packetJobs.maxXp;
    }

    public static void encode(PacketMinerJobs packetHunterJobs, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeLong(packetHunterJobs.xp);
        friendlyByteBuf.writeInt(packetHunterJobs.level);
        friendlyByteBuf.writeLong(packetHunterJobs.maxXp);
    }

    public static PacketMinerJobs decode(FriendlyByteBuf friendlyByteBuf) {
        return new PacketMinerJobs(friendlyByteBuf.readLong(), friendlyByteBuf.readInt(), friendlyByteBuf.readLong());
    }
}
