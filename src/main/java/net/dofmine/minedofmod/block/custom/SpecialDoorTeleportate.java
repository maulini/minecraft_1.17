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

    private  BlockPos blockPos;

    public SpecialDoorTeleportate() {
        super(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.DIAMOND).strength(5.0F).sound(SoundType.METAL).noOcclusion());
    }

    public SpecialDoorTeleportate(BlockPos blockPos) {
        super(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.DIAMOND).strength(5.0F).sound(SoundType.METAL).noOcclusion());
        this.blockPos = blockPos;
    }

    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        this.blockPos = blockPos;
        if (player.getItemInHand(hand).getItem() instanceof  TeleportateKey teleportateKey) {
            teleportateKey.resolveTeleportKeyComponents(player.getItemInHand(hand), player.createCommandSourceStack(), player);
            if (!isOpen(blockState)) {
                if (level.isClientSide) {
                    try {
                        SpecialDoorTeleportate clone = (SpecialDoorTeleportate) this.clone();
                        if (!teleportateKey.isPresent(clone)) {
                            teleportateKey.addDoorTeleportate(clone);
                        }
                        Minecraft.getInstance().setScreen(new TeleportateScreen(teleportateKey, player, level));
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
                blockState = blockState.cycle(OPEN);
                level.setBlock(blockPos, blockState, 10);
                level.levelEvent(player, this.getOpenSound(), blockPos, 0);
                level.gameEvent(player, GameEvent.BLOCK_OPEN, blockPos);
            }else {
                blockState = blockState.cycle(OPEN);
                level.setBlock(blockPos, blockState, 10);
                level.levelEvent(player, this.getCloseSound(), blockPos, 0);
                level.gameEvent(player, GameEvent.BLOCK_CLOSE, blockPos);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private int getCloseSound() {
        return 1011;
    }

    private int getOpenSound() {
        return 1005;
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

}
