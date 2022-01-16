package net.dofmine.minedofmod.setup;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.effects.ModEffect;
import net.dofmine.minedofmod.items.ModArmorMaterial;
import net.dofmine.minedofmod.job.HydrationEntityPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MinedofMod.MODS_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    @SubscribeEvent
    public static void playerTickEvent(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        HydrationEntityPlayer.get().tick(player);
        if (!player.getInventory().getArmor(0).isEmpty()) {
            if (((ArmorItem) player.getInventory().getArmor(0).getItem()).getMaterial().equals(ModArmorMaterial.GOD)) {
                walkOnWater(player.level, player);
                walkOnLava(player.level, player);
            }else if (((ArmorItem) player.getInventory().getArmor(0).getItem()).getMaterial().equals(ModArmorMaterial.ICE)) {
                walkOnWater(player.level, player);
            }else if (((ArmorItem) player.getInventory().getArmor(0).getItem()).getMaterial().equals(ModArmorMaterial.DARK)) {
                walkOnLava(player.level, player);
            }
        }
    }

    private static void walkOnLava(Level level, Player player) {
        if (!player.isCrouching()) {
            if (level.getBlockState(player.blockPosition().atY(player.getBlockY() - 1)).is(Blocks.LAVA) || level.getBlockState(player.blockPosition().atY(player.getBlockY() - 1)).is(Blocks.LAVA_CAULDRON)) {
                player.setDeltaMovement(player.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
            }
        }
    }

    private static void walkOnWater(Level level, Player player) {
        if (!player.isCrouching()) {
            if (level.getBlockState(player.blockPosition().atY(player.getBlockY() - 1)).is(Blocks.WATER) || level.getBlockState(player.blockPosition().atY(player.getBlockY() - 1)).is(Blocks.WATER_CAULDRON)) {
                player.setDeltaMovement(player.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerFinishUseItem(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntity() instanceof Player player) {
            ItemStack item = event.getItem();
            if (item.is(Items.MILK_BUCKET)) {
                HydrationEntityPlayer.get().addHydration(2);
                player.addEffect(new MobEffectInstance(ModEffect.THIRST.get(), 600, 0));
            }
        }
    }
}
