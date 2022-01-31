package net.dofmine.minedofmod.items.key;

import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.tabs.ModCreativeTabs;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.concurrent.ConcurrentLinkedQueue;

public class KeyItem extends Item implements ICurioItem {

    private final Block changeBlock;

    private final ConcurrentLinkedQueue<BlockPos> rectractWaterBlock;
    private Block blockProtect;

    public KeyItem(Block changedBlock) {
        super(new Properties().stacksTo(1).tab(ModCreativeTabs.MODS_TABS));
        rectractWaterBlock = new ConcurrentLinkedQueue<>();
        this.changeBlock = changedBlock;
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide) {
            Level level = pContext.getLevel();
            BlockPos blockPos = pContext.getClickedPos();
            Block blockClicked = level.getBlockState(blockPos).getBlock();

            if (!blockClicked.equals(changeBlock)) {
                level.setBlockAndUpdate(blockPos, changeBlock.defaultBlockState());
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        if (ModItems.RED_KEY.get().equals(this)){
            blockProtect = Blocks.LAVA;
        }else {
            blockProtect = Blocks.WATER;
        }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        Level level = slotContext.entity().level;
        for (BlockPos blockPos: rectractWaterBlock) {
            level.setBlock(blockPos, blockProtect.defaultBlockState(), 512);
            rectractWaterBlock.remove(blockPos);
        }
        for (double y = entity.getY() - 1; y < entity.getY() + 2; y++) {
            for (double x = entity.getX() - 1; x < entity.getX() + 2; x++) {
                for (double z = entity.getZ() - 1; z < entity.getZ() + 2; z++) {
                    BlockPos blockPos = new BlockPos(x, y, z);
                    Block block = level.getBlockState(blockPos).getBlock();
                    if (block.equals(blockProtect)) {
                        rectractWaterBlock.add(blockPos);
                        level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 0);
                    }
                }
            }
        }
        ICurioItem.super.curioTick(slotContext, stack);
    }
}
