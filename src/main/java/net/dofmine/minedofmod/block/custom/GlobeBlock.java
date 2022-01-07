package net.dofmine.minedofmod.block.custom;

import com.google.common.collect.Lists;
import net.dofmine.minedofmod.block.ModBlocks;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Arrays;
import java.util.List;

public class GlobeBlock extends Block {
    private final VoxelShape SHAPE = makeShape();

    public GlobeBlock() {
        super(Properties.copy(Blocks.ACACIA_WOOD));
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!level.isClientSide) {
            ItemStack itemStack = Items.FIREWORK_ROCKET.getDefaultInstance();
            CompoundTag compoundtag = itemStack.getOrCreateTagElement("Fireworks");
            CompoundTag compoundtag2 = new CompoundTag();
            CompoundTag compoundtag3 = new CompoundTag();
            ListTag listtag = new ListTag();
            compoundtag2.putIntArray("Colors", Arrays.asList(DyeColor.LIGHT_BLUE.getFireworkColor(), DyeColor.LIME.getFireworkColor()));
            compoundtag2.putBoolean("Flicker", true);

            compoundtag3.put("Explosion", compoundtag2);
            listtag.add(compoundtag3);
            compoundtag.putByte("Flight", (byte)3);
            compoundtag.put("Explosions", listtag);
            FireworkRocketEntity rocket = new FireworkRocketEntity(level, blockPos.getX(), blockPos.getY() + 0.5F, blockPos.getZ(), itemStack);
            level.addFreshEntity(rocket);

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

    private VoxelShape makeShape(){
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.03125, 0.8125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.34375, 0.4330582617584078, 0.25, 0.65625, 0.6919417382415922, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.34375, 0.4330582617584078, 0.25, 0.65625, 0.6919417382415922, 0.875), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.34375, 0.25, 0.4330582617584078, 0.65625, 0.875, 0.6919417382415922), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.34375, 0.25, 0.4330582617584078, 0.65625, 0.875, 0.6919417382415922), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.46875, 0.03125, 0.5, 0.53125, 0.28125, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.46875, 0.25, -0.125, 0.53125, 0.3125, 0.4375), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.46875, 0.25, -0.1875, 0.53125, 1.375, -0.125), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.46875, 0.625, 0.8125, 0.53125, 1.0625, 0.875), BooleanOp.OR);

        return shape;
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }
}
