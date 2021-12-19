package net.dofmine.minedofmod.items.tools;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeTier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GodShovelItem extends ShovelItem {

    private final List<Block> DIRT_COMPATIBLE =
            new ImmutableList.Builder<Block>()
                    .addAll(Stream.of(Blocks.class.getFields()).filter(field -> field.getName().contains("DIRT") || field.getName().contains("GRASS")).map(field -> {
                        try {
                            return (Block)field.get(field);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }).collect(Collectors.toList()))
                    .add(Blocks.GRAVEL)
                    .add(Blocks.CLAY)
                    .build();

    public GodShovelItem(Tier tier, float p_43115_, float p_43116_, Properties properties) {
        super(tier, p_43115_, p_43116_, properties);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        for (int x1 = -2; x1 < 3; x1++) {
            for (int y1 = -2; y1 < 3; y1++) {
                for (int z1 = -2; z1 < 3; z1++) {
                    BlockPos blockPos = new BlockPos(x + x1, y + y1, z + z1);
                    if (DIRT_COMPATIBLE.contains(player.level.getBlockState(blockPos).getBlock())) {
                        itemstack.hurtAndBreak(1, player, p -> {
                            p.broadcastBreakEvent(InteractionHand.MAIN_HAND);
                        });
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
