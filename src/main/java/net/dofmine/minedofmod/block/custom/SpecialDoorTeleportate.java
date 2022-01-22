package net.dofmine.minedofmod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.BlockHitResult;

public class SpecialDoorTeleportate extends DoorBlock implements Cloneable {

    public SpecialDoorTeleportate() {
        super(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.DIAMOND).strength(5.0F).sound(SoundType.METAL).noOcclusion());
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        return InteractionResult.PASS;
    }

    public void openOrCloseDoor(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        blockState = blockState.cycle(OPEN);
        level.setBlock(blockPos, blockState, 10);
        level.levelEvent(player, blockState.getValue(OPEN) ? 1005 : 1011, blockPos, 0);
        level.gameEvent(player, this.isOpen(blockState) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, blockPos);
    }
}
