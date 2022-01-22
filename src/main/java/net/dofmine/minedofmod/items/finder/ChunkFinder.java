package net.dofmine.minedofmod.items.finder;

import net.dofmine.minedofmod.screen.ChunkFinderScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ChunkFinder extends Item {

    public ChunkFinder(Item.Properties tab) {
        super(tab);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (level.isClientSide) {
            ChunkPos chunkPos = level.getChunk(player.blockPosition()).getPos();
            int playerYPos = player.getBlockY();
            Map<ResourceLocation, Integer> itemInChunk = new HashMap<>();
            for (int y = 0; y < playerYPos; y++) {
                for (int x = chunkPos.getMinBlockX(); x < chunkPos.getMaxBlockX(); x++) {
                    for (int z = chunkPos.getMinBlockZ(); z < chunkPos.getMaxBlockZ(); z++) {
                        BlockState blockState = level.getBlockState(new BlockPos(x, y, z));
                        List<BakedQuad> backedModelList = Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState).getQuads(blockState, Direction.SOUTH, new Random());
                        if (!backedModelList.isEmpty()) {
                            String nameSpace = "textures/" + backedModelList.get(0).getSprite().getName().getPath() + ".png";
                            ResourceLocation resourceLocation = new ResourceLocation(backedModelList.get(0).getSprite().getName().getNamespace(), nameSpace);
                            if (!itemInChunk.containsKey(resourceLocation)) {
                                itemInChunk.put(resourceLocation, 1);
                            }else {
                                itemInChunk.put(resourceLocation, itemInChunk.get(resourceLocation) + 1);
                            }
                        }
                    }
                }
            }
            openScreen(itemInChunk);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.fail(player.getItemInHand(hand));
    }

    @OnlyIn(Dist.CLIENT)
    private void openScreen(Map<ResourceLocation, Integer> itemInChunk) {
        Minecraft.getInstance().setScreen(new ChunkFinderScreen(itemInChunk));
    }
}
