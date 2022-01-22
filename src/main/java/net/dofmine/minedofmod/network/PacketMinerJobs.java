package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.job.ExtendedMinerJobsEntityPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketMinerJobs {

    private static long xp;
    private static int level;
    private static long maxXp;

    public PacketMinerJobs(long xp, int level, long maxXp) {
        this.xp = xp;
        this.level = level;
        this.maxXp = maxXp;
    }

    public PacketMinerJobs() {
    }

    public static boolean handle(PacketMinerJobs packetJobs, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ExtendedMinerJobsEntityPlayer props = ExtendedMinerJobsEntityPlayer.get();
            packetJobs.xp = props.xp;
            packetJobs.level = props.level;
            packetJobs.maxXp = props.maxXp;
        });
        return true;
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
