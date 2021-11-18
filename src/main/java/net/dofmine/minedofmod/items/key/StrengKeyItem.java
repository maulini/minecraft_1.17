package net.dofmine.minedofmod.items.key;

import net.dofmine.minedofmod.tabs.ModCreativeTabs;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class StrengKeyItem extends Item implements ICurioItem {

    public StrengKeyItem() {
        super(new Properties().tab(ModCreativeTabs.MODS_TABS).stacksTo(1));
    }

   @Override
   public void curioTick(SlotContext slotContext, ItemStack stack) {
        Player playerEntity = (Player) slotContext.entity();
        if (!playerEntity.hasEffect(MobEffects.DAMAGE_BOOST)) {
            playerEntity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200));
        }
        ICurioItem.super.curioTick(slotContext, stack);
   }
}
