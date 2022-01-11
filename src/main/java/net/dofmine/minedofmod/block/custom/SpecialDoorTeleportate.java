package net.dofmine.minedofmod.block.custom;

import com.google.common.util.concurrent.AtomicDouble;
import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.items.key.TeleportateKey;
import net.dofmine.minedofmod.screen.TeleportateScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookEditScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
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
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.items.CapabilityItemHandler;

import java.io.Serializable;

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
