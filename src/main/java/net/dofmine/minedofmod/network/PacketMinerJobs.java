package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.job.ExtendedFarmerJobsEntityPlayer;
import net.dofmine.minedofmod.job.ExtendedMinerJobsEntityPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketMinerJobs {

    private static long xp;
    private static int level;

    public PacketMinerJobs(long xp, int level) {
        this.xp = xp;
        this.level = level;
    }

    public PacketMinerJobs() {
    }

    public static boolean handle(PacketMinerJobs packetJobs, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ExtendedMinerJobsEntityPlayer props = ExtendedMinerJobsEntityPlayer.get();
            packetJobs.xp = props.xp;
            packetJobs.level = props.level;
        });
        return true;
    }

    public static void encode(PacketMinerJobs packetHunterJobs, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeLong(packetHunterJobs.xp);
        friendlyByteBuf.writeInt(packetHunterJobs.level);
    }

    public static PacketMinerJobs decode(FriendlyByteBuf friendlyByteBuf) {
        return new PacketMinerJobs(friendlyByteBuf.readLong(), friendlyByteBuf.readInt());
    }
}
