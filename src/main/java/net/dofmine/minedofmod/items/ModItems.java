package net.dofmine.minedofmod.items;

import com.google.common.collect.ImmutableMap;
import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.items.key.FlyKeyItem;
import net.dofmine.minedofmod.items.key.KeyItem;
import net.dofmine.minedofmod.items.key.StrengKeyItem;
import net.dofmine.minedofmod.tabs.ModCreativeTabs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MinedofMod.MODS_ID);

    public static final RegistryObject<Item> TITANUIM_INGOT = ITEMS.register("titanium_ingot", () -> new Item(new Item.Properties().tab(ModCreativeTabs.MODS_TABS)));
    public static final RegistryObject<Item> TITANUIM_NUGGET = ITEMS.register("titanium_nugget", () -> new Item(new Item.Properties().tab(ModCreativeTabs.MODS_TABS)));
    public static final RegistryObject<Item> RAW_TITANIUM = ITEMS.register("raw_titanium", () -> new Item(new Item.Properties().tab(ModCreativeTabs.MODS_TABS)));
    public static final RegistryObject<Item> RED_KEY = ITEMS.register("red_key", () -> new KeyItem(Blocks.LAVA));
    public static final RegistryObject<Item> WATER_KEY = ITEMS.register("water_key", () -> new KeyItem(Blocks.WATER));
    public static final RegistryObject<Item> STONE_KEY = ITEMS.register("stone_key", () -> new KeyItem(Blocks.STONE));
    public static final RegistryObject<Item> BASIC_KEY = ITEMS.register("basic_key", () -> new StrengKeyItem());
    public static final RegistryObject<Item> DARK_KEY = ITEMS.register("dark_key", () -> new KeyItem(Blocks.STRIPPED_DARK_OAK_LOG));
    public static final RegistryObject<Item> GOLDEN_KEY = ITEMS.register("golden_key", () -> new FlyKeyItem());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
