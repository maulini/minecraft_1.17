package net.dofmine.minedofmod;

import com.mojang.blaze3d.platform.ScreenManager;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dofmine.minedofmod.block.ModBlocks;
import net.dofmine.minedofmod.block.fluid.ModFluids;
import net.dofmine.minedofmod.container.BackPackContainer;
import net.dofmine.minedofmod.container.ModContainer;
import net.dofmine.minedofmod.data.recipes.ModRecipeType;
import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.job.ExtendedEntityPlayer;
import net.dofmine.minedofmod.network.Networking;
import net.dofmine.minedofmod.screen.*;
import net.dofmine.minedofmod.setup.ClientSetup;
import net.dofmine.minedofmod.tileentity.MjollnirEntity;
import net.dofmine.minedofmod.tileentity.ModEntity;
import net.dofmine.minedofmod.tileentity.ModTileEntity;
import net.dofmine.minedofmod.tileentity.renderer.MjollnirEntityRenderer;
import net.dofmine.minedofmod.world.ModWorldType;
import net.dofmine.minedofmod.world.biome.ModBiomes;
import net.dofmine.minedofmod.world.dimension.ModDimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.w3c.dom.css.Rect;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.ISlotType;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MinedofMod.MODS_ID)
public class MinedofMod {
    public static final String MODS_ID = "mods";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    private static ResourceLocation customKey = new ResourceLocation(MinedofMod.MODS_ID, "empty_key_slot");

    public MinedofMod() {
        // Register the setup method for modloading
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModTileEntity.register(modEventBus);
        ModContainer.register(modEventBus);
        ModRecipeType.register(modEventBus);
        ModBiomes.register(modEventBus);
        ModFluids.register(modEventBus);
        ModEntity.register(modEventBus);
        modEventBus.addListener(this::initSprite);
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::doClientStuff);
        Networking.registerMessages();
        // Register ourselves for server and other game events we are interested
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(ClientSetup::init));
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ClientSetup());
    }

    private void initSprite(final TextureStitchEvent.Pre event) {
        event.addSprite(customKey);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            //Rectangle maximumView = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            GLFW.glfwSetWindowSize(Minecraft.getInstance().getWindow().getWindow(), 1920, 1080);
            GLFW.glfwSetWindowPos(Minecraft.getInstance().getWindow().getWindow(), 0, 0);
            MenuScreens.register(ModContainer.LIGHTNING_CHANNELER_CONTAINER.get(), LightningChannelerScreen::new);
            MenuScreens.register(ModContainer.CRAFTING_TABLE_CONTAINER.get(), CraftingTableScreen::new);
            MenuScreens.register(ModContainer.BACK_PACK_CONTAINER.get(), BackPackScreen::new);
        });
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPECIAL_DOOR.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.TOMATO_PLANT.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.ORCHID.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.TELEPORTER_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.REDWOOD_LEAVES.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.REDWOOD_SAPLING.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModFluids.STYX.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModFluids.STYX_FLOWING.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.STYX.get(), RenderType.translucent());

        EntityRenderers.register(ModEntity.MJOLLNIR_PROJECTILE.get(), MjollnirEntityRenderer::new);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("key").icon(
                customKey).build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BELT.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BACK.getMessageBuilder().build());
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

}