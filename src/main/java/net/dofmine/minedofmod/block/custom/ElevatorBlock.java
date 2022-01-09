package net.dofmine.minedofmod.block.custom;

import net.dofmine.minedofmod.block.ModBlocks;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ElevatorBlock extends Block {

    private final int MAX_DISTANCE = 6;

    public ElevatorBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public void upTeleport(Player player) {
        for (int y = 0; y < MAX_DISTANCE; y++) {
            if (player.level.getBlockState(player.blockPosition().atY(player.blockPosition().getY() + y)).is(ModBlocks.ELEVATOR_BLOCK.get())) {
                player.teleportTo(player.position().x, player.position().y + y + 1, player.position().z);
                return;
            }
        }
    }

    public void downTeleport(Player player) {
        for (int y = 1; y < MAX_DISTANCE + 1; y++) {
            if (player.level.getBlockState(player.blockPosition().atY(player.blockPosition().getY() - y - 1)).is(ModBlocks.ELEVATOR_BLOCK.get())) {
                player.teleportTo(player.position().x, player.position().y - y, player.position().z);
                return;
            }
        }
    }

}
