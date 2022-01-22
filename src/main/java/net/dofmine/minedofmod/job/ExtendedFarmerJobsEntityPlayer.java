package net.dofmine.minedofmod.job;

import com.google.common.collect.ImmutableMap;
import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.network.Networking;
import net.dofmine.minedofmod.network.PacketFarmerJobs;
import net.dofmine.minedofmod.network.PacketHunterJobs;
import net.dofmine.minedofmod.setup.ClientSetup;
import net.dofmine.minedofmod.setup.EventHandler;
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

public class ExtendedFarmerJobsEntityPlayer implements ICapabilitySerializable {

    public final static ResourceLocation EXT_PROP_NAME = new ResourceLocation(MinedofMod.MODS_ID, "extpropplayerfarmerjobs");
    private Map<Integer, Long> xpByLvl = new ImmutableMap.Builder() //
            .put(1, 100L) //
            .put(2, 250L) //
            .put(3, 500L) //
            .put(4, 1000L) //
            .put(5, 2000L) //
            .put(6, 4000L) //
            .put(7, 8000L) //
            .put(8, 9500L) //
            .put(9, 11000L) //
            .put(10, 12000L) //
            .put(11, 13500L) //
            .put(12, 15000L) //
            .put(13, 16500L) //
            .put(14, 19000L) //
            .put(15, 21000L) //
            .put(16, 23500L) //
            .put(17, 25400L) //
            .put(18, 28000L) //
            .put(19, 30000L) //
            .put(20, 150_000L) //
            .build();

    private final Player player;
    protected static AttachCapabilitiesEvent attachCapabilitiesEvent;
    public long xp;
    public long maxXp;
    public int level;
    public int maxLevel;

    public ExtendedFarmerJobsEntityPlayer(Player player, AttachCapabilitiesEvent attachCapabilitiesEvent) {
        this.xp = 0;
        this.level = 1;
        this.maxLevel = 20;
        this.maxXp = 100;
        this.player = player;
        this.attachCapabilitiesEvent = attachCapabilitiesEvent;
        attachCapabilitiesEvent.addCapability(EXT_PROP_NAME, this);
    }

    public static final void register(Player player, AttachCapabilitiesEvent attachCapabilitiesEvent) {
        new ExtendedFarmerJobsEntityPlayer(player, attachCapabilitiesEvent);
    }

    public static final ExtendedFarmerJobsEntityPlayer get() {
        return attachCapabilitiesEvent == null ? null : (ExtendedFarmerJobsEntityPlayer) attachCapabilitiesEvent.getCapabilities().get(EXT_PROP_NAME);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return LazyOptional.empty();
    }

    @Override
    public Tag serializeNBT() {
        CompoundTag properties = new CompoundTag();

        properties.put("xp", LongTag.valueOf(this.xp));
        properties.put("level", IntTag.valueOf(this.level));
        properties.put("maxXp", LongTag.valueOf(this.maxLevel));
        EventHandler.storeEntityData(player.getDisplayName().getString(), properties);
        return properties;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        CompoundTag properties = (CompoundTag) nbt;
        this.xp = properties.getLong("xp");
        this.level = properties.getInt("level");
        this.maxXp = properties.getLong("maxXp");
    }

    public void addXp(long amount) {
        this.xp += amount;
        if (xp > maxXp) {
            level++;
            xp -= maxXp;
            if (level > maxLevel) {
                level = maxLevel;
            }else {
                maxXp = xpByLvl.get(level);
            }
        }
        this.sync();
    }

    public void sync() {
        PacketFarmerJobs packetJobs = new PacketFarmerJobs(this.xp, this.level, this.maxXp);
        Networking.sendToServer(packetJobs);

        if (!player.level.isClientSide) {
            Networking.sendToClient(packetJobs, (ServerPlayer) player);
        }
    }

    public long getXp() {
        return this.xp;
    }

}