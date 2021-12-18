package net.dofmine.minedofmod.block;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.block.custom.CraftingTableBlock;
import net.dofmine.minedofmod.block.custom.LightningChannelerBlock;
import net.dofmine.minedofmod.block.custom.SpecialDoorTeleportate;
import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.setup.ClientSetup;
import net.dofmine.minedofmod.tabs.ModCreativeTabs;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MinedofMod.MODS_ID);

    public static final RegistryObject<Block> TITANIUM_BLOCK = registerBlock("titanium_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(12f)), null);
    public static final RegistryObject<Block> TITANIUM_ORE = registerBlock("titanium_ore", () ->  new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0f, 3.0f)), null);
    public static final RegistryObject<Block> LIGHTNING_CHANNELER = registerBlock("lightning_channeler", () -> new LightningChannelerBlock(BlockBehaviour.Properties.of(Material.METAL)), null);
    public static final RegistryObject<Block> CRAFTING_TABLE = registerBlock("crafting_table", () -> new CraftingTableBlock(BlockBehaviour.Properties.of(Material.WOOD)), null);
    public static final RegistryObject<Block> SPECIAL_DOOR = registerBlock("special_door", () -> new SpecialDoorTeleportate(), null);
    public static final RegistryObject<Block> RUBY_ORE = registerBlock("ruby_ore", () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(0, 2)), i -> {
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
    public static final RegistryObject<Block> LAPIS_ORE = registerBlock("lapis_ore", () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(0, 2)), i -> {
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
    public static final RegistryObject<Block> DARK_ORE = registerBlock("dark_ore", () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(0, 2)),  i -> {
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
    public static final RegistryObject<Block> GOD_ORE = registerBlock("god_ore", () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0F, 3.0F), UniformInt.of(0, 2)), null);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, Function<Integer, Long> function) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registererBlockItem(name, toReturn, function);
        return toReturn;
    }

    private static <T extends Block> void registererBlockItem(String name, RegistryObject<T> block, Function<Integer, Long> function) {
        ModItems.ITEMS.register(name, () -> {
            T realBlock = block.get();
            if (function != null) {
                ClientSetup.addBlocks(realBlock, function);
            }
            return new BlockItem(realBlock, new Item.Properties().tab(ModCreativeTabs.MODS_TABS));
        });
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
