package net.dofmine.minedofmod.items.tools;

import com.google.common.collect.ImmutableList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeTier;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GodAxeItem extends AxeItem {
    private final List<Block> WOOD_COMPATIBLE =
            new ImmutableList.Builder<Block>()
                    .addAll(Stream.of(Blocks.class.getFields()).filter(field -> field.getName().contains("_LOG") || field.getName().contains("_WOOD")).map(field -> {
                        try {
                            return (Block)field.get(field);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }).collect(Collectors.toList()))
            .build();

    public GodAxeItem(Tier tier, float p_40522_, float p_40523_, Properties properties) {
        super(tier, p_40522_, p_40523_, properties);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        for (int x1 = 0; x1 < 5; x1++) {
            for (int y1 = 0; y1 < 10; y1++) {
                for (int z1 = 0; z1 < 5; z1++) {
                    BlockPos blockPos = new BlockPos(x, y + y1, z);
                    if (WOOD_COMPATIBLE.contains(player.level.getBlockState(blockPos).getBlock())) {
                        itemstack.hurtAndBreak(1, player, p -> {
                            p.broadcastBreakEvent(InteractionHand.MAIN_HAND);
                        });
                        itemstack.setDamageValue(itemstack.getDamageValue() - 1);
                        player.level.getBlockState(blockPos).canHarvestBlock(player.level, blockPos, player);
                        player.level.addFreshEntity(new ItemEntity(player.level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(player.level.getBlockState(blockPos).getBlock().asItem(), 1)));
                        player.level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
        return super.onBlockStartBreak(itemstack, pos, player);
    }

}
