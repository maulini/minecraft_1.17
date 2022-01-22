package net.dofmine.minedofmod;

import net.dofmine.minedofmod.block.ModBlocks;
import net.dofmine.minedofmod.block.fluid.ModFluids;
import net.dofmine.minedofmod.container.ModContainer;
import net.dofmine.minedofmod.data.recipes.ModRecipeType;
import net.dofmine.minedofmod.effects.ModEffect;
import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.network.Networking;
import net.dofmine.minedofmod.screen.BackPackScreen;
import net.dofmine.minedofmod.screen.CraftingTableScreen;
import net.dofmine.minedofmod.screen.LightningChannelerScreen;
import net.dofmine.minedofmod.screen.WaterCollectorScreen;
import net.dofmine.minedofmod.setup.ClientSetup;
import net.dofmine.minedofmod.tileentity.ModEntity;
import net.dofmine.minedofmod.tileentity.ModTileEntity;
import net.dofmine.minedofmod.tileentity.renderer.MjollnirEntityRenderer;
import net.dofmine.minedofmod.world.biome.ModBiomes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(MinedofMod.MODS_ID)
public class MinedofMod {
    public static final String MODS_ID = "mods";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    private static final ResourceLocation customKey = new ResourceLocation(MinedofMod.MODS_ID, "empty_key_slot");

    public MinedofMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModTileEntity.register(modEventBus);
        ModContainer.register(modEventBus);
        ModRecipeType.register(modEventBus);
        ModBiomes.register(modEventBus);
        ModFluids.register(modEventBus);
        ModEntity.register(modEventBus);
        ModEffect.register(modEventBus);
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::doClientStuff);
        // Register ourselves for server and other game events we are interested
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->  {
            modEventBus.addListener(this::initSprite);
            modEventBus.addListener(ClientSetup::init);
        });
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void initSprite(final TextureStitchEvent.Pre event) {
        event.addSprite(customKey);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Minecraft.getInstance().getWindow().toggleFullScreen();
            Minecraft.getInstance().getWindow().changeFullscreenVideoMode();
            MenuScreens.register(ModContainer.LIGHTNING_CHANNELER_CONTAINER.get(), LightningChannelerScreen::new);
            MenuScreens.register(ModContainer.CRAFTING_TABLE_CONTAINER.get(), CraftingTableScreen::new);
            MenuScreens.register(ModContainer.BACK_PACK_CONTAINER.get(), BackPackScreen::new);
            MenuScreens.register(ModContainer.WATER_COLLECTOR_CONTAINER.get(), WaterCollectorScreen::new);
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
        Networking.registerMessages();
    }

}