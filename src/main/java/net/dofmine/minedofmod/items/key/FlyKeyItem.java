package net.dofmine.minedofmod.items.key;

import net.dofmine.minedofmod.tabs.ModCreativeTabs;
import net.minecraft.advancements.critereon.EntityHurtPlayerTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.UUID;

public class FlyKeyItem extends Item implements ICurioItem {

    public FlyKeyItem() {
        super(new Properties().tab(ModCreativeTabs.MODS_TABS).stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        if (level instanceof ServerLevel serverLevel) {
            if (serverLevel.isRaining() && !serverLevel.isThundering()) {
                serverLevel.setWeatherParameters(0, 60000, true, true);
                player.sendMessage(new TextComponent("De l'orage se fait entendre au loin..."), UUID.randomUUID());
            }else if (serverLevel.isThundering()) {
                serverLevel.setWeatherParameters(60000, 0,false, false);
                player.sendMessage(new TextComponent("L'orage est enfin passé le soleil arrive"), UUID.randomUUID());
            }else {
                serverLevel.setWeatherParameters(0, 60000, true, false);
                player.sendMessage(new TextComponent("Sortez le parapluie les nuages arrivent"), UUID.randomUUID());
            }
            return InteractionResultHolder.success(new ItemStack(this));
        }
        return InteractionResultHolder.fail(new ItemStack(this));
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
