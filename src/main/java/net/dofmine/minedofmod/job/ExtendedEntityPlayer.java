package net.dofmine.minedofmod.job;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.network.Networking;
import net.dofmine.minedofmod.network.PacketMana;
import net.dofmine.minedofmod.setup.ClientSetup;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import org.spongepowered.asm.mixin.injection.At;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ExtendedEntityPlayer implements ICapabilitySerializable {

    public final static ResourceLocation EXT_PROP_NAME = new ResourceLocation(MinedofMod.MODS_ID, "extpropplayer");

    private final Player player;
    private static AttachCapabilitiesEvent<Player> attachCapabilitiesEvent;

    public int mana;
    public int maxMana;

    public ExtendedEntityPlayer(Player player, AttachCapabilitiesEvent attachCapabilitiesEvent) {
        this.player = player;
        this.mana = 100;
        this.maxMana = 100;
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

        properties.put("Mana", IntTag.valueOf(this.mana));
        properties.put("MaxMana", IntTag.valueOf(this.maxMana));
        ClientSetup.storeEntityData(player.getDisplayName().getString(), properties);
        return properties;
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        CompoundTag properties = (CompoundTag) nbt;
        this.mana = properties.getInt("Mana");
        this.maxMana = properties.getInt("MaxMana");
    }

    public static final void register(Player player, AttachCapabilitiesEvent attachCapabilitiesEvent) {
        new ExtendedEntityPlayer(player, attachCapabilitiesEvent);
    }

    public static final ExtendedEntityPlayer get() {
        return (ExtendedEntityPlayer) attachCapabilitiesEvent.getCapabilities().get(EXT_PROP_NAME);
    }

    private static String getSaveKey(Player player) {
        return player.getDisplayName() + ":" + EXT_PROP_NAME;
    }

    public void sync() {
        PacketMana packetMoney = new PacketMana(this.maxMana, this.mana);
        Networking.sendToServer(packetMoney);

        if (player.level.isClientSide) {
            Networking.sendToClient(packetMoney, (ServerPlayer) player);
        }
    }

    public boolean spend(int amount) {
        boolean sufficient = amount <= this.mana;

        if (sufficient) {
            this.mana -= amount;
            ExtendedWizardJobsEntityPlayer.get().addXp(10);
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
