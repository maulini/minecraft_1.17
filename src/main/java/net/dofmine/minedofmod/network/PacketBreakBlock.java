package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.job.client.ExtendedFarmerJobsEntityPlayer;
import net.dofmine.minedofmod.job.client.ExtendedMinerJobsEntityPlayer;
import net.dofmine.minedofmod.setup.ClientSetup;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class PacketBreakBlock {

    private final ItemStack blockBreakPos;
    private static final Logger LOGGER = LogManager.getLogger();

    public PacketBreakBlock(ItemStack playerPos) {
        this.blockBreakPos = playerPos;
    }

    public static boolean handle(PacketBreakBlock packetBreakBlock, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            handleClientSide(packetBreakBlock);
        });
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClientSide(PacketBreakBlock packetBreakBlock) {
        if (ClientSetup.xpByBlockMiner.containsKey(packetBreakBlock.blockBreakPos.getItem())) {
            ExtendedMinerJobsEntityPlayer miner = ExtendedMinerJobsEntityPlayer.get(Minecraft.getInstance().player);
            miner.addXp(ClientSetup.xpByBlockMiner.get(packetBreakBlock.blockBreakPos.getItem()).apply(miner.level));
        }else if (ClientSetup.xpByBlockFarmer.containsKey(packetBreakBlock.blockBreakPos.getItem())) {
            ExtendedFarmerJobsEntityPlayer farmer = ExtendedFarmerJobsEntityPlayer.get(Minecraft.getInstance().player);
            farmer.addXp(ClientSetup.xpByBlockFarmer.get(packetBreakBlock.blockBreakPos.getItem()).apply(farmer.level));
        }
    }

    public static void encode(PacketBreakBlock packetBreakBlock, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeItem(packetBreakBlock.blockBreakPos);
    }

    public static PacketBreakBlock decode(FriendlyByteBuf friendlyByteBuf) {
        return new PacketBreakBlock(friendlyByteBuf.readItem());
    }
}
