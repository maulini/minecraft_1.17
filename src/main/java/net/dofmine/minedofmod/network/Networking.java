package net.dofmine.minedofmod.network;

import net.dofmine.minedofmod.MinedofMod;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;

public class Networking {

    private static SimpleChannel INSTANCE;
    private static int ID = 0;

    private static int nextID() {
        return ID++;
    }

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MinedofMod.MODS_ID, "minedofmod"),
                () -> "1.0",
                s -> true,
                s -> true);

        INSTANCE.messageBuilder(PacketMana.class, nextID())
                .encoder(PacketMana::encode)
                .decoder(PacketMana::decode)
                .consumer(PacketMana::handle)
                .add();

        INSTANCE.messageBuilder(PacketFarmerJobs.class, nextID())
                .encoder(PacketFarmerJobs::encode)
                .decoder(PacketFarmerJobs::decode)
                .consumer(PacketFarmerJobs::handle)
                .add();

        INSTANCE.messageBuilder(PacketHunterJobs.class, nextID())
                .encoder(PacketHunterJobs::encode)
                .decoder(PacketHunterJobs::decode)
                .consumer(PacketHunterJobs::handle)
                .add();

        INSTANCE.messageBuilder(PacketLocksmithJobs.class, nextID())
                .encoder(PacketLocksmithJobs::encode)
                .decoder(PacketLocksmithJobs::decode)
                .consumer(PacketLocksmithJobs::handle)
                .add();

        INSTANCE.messageBuilder(PacketMinerJobs.class, nextID())
                .encoder(PacketMinerJobs::encode)
                .decoder(PacketMinerJobs::decode)
                .consumer(PacketMinerJobs::handle)
                .add();

        INSTANCE.messageBuilder(PacketWizardJobs.class, nextID())
                .encoder(PacketWizardJobs::encode)
                .decoder(PacketWizardJobs::decode)
                .consumer(PacketWizardJobs::handle)
                .add();

        INSTANCE.messageBuilder(PacketHydration.class, nextID())
                .encoder(PacketHydration::encode)
                .decoder(PacketHydration::decode)
                .consumer(PacketHydration::handle)
                .add();
    }

    public static void sendToClient(Object packet, ServerPlayer player) {
        INSTANCE.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static void sendToServer(Object packet) {
        if (Minecraft.getInstance().getConnection() != null) {
            INSTANCE.sendToServer(packet);
        }
    }
}
