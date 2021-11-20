package net.dofmine.minedofmod.block.custom;

import net.dofmine.minedofmod.container.CraftingTableContainer;
import net.dofmine.minedofmod.tileentity.CraftingTableTile;
import net.dofmine.minedofmod.tileentity.LightningChannelerTile;
import net.dofmine.minedofmod.tileentity.ModTileEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fmllegacy.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.Random;

public class CraftingTableBlock extends BaseEntityBlock {

    public CraftingTableBlock(Properties p_49224_) {
        super(p_49224_);
    }

    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide) {
            BlockEntity entity = level.getBlockEntity(blockPos);
            if (entity instanceof CraftingTableTile) {
                MenuProvider menuProvider = createMenuProvider(blockPos);
                NetworkHooks.openGui(((ServerPlayer) player), menuProvider, entity.getBlockPos());
            } else {
                throw new IllegalStateException("Problème chef !");
            }
        }
        return InteractionResult.SUCCESS;
    }

    private MenuProvider createMenuProvider(BlockPos blockPos) {
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return new TranslatableComponent("screen.mods.crafting_table");
            }

            @Nullable
            @Override
            public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
                return new CraftingTableContainer(i, player.level, blockPos, player, inventory);
            }
        };
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return ModTileEntity.CRAFTING_TABLE_TILE.get().create(p_153215_, p_153216_);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) {
            return null;
        }
        return (level1, blockPos, blockState, t) -> {
            if (t instanceof CraftingTableTile tile) {
                tile.craft();
            }
        };
    }
}
