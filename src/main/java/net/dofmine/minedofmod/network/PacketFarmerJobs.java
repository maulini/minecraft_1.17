package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.job.client.ExtendedFarmerJobsEntityPlayer;
import net.dofmine.minedofmod.job.server.ExtendedFarmerJobsEntityPlayerServer;
import net.dofmine.minedofmod.utils.JobsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;

import java.util.function.Supplier;

public class PacketFarmerJobs {

    private long xp;
    private int level;
    private long maxXp;

    public PacketFarmerJobs(long xp, int level, long maxXp) {
        this.xp = xp;
        this.level = level;
        this.maxXp = maxXp;
    }

    public PacketFarmerJobs() {
    }

    public static boolean handle(PacketFarmerJobs packetJobs, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() != null) {
                ExtendedFarmerJobsEntityPlayerServer props = (ExtendedFarmerJobsEntityPlayerServer) JobsUtil.getAllCapabilities(ctx.get().getSender()).stream().filter(iCapabilityProvider -> iCapabilityProvider instanceof ExtendedFarmerJobsEntityPlayerServer).findFirst().get();
                props.xp = packetJobs.xp;
                props.level = packetJobs.level;
                props.maxXp = packetJobs.maxXp;
            }else {
                getLocalPlayer(packetJobs);
            }
        });
        ctx.get().setPacketHandled(true);
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    private static void getLocalPlayer(PacketFarmerJobs packetJobs) {
        ExtendedFarmerJobsEntityPlayer props = ExtendedFarmerJobsEntityPlayer.get(Minecraft.getInstance().player);
        props.xp = packetJobs.xp;
        props.level = packetJobs.level;
        props.maxXp = props.getXpByLevel(packetJobs.level);
    }

    public static void encode(PacketFarmerJobs packetHunterJobs, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeLong(packetHunterJobs.xp);
        friendlyByteBuf.writeInt(packetHunterJobs.level);
        friendlyByteBuf.writeLong(packetHunterJobs.maxXp);
    }

    public static PacketFarmerJobs decode(FriendlyByteBuf friendlyByteBuf) {
        return new PacketFarmerJobs(friendlyByteBuf.readLong(), friendlyByteBuf.readInt(), friendlyByteBuf.readLong());
    }
}
