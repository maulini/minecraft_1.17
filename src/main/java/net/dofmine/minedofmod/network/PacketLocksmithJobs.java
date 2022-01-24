package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.job.ExtendedLocksmithJobsEntityPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketLocksmithJobs {

    private static int level;

    public PacketLocksmithJobs(int level) {
        this.level = level;
    }

    public PacketLocksmithJobs() {
    }

    public static boolean handle(PacketLocksmithJobs packetJobs, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ExtendedLocksmithJobsEntityPlayer props = ExtendedLocksmithJobsEntityPlayer.get();
            props.level = packetJobs.level;
        });
        return true;
    }

    public static void encode(PacketLocksmithJobs packetHunterJobs, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(packetHunterJobs.level);
    }

    public static PacketLocksmithJobs decode(FriendlyByteBuf friendlyByteBuf) {
        return new PacketLocksmithJobs(friendlyByteBuf.readInt());
    }
}
