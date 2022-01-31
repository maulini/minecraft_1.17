package net.dofmine.minedofmod.job.server;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.job.client.ExtendedEntityPlayer;
import net.dofmine.minedofmod.network.Networking;
import net.dofmine.minedofmod.network.PacketMana;
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

public class ExtendedEntityPlayerServer implements ICapabilitySerializable {

    public final static ResourceLocation EXT_PROP_NAME = new ResourceLocation(MinedofMod.MODS_ID, "extpropplayer_server");

    private final Player player;

    public int mana;
    public int maxMana;
    private static LazyOptional<ExtendedEntityPlayerServer> lazyOptional;
    public static Capability<ExtendedEntityPlayerServer> WIZARD_JOBS = CapabilityManager.get(new CapabilityToken<>() {
        @Override
        public String toString() {
            return EXT_PROP_NAME.toString();
        }
    });

    public ExtendedEntityPlayerServer(Player player, AttachCapabilitiesEvent<Entity> attachCapabilitiesEvent) {
        this.player = player;
        this.mana = 100;
        this.maxMana = 100;
        attachCapabilitiesEvent.addCapability(EXT_PROP_NAME, this);
        attachCapabilitiesEvent.addListener(() -> lazyOptional.invalidate());
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == WIZARD_JOBS) {
            return lazyOptional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public Tag serializeNBT() {
        CompoundTag properties = new CompoundTag();

        properties.put("Mana", IntTag.valueOf(this.mana));
        properties.put("MaxMana", IntTag.valueOf(this.maxMana));
        return properties;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        CompoundTag properties = (CompoundTag) nbt;
        this.mana = properties.getInt("Mana");
        this.maxMana = properties.getInt("MaxMana");
    }

    public static final void register(Player player, AttachCapabilitiesEvent<Entity> attachCapabilitiesEvent) {
        ExtendedEntityPlayerServer wizard = new ExtendedEntityPlayerServer(player, attachCapabilitiesEvent);
        lazyOptional = LazyOptional.of(() -> wizard);
    }

    @Nullable
    public static final ExtendedEntityPlayerServer get(Player player) {
        return player.getCapability(WIZARD_JOBS).orElse(null);
    }

    public void sync() {
        PacketMana packetMoney = new PacketMana(this.maxMana, this.mana);
        Networking.sendToClient(packetMoney, (ServerPlayer) player);
    }

    public boolean spend(int amount) {
        boolean sufficient = amount <= this.mana;

        if (sufficient) {
            this.mana -= amount;
            ExtendedWizardJobsEntityPlayerServer.get(player).addXp(10);
            this.sync();
        } else {
            return false;
        }

        return sufficient;
    }

    public void addMana(int amount) {
        this.mana += amount;
        if (mana > maxMana) {
            mana = maxMana;
        }
        this.sync();
    }

    public long getMana() {
        return this.mana;
    }

    public void setMana(int newMoney) {
        this.mana = newMoney;
        this.sync();
    }
}
