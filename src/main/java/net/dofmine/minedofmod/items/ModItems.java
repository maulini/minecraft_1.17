package net.dofmine.minedofmod.items;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.block.ModBlocks;
import net.dofmine.minedofmod.block.fluid.ModFluids;
import net.dofmine.minedofmod.items.armor.ModArmorItem;
import net.dofmine.minedofmod.items.armor.SpecialBootsArmor;
import net.dofmine.minedofmod.items.backpack.BackPackItem;
import net.dofmine.minedofmod.items.backpack.VacuumBackPack;
import net.dofmine.minedofmod.items.drink.DrinkItem;
import net.dofmine.minedofmod.items.drink.FilterItem;
import net.dofmine.minedofmod.items.finder.ChunkFinder;
import net.dofmine.minedofmod.items.key.FlyKeyItem;
import net.dofmine.minedofmod.items.key.KeyItem;
import net.dofmine.minedofmod.items.key.StrengKeyItem;
import net.dofmine.minedofmod.items.key.TeleportateKey;
import net.dofmine.minedofmod.items.tools.*;
import net.dofmine.minedofmod.items.weapon.ThorHammer;
import net.dofmine.minedofmod.tabs.ModCreativeTabs;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MinedofMod.MODS_ID);

    public static final RegistryObject<Item> TITANIUM_INGOT = ITEMS.register("titanium_ingot", () -> new Item(new Item.Properties().tab(ModCreativeTabs.MODS_TABS)));
    public static final RegistryObject<Item> RUBIS_INGOT = ITEMS.register("rubis_ingot", () -> new Item(new Item.Properties().tab(ModCreativeTabs.MODS_TABS)));
    public static final RegistryObject<Item> DARK_INGOT = ITEMS.register("dark_ingot", () -> new Item(new Item.Properties().tab(ModCreativeTabs.MODS_TABS)));
    public static final RegistryObject<Item> LAPIS_INGOT = ITEMS.register("lapis_ingot", () -> new Item(new Item.Properties().tab(ModCreativeTabs.MODS_TABS)));
    public static final RegistryObject<Item> GOD_INGOT = ITEMS.register("god_ingot", () -> new Item(new Item.Properties().tab(ModCreativeTabs.MODS_TABS)));
    public static final RegistryObject<Item> TITANUIM_NUGGET = ITEMS.register("titanium_nugget", () -> new Item(new Item.Properties().tab(ModCreativeTabs.MODS_TABS)));
    public static final RegistryObject<Item> RAW_TITANIUM = ITEMS.register("raw_titanium", () -> new Item(new Item.Properties().tab(ModCreativeTabs.MODS_TABS)));
    public static final RegistryObject<Item> RED_KEY = ITEMS.register("red_key", () -> new KeyItem(Blocks.LAVA));
    public static final RegistryObject<Item> WATER_KEY = ITEMS.register("water_key", () -> new KeyItem(Blocks.WATER));
    public static final RegistryObject<Item> STONE_KEY = ITEMS.register("stone_key", () -> new KeyItem(Blocks.STONE));
    public static final RegistryObject<Item> BASIC_KEY = ITEMS.register("basic_key", StrengKeyItem::new);
    public static final RegistryObject<Item> DARK_KEY = ITEMS.register("dark_key", () -> new KeyItem(Blocks.STRIPPED_DARK_OAK_LOG));
    public static final RegistryObject<Item> GOLDEN_KEY = ITEMS.register("golden_key", FlyKeyItem::new);
    public static final RegistryObject<Item> TELEPORTATE_KEY = ITEMS.register("teleportate_key", TeleportateKey::new);
    public static final RegistryObject<Item> COINS = ITEMS.register("coin", () -> new Item(new Item.Properties().tab(ModCreativeTabs.MODS_TABS)));
    public static final RegistryObject<Item> BACK_PACK = ITEMS.register("backpack", () -> new BackPackItem(new Item.Properties().tab(ModCreativeTabs.MODS_TABS).stacksTo(1)));
    public static final RegistryObject<Item> VACUUM_BACK_PACK = ITEMS.register("back_pack", () -> new VacuumBackPack(new Item.Properties().tab(ModCreativeTabs.MODS_TABS).stacksTo(1)));

    /***********************************************
     ******************* TOOLS *********************
     ***********************************************/
    public static final RegistryObject<Item> TITANIUM_SWORD = ITEMS.register("titanium_sword",
            () -> new SwordItem(ModTiers.TITANIUM, 2, 3f, new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> TITANIUM_PICKAXE = ITEMS.register("titanium_pickaxe",
            () -> new PickaxeItem(ModTiers.TITANIUM, 0, 1f, new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> TITANIUM_SHOVEL = ITEMS.register("titanium_shovel",
            () -> new ShovelItem(ModTiers.TITANIUM, 2, 3f, new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> TITANIUM_AXE = ITEMS.register("titanium_axe",
            () -> new AxeItem(ModTiers.TITANIUM, 4, -2f,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> TITANIUM_HOE = ITEMS.register("titanium_hoe",
            () -> new HoeItem(ModTiers.TITANIUM, 2, 3f, new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> RUBIS_SWORD = ITEMS.register("rubis_sword",
            () -> new SwordItem(ModTiers.RUBIS, 2, 3f, new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> RUBIS_PICKAXE = ITEMS.register("rubis_pickaxe",
            () -> new PickaxeItem(ModTiers.RUBIS, 0, 1f, new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> RUBIS_SHOVEL = ITEMS.register("rubis_shovel",
            () -> new ShovelItem(ModTiers.RUBIS, 2, 3f, new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> RUBIS_AXE = ITEMS.register("rubis_axe",
            () -> new AxeItem(ModTiers.RUBIS, 4, -2f,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> RUBIS_HOE = ITEMS.register("rubis_hoe",
            () -> new HoeItem(ModTiers.RUBIS, 2, 3f, new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> LAPIS_SWORD = ITEMS.register("lapis_sword",
            () -> new SwordItem(ModTiers.LAPIS, 2, 3f, new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> LAPIS_PICKAXE = ITEMS.register("lapis_pickaxe",
            () -> new PickaxeItem(ModTiers.LAPIS, 0, 1f, new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> LAPIS_SHOVEL = ITEMS.register("lapis_shovel",
            () -> new ShovelItem(ModTiers.LAPIS, 2, 3f, new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> LAPIS_AXE = ITEMS.register("lapis_axe",
            () -> new AxeItem(ModTiers.LAPIS, 4, -2f,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> LAPIS_HOE = ITEMS.register("lapis_hoe",
            () -> new HoeItem(ModTiers.LAPIS, 2, 3f, new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> DARK_SWORD = ITEMS.register("dark_sword",
            () -> new SwordItem(ModTiers.DARK, 2, 3f, new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> DARK_PICKAXE = ITEMS.register("dark_pickaxe",
            () -> new DarkPickaxeItem(ModTiers.DARK, 0, 1f, new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> DARK_SHOVEL = ITEMS.register("dark_shovel",
            () -> new ShovelItem(ModTiers.DARK, 2, 3f, new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> DARK_AXE = ITEMS.register("dark_axe",
            () -> new AxeItem(ModTiers.DARK, 4, -2f,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> DARK_HOE = ITEMS.register("dark_hoe",
            () -> new HoeItem(ModTiers.DARK, 2, 3f, new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> GOD_SWORD = ITEMS.register("god_sword",
            () -> new GodSwordItem(ModTiers.GOD, 2, 3f, new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> GOD_PICKAXE = ITEMS.register("god_pickaxe",
            () -> new GodPickaxeItem(ModTiers.GOD, 0, 1f, new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> GOD_SHOVEL = ITEMS.register("god_shovel",
            () -> new GodShovelItem(ModTiers.GOD, 2, 3f, new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> GOD_AXE = ITEMS.register("god_axe",
            () -> new GodAxeItem(ModTiers.GOD, 4, -2f,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> GOD_HOE = ITEMS.register("god_hoe",
            () -> new GodHoeItem(ModTiers.GOD, 2, 3f, new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> THOR_HAMMER = ITEMS.register("thor_hammer",
            () -> new ThorHammer(new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));

    /***********************************
     ************** ARMOR **************
     ***********************************/
    public static final RegistryObject<Item> TITANIUM_BOOTS = ITEMS.register("titanium_boots",
            () -> new ArmorItem(ModArmorMaterial.TITANIUM, EquipmentSlot.FEET,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> TITANIUM_LEGGINGS = ITEMS.register("titanium_leggings",
            () -> new ArmorItem(ModArmorMaterial.TITANIUM, EquipmentSlot.LEGS,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> TITANIUM_CHESTPLATE = ITEMS.register("titanium_chestplate",
            () -> new ArmorItem(ModArmorMaterial.TITANIUM, EquipmentSlot.CHEST,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> TITANIUM_HELMET = ITEMS.register("titanium_helmet",
            () -> new ModArmorItem(ModArmorMaterial.TITANIUM, EquipmentSlot.HEAD,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> RUBY_BOOTS = ITEMS.register("ruby_boots",
            () -> new ArmorItem(ModArmorMaterial.RUBY, EquipmentSlot.FEET,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> RUBY_LEGGINGS = ITEMS.register("ruby_leggings",
            () -> new ArmorItem(ModArmorMaterial.RUBY, EquipmentSlot.LEGS,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> RUBY_CHESTPLATE = ITEMS.register("ruby_chestplate",
            () -> new ArmorItem(ModArmorMaterial.RUBY, EquipmentSlot.CHEST,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> RUBY_HELMET = ITEMS.register("ruby_helmet",
            () -> new ModArmorItem(ModArmorMaterial.RUBY, EquipmentSlot.HEAD,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> LAPIS_BOOTS = ITEMS.register("lapis_boots",
            () -> new ArmorItem(ModArmorMaterial.LAPIS, EquipmentSlot.FEET,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> LAPIS_LEGGINGS = ITEMS.register("lapis_leggings",
            () -> new ArmorItem(ModArmorMaterial.LAPIS, EquipmentSlot.LEGS,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> LAPIS_CHESTPLATE = ITEMS.register("lapis_chestplate",
            () -> new ArmorItem(ModArmorMaterial.LAPIS, EquipmentSlot.CHEST,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> LAPIS_HELMET = ITEMS.register("lapis_helmet",
            () -> new ModArmorItem(ModArmorMaterial.LAPIS, EquipmentSlot.HEAD,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> DARK_BOOTS = ITEMS.register("dark_boots",
            () -> new ArmorItem(ModArmorMaterial.DARK, EquipmentSlot.FEET,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> DARK_LEGGINGS = ITEMS.register("dark_leggings",
            () -> new ArmorItem(ModArmorMaterial.DARK, EquipmentSlot.LEGS,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> DARK_CHESTPLATE = ITEMS.register("dark_chestplate",
            () -> new ArmorItem(ModArmorMaterial.DARK, EquipmentSlot.CHEST,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> DARK_HELMET = ITEMS.register("dark_helmet",
            () -> new ModArmorItem(ModArmorMaterial.DARK, EquipmentSlot.HEAD,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> GOD_BOOTS = ITEMS.register("god_boots",
            () -> new ModArmorItem(ModArmorMaterial.GOD, EquipmentSlot.FEET,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> GOD_LEGGINGS = ITEMS.register("god_leggings",
            () -> new ArmorItem(ModArmorMaterial.GOD, EquipmentSlot.LEGS,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> GOD_CHESTPLATE = ITEMS.register("god_chestplate",
            () -> new ArmorItem(ModArmorMaterial.GOD, EquipmentSlot.CHEST,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> GOD_HELMET = ITEMS.register("god_helmet",
            () -> new ArmorItem(ModArmorMaterial.GOD, EquipmentSlot.HEAD,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> ICE_BOOTS = ITEMS.register("ice_boots",
            () -> new SpecialBootsArmor(ModArmorMaterial.ICE, EquipmentSlot.FEET,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));
    public static final RegistryObject<Item> FIRE_BOOTS = ITEMS.register("lava_boots",
            () -> new SpecialBootsArmor(ModArmorMaterial.LAVA, EquipmentSlot.FEET,
                    new Item.Properties().tab(ModCreativeTabs.TOOLS_TABS)));

    /***********************************************
     ******************* CROPS *********************
     ***********************************************/
    public static final RegistryObject<Item> TOMATO = ITEMS.register("tomato",
            () -> new Item(new Item.Properties().tab(ModCreativeTabs.FOODS_TABS)
                    .food(new FoodProperties.Builder().nutrition(2).saturationMod(0.2f).build())));

    public static final RegistryObject<Item> TOMATO_SEEDS = ITEMS.register("tomato_seeds",
            () -> new ItemNameBlockItem(ModBlocks.TOMATO_PLANT.get(),
                    new Item.Properties().tab(ModCreativeTabs.MODS_TABS)));

    /***********************************************
     ******************* FINDER ********************
     ***********************************************/
    public static final RegistryObject<Item> CHUNK_FINDER = ITEMS.register("chunk_finder",
            () -> new ChunkFinder(new Item.Properties().tab(ModCreativeTabs.MODS_TABS).stacksTo(1)));

    /***********************************************
     *************** BUCKET FLUID ******************
     ***********************************************/
    public static final RegistryObject<Item> STYX_BUCKET = ITEMS.register("styx_bucket",
            () -> new BucketItem(ModFluids.STYX, new Item.Properties().tab(ModCreativeTabs.MODS_TABS).stacksTo(1)));

    /***********************************************
     *************** ITEM FOR DRINK ****************
     ***********************************************/
    public static final RegistryObject<Item> WATER_BOTTLE = ITEMS.register("water_bottle",
            () -> new DrinkItem(8));
    public static final RegistryObject<Item> FILTER_WATER = ITEMS.register("filter_water",
            () -> new FilterItem(100));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
