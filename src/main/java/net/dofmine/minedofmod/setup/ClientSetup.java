package net.dofmine.minedofmod.setup;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.block.ModBlocks;
import net.dofmine.minedofmod.block.custom.ElevatorBlock;
import net.dofmine.minedofmod.container.BackPackContainer;
import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.items.backpack.VacuumBackPack;
import net.dofmine.minedofmod.job.*;
//import net.dofmine.minedofmod.screen.ChooseSpellScreen;
import net.dofmine.minedofmod.screen.JobsScreen;
import net.dofmine.minedofmod.screen.ManaBar;
import net.dofmine.minedofmod.tileentity.Spells;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fmlclient.registry.ClientRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = MinedofMod.MODS_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    private static final Map<String, CompoundTag> extendedEntityData = new HashMap<String, CompoundTag>();
    private static Map<Block, Function<Integer, Long>> xpByBlockMiner = new HashMap<>();
    private static Map<Block, Function<Integer, Long>> xpByBlockFarmer = new HashMap<>();
    private static Map<Item, Function<Integer, Long>> xpByItemFarmer = new HashMap<>();
    private static Map<EntityType, Function<Integer, Long>> xpByEntityHunter = new HashMap<>();
    private static Map<EntityType, Integer> canAttackEntity = new HashMap<>();
    private static Map<ResourceLocation, Map<Class<?>, Integer>> canUseItem = new HashMap<>();
    public static final RecipeSerializer<net.dofmine.minedofmod.data.recipes.vacuum.VacuumBackPack> VACUUM_CRAFTING = null;
    public static final RecipeSerializer<net.dofmine.minedofmod.data.recipes.vacuum.RevertVacuumBackPack> REVERT_VACUUM_CRAFTING = null;
    private static KeyMapping jobsKey;
    private static KeyMapping chooseSpell;
    public static KeyMapping spell1;
    public static KeyMapping spell2;
    public static KeyMapping spell3;

    private float addMana = 0f;
    private boolean canTeleportate = true;
    private ServerLevel serverLevel;

    public ClientSetup() {
        addBlocks(Blocks.COAL_ORE, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 15L;
            } else if (i >= 5 && i < 10) {
                value = 8L;
            } else if (i >= 10) {
                value = 0L;
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
            } else if (i >= 15) {
                value = 0L;
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
            } else if (i >= 15) {
                value = 0L;
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
            } else if (i >= 15) {
                value = 0L;
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
            } else if (i >= 15) {
                value = 0L;
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
            } else if (i >= 15) {
                value = 0L;
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
            } else if (i >= 15) {
                value = 0L;
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
            } else if (i >= 15) {
                value = 0L;
            }
            return value;
        });
        addBlocks(Blocks.STONE, i -> {
            return 2L;
        });
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
            } else if (i >= 5) {
                value = 0L;
            }
            return value;
        });
        xpByEntityHunter.put(EntityType.SKELETON, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 10L;
            } else if (i >= 5 && i < 10) {
                value = 5L;
            } else if (i >= 5) {
                value = 0L;
            }
            return value;
        });
        xpByEntityHunter.put(EntityType.SPIDER, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 10L;
            } else if (i >= 5 && i < 10) {
                value = 5L;
            } else if (i >= 5) {
                value = 0L;
            }
            return value;
        });
        xpByEntityHunter.put(EntityType.CAVE_SPIDER, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 10L;
            } else if (i >= 5 && i < 10) {
                value = 5L;
            } else if (i >= 5) {
                value = 0L;
            }
            return value;
        });
        xpByEntityHunter.put(EntityType.CREEPER, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 10L;
            } else if (i >= 5 && i < 10) {
                value = 5L;
            } else if (i >= 5) {
                value = 0L;
            }
            return value;
        });
        xpByEntityHunter.put(EntityType.WITCH, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 25L;
            } else if (i >= 5 && i < 10) {
                value = 10L;
            } else if (i >= 10) {
                value = 0L;
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
            } else if (i >= 10) {
                value = 0L;
            }
            return value;
        });
        xpByEntityHunter.put(EntityType.CHICKEN, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 25L;
            } else if (i >= 5 && i < 10) {
                value = 10L;
            } else if (i >= 10) {
                value = 0L;
            }
            return value;
        });
        xpByEntityHunter.put(EntityType.SHEEP, i -> {
            long value = 0L;
            if (i >= 1 && i < 5) {
                value = 25L;
            } else if (i >= 5 && i < 10) {
                value = 10L;
            } else if (i >= 10) {
                value = 0L;
            }
            return value;
        });
        canAttackEntity.put(EntityType.BLAZE, 5);
        canAttackEntity.put(EntityType.SLIME, 5);
    }

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
        event.enqueueWork(() -> {
        });
    }

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }

    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
    }

    @SubscribeEvent
    public static void onItemColor(ColorHandlerEvent.Item event) {
    }

    @SubscribeEvent
    public static void onModelRegistryEvent(ModelRegistryEvent event) {
    }

    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {

    }

    @SubscribeEvent
    public void onTooltipPre(RenderTooltipEvent.Pre event) {
    }

    @SubscribeEvent
    public void entity(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject().getType().equals(EntityType.PLAYER) && event.getObject() instanceof ServerPlayer player) {
            ExtendedEntityPlayer.register(player, event);
            ExtendedMinerJobsEntityPlayer.register(player, event);
            ExtendedFarmerJobsEntityPlayer.register(player, event);
            ExtendedLocksmithJobsEntityPlayer.register(player, event);
            ExtendedWizardJobsEntityPlayer.register(player, event);
            ExtendedHunterJobsEntityPlayer.register(player, event);
            new Spells((ServerLevel) player.level);
            this.serverLevel = (ServerLevel) player.level;
        }
    }

    public static void storeEntityData(String name, CompoundTag compound) {
        extendedEntityData.put(name, compound);
    }

    public static CompoundTag getEntityData(String name) {
        return extendedEntityData.remove(name);
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity().level.isClientSide && event.getEntity() instanceof Player player) {
            CompoundTag playerData = getEntityData(player.getDisplayName().getString());
            if (playerData != null) {
                ExtendedEntityPlayer.get().deserializeNBT(playerData);
                ExtendedMinerJobsEntityPlayer.get().deserializeNBT(playerData);
                ExtendedFarmerJobsEntityPlayer.get().deserializeNBT(playerData);
                ExtendedLocksmithJobsEntityPlayer.get().deserializeNBT(playerData);
                ExtendedWizardJobsEntityPlayer.get().deserializeNBT(playerData);
                ExtendedHunterJobsEntityPlayer.get().deserializeNBT(playerData);
            }

            ExtendedEntityPlayer.get().sync();
            ExtendedMinerJobsEntityPlayer.get().sync();
            ExtendedFarmerJobsEntityPlayer.get().sync();
            ExtendedLocksmithJobsEntityPlayer.get().sync();
            ExtendedHunterJobsEntityPlayer.get().sync();
            ExtendedWizardJobsEntityPlayer.get().sync();
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
        }
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event) {
        if (!event.getEntity().level.isClientSide) {
            if (event.getEntity() instanceof Ravager) {
                ExtendedEntityPlayer.get().addMana(2);
            }
            if (xpByEntityHunter.containsKey(event.getEntity().getType())) {
                ExtendedHunterJobsEntityPlayer hunter = ExtendedHunterJobsEntityPlayer.get();
                hunter.addXp(xpByEntityHunter.get(event.getEntity().getType()).apply(hunter.level));
            }
        }
    }

    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Post event) {
        new ManaBar(Minecraft.getInstance(), event.getMatrixStack());
    }

    @SubscribeEvent
    public void onBlockDestroy(BlockEvent.BreakEvent event) {
        if (!event.getPlayer().isCreative() && !event.getPlayer().level.isClientSide) {
            if (xpByBlockMiner.containsKey(event.getState().getBlock())) {
                ExtendedMinerJobsEntityPlayer minerJobs = ExtendedMinerJobsEntityPlayer.get();
                Function<Integer, Long> func = xpByBlockMiner.get(event.getState().getBlock());
                minerJobs.addXp(func.apply(minerJobs.level));
            }else if (xpByBlockFarmer.containsKey(event.getState().getBlock())) {
                ExtendedFarmerJobsEntityPlayer farmerJobs = ExtendedFarmerJobsEntityPlayer.get();
                Function<Integer, Long> func = xpByBlockFarmer.get(event.getState().getBlock());
                farmerJobs.addXp(func.apply(farmerJobs.level));
            }
        }
    }

    @SubscribeEvent
    public void onStartBlockDestroy(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getEntity() instanceof Player player && !player.isCreative() && player.level.isClientSide) {
            if (canUseItem.containsKey(event.getItemStack().getItem().getRegistryName())) {
                Map<Class<?>, Integer> map = canUseItem.get(event.getItemStack().getItem().getRegistryName());
                if (map.containsKey(ExtendedFarmerJobsEntityPlayer.class)) {
                    ExtendedFarmerJobsEntityPlayer farmer = ExtendedFarmerJobsEntityPlayer.get();
                    Integer level = map.get(ExtendedFarmerJobsEntityPlayer.class);
                    if (farmer.level < level) {
                        event.getEntity().sendMessage(new TextComponent(String.format("Your farmer level must be level %d to use %s", level, event.getItemStack().getItem().getRegistryName())), UUID.randomUUID());
                        event.setCanceled(true);
                    }
                } else if (map.containsKey(ExtendedMinerJobsEntityPlayer.class)) {
                    ExtendedMinerJobsEntityPlayer miner = ExtendedMinerJobsEntityPlayer.get();
                    Integer level = map.get(ExtendedMinerJobsEntityPlayer.class);
                    if (miner.level < level) {
                        event.getEntity().sendMessage(new TextComponent(String.format("Your miner level must be level %d to use %s", level, event.getItemStack().getItem().getRegistryName())), UUID.randomUUID());
                        event.setCanceled(true);
                    }
                } else if (map.containsKey(ExtendedLocksmithJobsEntityPlayer.class)) {
                    ExtendedLocksmithJobsEntityPlayer locksmith = ExtendedLocksmithJobsEntityPlayer.get();
                    Integer level = map.get(ExtendedLocksmithJobsEntityPlayer.class);
                    if (locksmith.level < level) {
                        event.getEntity().sendMessage(new TextComponent(String.format("Your locksmith level must be level %d to use %s", level, event.getItemStack().getItem().getRegistryName())), UUID.randomUUID());
                        event.setCanceled(true);
                    }
                } else if (map.containsKey(ExtendedHunterJobsEntityPlayer.class)) {
                    ExtendedHunterJobsEntityPlayer hunter = ExtendedHunterJobsEntityPlayer.get();
                    Integer level = map.get(ExtendedHunterJobsEntityPlayer.class);
                    if (hunter.level < level) {
                        event.getEntity().sendMessage(new TextComponent(String.format("Your hunter level must be level %d to use %s", level, event.getItemStack().getItem().getRegistryName())), UUID.randomUUID());
                        event.setCanceled(true);
                    }
                }
            }
            Block block = event.getPlayer().level.getBlockState(event.getPos()).getBlock();
            ExtendedMinerJobsEntityPlayer minerJob = ExtendedMinerJobsEntityPlayer.get();
            if (block.equals(ModBlocks.RUBY_ORE.get()) && minerJob.level < 10) {
                event.setCanceled(true);
                event.getPlayer().sendMessage(new TextComponent(String.format("Impossible to break this block, miner level must be 10 but your are level %d", minerJob.level)), UUID.randomUUID());
            } else if (block.equals(ModBlocks.LAPIS_ORE.get()) && minerJob.level < 5) {
                event.setCanceled(true);
                event.getPlayer().sendMessage(new TextComponent(String.format("Impossible to break this block, miner level must be 5 but your are level %d", minerJob.level)), UUID.randomUUID());
            } else if (block.equals(ModBlocks.DARK_ORE.get()) && minerJob.level < 15) {
                event.setCanceled(true);
                event.getPlayer().sendMessage(new TextComponent(String.format("Impossible to break this block, miner level must be 15 but your are level %d", minerJob.level)), UUID.randomUUID());
            } else if (block.equals(ModBlocks.GOD_ORE.get()) && minerJob.level < 20) {
                event.setCanceled(true);
                event.getPlayer().sendMessage(new TextComponent(String.format("Impossible to break this block, miner level must be 20 but your are level %d", minerJob.level)), UUID.randomUUID());
            }
        }
    }

    @SubscribeEvent
    public void onEntityAttack(AttackEntityEvent event) {
        if (!event.getPlayer().level.isClientSide && !event.getPlayer().isCreative()) {
            if (canUseItem.containsKey(event.getPlayer().getMainHandItem().getItem().getRegistryName())) {
                Map<Class<?>, Integer> map = canUseItem.get(event.getPlayer().getMainHandItem().getItem().getRegistryName());
                if (map.containsKey(ExtendedHunterJobsEntityPlayer.class)) {
                    ExtendedHunterJobsEntityPlayer hunter = ExtendedHunterJobsEntityPlayer.get();
                    Integer level = map.get(ExtendedHunterJobsEntityPlayer.class);
                    if (hunter.level < level) {
                        event.getEntity().sendMessage(new TextComponent(String.format("Your hunter level must be level %d to use %s", level, event.getPlayer().getMainHandItem().getItem().getRegistryName())), UUID.randomUUID());
                        event.setCanceled(true);
                    }
                }
            }
            if (!event.getPlayer().isCreative() && canAttackEntity.containsKey(event.getTarget().getType())) {
                ExtendedHunterJobsEntityPlayer hunter = ExtendedHunterJobsEntityPlayer.get();
                if (hunter.level < canAttackEntity.get(event.getTarget().getType())) {
                    event.getPlayer().sendMessage(new TextComponent(String.format("Your hunter level must be %d to can attack %s", canAttackEntity.get(event.getTarget().getType()) ,event.getTarget().getType())), UUID.randomUUID());
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerCraft(PlayerEvent.ItemCraftedEvent event) {
        if (xpByItemFarmer.containsKey(event.getCrafting().getItem())) {
            ExtendedFarmerJobsEntityPlayer farmer = ExtendedFarmerJobsEntityPlayer.get();
            farmer.addXp(xpByItemFarmer.get(event.getCrafting().getItem()).apply(farmer.level));
        }
        for (int i = 0; i < event.getInventory().getContainerSize(); i++) {
            ItemStack itemStack = event.getInventory().getItem(i);
            if (itemStack.is(ModItems.VACUUM_BACK_PACK.get())) {
                VacuumBackPack.decreaseSize(itemStack, event.getCrafting().getCount());
                if (!event.getPlayer().getInventory().add(itemStack)) {
                    serverLevel.addFreshEntity(event.getPlayer().drop(itemStack, true, false));
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerSmelted(PlayerEvent.ItemSmeltedEvent event) {
        if (xpByItemFarmer.containsKey(event.getSmelting().getItem())) {
            ExtendedFarmerJobsEntityPlayer farmer = ExtendedFarmerJobsEntityPlayer.get();
            farmer.addXp(xpByItemFarmer.get(event.getSmelting().getItem()).apply(farmer.level));
        }
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.KeyInputEvent event) {
        if (jobsKey.isDown()) {
            Minecraft.getInstance().setScreen(new JobsScreen(new TextComponent("")));
        }
        if (chooseSpell.isDown()) {
            //Minecraft.getInstance().setScreen(new ChooseSpellScreen(new TextComponent("")));
        }
        if (spell1.isDown()) {
            Spells.thunderBolt();
        }
        if (spell2.isDown()) {
            Spells.levitation();
        }
        if (spell3.isDown()) {
            Spells.witch();
        }
        if (event.getKey() == Minecraft.getInstance().options.keyShift.getKey().getValue() && !Minecraft.getInstance().options.keyShift.isDown()) {
            canTeleportate = true;
        }
    }

    @SubscribeEvent
    public void onCloseGUI(PlayerContainerEvent.Close event) {
        if (event.getContainer() instanceof BackPackContainer backPackContainer) {
            backPackContainer.saveContent(event.getPlayer());
        }
    }

    @SubscribeEvent
    public void onPlayerSleep(PlayerSleepInBedEvent event) {
        if (event.getEntity().level.isClientSide) {
            ExtendedEntityPlayer.get().spend(-10);
        }
    }

    @SubscribeEvent
    public void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.level.getBlockState(serverPlayer.blockPosition().atY(serverPlayer.blockPosition().getY() - 1)).getBlock() instanceof ElevatorBlock elevatorBlock) {
                elevatorBlock.upTeleport(serverPlayer);
            }
        }
    }

    @SubscribeEvent
    public void onUpdateMovement(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            if (canTeleportate && serverPlayer.isShiftKeyDown() && serverPlayer.level.getBlockState(serverPlayer.blockPosition().atY(serverPlayer.blockPosition().getY() - 1)).getBlock() instanceof ElevatorBlock elevatorBlock) {
                event.setCanceled(true);
                canTeleportate = false;
                elevatorBlock.downTeleport(serverPlayer);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerPickupItem(EntityItemPickupEvent event) {
        Optional<ItemStack> vacuumItemStack = event.getPlayer().getInventory().items.stream().filter(itemStack -> {
            boolean hasGoodVacuum = false;
            if (itemStack.is(ModItems.VACUUM_BACK_PACK.get()) && ((VacuumBackPack)itemStack.getItem()).containItem(itemStack, event.getItem().getItem())) {
                hasGoodVacuum = true;
            }
            return hasGoodVacuum;
        }).findFirst();
        if (vacuumItemStack.isPresent()) {
                event.setCanceled(true);
                ((VacuumBackPack)vacuumItemStack.get().getItem()).addItemToStack(vacuumItemStack.get(), event.getItem().getItem(), serverLevel, event.getPlayer());
                event.getItem().move(MoverType.PLAYER, event.getPlayer().position());
                event.getItem().kill();
            }
        }

    @SubscribeEvent
    public static void registerRecipes(final RegistryEvent.Register<RecipeSerializer<?>> event) {
        event.getRegistry().register(new SimpleRecipeSerializer<>(net.dofmine.minedofmod.data.recipes.vacuum.VacuumBackPack::new).setRegistryName(MinedofMod.MODS_ID, "vacuum_recipe"));
        event.getRegistry().register(new SimpleRecipeSerializer<>(net.dofmine.minedofmod.data.recipes.vacuum.RevertVacuumBackPack::new).setRegistryName(MinedofMod.MODS_ID, "revert_vacuum_recipe"));
    }

}