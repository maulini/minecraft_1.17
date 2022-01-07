package net.dofmine.minedofmod.block.custom;

import net.dofmine.minedofmod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class FlowerCustomBlock extends FlowerBlock {

    public FlowerCustomBlock(MobEffect p_53512_, int p_53513_, Properties p_53514_) {
        super(p_53512_, p_53513_, p_53514_);
    }

    @Override
    protected boolean mayPlaceOn(BlockState p_51042_, BlockGetter p_51043_, BlockPos p_51044_) {
        return p_51042_.is(ModBlocks.DIRT_DARK.get()) || p_51042_.is(ModBlocks.GRASS_DARK.get());
    }
}
