package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.job.ExtendedEntityPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

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
