package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.job.ExtendedHunterJobsEntityPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketHunterJobs {

    private static long xp;
    private static int level;

    public PacketHunterJobs(long xp, int level) {
        this.xp = xp;
        this.level = level;
    }

    public PacketHunterJobs() {
    }

    public static boolean handle(PacketHunterJobs packetJobs, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ExtendedHunterJobsEntityPlayer props = ExtendedHunterJobsEntityPlayer.get();
            packetJobs.xp = props.xp;
            packetJobs.level = props.level;
        });
        return true;
    }

    public static void encode(PacketHunterJobs packetHunterJobs, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeLong(packetHunterJobs.xp);
        friendlyByteBuf.writeInt(packetHunterJobs.level);
    }

    public static PacketHunterJobs decode(FriendlyByteBuf friendlyByteBuf) {
        return new PacketHunterJobs(friendlyByteBuf.readLong(), friendlyByteBuf.readInt());
    }
}
