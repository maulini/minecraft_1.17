package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.job.ExtendedWizardJobsEntityPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketWizardJobs {

    private static long xp;
    private static long maxXp;
    private static int level;

    public PacketWizardJobs(long xp, int level, long maxXp) {
        this.xp = xp;
        this.level = level;
        this.maxXp = maxXp;
    }

    public PacketWizardJobs() {
    }

    public static boolean handle(PacketWizardJobs packetJobs, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ExtendedWizardJobsEntityPlayer props = ExtendedWizardJobsEntityPlayer.get();
            props.xp = packetJobs.xp;
            props.level = packetJobs.level;
            props.maxXp = packetJobs.maxXp;
        });
        return true;
    }

    public static void encode(PacketWizardJobs packetHunterJobs, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeLong(packetHunterJobs.xp);
        friendlyByteBuf.writeInt(packetHunterJobs.level);
        friendlyByteBuf.writeLong(packetHunterJobs.maxXp);
    }

    public static PacketWizardJobs decode(FriendlyByteBuf friendlyByteBuf) {
        return new PacketWizardJobs(friendlyByteBuf.readLong(), friendlyByteBuf.readInt(), friendlyByteBuf.readLong());
    }
}
