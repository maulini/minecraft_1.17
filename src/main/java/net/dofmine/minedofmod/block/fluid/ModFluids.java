package net.dofmine.minedofmod.block.fluid;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.block.ModBlocks;
import net.dofmine.minedofmod.block.custom.CustomLiquidBlock;
import net.dofmine.minedofmod.items.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModFluids {

    private static final ResourceLocation WATER_STILL = new ResourceLocation("block/water_still");
    private static final ResourceLocation WATER_FLOW = new ResourceLocation("block/water_flow");
    private static final ResourceLocation OVERLAY = new ResourceLocation("block/water_overlay");

    private static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, MinedofMod.MODS_ID);
    public static final RegistryObject<Fluid> STYX = register("styx", () -> new ForgeFlowingFluid.Source(ModFluids.STYX_PROPERTIES));
    public static final RegistryObject<Fluid> STYX_FLOWING = register("flowing_styx", () -> new ForgeFlowingFluid.Flowing(ModFluids.STYX_PROPERTIES));
    public static final ForgeFlowingFluid.Properties STYX_PROPERTIES = new ForgeFlowingFluid.Properties(STYX, STYX_FLOWING,
            FluidAttributes.builder(WATER_STILL, WATER_FLOW).overlay(OVERLAY).rarity(Rarity.EPIC).color(0xFFAA0000).luminosity(2)
                    .sound(SoundEvents.WATER_AMBIENT)).slopeFindDistance(2).levelDecreasePerBlock(2)
            .block(() -> (CustomLiquidBlock) ModBlocks.STYX.get()).bucket(ModItems.STYX_BUCKET::get);
    public static final ForgeFlowingFluid.Properties OIL_PROPERTIES = new ForgeFlowingFluid.Properties(STYX, STYX_FLOWING,
            FluidAttributes.builder(WATER_STILL, WATER_FLOW).overlay(OVERLAY).rarity(Rarity.EPIC).color(0xFF000000).luminosity(2)
                    .sound(SoundEvents.WATER_AMBIENT)).slopeFindDistance(2).levelDecreasePerBlock(2)
            .block(() -> (CustomLiquidBlock) ModBlocks.STYX.get()).bucket(ModItems.STYX_BUCKET::get);

    public static RegistryObject<Fluid> register(String name, Supplier<? extends  Fluid> fluid) {
        return FLUIDS.register(name, fluid);
    }

    public static void register(IEventBus event) {
        FLUIDS.register(event);
    }
}
