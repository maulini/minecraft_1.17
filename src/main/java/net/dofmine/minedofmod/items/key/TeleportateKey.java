package net.dofmine.minedofmod.items.key;

import net.dofmine.minedofmod.block.ModBlocks;
import net.dofmine.minedofmod.block.custom.SpecialDoorTeleportate;
import net.dofmine.minedofmod.screen.TeleportateScreen;
import net.dofmine.minedofmod.tabs.ModCreativeTabs;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class TeleportateKey extends Item {

    public static final String XPOS = "X Position";
    public static final String YPOS = "Y Position";
    public static final String ZPOS = "Z Position";
    public static final String DOOR = "Door";
    public String dimension;

    public TeleportateKey() {
        super(new Properties().stacksTo(1).tab(ModCreativeTabs.MODS_TABS));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        ItemStack itemStack = context.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);
        BlockState blockState = level.getBlockState(context.getClickedPos());
        if (blockState.is(ModBlocks.SPECIAL_DOOR.get())) {
            if (dimension == null || !dimension.equalsIgnoreCase(level.dimensionType().effectsLocation().getPath())) {
                dimension = level.dimensionType().effectsLocation().getPath();
            }
            if (level.isClientSide) {
                if (itemStack.hasTag()) {
                    ListTag doors = itemStack.getTag().getList(dimension, Constants.NBT.TAG_COMPOUND);
                    if (!havePosition(doors, context.getClickedPos())) {
                        addDoorTeleportate(itemStack, context.getClickedPos());
                    }
                }else {
                    addDoorTeleportate(itemStack, context.getClickedPos());
                }
                Minecraft.getInstance().setScreen(new TeleportateScreen(itemStack, context.getPlayer(), level));
            }
            ((SpecialDoorTeleportate) blockState.getBlock()).openOrCloseDoor(blockState, level, context.getClickedPos(), context.getPlayer());
        }
        return super.useOn(context);
    }

    private boolean havePosition(ListTag doors, BlockPos clickedPos) {
        for (int i = 0; i < doors.size(); i++) {
            CompoundTag position = doors.getCompound(i).getCompound(DOOR+i);
            if (new BlockPos(position.getInt(XPOS), position.getInt(YPOS), position.getInt(ZPOS)).equals(clickedPos) || new BlockPos(position.getInt(XPOS), position.getInt(YPOS) - 1, position.getInt(ZPOS)).equals(clickedPos) || new BlockPos(position.getInt(XPOS), position.getInt(YPOS) + 1, position.getInt(ZPOS)).equals(clickedPos)) {
                return true;
            }
        }

        return false;
    }

    public void addDoorTeleportate(ItemStack teleportateKey, BlockPos blockPos) {
        if (!teleportateKey.hasTag()) {
            teleportateKey.getOrCreateTag().put(dimension, new ListTag());
        }
        ListTag doors = teleportateKey.getTag().getList(dimension, Constants.NBT.TAG_COMPOUND);
        CompoundTag door = new CompoundTag();
        CompoundTag position = new CompoundTag();
        position.putInt(XPOS, blockPos.getX());
        position.putInt(YPOS, blockPos.getY());
        position.putInt(ZPOS, blockPos.getZ());
        door.put(DOOR + doors.size(), position);
        doors.add(door);
        teleportateKey.getOrCreateTag().put(dimension, doors);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public List<BlockPos> getBlockPositions(ItemStack itemStack) {
        List<BlockPos> blockPos = new ArrayList<>();
        if (itemStack.hasTag()) {
            ListTag doors = itemStack.getTag().getList(dimension, Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < doors.size(); i++) {
                CompoundTag position = doors.getCompound(i).getCompound(DOOR+i);
                blockPos.add(new BlockPos(position.getInt(XPOS), position.getInt(YPOS), position.getInt(ZPOS)));
            }
        }
        return blockPos;
    }

}
