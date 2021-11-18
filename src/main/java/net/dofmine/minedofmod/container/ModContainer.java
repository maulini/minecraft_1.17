package net.dofmine.minedofmod.container;

import net.dofmine.minedofmod.MinedofMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainer {

    public static DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MinedofMod.MODS_ID);

    public static final RegistryObject<MenuType<LightningChannelerContainer>> LIGHTNING_CHANNELER_CONTAINER = CONTAINERS.register("lightning_channeler", () ->
        IForgeContainerType.create(((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            Level level = inv.player.level;
            return new LightningChannelerContainer(windowId, level, pos, inv.player, inv);
        })));

    public static final RegistryObject<MenuType<CraftingTableContainer>> CRAFTING_TABLE_CONTAINER = CONTAINERS.register("crafting_table", () ->
        IForgeContainerType.create(((windowId, inv, data) -> {
            BlockPos pos = data.readBlockPos();
            Level level = inv.player.level;
            return new CraftingTableContainer(windowId, level, pos, inv.player, inv);
        })));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }

}
