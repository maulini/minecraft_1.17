package net.dofmine.minedofmod.items.armor;

import com.google.common.collect.ImmutableMap;
import net.dofmine.minedofmod.items.ModArmorMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SpecialBootsArmor extends ArmorItem {

    public SpecialBootsArmor(ArmorMaterial material, EquipmentSlot slot, Properties settings) {
        super(material, slot, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if(!world.isClientSide()) {
            if(entity instanceof Player player) {
                if (hasBoots(player)) {
                    ArmorMaterial material = ((ArmorItem) player.getInventory().getArmor(0).getItem()).getMaterial();
                    if (material.equals(ModArmorMaterial.ICE)) {
                        changeWaterBlock(world, player);
                    }else if (material.equals(ModArmorMaterial.LAVA)) {
                        changeLavaBlock(world, player);
                    }
                }
            }
        }

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    private void changeLavaBlock(Level world, Player player) {
        for (int x = -2; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -2; z < 2; z++) {
                    BlockPos blockPos = new BlockPos(player.getBlockX() + x, player.getBlockY() + y, player.getBlockZ() + z);
                    if (world.getBlockState(blockPos).getBlock().equals(Blocks.LAVA) || world.getBlockState(blockPos).getBlock().equals(Blocks.LAVA_CAULDRON)) {
                        if (player.getRandom().nextBoolean()) {
                            world.setBlockAndUpdate(blockPos, Blocks.OBSIDIAN.defaultBlockState());
                        } else {
                            world.setBlockAndUpdate(blockPos, Blocks.NETHER_BRICKS.defaultBlockState());
                        }
                    }
                }
            }
        }
    }

    private void changeWaterBlock(Level world, Player player) {
        for (int x = -2; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -2; z < 2; z++) {
                    BlockPos blockPos = new BlockPos(player.getBlockX() + x, player.getBlockY() + y, player.getBlockZ() + z);
                    if (world.getBlockState(blockPos).getBlock().equals(Blocks.WATER) || world.getBlockState(blockPos).getBlock().equals(Blocks.WATER_CAULDRON)) {
                        boolean randomBol = player.getRandom().nextBoolean();
                        if (randomBol) {
                            world.setBlockAndUpdate(blockPos, Blocks.ICE.defaultBlockState());
                        } else {
                            world.setBlockAndUpdate(blockPos, Blocks.PACKED_ICE.defaultBlockState());
                        }
                    }
                }
            }
        }
    }

    private boolean hasBoots(Player player) {
        return !player.getInventory().getArmor(0).isEmpty();
    }

}