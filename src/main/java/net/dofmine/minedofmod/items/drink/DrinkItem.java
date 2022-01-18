package net.dofmine.minedofmod.items.drink;

import net.dofmine.minedofmod.job.HydrationEntityPlayer;
import net.dofmine.minedofmod.tabs.ModCreativeTabs;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class DrinkItem extends Item {

    private static final int DRINK_DURATION = 32;
    private final int nbHydration;

    public DrinkItem(int nbHydration) {
        super(new Item.Properties().tab(ModCreativeTabs.FOODS_TABS));
        this.nbHydration = nbHydration;
    }

    @Override
    public int getUseDuration(ItemStack p_41454_) {
        return DRINK_DURATION;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        return ItemUtils.startUsingInstantly(level, player, interactionHand);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, itemStack);
        }

        if (!level.isClientSide && livingEntity instanceof Player player && !player.isCreative()) {
            HydrationEntityPlayer.get().addHydration(nbHydration);
        }

        if (livingEntity instanceof Player player && !player.getAbilities().instabuild) {
            itemStack.shrink(1);
        }
        return itemStack.isEmpty() ? new ItemStack(Items.GLASS_BOTTLE) : itemStack;
    }
}
