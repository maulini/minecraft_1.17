package net.dofmine.minedofmod.items.backpack;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class VacuumBackPack extends Item {

    public static final String INVENTORY_CONTENT = "Content";
    public static final String INVENTORY_CONTENT_SIZE = "Content Size";
    private final int MAX_STACK_SIZE = 3456;
    private ItemStack content;

    public VacuumBackPack(Item.Properties properties) {
        super(properties);
    }

    public static void saveInventoryContent(ItemStack itemStack, ItemStack itemStackToInventory) {
        CompoundTag tag = itemStack.getOrCreateTag();
        ItemStack itemToSave = itemStackToInventory.copy();
        itemToSave.setCount(1);
        tag.put(INVENTORY_CONTENT, itemToSave.save(new CompoundTag()));
        tag.putInt(INVENTORY_CONTENT_SIZE, 1);
    }

    public void setContent(ItemStack itemStack) {
        content = itemStack;
    }

    public void addItemToStack(ItemStack vacuumStack, ItemStack item, ServerLevel serverLevel, Player player) {
        if (content == null && vacuumStack.hasTag()) {
            content = ItemStack.of((CompoundTag) vacuumStack.getTag().get(INVENTORY_CONTENT));
        }
        if (vacuumStack.hasTag()) {
            int size = vacuumStack.getOrCreateTag().getInt(INVENTORY_CONTENT_SIZE);
            if (size + item.getCount() > MAX_STACK_SIZE) {
                item.setCount(item.getCount() - MAX_STACK_SIZE);
                size += item.getCount();
                serverLevel.addFreshEntity(player.drop(item, true, false));
                vacuumStack.getOrCreateTag().putInt(INVENTORY_CONTENT_SIZE, size);
            }else {
                vacuumStack.getOrCreateTag().putInt(INVENTORY_CONTENT_SIZE, size + item.getCount());
            }
        }
    }

    public boolean containItem(ItemStack vacuumStack, ItemStack itemStack) {
        if (content == null && vacuumStack.hasTag()) {
            content = ItemStack.of((CompoundTag) vacuumStack.getTag().get(INVENTORY_CONTENT));
        }
        ItemStack stack = ItemStack.of((CompoundTag) vacuumStack.getTag().get(INVENTORY_CONTENT));
        int size = vacuumStack.getOrCreateTag().getInt(INVENTORY_CONTENT_SIZE);
        return stack.is(itemStack.getItem()) && size < MAX_STACK_SIZE;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        ItemStack item = ItemStack.of((CompoundTag) itemStack.getOrCreateTag().get(INVENTORY_CONTENT) == null ? ItemStack.EMPTY.getOrCreateTag() : (CompoundTag) itemStack.getOrCreateTag().get(INVENTORY_CONTENT));
        int size = itemStack.getOrCreateTag().getInt(INVENTORY_CONTENT_SIZE);
        if (item != null)
            components.add(new TextComponent("\u00A7c" + size + "\u00A7f " + item.getDisplayName().getString().replace('[', ' ').replace(']', ' ')));

        super.appendHoverText(itemStack, level, components, tooltipFlag);
    }

    public static ItemStack itemContent(ItemStack vacuum) {
        ItemStack output = ItemStack.of((CompoundTag) vacuum.getOrCreateTag().get(INVENTORY_CONTENT));
        int size = vacuum.getOrCreateTag().getInt(INVENTORY_CONTENT_SIZE);
        if (size >= 64) {
            output.setCount(64);
        }else {
            output.setCount(size);
        }
        return output;
    }

    public static void decreaseSize(ItemStack vacuum, int size) {
        vacuum.getOrCreateTag().putInt(INVENTORY_CONTENT_SIZE, vacuum.getOrCreateTag().getInt(INVENTORY_CONTENT_SIZE) - size);
    }

}
