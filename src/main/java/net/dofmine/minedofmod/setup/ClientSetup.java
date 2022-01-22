package net.dofmine.minedofmod.setup;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.block.ModBlocks;
import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.job.*;
import net.dofmine.minedofmod.tileentity.MjollnirModel;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = MinedofMod.MODS_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    public static final Map<Block, Function<Integer, Long>> xpByBlockMiner = new HashMap<>();
    public static final Map<Block, Function<Integer, Long>> xpByBlockFarmer = new HashMap<>();
    public static final Map<Item, Function<Integer, Long>> xpByItemFarmer = new HashMap<>();
    public static final Map<EntityType<? extends Entity>, Function<Integer, Long>> xpByEntityHunter = new HashMap<>();
    public static final Map<EntityType<? extends Entity>, Integer> canAttackEntity = new HashMap<>();
    public static final Map<ResourceLocation, Map<Class<?>, Integer>> canUseItem = new HashMap<>();
    public static KeyMapping jobsKey;
    public static KeyMapping chooseSpell;
    public static KeyMapping spell1;
    public static KeyMapping spell2;
    public static KeyMapping spell3;
    public static ServerLevel serverLevel;

    public static void addBlocks(Block block, Function<Integer, Long> function) {
        xpByBlockMiner.put(block, function);
    }

    public static void addUseItem(ResourceLocation item, Class<?> clazz, int levelToUse) {
        HashMap<Class<?>, Integer> hashMap = new HashMap<>();
        hashMap.put(clazz, levelToUse);
        canUseItem.put(item, hashMap);
    }

    public static void init(final FMLClientSetupEvent event) {
        jobsKey = new KeyMapping(MinedofMod.MODS_ID + ".jobs", 74, "key.categories.inventory");
        ClientRegistry.registerKeyBinding(jobsKey);
        chooseSpell = new KeyMapping(MinedofMod.MODS_ID + ".chooseSpell", 65, "key.categories.wizard");
        ClientRegistry.registerKeyBinding(chooseSpell);
        spell1 = new KeyMapping(MinedofMod.MODS_ID + ".spell1", 87, "key.categories.wizard");
        ClientRegistry.registerKeyBinding(spell1);
        spell2 = new KeyMapping(MinedofMod.MODS_ID + ".spell2", 88, "key.categories.wizard");
        ClientRegistry.registerKeyBinding(spell2);
        spell3 = new KeyMapping(MinedofMod.MODS_ID + ".spell3", 67, "key.categories.wizard");
        ClientRegistry.registerKeyBinding(spell3);
        addBlocks(Blocks.COAL_ORE, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 15L;
            } else if (i >= 5 && i < 10) {
                value = 8L;
            }
            return value;
        });
        addBlocks(Blocks.DIAMOND_ORE, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 150L;
            } else if (i >= 5 && i < 10) {
                value = 80L;
            } else if (i >= 10 && i < 15) {
                value = 35L;
            }
            return value;
        });
        addBlocks(Blocks.EMERALD_ORE, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 150L;
            } else if (i >= 5 && i < 10) {
                value = 80L;
            } else if (i >= 10 && i < 15) {
                value = 35L;
            }
            return value;
        });
        addBlocks(Blocks.GOLD_ORE, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 50L;
            } else if (i >= 5 && i < 10) {
                value = 25L;
            } else if (i >= 10 && i < 15) {
                value = 5L;
            }
            return value;
        });
        addBlocks(Blocks.IRON_ORE, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 50L;
            } else if (i >= 5 && i < 10) {
                value = 25L;
            } else if (i >= 10 && i < 15) {
                value = 5L;
            }
            return value;
        });
        addBlocks(Blocks.LAPIS_ORE, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 100L;
            } else if (i >= 5 && i < 10) {
                value = 50L;
            } else if (i >= 10 && i < 15) {
                value = 20L;
            }
            return value;
        });
        addBlocks(Blocks.REDSTONE_ORE, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 75L;
            } else if (i >= 5 && i < 10) {
                value = 40L;
            } else if (i >= 10 && i < 15) {
                value = 20L;
            }
            return value;
        });
        addBlocks(Blocks.NETHER_QUARTZ_ORE, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 100L;
            } else if (i >= 5 && i < 10) {
                value = 50L;
            } else if (i >= 10 && i < 15) {
                value = 20L;
            }
            return value;
        });
        addBlocks(Blocks.STONE, i -> 2L);
        xpByItemFarmer.put(Items.BAKED_POTATO, i -> {
            long value = 0;
            if (i >= 1 && i < 5) {
                value = 50L;
            } else if (i >= 5 && i < 10) {
                value = 25L;
            } else if (i >= 10) {
                value = 3L;
            }
            return value;
        });
        xpByItemFarmer.put(Items.BREAD, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 10L;
            } else if (i >= 5 && i < 10) {
                value = 2L;
            }
            return value;
        });
        xpByItemFarmer.put(Items.CAKE, i -> {
            long value = 3L;
            if (i >= 1 && i < 5) {
                value = 50L;
            } else if (i >= 5 && i < 10) {
                value = 25L;
            }
            return value;
        });
        xpByItemFarmer.put(Items.GOLDEN_CARROT, i -> {
            long value = 0L;
            if (i >= 5 && i < 10) {
                value = 20L;
            } else if (i >= 10 && i < 15) {
                value = 10L;
            } else if (i >= 15 && i < 20) {
                value = 5L;
            }
            return value;
        });
        xpByBlockFarmer.put(Blocks.WHEAT, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 10L;
            } else if (i >= 5 && i < 10) {
                value = 2L;
            }
            return value;
        });
        xpByBlockFarmer.put(Blocks.CARROTS, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 15L;
            } else if (i >= 5 && i < 10) {
                value = 5L;
            } else if (i >= 10 && i < 15) {
                value = 2L;
            }
            return value;
        });
        xpByBlockFarmer.put(Blocks.POTATOES, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 15L;
            } else if (i >= 5 && i < 10) {
                value = 5L;
            } else if (i >= 10 && i < 15) {
                value = 2L;
            }
            return value;
        });
        xpByBlockFarmer.put(Blocks.MELON, i -> {
            long value = 0L;
            if (i >= 5 && i < 10) {
                value = 150L;
            } else if (i >= 10 && i < 15) {
                value = 75L;
            } else if (i >= 15) {
                value = 20L;
            }
            return value;
        });
        xpByEntityHunter.put(EntityType.ZOMBIE, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 10L;
            } else if (i >= 5 && i < 10) {
                value = 5L;
            }
            return value;
        });
        xpByEntityHunter.put(EntityType.SKELETON, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 10L;
            } else if (i >= 5 && i < 10) {
                value = 5L;
            }
            return value;
        });
        xpByEntityHunter.put(EntityType.SPIDER, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 10L;
            } else if (i >= 5 && i < 10) {
                value = 5L;
            }
            return value;
        });
        xpByEntityHunter.put(EntityType.CAVE_SPIDER, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 10L;
            } else if (i >= 5 && i < 10) {
                value = 5L;
            }
            return value;
        });
        xpByEntityHunter.put(EntityType.CREEPER, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 10L;
            } else if (i >= 5 && i < 10) {
                value = 5L;
            }
            return value;
        });
        xpByEntityHunter.put(EntityType.WITCH, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 25L;
            } else if (i >= 5 && i < 10) {
                value = 10L;
            }
            return value;
        });
        xpByEntityHunter.put(EntityType.BLAZE, i -> {
            long value = 0L;
            if (i >= 5 && i < 10) {
                value = 150L;
            } else if (i >= 10 && i < 15) {
                value = 75L;
            } else if (i >= 15) {
                value = 20L;
            }
            return value;
        });
        xpByEntityHunter.put(EntityType.SLIME, i -> {
            long value = 0L;
            if (i >= 5 && i < 10) {
                value = 150L;
            } else if (i >= 10 && i < 15) {
                value = 75L;
            } else if (i >= 15) {
                value = 20L;
            }
            return value;
        });
        xpByEntityHunter.put(EntityType.WOLF, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 25L;
            } else if (i >= 5 && i < 10) {
                value = 10L;
            }
            return value;
        });
        xpByEntityHunter.put(EntityType.CHICKEN, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 25L;
            } else if (i >= 5 && i < 10) {
                value = 10L;
            }
            return value;
        });
        xpByEntityHunter.put(EntityType.SHEEP, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 25L;
            } else if (i >= 5 && i < 10) {
                value = 10L;
            }
            return value;
        });
        canAttackEntity.put(EntityType.BLAZE, 5);
        canAttackEntity.put(EntityType.SLIME, 5);
        xpByBlockMiner.putAll(ModBlocks.registerFunction);
        addUseItem(ModItems.TITANIUM_SWORD.get().getRegistryName(), ExtendedHunterJobsEntityPlayer.class, 2);
        addUseItem(ModItems.TITANIUM_HOE.get().getRegistryName(), ExtendedFarmerJobsEntityPlayer.class, 2);
        addUseItem(ModItems.TITANIUM_PICKAXE.get().getRegistryName(), ExtendedMinerJobsEntityPlayer.class, 2);
        addUseItem(ModItems.TITANIUM_SHOVEL.get().getRegistryName(), ExtendedFarmerJobsEntityPlayer.class, 2);
        addUseItem(ModItems.DARK_HOE.get().getRegistryName(), ExtendedFarmerJobsEntityPlayer.class, 15);
        addUseItem(ModItems.DARK_PICKAXE.get().getRegistryName(), ExtendedMinerJobsEntityPlayer.class, 15);
        addUseItem(ModItems.DARK_SHOVEL.get().getRegistryName(), ExtendedFarmerJobsEntityPlayer.class, 15);
        addUseItem(ModItems.DARK_SWORD.get().getRegistryName(), ExtendedHunterJobsEntityPlayer.class, 15);
        addUseItem(ModItems.LAPIS_SWORD.get().getRegistryName(), ExtendedHunterJobsEntityPlayer.class, 5);
        addUseItem(ModItems.LAPIS_HOE.get().getRegistryName(), ExtendedFarmerJobsEntityPlayer.class, 5);
        addUseItem(ModItems.LAPIS_PICKAXE.get().getRegistryName(), ExtendedMinerJobsEntityPlayer.class, 5);
        addUseItem(ModItems.LAPIS_SHOVEL.get().getRegistryName(), ExtendedFarmerJobsEntityPlayer.class, 5);
        addUseItem(ModItems.RUBIS_SWORD.get().getRegistryName(), ExtendedHunterJobsEntityPlayer.class, 10);
        addUseItem(ModItems.RUBIS_HOE.get().getRegistryName(), ExtendedFarmerJobsEntityPlayer.class, 10);
        addUseItem(ModItems.RUBIS_PICKAXE.get().getRegistryName(), ExtendedMinerJobsEntityPlayer.class, 10);
        addUseItem(ModItems.RUBIS_SHOVEL.get().getRegistryName(), ExtendedFarmerJobsEntityPlayer.class, 10);
        addUseItem(ModItems.GOD_HOE.get().getRegistryName(), ExtendedFarmerJobsEntityPlayer.class, 20);
        addUseItem(ModItems.GOD_SWORD.get().getRegistryName(), ExtendedHunterJobsEntityPlayer.class, 20);
        addUseItem(ModItems.GOD_SHOVEL.get().getRegistryName(), ExtendedFarmerJobsEntityPlayer.class, 20);
        addUseItem(ModItems.GOD_PICKAXE.get().getRegistryName(), ExtendedMinerJobsEntityPlayer.class, 20);
        addUseItem(ModItems.THOR_HAMMER.get().getRegistryName(), ExtendedHunterJobsEntityPlayer.class, 20);

        addUseItem(ModItems.TELEPORTATE_KEY.get().getRegistryName(), ExtendedLocksmithJobsEntityPlayer.class, 7);
        addUseItem(ModItems.RED_KEY.get().getRegistryName(), ExtendedLocksmithJobsEntityPlayer.class, 6);
        addUseItem(ModItems.BASIC_KEY.get().getRegistryName(), ExtendedLocksmithJobsEntityPlayer.class, 2);
        addUseItem(ModItems.DARK_KEY.get().getRegistryName(), ExtendedLocksmithJobsEntityPlayer.class, 3);
        addUseItem(ModItems.GOLDEN_KEY.get().getRegistryName(), ExtendedLocksmithJobsEntityPlayer.class, 4);
        addUseItem(ModItems.STONE_KEY.get().getRegistryName(), ExtendedLocksmithJobsEntityPlayer.class, 1);
        addUseItem(ModItems.WATER_KEY.get().getRegistryName(), ExtendedLocksmithJobsEntityPlayer.class, 5);
        event.enqueueWork(() -> {
        });
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(MjollnirModel.LAYER_LOCATION, MjollnirModel::createBodyLayer);
    }

}
