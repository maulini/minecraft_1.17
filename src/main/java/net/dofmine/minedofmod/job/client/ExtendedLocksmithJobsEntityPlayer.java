package net.dofmine.minedofmod.job.client;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.job.server.ExtendedHunterJobsEntityPlayerServer;
import net.dofmine.minedofmod.job.server.ExtendedLocksmithJobsEntityPlayerServer;
import net.dofmine.minedofmod.network.Networking;
import net.dofmine.minedofmod.network.PacketLocksmithJobs;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ExtendedLocksmithJobsEntityPlayer implements ICapabilitySerializable {

    public final static ResourceLocation EXT_PROP_NAME = new ResourceLocation(MinedofMod.MODS_ID, "extpropplayerlocksmithjobs_client");

    private final Player player;
    public int level;
    private static LazyOptional<ExtendedLocksmithJobsEntityPlayer> lazyOptional;
    public static Capability<ExtendedLocksmithJobsEntityPlayer> LOCKSMITH_JOBS = CapabilityManager.get(new CapabilityToken<>() {
        @Override
        public String toString() {
            return EXT_PROP_NAME.toString();
        }
    });

    public ExtendedLocksmithJobsEntityPlayer(Player player, AttachCapabilitiesEvent<Entity> attachCapabilitiesEvent) {
        this.level = 1;
        this.player = player;
        attachCapabilitiesEvent.addCapability(EXT_PROP_NAME, this);
        attachCapabilitiesEvent.addListener(() -> lazyOptional.invalidate());
    }

    public static final void register(Player player, AttachCapabilitiesEvent<Entity> attachCapabilitiesEvent) {
        ExtendedLocksmithJobsEntityPlayer locksmith = new ExtendedLocksmithJobsEntityPlayer(player, attachCapabilitiesEvent);
        lazyOptional = LazyOptional.of(() -> locksmith);
    }

    public static final ExtendedLocksmithJobsEntityPlayer get(Player player) {
        return player.getCapability(LOCKSMITH_JOBS).orElse(null);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == LOCKSMITH_JOBS) {
            return lazyOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public Tag serializeNBT() {
        CompoundTag properties = new CompoundTag();

        properties.put("level", IntTag.valueOf(this.level));
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
    }

    public long getLevel() {
        return this.level;
    }

}