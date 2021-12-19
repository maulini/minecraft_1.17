package net.dofmine.minedofmod.items.tools;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeTier;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class GodHoeItem extends HoeItem {

    public GodHoeItem(Tier tier, int p_41337_, float p_41338_, Properties properties) {
        super(tier, p_41337_, p_41338_, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        for (int x = -1; x < 1; x++) {
            for (int z = -1; z < 1; z++) {
                BlockPos blockpos = new BlockPos(context.getClickedPos().getX() + x, context.getClickedPos().getY(), context.getClickedPos().getZ() + z);
                if (TILLABLES.containsKey(level.getBlockState(blockpos).getBlock())) {
                    context.getItemInHand().hurtAndBreak(1, context.getPlayer(), p -> {
                        p.broadcastBreakEvent(context.getHand());
                    });
                    level.setBlockAndUpdate(blockpos, Blocks.FARMLAND.defaultBlockState());
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
