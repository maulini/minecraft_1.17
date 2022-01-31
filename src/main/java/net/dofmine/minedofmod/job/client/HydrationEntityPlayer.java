package net.dofmine.minedofmod.job.client;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.job.server.HydrationEntityPlayerServer;
import net.dofmine.minedofmod.network.Networking;
import net.dofmine.minedofmod.network.PacketHydration;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HydrationEntityPlayer implements ICapabilitySerializable {

    public final static ResourceLocation EXT_PROP_NAME = new ResourceLocation(MinedofMod.MODS_ID, "hydration_player_client");

    private final Player player;

    public int actualHydration;
    public int maxHydration;
    public float exhaustionLevel;
    public int tickTimer;
    private static LazyOptional<HydrationEntityPlayer> lazyOptional;
    public static Capability<HydrationEntityPlayer> HYDRATION = CapabilityManager.get(new CapabilityToken<>() {
        @Override
        public String toString() {
            return EXT_PROP_NAME.toString();
        }
    });

    public HydrationEntityPlayer(Player player, AttachCapabilitiesEvent<Entity> attachCapabilitiesEvent) {
        this.player = player;
        this.actualHydration = 20;
        this.maxHydration = 20;
        attachCapabilitiesEvent.addCapability(EXT_PROP_NAME, this);
        attachCapabilitiesEvent.addListener(() -> lazyOptional.invalidate());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == HYDRATION) {
            return lazyOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public Tag serializeNBT() {
        CompoundTag properties = new CompoundTag();

        properties.put("Hydration", IntTag.valueOf(this.actualHydration));
        properties.put("MaxHydration", IntTag.valueOf(this.maxHydration));
        properties.put("ExhaustionLevel", FloatTag.valueOf(this.maxHydration));
        properties.put("TickTimer", IntTag.valueOf(this.tickTimer));
        return properties;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        CompoundTag properties = (CompoundTag) nbt;
        this.actualHydration = properties.getInt("Hydration");
        this.maxHydration = properties.getInt("MaxHydration");
        this.exhaustionLevel = properties.getFloat("ExhaustionLevel");
        this.tickTimer = properties.getInt("TickTimer");
    }

    public void addHydration(int p_38708_) {
        this.actualHydration = Math.min(p_38708_ + this.actualHydration, 20);
        tickTimer = 0;
        sync();
    }

    public static final void register(Player player, AttachCapabilitiesEvent attachCapabilitiesEvent) {
        HydrationEntityPlayer hydrationEntityPlayer = new HydrationEntityPlayer(player, attachCapabilitiesEvent);
        lazyOptional = LazyOptional.of(() -> hydrationEntityPlayer);
    }

    public static final HydrationEntityPlayer get(Player player) {
        return player.getCapability(HYDRATION).orElse(null);
    }

    public void sync() {
        PacketHydration packetHydration = new PacketHydration(this.maxHydration, this.actualHydration, this.exhaustionLevel, tickTimer);
        Networking.sendToServer(packetHydration);
    }

    public void tick(Player player) {
        Difficulty difficulty = player.level.getDifficulty();
        if (this.exhaustionLevel >= 4.0F) {
            this.exhaustionLevel -= 4.0F;
            if (difficulty != Difficulty.PEACEFUL) {
                this.actualHydration = Math.max(this.actualHydration - 1, 0);
            }
        }
        boolean flag = player.level.getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
        if (flag && actualHydration >= 16 && player.isHurt()) {
            ++this.tickTimer;
            if (this.tickTimer >= 80) {
                this.addExhaustion(6.0F);
                this.tickTimer = 0;
            }
        }else if (flag && actualHydration > 0) {
            ++this.tickTimer;
            if (this.tickTimer >= 800) {
                this.addExhaustion(2.0F);
                this.tickTimer = 0;
            }
        } else if (this.actualHydration <= 0) {
            ++this.tickTimer;
            if (this.tickTimer >= 80) {
                if (player.getHealth() > 10.0F || difficulty == Difficulty.HARD || player.getHealth() > 1.0F && difficulty == Difficulty.NORMAL) {
                    player.hurt(DamageSource.STARVE, 1.0F);
                }

                this.tickTimer = 0;
            }
        } else {
            this.tickTimer = 0;
        }
        sync();
    }

    public void addExhaustion(float p_38704_) {
        this.exhaustionLevel = Math.min(this.exhaustionLevel + p_38704_, 40.0F);
        sync();
    }

    public boolean needHydration() {
        return actualHydration < maxHydration;
    }

    public long getActualHydration() {
        return this.actualHydration;
    }

}
