package net.dofmine.minedofmod.items.weapon;

import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.items.ModTiers;
import net.dofmine.minedofmod.job.ExtendedEntityPlayer;
import net.minecraft.client.model.TridentModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.TridentLoyaltyEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class ThorHammer extends TridentItem {

    public ThorHammer(Properties p_43381_) {
        super(p_43381_);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos firstBlock = context.getClickedPos();
        BlockPos rightBlock = new BlockPos(firstBlock.getX() + 1, firstBlock.getY(), firstBlock.getZ());
        BlockPos leftBlock = new BlockPos(firstBlock.getX() - 1, firstBlock.getY(), firstBlock.getZ());
        BlockPos torchBlockLeft = new BlockPos(leftBlock.getX(), leftBlock.getY() + 1, leftBlock.getZ());
        BlockPos torchBlockRight = new BlockPos(rightBlock.getX(), rightBlock.getY() + 1, rightBlock.getZ());
        BlockPos rightBlockZ = new BlockPos(firstBlock.getX(), firstBlock.getY(), firstBlock.getZ() + 1);
        BlockPos leftBlockZ = new BlockPos(firstBlock.getX(), firstBlock.getY(), firstBlock.getZ() - 1);
        BlockPos torchBlockLeftZ = new BlockPos(leftBlockZ.getX(), leftBlockZ.getY() + 1, leftBlockZ.getZ());
        BlockPos torchBlockRightZ = new BlockPos(rightBlockZ.getX(), rightBlockZ.getY() + 1, rightBlockZ.getZ());
        if (level.getBlockState(firstBlock).getBlock().equals(Blocks.QUARTZ_BLOCK)
        && level.getBlockState(rightBlock).getBlock().equals(Blocks.QUARTZ_BLOCK) && level.getBlockState(leftBlock).getBlock().equals(Blocks.QUARTZ_BLOCK)
        && level.getBlockState(torchBlockLeft).getBlock().equals(Blocks.TORCH) && level.getBlockState(torchBlockRight).getBlock().equals(Blocks.TORCH)) {
            ItemEntity itemGod = new ItemEntity(level, firstBlock.getX(), firstBlock.getY(), firstBlock.getZ(), new ItemStack(ModItems.GOD_INGOT.get(), 1));
            ExtendedEntityPlayer.get().addMana(10);
            level.addFreshEntity(itemGod);
            context.getPlayer().getInventory().removeItem(context.getItemInHand());
        } else if (level.getBlockState(firstBlock).getBlock().equals(Blocks.QUARTZ_BLOCK)
                && level.getBlockState(rightBlockZ).getBlock().equals(Blocks.QUARTZ_BLOCK) && level.getBlockState(leftBlockZ).getBlock().equals(Blocks.QUARTZ_BLOCK)
                && level.getBlockState(torchBlockLeftZ).getBlock().equals(Blocks.TORCH) && level.getBlockState(torchBlockRightZ).getBlock().equals(Blocks.TORCH)) {
            ItemEntity itemGod = new ItemEntity(level, firstBlock.getX(), firstBlock.getY(), firstBlock.getZ(), new ItemStack(ModItems.GOD_INGOT.get(), 1));
            ExtendedEntityPlayer.get().addMana(10);
            level.addFreshEntity(itemGod);
            context.getPlayer().getInventory().removeItem(context.getItemInHand());
        }
        return super.useOn(context);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if (!itemStack.isEnchanted()) {
            itemStack.enchant(Enchantments.LOYALTY, 3);
        }
        super.inventoryTick(itemStack, level, entity, p_41407_, p_41408_);
    }
}
