package net.dofmine.minedofmod.world.features.tree;

import com.google.common.collect.ImmutableList;
import net.dofmine.minedofmod.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class StraightTrunkPlacerCustom extends StraightTrunkPlacer {

    public StraightTrunkPlacerCustom(int p_70248_, int p_70249_, int p_70250_) {
        super(p_70248_, p_70249_, p_70250_);
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader p_161859_, BiConsumer<BlockPos, BlockState> p_161860_, Random p_161861_, int p_161862_, BlockPos p_161863_, TreeConfiguration p_161864_) {
        setCustomDirtAt(p_161859_, p_161860_, p_161861_, p_161863_.below(), p_161864_);

        for(int i = 0; i < p_161862_; ++i) {
            placeLog(p_161859_, p_161860_, p_161861_, p_161863_.above(i), p_161864_);
        }

        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(p_161863_.above(p_161862_), 0, false));
    }

    private void setCustomDirtAt(LevelSimulatedReader p_161859_, BiConsumer<BlockPos, BlockState> p_161860_, Random p_161861_, BlockPos below, TreeConfiguration p_161864_) {
        if (p_161864_.forceDirt || !isCustomDirt(p_161859_, below)) {
            p_161860_.accept(below, p_161864_.dirtProvider.getState(p_161861_, below));
        }
    }

    private boolean isCustomDirt(LevelSimulatedReader p_161859_, BlockPos below) {
        return p_161859_.isStateAtPosition(below, (p_70304_) -> {
            return !p_70304_.is(ModBlocks.GRASS_DARK.get()) && !p_70304_.is(ModBlocks.DIRT_DARK.get());
        });
    }


}
