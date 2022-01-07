package net.dofmine.minedofmod.world.gen;

import net.dofmine.minedofmod.block.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.Lazy;

public enum OreType {
    TITANIUM_ORE(Lazy.of(ModBlocks.TITANIUM_ORE), 8, 25, 50, 3),
    TITANIUM_BLOCK(Lazy.of(ModBlocks.TITANIUM_BLOCK), 3, 10, 80, 5),
    DARK_ORE(Lazy.of(ModBlocks.DARK_ORE), 3, 10, 15, 3),
    GODS_ORE(Lazy.of(ModBlocks.GOD_ORE), 2, 0, 9, 3),
    RUBY_ORE(Lazy.of(ModBlocks.RUBY_ORE), 7, 20, 40, 3),
    LAPIS_ORE(Lazy.of(ModBlocks.LAPIS_ORE), 8, 30, 45, 3);

    private final Lazy<Block> block;
    private final int maxVeinSize;
    private final int minHeight;
    private final int maxHeight;
    private final int veinsPerChunk;

    OreType(Lazy<Block> block, int maxVeinSize, int minHeight, int maxHeight, int veinsPerChunk) {
        this.block = block;
        this.maxVeinSize = maxVeinSize;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.veinsPerChunk = veinsPerChunk;
    }

    public Lazy<Block> getBlock() {
        return block;
    }

    public int getMaxVeinSize() {
        return maxVeinSize;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getVeinsPerChunk() {
        return veinsPerChunk;
    }

    public static OreType get(Block block) {
        for (OreType ore : values()) {
            if(block == ore.block) {
                return ore;
            }
        }
        return null;
    }
}
