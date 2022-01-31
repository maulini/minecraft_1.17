package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.job.client.ExtendedHunterJobsEntityPlayer;
import net.dofmine.minedofmod.job.client.ExtendedMinerJobsEntityPlayer;
import net.dofmine.minedofmod.job.client.ExtendedWizardJobsEntityPlayer;
import net.dofmine.minedofmod.job.server.ExtendedHunterJobsEntityPlayerServer;
import net.dofmine.minedofmod.job.server.ExtendedWizardJobsEntityPlayerServer;
import net.dofmine.minedofmod.utils.JobsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketWizardJobs {

    private long xp;
    private long maxXp;
    private int level;

    public PacketWizardJobs(long xp, int level, long maxXp) {
        this.xp = xp;
        this.level = level;
        this.maxXp = maxXp;
    }

    public PacketWizardJobs() {
    }

    public static boolean handle(PacketWizardJobs packetJobs, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getSender() != null) {
                ExtendedWizardJobsEntityPlayerServer props = (ExtendedWizardJobsEntityPlayerServer) JobsUtil.getAllCapabilities(ctx.get().getSender()).stream().filter(iCapabilityProvider -> iCapabilityProvider instanceof ExtendedWizardJobsEntityPlayerServer).findFirst().get();
                props.xp = packetJobs.xp;
                props.level = packetJobs.level;
                props.maxXp = packetJobs.maxXp;
            } else {
                getLocalPlayer(packetJobs);
            }
        });
        ctx.get().setPacketHandled(true);
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    private static void getLocalPlayer(PacketWizardJobs packetJobs) {
        ExtendedWizardJobsEntityPlayer props = ExtendedWizardJobsEntityPlayer.get(Minecraft.getInstance().player);
        props.xp = packetJobs.xp;
        props.level = packetJobs.level;
        props.maxXp = packetJobs.maxXp;
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
