package net.dofmine.minedofmod.items.key;

import net.dofmine.minedofmod.tabs.ModCreativeTabs;
import net.minecraft.advancements.critereon.EntityHurtPlayerTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class FlyKeyItem extends Item implements ICurioItem {

    public FlyKeyItem() {
        super(new Properties().tab(ModCreativeTabs.MODS_TABS).stacksTo(1));
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        player.getAbilities().mayfly = true;
        ((Player) slotContext.entity()).onUpdateAbilities();
        ICurioItem.super.onEquip(slotContext, prevStack, stack);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        Player player = (Player) slotContext.entity();
        player.getAbilities().mayfly = false;
        ((Player) slotContext.entity()).onUpdateAbilities();
        ICurioItem.super.onUnequip(slotContext, newStack, stack);
    }
}
