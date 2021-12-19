package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.job.ExtendedFarmerJobsEntityPlayer;
import net.dofmine.minedofmod.job.ExtendedHunterJobsEntityPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketFarmerJobs {

    private static long xp;
    private static int level;

    public PacketFarmerJobs(long xp, int level) {
        this.xp = xp;
        this.level = level;
    }

    public PacketFarmerJobs() {
    }

    public static boolean handle(PacketFarmerJobs packetJobs, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ExtendedFarmerJobsEntityPlayer props = ExtendedFarmerJobsEntityPlayer.get();
            packetJobs.xp = props.xp;
            packetJobs.level = props.level;
        });
        return true;
    }

    public static void encode(PacketFarmerJobs packetHunterJobs, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeLong(packetHunterJobs.xp);
        friendlyByteBuf.writeInt(packetHunterJobs.level);
    }

    public static PacketFarmerJobs decode(FriendlyByteBuf friendlyByteBuf) {
        return new PacketFarmerJobs(friendlyByteBuf.readLong(), friendlyByteBuf.readInt());
    }
}
