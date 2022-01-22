package net.dofmine.minedofmod.data;

import net.dofmine.minedofmod.block.ModBlocks;
import net.dofmine.minedofmod.block.custom.TomatoPlantBlock;
import net.dofmine.minedofmod.items.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockLootTables extends BlockLoot {

    @Override
    protected void addTables() {
        this.add(ModBlocks.TITANIUM_ORE.get(), (block) -> {
            return createOreDrop(ModBlocks.TITANIUM_ORE.get(), ModItems.RAW_TITANIUM.get());
        });
        this.add(ModBlocks.DARK_ORE.get(), (block) -> {
            return createOreDrop(ModBlocks.DARK_ORE.get(), ModBlocks.DARK_ORE.get().asItem());
        });
        this.add(ModBlocks.LAPIS_ORE.get(), (block) -> {
            return createOreDrop(ModBlocks.LAPIS_ORE.get(), ModItems.LAPIS_INGOT.get());
        });
        this.add(ModBlocks.GOD_ORE.get(), (block) -> {
            return createOreDrop(ModBlocks.GOD_ORE.get(), ModBlocks.GOD_ORE.get().asItem());
        });
        this.add(ModBlocks.RUBY_ORE.get(), (block) -> {
            return createOreDrop(ModBlocks.GOD_ORE.get(), ModBlocks.GOD_ORE.get().asItem());
        });
        this.add(Blocks.GRASS, block -> {
           return createShearsDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(ModItems.TOMATO_SEEDS.get()).when(LootItemRandomChanceCondition.randomChance(0.125F)).apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 2))));
        });

        this.dropSelf(ModBlocks.LIGHTNING_CHANNELER.get());
        this.dropSelf(ModBlocks.CRAFTING_TABLE.get());
        this.dropSelf(ModBlocks.WATER_COLLECTOR.get());
        this.dropSelf(ModBlocks.ELEVATOR_BLOCK.get());
        this.dropSelf(ModBlocks.SPECIAL_DOOR.get());
        this.dropSelf(ModBlocks.GLOBE.get());
        this.dropSelf(ModBlocks.ORCHID.get());
        this.dropSelf(ModBlocks.REDWOOD_LOG.get());
        this.dropSelf(ModBlocks.REDWOOD_PLANKS.get());
        this.dropSelf(ModBlocks.REDWOOD_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_REDWOOD_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_REDWOOD_WOOD.get());
        this.dropSelf(ModBlocks.REDWOOD_SAPLING.get());
        this.add(ModBlocks.TELEPORTER_BLOCK.get(), noDrop());
        this.dropSelf(ModBlocks.TITANIUM_BLOCK.get());
        this.dropSelf(ModBlocks.DARK_BLOCK.get());
        this.dropSelf(ModBlocks.RUBY_BLOCK.get());
        this.dropSelf(ModBlocks.LAPIS_BLOCK.get());

        this.dropSelf(ModBlocks.COBBLE_DARK.get());
        this.add(ModBlocks.STONE_DARK.get(), (p_124195_) -> {
            return createSingleItemTableWithSilkTouch(p_124195_, ModBlocks.COBBLE_DARK.get());
        });
        this.dropSelf(ModBlocks.DIRT_DARK.get());
        this.add(ModBlocks.GRASS_DARK.get(), (p_124195_) -> {
            return createSingleItemTableWithSilkTouch(p_124195_, ModBlocks.DIRT_DARK.get());
        });

        LootItemCondition.Builder tomatoLootConditionBuilder = LootItemBlockStatePropertyCondition.
                hasBlockStateProperties(ModBlocks.TOMATO_PLANT.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(TomatoPlantBlock.AGE, 7));

        this.add(ModBlocks.TOMATO_PLANT.get(), createCropDrops(ModBlocks.TOMATO_PLANT.get(),
                ModItems.TOMATO.get(), ModItems.TOMATO_SEEDS.get(), tomatoLootConditionBuilder));
        this.add(ModBlocks.REDWOOD_LEAVES.get(), (p_124110_) -> {
            return createLeavesDrops(p_124110_, ModBlocks.REDWOOD_SAPLING.get(), 0.05F, 0.0625F, 0.083333336F, 0.1F);
        });
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
