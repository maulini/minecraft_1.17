package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.job.ExtendedFarmerJobsEntityPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketFarmerJobs {

    private static long xp;
    private static int level;
    private static long maxXp;

    public PacketFarmerJobs(long xp, int level, long maxXp) {
        PacketFarmerJobs.xp = xp;
        PacketFarmerJobs.level = level;
        PacketFarmerJobs.maxXp = maxXp;
    }

    public PacketFarmerJobs() {
    }

    public static boolean handle(PacketFarmerJobs packetJobs, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ExtendedFarmerJobsEntityPlayer props = ExtendedFarmerJobsEntityPlayer.get();
            props.xp = packetJobs.xp;
            props.level = packetJobs.level;
            props.maxXp = packetJobs.maxXp;
        });
        return true;
    }

    public static void encode(PacketFarmerJobs packetHunterJobs, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeLong(xp);
        friendlyByteBuf.writeInt(level);
        friendlyByteBuf.writeLong(maxXp);
    }

    public static PacketFarmerJobs decode(FriendlyByteBuf friendlyByteBuf) {
        return new PacketFarmerJobs(friendlyByteBuf.readLong(), friendlyByteBuf.readInt(), friendlyByteBuf.readLong());
    }
}
