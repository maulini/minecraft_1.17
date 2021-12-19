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

public class ModArmorItem extends ArmorItem {
    private static final Map<ArmorMaterial, List<MobEffect>> MATERIAL_TO_EFFECT_MAP =
            (new ImmutableMap.Builder<ArmorMaterial, List<MobEffect>>())
                    .put(ModArmorMaterial.TITANIUM, Arrays.asList(MobEffects.LUCK))
                    .put(ModArmorMaterial.GOD, Arrays.asList(MobEffects.ABSORPTION, MobEffects.FIRE_RESISTANCE, MobEffects.DAMAGE_RESISTANCE, MobEffects.DAMAGE_BOOST, MobEffects.JUMP))
                    .build();

    public ModArmorItem(ArmorMaterial material, EquipmentSlot slot, Properties settings) {
        super(material, slot, settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        if(!world.isClientSide()) {
            if(entity instanceof Player player) {
                if(hasFullSuitOfArmorOn(player)) {
                    evaluateArmorEffects(player);
                }
                if (hasBoots(player)) {
                    if (((ArmorItem)player.getInventory().getArmor(0).getItem()).getMaterial().equals(ModArmorMaterial.GOD)) {
                        changeWaterBlock(world, player);
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

    private void evaluateArmorEffects(Player player) {
        for (Map.Entry<ArmorMaterial, List<MobEffect>> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            ArmorMaterial mapArmorMaterial = entry.getKey();
            List<MobEffect> mapStatusEffect = entry.getValue();

            if(hasCorrectArmorOn(mapArmorMaterial, player)) {
                addStatusEffectForMaterial(player, mapArmorMaterial, mapStatusEffect);
            }
        }
    }

    private boolean hasBoots(Player player) {
        return !player.getInventory().getArmor(0).isEmpty();
    }

    private void addStatusEffectForMaterial(Player player, ArmorMaterial mapArmorMaterial,
                                            List<MobEffect> mapStatusEffect) {
        for (MobEffect mobEffect : mapStatusEffect) {
            boolean hasPlayerEffect = player.hasEffect(mobEffect);

            if(hasCorrectArmorOn(mapArmorMaterial, player) && !hasPlayerEffect) {
                player.addEffect(new MobEffectInstance(mobEffect, 1, 5));
                if (mapArmorMaterial.equals(ModArmorMaterial.GOD)) {
                    player.getFoodData().setFoodLevel(20);
                }
            }
        }
    }

    private boolean hasFullSuitOfArmorOn(Player player) {
        ItemStack boots = player.getInventory().getArmor(0);
        ItemStack leggings = player.getInventory().getArmor(1);
        ItemStack breastplate = player.getInventory().getArmor(2);
        ItemStack helmet = player.getInventory().getArmor(3);

        return !helmet.isEmpty() && !breastplate.isEmpty()
                && !leggings.isEmpty() && !boots.isEmpty();
    }

    private boolean hasCorrectArmorOn(ArmorMaterial material, Player player) {
        ArmorItem boots = ((ArmorItem)player.getInventory().getArmor(0).getItem());
        ArmorItem leggings = ((ArmorItem)player.getInventory().getArmor(1).getItem());
        ArmorItem breastplate = ((ArmorItem)player.getInventory().getArmor(2).getItem());
        ArmorItem helmet = ((ArmorItem)player.getInventory().getArmor(3).getItem());

        return helmet.getMaterial() == material && breastplate.getMaterial() == material &&
                leggings.getMaterial() == material && boots.getMaterial() == material;
    }
}