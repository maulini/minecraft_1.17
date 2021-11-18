package net.dofmine.minedofmod;

import com.mojang.blaze3d.platform.ScreenManager;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dofmine.minedofmod.block.ModBlocks;
import net.dofmine.minedofmod.container.ModContainer;
import net.dofmine.minedofmod.data.recipes.ModRecipeType;
import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.screen.CraftingTableScreen;
import net.dofmine.minedofmod.screen.LightningChannelerScreen;
import net.dofmine.minedofmod.tileentity.ModTileEntity;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.ISlotType;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MinedofMod.MODS_ID)
public class MinedofMod {
    public static final String MODS_ID = "mods";

    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public MinedofMod() {
        // Register the setup method for modloading
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModTileEntity.register(modEventBus);
        ModContainer.register(modEventBus);
        ModRecipeType.register(modEventBus);
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::enqueueIMC);
        modEventBus.addListener(this::doClientStuff);
        // Register ourselves for server and other game events we are interested
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MenuScreens.register(ModContainer.LIGHTNING_CHANNELER_CONTAINER.get(), LightningChannelerScreen::new);
            MenuScreens.register(ModContainer.CRAFTING_TABLE_CONTAINER.get(), CraftingTableScreen::new);
        });
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("key").icon(
                new ResourceLocation(MinedofMod.MODS_ID, "textures/empty_key_slot.png")).build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BELT.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BACK.getMessageBuilder().build());
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }
}