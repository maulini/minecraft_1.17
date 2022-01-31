package net.dofmine.minedofmod.items.drink;

import net.dofmine.minedofmod.job.client.HydrationEntityPlayer;
import net.dofmine.minedofmod.tabs.ModCreativeTabs;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class DrinkItem extends Item {

    private final int nbHydration;

    public DrinkItem(int nbHydration) {
        super(new Item.Properties().tab(ModCreativeTabs.FOODS_TABS).craftRemainder(Items.GLASS_BOTTLE));
        this.nbHydration = nbHydration;
    }

    @Override
    public int getUseDuration(ItemStack p_41454_) {
        return 32;
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
        if (livingEntity instanceof Player player && !player.isCreative() && player.level.isClientSide && HydrationEntityPlayer.get(player).needHydration()) {
            HydrationEntityPlayer.get(player).addHydration(nbHydration);
            itemStack.shrink(1);
            player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
        }
        return itemStack;
    }
}
