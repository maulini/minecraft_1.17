package net.dofmine.minedofmod.block.custom;

import net.dofmine.minedofmod.items.ModItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;

public class TomatoPlantBlock extends CropBlock {
    public TomatoPlantBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.TOMATO_SEEDS.get();
    }
}
