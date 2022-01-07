package net.dofmine.minedofmod.items.finder;

import net.dofmine.minedofmod.block.ModBlocks;
import net.dofmine.minedofmod.job.ExtendedHunterJobsEntityPlayer;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChunkFinder extends Item {

    public ChunkFinder(Item.Properties tab) {
        super(tab);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide) {
            ChunkPos chunkPos = level.getChunk(player.blockPosition()).getPos();
            int playerYPos = player.getBlockY();
            Map<Block, Integer> nbBlockMap = new HashMap<>();
            for (int y = 0; y < playerYPos; y++) {
                for (int x = chunkPos.getMinBlockX(); x < chunkPos.getMaxBlockX(); x++) {
                    for (int z = chunkPos.getMinBlockZ(); z < chunkPos.getMaxBlockZ(); z++) {
                        BlockState blockState = level.getBlockState(new BlockPos(x, y, z));
                        if (blockState.is(ModBlocks.RUBY_ORE.get()) || blockState.is(ModBlocks.LAPIS_ORE.get()) || blockState.is(ModBlocks.GOD_ORE.get()) || blockState.is(ModBlocks.DARK_ORE.get())) {
                            if (nbBlockMap.containsKey(blockState.getBlock())) {
                                nbBlockMap.put(blockState.getBlock(), nbBlockMap.get(blockState.getBlock()) + 1);
                            }else {
                                nbBlockMap.put(blockState.getBlock(), 1);
                            }
                        }
                    }
                }
            }
            nbBlockMap.forEach(((block, integer) -> player.sendMessage(new TextComponent("Il y a " + integer + " de " + block), UUID.randomUUID())));
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.fail(player.getItemInHand(hand));
    }
}
