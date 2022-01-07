package net.dofmine.minedofmod.block.custom;

import net.dofmine.minedofmod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;

public class SaplingCustomBlock extends SaplingBlock {

    public SaplingCustomBlock(AbstractTreeGrower p_55978_, Properties p_55979_) {
        super(p_55978_, p_55979_);
    }

    @Override
    protected boolean mayPlaceOn(BlockState p_51042_, BlockGetter p_51043_, BlockPos p_51044_) {
        return p_51042_.is(ModBlocks.DIRT_DARK.get()) || p_51042_.is(ModBlocks.GRASS_DARK.get());
    }
}
