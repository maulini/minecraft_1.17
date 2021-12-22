package net.dofmine.minedofmod.items.tools;

import net.dofmine.minedofmod.job.ExtendedEntityPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;

public class DarkPickaxeItem extends PickaxeItem {

    public DarkPickaxeItem(Tier tier, int p_42962_, float p_42963_, Properties properties) {
        super(tier, p_42962_, p_42963_, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        BlockPos blockPos = context.getClickedPos();
        if (player.level.getBlockState(blockPos).getBlock().equals(Blocks.BEDROCK)) {
            context.getItemInHand().hurtAndBreak(1, context.getPlayer(), p -> {
                p.broadcastBreakEvent(context.getHand());
            });
            player.level.getBlockState(blockPos).canHarvestBlock(player.level, blockPos, player);
            player.level.addFreshEntity(new ItemEntity(player.level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), new ItemStack(player.level.getBlockState(blockPos).getBlock().asItem(), 1)));
            player.level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
        }
        return super.useOn(context);
    }
}
