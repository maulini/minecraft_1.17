package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.job.ExtendedLocksmithJobsEntityPlayer;
import net.dofmine.minedofmod.job.ExtendedWizardJobsEntityPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketWizardJobs {

    private static long xp;
    private static int level;

    public PacketWizardJobs(long xp, int level) {
        this.xp = xp;
        this.level = level;
    }

    public PacketWizardJobs() {
    }

    public static boolean handle(PacketWizardJobs packetJobs, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ExtendedWizardJobsEntityPlayer props = ExtendedWizardJobsEntityPlayer.get();
            packetJobs.xp = props.xp;
            packetJobs.level = props.level;
        });
        return true;
    }

    public static void encode(PacketWizardJobs packetHunterJobs, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeLong(packetHunterJobs.xp);
        friendlyByteBuf.writeInt(packetHunterJobs.level);
    }

    public static PacketWizardJobs decode(FriendlyByteBuf friendlyByteBuf) {
        return new PacketWizardJobs(friendlyByteBuf.readLong(), friendlyByteBuf.readInt());
    }
}
