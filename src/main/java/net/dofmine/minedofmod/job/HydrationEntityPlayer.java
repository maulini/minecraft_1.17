package net.dofmine.minedofmod.job;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.network.Networking;
import net.dofmine.minedofmod.network.PacketHydration;
import net.dofmine.minedofmod.setup.EventHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HydrationEntityPlayer implements ICapabilitySerializable {

    public final static ResourceLocation EXT_PROP_NAME = new ResourceLocation(MinedofMod.MODS_ID, "hydration_player");

    private final Player player;
    private static AttachCapabilitiesEvent<Player> attachCapabilitiesEvent;

    public int actualHydration;
    public int maxHydration;
    private int lastHydrationLevel = 20;
    private float exhaustionLevel;
    private int tickTimer;

    public HydrationEntityPlayer(Player player, AttachCapabilitiesEvent attachCapabilitiesEvent) {
        this.player = player;
        this.actualHydration = 20;
        this.maxHydration = 20;
        this.attachCapabilitiesEvent = attachCapabilitiesEvent;
        attachCapabilitiesEvent.addCapability(EXT_PROP_NAME, this);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return LazyOptional.empty();
    }

    @Override
    public Tag serializeNBT() {
        CompoundTag properties = new CompoundTag();

        properties.put("Hydration", IntTag.valueOf(this.actualHydration));
        properties.put("MaxHydration", IntTag.valueOf(this.maxHydration));
        EventHandler.storeEntityData(player.getDisplayName().getString(), properties);
        return properties;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        CompoundTag properties = (CompoundTag) nbt;
        this.actualHydration = properties.getInt("Hydration");
        this.maxHydration = properties.getInt("MaxHydration");
    }

    public void drink(int p_38708_) {
        this.actualHydration = Math.min(p_38708_ + this.actualHydration, 20);
    }

    public static final void register(Player player, AttachCapabilitiesEvent attachCapabilitiesEvent) {
        new HydrationEntityPlayer(player, attachCapabilitiesEvent);
    }

    public static final HydrationEntityPlayer get() {
        return attachCapabilitiesEvent == null ? null : (HydrationEntityPlayer) attachCapabilitiesEvent.getCapabilities().get(EXT_PROP_NAME);
    }

    private static String getSaveKey(Player player) {
        return player.getDisplayName() + ":" + EXT_PROP_NAME;
    }

    public void sync() {
        PacketHydration packetHydration = new PacketHydration(this.maxHydration, this.actualHydration);
        Networking.sendToServer(packetHydration);

        if (!player.level.isClientSide) {
            Networking.sendToClient(packetHydration, (ServerPlayer) player);
        }
    }

    public void tick(Player player) {
        Difficulty difficulty = player.level.getDifficulty();
        this.lastHydrationLevel = this.actualHydration;
        if (this.exhaustionLevel > 2.0F) {
            this.exhaustionLevel -= 2.0F;
            if (difficulty != Difficulty.PEACEFUL) {
                this.actualHydration = Math.max(this.actualHydration - 1, 0);
            }
        }
        if (player.isHurt() && this.actualHydration >= 20) {
            ++this.tickTimer;
            if (this.tickTimer >= 10) {
                float f = Math.min(this.actualHydration, 6.0F);
                this.addExhaustion(f);
                this.tickTimer = 0;
            }
        } else if (this.actualHydration >= 18 && player.isHurt()) {
            ++this.tickTimer;
            if (this.tickTimer >= 80) {
                this.addExhaustion(6.0F);
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
            ++this.tickTimer;
            if (this.tickTimer >= 24_000) {
                this.addExhaustion(2.0F);
                this.tickTimer = 0;
            }
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

    public void addHydration(int hydration) {
        if (needHydration()) {
            this.actualHydration += hydration;
            if (actualHydration > maxHydration) {
                actualHydration = maxHydration;
            }
        }
        this.sync();
    }
}
