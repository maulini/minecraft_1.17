package net.dofmine.minedofmod.block;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.block.custom.CraftingTableBlock;
import net.dofmine.minedofmod.block.custom.LightningChannelerBlock;
import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.tabs.ModCreativeTabs;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MinedofMod.MODS_ID);

    public static final RegistryObject<Block> TITANIUM_BLOCK = registerBlock("titanium_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(12f)));
    public static final RegistryObject<Block> TITANIUM_ORE = registerBlock("titanium_ore", () ->  new Block(BlockBehaviour.Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(3.0f, 3.0f)));
    public static final RegistryObject<Block> LIGHTNING_CHANNELER = registerBlock("lightning_channeler", () -> new LightningChannelerBlock(BlockBehaviour.Properties.of(Material.METAL)));
    public static final RegistryObject<Block> CRAFTING_TABLE = registerBlock("crafting_table", () -> new CraftingTableBlock(BlockBehaviour.Properties.of(Material.WOOD)));

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registererBlockIte(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registererBlockIte(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(ModCreativeTabs.MODS_TABS)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
