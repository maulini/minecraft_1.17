package net.dofmine.minedofmod.tileentity;

import net.dofmine.minedofmod.MinedofMod;
import net.dofmine.minedofmod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntity {

    public static DeferredRegister<BlockEntityType<?>> BLOCK_TILE_ENTITY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MinedofMod.MODS_ID);

    public static RegistryObject<BlockEntityType<LightningChannelerTile>> LIGHTNING_CHANNELER_TILE = BLOCK_TILE_ENTITY.register("lightning_channeler", () -> BlockEntityType.Builder.of(
            LightningChannelerTile::new, ModBlocks.LIGHTNING_CHANNELER.get()).build(null));

    public static RegistryObject<BlockEntityType<CraftingTableTile>> CRAFTING_TABLE_TILE = BLOCK_TILE_ENTITY.register("crafting_table", () -> BlockEntityType.Builder.of(
            CraftingTableTile::new, ModBlocks.CRAFTING_TABLE.get()).build(null));

    public static void register(IEventBus event) {
        BLOCK_TILE_ENTITY.register(event);
    }

}
