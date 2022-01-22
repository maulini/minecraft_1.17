package net.dofmine.minedofmod.setup;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.block.ModBlocks;
import net.dofmine.minedofmod.block.custom.ElevatorBlock;
import net.dofmine.minedofmod.container.BackPackContainer;
import net.dofmine.minedofmod.effects.ModEffect;
import net.dofmine.minedofmod.items.ModArmorMaterial;
import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.items.backpack.VacuumBackPack;
import net.dofmine.minedofmod.job.*;
import net.dofmine.minedofmod.screen.HydrationBar;
import net.dofmine.minedofmod.screen.JobsScreen;
import net.dofmine.minedofmod.screen.ManaBar;
import net.dofmine.minedofmod.tileentity.Spells;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = MinedofMod.MODS_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {

    private static final Map<String, CompoundTag> extendedEntityData = new HashMap<String, CompoundTag>();
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();
    private static boolean canTeleportate;

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

    @SubscribeEvent
    public static void entity(final AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {
            if (ExtendedEntityPlayer.get() == null) {
                ExtendedEntityPlayer.register(player, event);
            }
            if (ExtendedMinerJobsEntityPlayer.get() == null) {
                ExtendedMinerJobsEntityPlayer.register(player, event);
            }
            if (ExtendedFarmerJobsEntityPlayer.get() == null) {
                ExtendedFarmerJobsEntityPlayer.register(player, event);
            }
            if (ExtendedLocksmithJobsEntityPlayer.get() == null) {
                ExtendedLocksmithJobsEntityPlayer.register(player, event);
            }
            if (ExtendedWizardJobsEntityPlayer.get() == null) {
                ExtendedWizardJobsEntityPlayer.register(player, event);
            }
            if (ExtendedHunterJobsEntityPlayer.get() == null) {
                ExtendedHunterJobsEntityPlayer.register(player, event);
            }
            if (HydrationEntityPlayer.get() == null) {
                HydrationEntityPlayer.register(player, event);
            }
        }
        if (event.getObject() instanceof ServerPlayer player) {
            Spells.serverLevel = (ServerLevel) player.level;
            ClientSetup.serverLevel = (ServerLevel) player.level;
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
            Spells.thunderBolt();
        }
        if (ClientSetup.spell2.isDown()) {
            Spells.levitation();
        }
        if (ClientSetup.spell3.isDown()) {
            Spells.witch();
        }
        if (event.getKey() == Minecraft.getInstance().options.keyShift.getKey().getValue() && !Minecraft.getInstance().options.keyShift.isDown()) {
            canTeleportate = true;
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
    public static void onEntityDeath(LivingDeathEvent event) {
        if (!event.getEntity().level.isClientSide) {
            if (event.getEntity() instanceof Ravager) {
                ExtendedEntityPlayer.get().addMana(2);
            }
            if (ClientSetup.xpByEntityHunter.containsKey(event.getEntity().getType())) {
                ExtendedHunterJobsEntityPlayer hunter = ExtendedHunterJobsEntityPlayer.get();
                hunter.addXp(ClientSetup.xpByEntityHunter.get(event.getEntity().getType()).apply(hunter.level));
            }
            if (event.getEntity() instanceof Player) {
                HydrationEntityPlayer.get().addHydration(20);
            }
        }
    }
    
    @SubscribeEvent
    public static void onBlockDestroy(BlockEvent.BreakEvent event) {
        if (!event.getPlayer().isCreative() && !event.getPlayer().level.isClientSide) {
            if (ClientSetup.xpByBlockMiner.containsKey(event.getState().getBlock())) {
                ExtendedMinerJobsEntityPlayer minerJobs = ExtendedMinerJobsEntityPlayer.get();
                Function<Integer, Long> func = ClientSetup.xpByBlockMiner.get(event.getState().getBlock());
                minerJobs.addXp(func.apply(minerJobs.level));
            }else if (ClientSetup.xpByBlockFarmer.containsKey(event.getState().getBlock())) {
                ExtendedFarmerJobsEntityPlayer farmerJobs = ExtendedFarmerJobsEntityPlayer.get();
                Function<Integer, Long> func = ClientSetup.xpByBlockFarmer.get(event.getState().getBlock());
                farmerJobs.addXp(func.apply(farmerJobs.level));
            }
        }
    }

    @SubscribeEvent
    public static void onEntityAttack(AttackEntityEvent event) {
        if (!event.getPlayer().level.isClientSide && !event.getPlayer().isCreative()) {
            if (ClientSetup.canUseItem.containsKey(event.getPlayer().getMainHandItem().getItem().getRegistryName())) {
                Map<Class<?>, Integer> map = ClientSetup.canUseItem.get(event.getPlayer().getMainHandItem().getItem().getRegistryName());
                if (map.containsKey(ExtendedHunterJobsEntityPlayer.class)) {
                    ExtendedHunterJobsEntityPlayer hunter = ExtendedHunterJobsEntityPlayer.get();
                    Integer level = map.get(ExtendedHunterJobsEntityPlayer.class);
                    if (hunter.level < level) {
                        event.getEntity().sendMessage(new TextComponent(String.format("Your hunter level must be level %d to use %s", level, event.getPlayer().getMainHandItem().getItem().getRegistryName())), UUID.randomUUID());
                        event.setCanceled(true);
                    }
                }
            }
            if (!event.getPlayer().isCreative() && ClientSetup.canAttackEntity.containsKey(event.getTarget().getType())) {
                ExtendedHunterJobsEntityPlayer hunter = ExtendedHunterJobsEntityPlayer.get();
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
            ExtendedFarmerJobsEntityPlayer farmer = ExtendedFarmerJobsEntityPlayer.get();
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
            ExtendedFarmerJobsEntityPlayer farmer = ExtendedFarmerJobsEntityPlayer.get();
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
        if (event.getEntity().level.isClientSide) {
            ExtendedEntityPlayer.get().spend(-10);
        }
    }

    @SubscribeEvent
    public static void onUpdateMovement(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            if (canTeleportate && serverPlayer.isShiftKeyDown() && serverPlayer.level.getBlockState(serverPlayer.blockPosition().atY(serverPlayer.blockPosition().getY() - 1)).getBlock() instanceof ElevatorBlock elevatorBlock) {
                event.setCanceled(true);
                canTeleportate = false;
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
        if (event.getEntity() instanceof Player player && !player.level.isClientSide) {
            ItemStack item = event.getItem();
            if (item.is(Items.MILK_BUCKET)) {
                HydrationEntityPlayer.get().addHydration(3);
                player.addEffect(new MobEffectInstance(ModEffect.THIRST.get(), 300, 0));
            }else if (item.is(Items.POTION)) {
                Potion potion = PotionUtils.getPotion(item);
                if (potion.equals(Potions.WATER)) {
                    HydrationEntityPlayer.get().addHydration(6);
                    player.addEffect(new MobEffectInstance(ModEffect.THIRST.get(), 600, 0));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (!event.getEntity().level.isClientSide && event.getEntity() instanceof Player player) {
            CompoundTag playerData = getEntityData(player.getDisplayName().getString());
            if (playerData != null) {
                ExtendedEntityPlayer.get().deserializeNBT(playerData);
                ExtendedMinerJobsEntityPlayer.get().deserializeNBT(playerData);
                ExtendedFarmerJobsEntityPlayer.get().deserializeNBT(playerData);
                ExtendedLocksmithJobsEntityPlayer.get().deserializeNBT(playerData);
                ExtendedWizardJobsEntityPlayer.get().deserializeNBT(playerData);
                ExtendedHunterJobsEntityPlayer.get().deserializeNBT(playerData);
                HydrationEntityPlayer.get().deserializeNBT(playerData);
            }

            ExtendedEntityPlayer.get().sync();
            ExtendedMinerJobsEntityPlayer.get().sync();
            ExtendedFarmerJobsEntityPlayer.get().sync();
            ExtendedLocksmithJobsEntityPlayer.get().sync();
            ExtendedHunterJobsEntityPlayer.get().sync();
            ExtendedWizardJobsEntityPlayer.get().sync();
            HydrationEntityPlayer.get().sync();
        }
    }
    public static void storeEntityData(String name, CompoundTag compound) {
        extendedEntityData.put(name, compound);
    }

    public static CompoundTag getEntityData(String name) {
        return extendedEntityData.remove(name);
    }

}
