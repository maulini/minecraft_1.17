package net.dofmine.minedofmod.job;

import com.google.common.collect.ImmutableMap;
import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.network.Networking;
import net.dofmine.minedofmod.network.PacketHunterJobs;
import net.dofmine.minedofmod.network.PacketLocksmithJobs;
import net.dofmine.minedofmod.setup.ClientSetup;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class ExtendedLocksmithJobsEntityPlayer implements ICapabilitySerializable {

    public final static ResourceLocation EXT_PROP_NAME = new ResourceLocation(MinedofMod.MODS_ID, "extpropplayerlocksmithjobs");

    private final Player player;
    protected static AttachCapabilitiesEvent attachCapabilitiesEvent;
    public int level;

    public ExtendedLocksmithJobsEntityPlayer(Player player, AttachCapabilitiesEvent attachCapabilitiesEvent) {
        this.level = 1;
        this.player = player;
        this.attachCapabilitiesEvent = attachCapabilitiesEvent;
        attachCapabilitiesEvent.addCapability(EXT_PROP_NAME, this);
    }

    public static final void register(Player player, AttachCapabilitiesEvent attachCapabilitiesEvent) {
        new ExtendedLocksmithJobsEntityPlayer(player, attachCapabilitiesEvent);
    }

    public static final ExtendedLocksmithJobsEntityPlayer get() {
        return (ExtendedLocksmithJobsEntityPlayer) attachCapabilitiesEvent.getCapabilities().get(EXT_PROP_NAME);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return LazyOptional.empty();
    }

    @Override
    public Tag serializeNBT() {
        CompoundTag properties = new CompoundTag();

        properties.put("level", IntTag.valueOf(this.level));
        ClientSetup.storeEntityData(player.getDisplayName().getString(), properties);
        return properties;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        CompoundTag properties = (CompoundTag) nbt;
        this.level = properties.getInt("level");
    }

    public void addLevel(int amount) {
        level += amount;
        if (level > 20) {
            level = 20;
        }
        this.sync();
    }

    public void sync() {
        PacketLocksmithJobs packetJobs = new PacketLocksmithJobs(this.level);
        Networking.sendToServer(packetJobs);

        if (player.level.isClientSide) {
            Networking.sendToClient(packetJobs, (ServerPlayer) player);
        }
    }

    public long getLevel() {
        return this.level;
    }

}