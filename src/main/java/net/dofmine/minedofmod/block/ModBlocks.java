package net.dofmine.minedofmod.block;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.block.custom.*;
import net.dofmine.minedofmod.block.custom.CraftingTableBlock;
import net.dofmine.minedofmod.block.fluid.ModFluids;
import net.dofmine.minedofmod.block.thirst.WaterCollector;
import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.setup.ClientSetup;
import net.dofmine.minedofmod.tabs.ModCreativeTabs;
import net.dofmine.minedofmod.world.features.tree.RedwoodTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MinedofMod.MODS_ID);
    public static final Map<Item, Function<Integer, Long>> registerFunction = new HashMap<>();

    /***********************************
     ***************BLOCK***************
     ***********************************/
    public static final RegistryObject<Block> TITANIUM_BLOCK = registerBlock("titanium_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(12f)), null);
    public static final RegistryObject<Block> DARK_BLOCK = registerBlock("dark_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(12f)), null);
    public static final RegistryObject<Block> LAPIS_BLOCK = registerBlock("lapis_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(12f)), null);
    public static final RegistryObject<Block> RUBY_BLOCK = registerBlock("ruby_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(12f)), null);

    /***********************************
     ***********ENTITY BLOCK************
     ***********************************/
    public static final RegistryObject<Block> LIGHTNING_CHANNELER = registerBlock("lightning_channeler", () -> new LightningChannelerBlock(BlockBehaviour.Properties.of(Material.METAL)), null);
    public static final RegistryObject<Block> CRAFTING_TABLE = registerBlock("crafting_table", () -> new CraftingTableBlock(BlockBehaviour.Properties.of(Material.WOOD)), null);
    public static final RegistryObject<Block> SPECIAL_DOOR = registerBlock("special_door", SpecialDoorTeleportate::new, null);
    public static final RegistryObject<Block> GLOBE = registerBlock("globe", GlobeBlock::new, null);

    /***********************************
     **************ORE BLOCK************
     ***********************************/
    public static final RegistryObject<Block> TITANIUM_ORE = registerBlock("titanium_ore", () ->  new OreBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0f, 3.0f), UniformInt.of(0, 2)), null);
    public static final RegistryObject<Block> RUBY_ORE = registerBlock("ruby_ore", () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(0, 4)), i -> {
        long value = 0L;
        if (i >= 10 && i < 15) {
            value = 150L;
        }else if (i >= 15 && i < 18) {
            value = 75L;
        }else if (i >= 18) {
            value = 25L;
        }
        return value;
    });
    public static final RegistryObject<Block> LAPIS_ORE = registerBlock("lapis_ore", () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(0, 3)), i -> {
        long value = 0L;
        if (i >= 5 && i < 10) {
            value = 150L;
        }else if (i >= 10 && i < 15) {
            value = 50L;
        }else if (i >= 15 && i < 18) {
            value = 30L;
        }else if (i >= 18) {
            value = 10L;
        }
        return value;
    });
    public static final RegistryObject<Block> DARK_ORE = registerBlock("dark_ore", () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(0, 6)),  i -> {
        long value = 0L;
        if (i >= 15 && i < 18) {
            value = 250L;
        }else if (i == 18) {
            value = 125L;
        }else if (i >= 19) {
            value = 90L;
        }
        return value;
    });
    public static final RegistryObject<Block> GOD_ORE = registerBlock("god_ore", () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(0, 7)), null);

    /***********************************
     **********VEGETAL BLOCK************
     ***********************************/
    public static final RegistryObject<Block> TOMATO_PLANT = BLOCKS.register("tomato_plant",
            () -> new TomatoPlantBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<Block> ORCHID = registerBlock("orchid",
            () -> new FlowerCustomBlock(MobEffects.BLINDNESS, 2,
                    BlockBehaviour.Properties.copy(Blocks.DANDELION)), null);

    public static final RegistryObject<Block> REDWOOD_WOOD = registerBlock("redwood_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)),null);

    public static final RegistryObject<Block> STRIPPED_REDWOOD_LOG = registerBlock("stripped_redwood_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_DARK_OAK_LOG)), null);
    public static final RegistryObject<Block> STRIPPED_REDWOOD_WOOD = registerBlock("stripped_redwood_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)), null);

    public static final RegistryObject<Block> REDWOOD_PLANKS = registerBlock("redwood_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 20;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 5;
                }
            }, null);


    public static final RegistryObject<Block> REDWOOD_SAPLING = registerBlock("redwood_sapling",
            () -> new SaplingCustomBlock(new RedwoodTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)), null);

    /***********************************
     ***********DIMENSION BLOCK*********
     ***********************************/
    public static final RegistryObject<Block> TELEPORTER_BLOCK = registerBlock("teleporter_block", () -> new TeleporterBlock(BlockBehaviour.Properties.of(Material.PORTAL).strength(12f)), null);
    public static final RegistryObject<Block> REDWOOD_LOG = registerBlock("redwood_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)),null);
    public static final RegistryObject<Block> REDWOOD_LEAVES = registerBlock("redwood_leaves",
            () -> new CustomLeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return true;
                }

                @Override
                public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 60;
                }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
                    return 30;
                }

            }, null);
    public static final RegistryObject<Block> DIRT_DARK = registerBlock("dirt_dark", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT)), null);
    public static final RegistryObject<Block> GRASS_DARK = registerBlock("grass_dark", () -> new Block(BlockBehaviour.Properties.copy(Blocks.DIRT)), null);
    public static final RegistryObject<Block> STONE_DARK = registerBlock("stone_dark", () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)), null);
    public static final RegistryObject<Block> COBBLE_DARK = registerBlock("cobble_dark", () -> new Block(BlockBehaviour.Properties.copy(Blocks.COBBLESTONE)), null);

    /***********************************
     ************** FLUIDS *************
     ***********************************/
    public static final RegistryObject<Block> STYX = BLOCKS.register("styx", () -> new CustomLiquidBlock(() -> (ForgeFlowingFluid) ModFluids.STYX.get(), BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(50.0F).noDrops()));

    /***********************************
     ************** CUSTOM *************
     ***********************************/
    public static final RegistryObject<Block> ELEVATOR_BLOCK = registerBlock("elevator_block", () -> new ElevatorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)), null);

    /***********************************
     ***************THIRST***************
     ***********************************/
    public static final RegistryObject<Block> WATER_COLLECTOR = registerBlock("water_collector", () -> new WaterCollector(BlockBehaviour.Properties.of(Material.STONE).strength(3.5F)), null);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, Function<Integer, Long> function) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registererBlockItem(name, toReturn, function);
        return toReturn;
    }

    private static <T extends Block> void registererBlockItem(String name, RegistryObject<T> block, Function<Integer, Long> function) {
        ModItems.ITEMS.register(name, () -> {
            T realBlock = block.get();
            if (function != null) {
                registerFunction.put(realBlock.asItem(), function);
            }
            return new BlockItem(realBlock, new Item.Properties().tab(ModCreativeTabs.MODS_TABS));
        });
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
