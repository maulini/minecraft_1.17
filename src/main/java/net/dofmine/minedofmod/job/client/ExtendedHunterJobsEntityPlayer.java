package net.dofmine.minedofmod.job.client;

import com.google.common.collect.ImmutableMap;
import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.job.server.ExtendedHunterJobsEntityPlayerServer;
import net.dofmine.minedofmod.network.Networking;
import net.dofmine.minedofmod.network.PacketHunterJobs;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.LongTag;
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
import java.util.Map;

public class ExtendedHunterJobsEntityPlayer implements ICapabilitySerializable {

    public final static ResourceLocation EXT_PROP_NAME = new ResourceLocation(MinedofMod.MODS_ID, "extpropplayerhunterjobs_client");
    private final Map<Integer, Long> xpByLvl = new ImmutableMap.Builder<Integer, Long>() //
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
    public long xp;
    public long maxXp;
    public int level;
    public int maxLevel;
    private static LazyOptional<ExtendedHunterJobsEntityPlayer> lazyOptional;
    public static Capability<ExtendedHunterJobsEntityPlayer> HUNTER_JOBS = CapabilityManager.get(new CapabilityToken<>() {
        @Override
        public String toString() {
            return EXT_PROP_NAME.toString();
        }
    });

    public ExtendedHunterJobsEntityPlayer(Player player, AttachCapabilitiesEvent<Entity> attachCapabilitiesEvent) {
        this.xp = 0;
        this.level = 1;
        this.maxLevel = 20;
        this.maxXp = 100;
        this.player = player;
        attachCapabilitiesEvent.addCapability(EXT_PROP_NAME, this);
        attachCapabilitiesEvent.addListener(() -> lazyOptional.invalidate());
    }

    public static final void register(Player player, AttachCapabilitiesEvent<Entity> attachCapabilitiesEvent) {
        ExtendedHunterJobsEntityPlayer hunter = new ExtendedHunterJobsEntityPlayer(player, attachCapabilitiesEvent);
        lazyOptional = LazyOptional.of(() -> hunter);
    }

    public static final ExtendedHunterJobsEntityPlayer get(Player player) {
        return player.getCapability(HUNTER_JOBS).orElseGet(null);
    }

    public void setLevel(int level) {
        this.level = level;
        sync();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == HUNTER_JOBS) {
            return lazyOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public Tag serializeNBT() {
        CompoundTag properties = new CompoundTag();

        properties.put("xp", LongTag.valueOf(this.xp));
        properties.put("level", IntTag.valueOf(this.level));
        properties.put("maxXp", LongTag.valueOf(this.maxXp));
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
        PacketHunterJobs packetJobs = new PacketHunterJobs(this.xp, this.level, this.maxXp);
        Networking.sendToServer(packetJobs);
    }

    public long getXp() {
        return this.xp;
    }
}