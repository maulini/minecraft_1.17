package net.dofmine.minedofmod.setup;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.block.ModBlocks;
import net.dofmine.minedofmod.block.custom.ElevatorBlock;
import net.dofmine.minedofmod.container.BackPackContainer;
import net.dofmine.minedofmod.effects.ModEffect;
import net.dofmine.minedofmod.items.ModArmorMaterial;
import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.items.backpack.VacuumBackPack;
import net.dofmine.minedofmod.job.client.*;
import net.dofmine.minedofmod.job.server.*;
import net.dofmine.minedofmod.network.*;
import net.dofmine.minedofmod.screen.HydrationBar;
import net.dofmine.minedofmod.screen.JobsScreen;
import net.dofmine.minedofmod.screen.ManaBar;
import net.dofmine.minedofmod.tileentity.Spells;
import net.dofmine.minedofmod.utils.JobsUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = MinedofMod.MODS_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderGui(RenderGameOverlayEvent.Post event) {
        if (!Minecraft.getInstance().player.isCreative()) {
            new ManaBar(Minecraft.getInstance(), event.getMatrixStack());
            new HydrationBar(Minecraft.getInstance(), event.getMatrixStack());
        }
    }

    @SubscribeEvent
    public static void playerTickEvent(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player.level.isClientSide) {
            HydrationEntityPlayer hydrationEntityPlayer = (HydrationEntityPlayer) JobsUtil.getAllCapabilities(player).stream().filter(iCapabilityProvider -> iCapabilityProvider instanceof HydrationEntityPlayer).findFirst().get();
            if (hydrationEntityPlayer != null) {
                hydrationEntityPlayer.tick(player);
            }
        }
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

    @SubscribeEvent
    public static void entity(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (player.level.isClientSide) {
                if (player.getCapability(ExtendedMinerJobsEntityPlayer.MINER_JOBS).orElse(null) == null) {
                    ExtendedMinerJobsEntityPlayer.register(player, event);
                }
                if (player.getCapability(ExtendedEntityPlayer.WIZARD_JOBS).orElse(null) == null) {
                    ExtendedEntityPlayer.register(player, event);
                }
                if (player.getCapability(ExtendedFarmerJobsEntityPlayer.FARMER_JOBS).orElse(null) == null) {
                    ExtendedFarmerJobsEntityPlayer.register(player, event);
                }
                if (player.getCapability(ExtendedLocksmithJobsEntityPlayer.LOCKSMITH_JOBS).orElse(null) == null) {
                    ExtendedLocksmithJobsEntityPlayer.register(player, event);
                }
                if (player.getCapability(ExtendedWizardJobsEntityPlayer.WIZARD_JOBS).orElse(null) == null) {
                    ExtendedWizardJobsEntityPlayer.register(player, event);
                }
                if (player.getCapability(ExtendedHunterJobsEntityPlayer.HUNTER_JOBS).orElse(null) == null) {
                    ExtendedHunterJobsEntityPlayer.register(player, event);
                }
                if (player.getCapability(HydrationEntityPlayer.HYDRATION).orElse(null) == null) {
                    HydrationEntityPlayer.register(player, event);
                }
            }else {
                if (player.getCapability(ExtendedMinerJobsEntityPlayerServer.MINER_JOBS).orElse(null) == null) {
                    ExtendedMinerJobsEntityPlayerServer.register(player, event);
                }
                if (player.getCapability(ExtendedEntityPlayerServer.WIZARD_JOBS).orElse(null) == null) {
                    ExtendedEntityPlayerServer.register(player, event);
                }
                if (player.getCapability(ExtendedFarmerJobsEntityPlayerServer.FARMER_JOBS).orElse(null) == null) {
                    ExtendedFarmerJobsEntityPlayerServer.register(player, event);
                }
                if (player.getCapability(ExtendedLocksmithJobsEntityPlayerServer.LOCKSMITH_JOBS).orElse(null) == null) {
                    ExtendedLocksmithJobsEntityPlayerServer.register(player, event);
                }
                if (player.getCapability(ExtendedWizardJobsEntityPlayerServer.WIZARD_JOBS).orElse(null) == null) {
                    ExtendedWizardJobsEntityPlayerServer.register(player, event);
                }
                if (player.getCapability(ExtendedHunterJobsEntityPlayerServer.HUNTER_JOBS).orElse(null) == null) {
                    ExtendedHunterJobsEntityPlayerServer.register(player, event);
                }
                if (player.getCapability(HydrationEntityPlayerServer.HYDRATION).orElse(null) == null) {
                    HydrationEntityPlayerServer.register(player, event);
                }
            }
        }
        if (event.getObject() instanceof ServerPlayer player) {
            Spells.serverPlayer = player;
            ClientSetup.serverLevel = (ServerLevel) player.level;
        }
    }

    @SubscribeEvent
    public static void onPlayerDeathClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            Class<ServerPlayer> clazz = (Class<ServerPlayer>) event.getOriginal().getClass();
            try {
                Method method = clazz.getSuperclass().getSuperclass().getSuperclass().getSuperclass().getDeclaredMethod("getCapabilities");
                method.setAccessible(true);
                CapabilityDispatcher capabilityDispatcher = (CapabilityDispatcher) method.invoke(event.getOriginal());
                Field field = capabilityDispatcher.getClass().getDeclaredField("caps");
                field.setAccessible(true);
                ICapabilityProvider[] allCapabilities = (ICapabilityProvider[]) field.get(capabilityDispatcher);
                for (int i = 0; i < allCapabilities.length; i++) {
                    if (allCapabilities[i] instanceof ExtendedFarmerJobsEntityPlayerServer farmer) {
                        ExtendedFarmerJobsEntityPlayerServer farmerNew = ExtendedFarmerJobsEntityPlayerServer.get(event.getPlayer());
                        farmerNew.level = farmer.level;
                        farmerNew.maxXp = farmer.maxXp;
                        farmerNew.xp = farmer.xp;
                    } else if (allCapabilities[i] instanceof ExtendedEntityPlayerServer wizard) {
                        ExtendedEntityPlayerServer farmerNew = ExtendedEntityPlayerServer.get(event.getPlayer());
                        farmerNew.mana = wizard.mana;
                    } else if (allCapabilities[i] instanceof ExtendedHunterJobsEntityPlayerServer hunter) {
                        ExtendedHunterJobsEntityPlayerServer hunterNew = ExtendedHunterJobsEntityPlayerServer.get(event.getPlayer());
                        hunterNew.level = hunter.level;
                        hunterNew.maxXp = hunter.maxXp;
                        hunterNew.xp = hunter.xp;
                    } else if (allCapabilities[i] instanceof ExtendedLocksmithJobsEntityPlayerServer locksmith) {
                        ExtendedLocksmithJobsEntityPlayerServer locksmithNew = ExtendedLocksmithJobsEntityPlayerServer.get(event.getPlayer());
                        locksmithNew.level = locksmith.level;
                    } else if (allCapabilities[i] instanceof ExtendedMinerJobsEntityPlayerServer miner) {
                        ExtendedMinerJobsEntityPlayerServer minerNew = ExtendedMinerJobsEntityPlayerServer.get(event.getPlayer());
                        minerNew.level = miner.level;
                        minerNew.maxXp = miner.maxXp;
                        minerNew.xp = miner.xp;
                    } else if (allCapabilities[i] instanceof HydrationEntityPlayerServer) {
                        HydrationEntityPlayerServer.get(event.getPlayer()).actualHydration = 20;
                    }
                }
            } catch (NoSuchMethodException | NoSuchFieldException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.level.getBlockState(serverPlayer.blockPosition().atY(serverPlayer.blockPosition().getY() - 1)).getBlock() instanceof ElevatorBlock elevatorBlock) {
                elevatorBlock.upTeleport(serverPlayer);
            }
        }
    }

    @SubscribeEvent
    public static void onKeyPressed(InputEvent.KeyInputEvent event) {
        if (ClientSetup.jobsKey.isDown()) {
            openGuiJobs();
        }
        if (ClientSetup.chooseSpell.isDown()) {
            //Minecraft.getInstance().setScreen(new ChooseSpellScreen(new TextComponent("")));
        }
        if (ClientSetup.spell1.isDown()) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Networking.sendToServer(new PacketSpawnThunderBlot(Minecraft.getInstance().player.blockPosition(), Minecraft.getInstance().player.isCreative())));
        }
        if (ClientSetup.spell2.isDown()) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Networking.sendToServer(new PacketLevitationSpell(Minecraft.getInstance().player.blockPosition(), Minecraft.getInstance().player.isCreative())));
        }
        if (ClientSetup.spell3.isDown()) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> Networking.sendToServer(new PacketSpawnWitch(Minecraft.getInstance().player.blockPosition(), Minecraft.getInstance().player.isCreative())));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void openGuiJobs() {
        Minecraft.getInstance().setScreen(new JobsScreen(new TextComponent("")));
    }

    @SubscribeEvent
    public static void onStartBlockDestroy(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getEntity() instanceof Player player && !player.isCreative() && player.level.isClientSide) {
            if (ClientSetup.canUseItem.containsKey(event.getItemStack().getItem().getRegistryName())) {
                Map<Class<?>, Integer> map = ClientSetup.canUseItem.get(event.getItemStack().getItem().getRegistryName());
                if (map.containsKey(ExtendedFarmerJobsEntityPlayer.class)) {
                    ExtendedFarmerJobsEntityPlayer farmer = ExtendedFarmerJobsEntityPlayer.get(event.getPlayer());
                    Integer level = map.get(ExtendedFarmerJobsEntityPlayer.class);
                    if (farmer.level < level) {
                        event.getEntity().sendMessage(new TextComponent(String.format("Your farmer level must be level %d to use %s", level, event.getItemStack().getItem().getRegistryName())), UUID.randomUUID());
                        event.setCanceled(true);
                    }
                } else if (map.containsKey(ExtendedMinerJobsEntityPlayer.class)) {
                    ExtendedMinerJobsEntityPlayer miner = ExtendedMinerJobsEntityPlayer.get(event.getPlayer());
                    Integer level = map.get(ExtendedMinerJobsEntityPlayer.class);
                    if (miner.level < level) {
                        event.getEntity().sendMessage(new TextComponent(String.format("Your miner level must be level %d to use %s", level, event.getItemStack().getItem().getRegistryName())), UUID.randomUUID());
                        event.setCanceled(true);
                    }
                } else if (map.containsKey(ExtendedLocksmithJobsEntityPlayer.class)) {
                    ExtendedLocksmithJobsEntityPlayer locksmith = ExtendedLocksmithJobsEntityPlayer.get(event.getPlayer());
                    Integer level = map.get(ExtendedLocksmithJobsEntityPlayer.class);
                    if (locksmith.level < level) {
                        event.getEntity().sendMessage(new TextComponent(String.format("Your locksmith level must be level %d to use %s", level, event.getItemStack().getItem().getRegistryName())), UUID.randomUUID());
                        event.setCanceled(true);
                    }
                } else if (map.containsKey(ExtendedHunterJobsEntityPlayer.class)) {
                    ExtendedHunterJobsEntityPlayer hunter = ExtendedHunterJobsEntityPlayer.get(event.getPlayer());
                    Integer level = map.get(ExtendedHunterJobsEntityPlayer.class);
                    if (hunter.level < level) {
                        event.getEntity().sendMessage(new TextComponent(String.format("Your hunter level must be level %d to use %s", level, event.getItemStack().getItem().getRegistryName())), UUID.randomUUID());
                        event.setCanceled(true);
                    }
                }
            }
            Block block = event.getPlayer().level.getBlockState(event.getPos()).getBlock();
            ExtendedMinerJobsEntityPlayer minerJob = ExtendedMinerJobsEntityPlayer.get(event.getPlayer());
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
    public static void onEntityDeath(LivingDeathEvent event) {
        if (event.getEntity().level.isClientSide) {
            if (event.getSource().getEntity() instanceof Player player) {
                if (event.getEntity() instanceof Ravager) {
                    ExtendedEntityPlayer.get(player).addMana(2);
                }
                if (ClientSetup.xpByEntityHunter.containsKey(event.getEntity().getType())) {
                    ExtendedHunterJobsEntityPlayer hunter = (ExtendedHunterJobsEntityPlayer) JobsUtil.getAllCapabilities(player).stream().filter(iCapabilityProvider -> iCapabilityProvider instanceof  ExtendedHunterJobsEntityPlayer).findFirst().get();
                    hunter.addXp(ClientSetup.xpByEntityHunter.get(event.getEntity().getType()).apply(hunter.level));
                }
            }
        }
    }
    
    @SubscribeEvent
    public static void onBlockDestroy(BlockEvent.BreakEvent event) {
        if (!event.getPlayer().isCreative()) {
            Networking.sendToClient(new PacketBreakBlock(new ItemStack(event.getState().getBlock().asItem())), (ServerPlayer) event.getPlayer());
        }
    }

    @SubscribeEvent
    public static void onEntityAttack(AttackEntityEvent event) {
        if (event.getPlayer().level.isClientSide && !event.getPlayer().isCreative()) {
            if (ClientSetup.canUseItem.containsKey(event.getPlayer().getMainHandItem().getItem().getRegistryName())) {
                Map<Class<?>, Integer> map = ClientSetup.canUseItem.get(event.getPlayer().getMainHandItem().getItem().getRegistryName());
                if (map.containsKey(ExtendedHunterJobsEntityPlayer.class)) {
                    ExtendedHunterJobsEntityPlayer hunter = ExtendedHunterJobsEntityPlayer.get(event.getPlayer());
                    Integer level = map.get(ExtendedHunterJobsEntityPlayer.class);
                    if (hunter.level < level) {
                        event.getEntity().sendMessage(new TextComponent(String.format("Your hunter level must be level %d to use %s", level, event.getPlayer().getMainHandItem().getItem().getRegistryName())), UUID.randomUUID());
                        event.setCanceled(true);
                    }
                }
            }
            if (!event.getPlayer().isCreative() && ClientSetup.canAttackEntity.containsKey(event.getTarget().getType())) {
                ExtendedHunterJobsEntityPlayer hunter = ExtendedHunterJobsEntityPlayer.get(event.getPlayer());
                if (hunter.level < ClientSetup.canAttackEntity.get(event.getTarget().getType())) {
                    event.getPlayer().sendMessage(new TextComponent(String.format("Your hunter level must be %d to can attack %s", ClientSetup.canAttackEntity.get(event.getTarget().getType()) ,event.getTarget().getType())), UUID.randomUUID());
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerCraft(PlayerEvent.ItemCraftedEvent event) {
        if (ClientSetup.xpByItemFarmer.containsKey(event.getCrafting().getItem())) {
            ExtendedFarmerJobsEntityPlayer farmer = ExtendedFarmerJobsEntityPlayer.get(event.getPlayer());
            farmer.addXp(ClientSetup.xpByItemFarmer.get(event.getCrafting().getItem()).apply(farmer.level));
        }
        for (int i = 0; i < event.getInventory().getContainerSize(); i++) {
            ItemStack itemStack = event.getInventory().getItem(i);
            if (itemStack.is(ModItems.VACUUM_BACK_PACK.get())) {
                VacuumBackPack.decreaseSize(itemStack, event.getCrafting().getCount());
                if (!event.getPlayer().getInventory().add(itemStack)) {
                    ClientSetup.serverLevel.addFreshEntity(event.getPlayer().drop(itemStack, true, false));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerSmelted(PlayerEvent.ItemSmeltedEvent event) {
        if (ClientSetup.xpByItemFarmer.containsKey(event.getSmelting().getItem())) {
            ExtendedFarmerJobsEntityPlayer farmer = ExtendedFarmerJobsEntityPlayer.get(event.getPlayer());
            farmer.addXp(ClientSetup.xpByItemFarmer.get(event.getSmelting().getItem()).apply(farmer.level));
        }
    }
    
    @SubscribeEvent
    public static void onCloseGUI(PlayerContainerEvent.Close event) {
        if (event.getContainer() instanceof BackPackContainer backPackContainer) {
            backPackContainer.saveContent(event.getPlayer());
        }
    }

    @SubscribeEvent
    public static void onPlayerSleep(PlayerSleepInBedEvent event) {
    }

    @SubscribeEvent
    public static void onUpdateMovement(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.isShiftKeyDown() && serverPlayer.level.getBlockState(serverPlayer.blockPosition().atY(serverPlayer.blockPosition().getY() - 1)).getBlock() instanceof ElevatorBlock elevatorBlock) {
                event.setCanceled(true);
                elevatorBlock.downTeleport(serverPlayer);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerPickupItem(EntityItemPickupEvent event) {
        Optional<ItemStack> vacuumItemStack = event.getPlayer().getInventory().items.stream().filter(itemStack -> {
            boolean hasGoodVacuum = false;
            if (itemStack.is(ModItems.VACUUM_BACK_PACK.get()) && ((VacuumBackPack)itemStack.getItem()).containItem(itemStack, event.getItem().getItem())) {
                hasGoodVacuum = true;
            }
            return hasGoodVacuum;
        }).findFirst();
        if (vacuumItemStack.isPresent()) {
            event.setCanceled(true);
            ((VacuumBackPack)vacuumItemStack.get().getItem()).addItemToStack(vacuumItemStack.get(), event.getItem().getItem(), ClientSetup.serverLevel, event.getPlayer());
            event.getItem().move(MoverType.PLAYER, event.getPlayer().position());
            event.getItem().kill();
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
                HydrationEntityPlayer.get(player).addHydration(3);
                player.addEffect(new MobEffectInstance(ModEffect.THIRST.get(), 300, 0));
            }else if (item.is(Items.POTION)) {
                Potion potion = PotionUtils.getPotion(item);
                if (potion.equals(Potions.WATER)) {
                    HydrationEntityPlayer.get(player).addHydration(6);
                    player.addEffect(new MobEffectInstance(ModEffect.THIRST.get(), 600, 0));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ExtendedFarmerJobsEntityPlayerServer farmer = ExtendedFarmerJobsEntityPlayerServer.get(player);
            if (farmer != null) {
                farmer.sync();
            }
            ExtendedEntityPlayerServer wizard = ExtendedEntityPlayerServer.get(player);
            if (wizard != null) {
                wizard.sync();
            }
            ExtendedMinerJobsEntityPlayerServer miner = ExtendedMinerJobsEntityPlayerServer.get(player);
            if (miner != null) {
                miner.sync();
            }
            ExtendedLocksmithJobsEntityPlayerServer locksmith = ExtendedLocksmithJobsEntityPlayerServer.get(player);
            if (locksmith != null) {
                locksmith.sync();
            }
            ExtendedWizardJobsEntityPlayerServer wizardJobs = ExtendedWizardJobsEntityPlayerServer.get(player);
            if (wizardJobs != null) {
                wizardJobs.sync();
            }
            ExtendedHunterJobsEntityPlayerServer hunter = ExtendedHunterJobsEntityPlayerServer.get(player);
            if (hunter != null) {
                hunter.sync();
            }
            HydrationEntityPlayerServer hydration = HydrationEntityPlayerServer.get(player);
            if (hydration != null) {
                hydration.sync();
            }
        }
    }
}
