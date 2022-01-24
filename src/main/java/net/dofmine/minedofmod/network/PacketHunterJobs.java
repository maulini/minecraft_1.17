package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.job.ExtendedHunterJobsEntityPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketHunterJobs {

    private static long xp;
    private static int level;
    private static long maxXp;

    public PacketHunterJobs(long xp, int level, long maxXp) {
        this.xp = xp;
        this.level = level;
        this.maxXp = maxXp;
    }

    public PacketHunterJobs() {
    }

    public static boolean handle(PacketHunterJobs packetJobs, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ExtendedHunterJobsEntityPlayer props = ExtendedHunterJobsEntityPlayer.get();
            props.xp = packetJobs.xp;
            props.level = packetJobs.level;
            props.maxXp = packetJobs.maxXp;
        });
        return true;
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
