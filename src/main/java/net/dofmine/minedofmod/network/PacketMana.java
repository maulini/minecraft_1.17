package net.dofmine.minedofmod.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.dofmine.minedofmod.job.ExtendedEntityPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class PacketMana {

    private static int max;
    private static int mana;

    public PacketMana(int max, int mana) {
        this.max = max;
        this.mana = mana;
    }

    public PacketMana() {
    }

    public static boolean handle(PacketMana packetMana, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ExtendedEntityPlayer props = ExtendedEntityPlayer
                    .get();
            packetMana.max = props.maxMana;
            packetMana.mana = props.mana;
        });
        return true;
    }

    public static void encode(PacketMana packetMana, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(packetMana.max);
        friendlyByteBuf.writeInt(packetMana.mana);
    }

    public static PacketMana decode(FriendlyByteBuf friendlyByteBuf) {
        return new PacketMana(friendlyByteBuf.readInt(), friendlyByteBuf.readInt());
    }
}
