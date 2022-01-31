package net.dofmine.minedofmod.items.weapon;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.job.client.ExtendedEntityPlayer;
import net.dofmine.minedofmod.tileentity.MjollnirEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class ThorHammer extends Item implements Vanishable {
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public ThorHammer(Properties p_43381_) {
        super(p_43381_);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 8.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double) -2.9F, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    public boolean canAttackBlock(BlockState blockState, Level level, BlockPos blockPos, Player player) {
        return !player.isCreative();
    }

    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.SPEAR;
    }

    public int getUseDuration(ItemStack itemStack) {
        return Integer.MAX_VALUE;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        if (level instanceof ServerLevel serverLevel) {
            if (player.isCrouching()) {
                List<? extends Entity> entities = serverLevel.getEntitiesOfClass(Entity.class, Minecraft.getInstance().player.getBoundingBox().expandTowards(5.0d, 5.0d, 5.0d), entity -> !(entity instanceof Player));
                if (!entities.isEmpty()) {
                    for (Entity entity: entities) {
                        EntityType.LIGHTNING_BOLT.spawn(serverLevel, null, null, entity.blockPosition(), MobSpawnType.TRIGGERED, true, true);
                    }
                }else {
                    entities = serverLevel.getEntitiesOfClass(Entity.class, Minecraft.getInstance().player.getBoundingBox().expandTowards(-5.0d, -5.0d, -5.0d), entity -> !(entity instanceof Player));
                    if (!entities.isEmpty()) {
                        for (Entity entity : entities) {
                            EntityType.LIGHTNING_BOLT.spawn(serverLevel, null, null, entity.blockPosition(), MobSpawnType.TRIGGERED, true, true);
                        }
                    }
                }
                return InteractionResultHolder.consume(itemstack);
            }else {
                MjollnirEntity mjollnirEntity = new MjollnirEntity(player, level,0);
                mjollnirEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F + (float)0 * 0.5F, 1.0F);
                if (player.getAbilities().instabuild) {
                    mjollnirEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }

                level.addFreshEntity(mjollnirEntity);
                level.playSound((Player)null, mjollnirEntity, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                    player.getInventory().removeItem(itemstack);
            }
            return InteractionResultHolder.consume(itemstack);
        }
        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide) {
            BlockPos firstBlock = context.getClickedPos();
            BlockPos rightBlock = new BlockPos(firstBlock.getX() + 1, firstBlock.getY(), firstBlock.getZ());
            BlockPos leftBlock = new BlockPos(firstBlock.getX() - 1, firstBlock.getY(), firstBlock.getZ());
            BlockPos torchBlockLeft = new BlockPos(leftBlock.getX(), leftBlock.getY() + 1, leftBlock.getZ());
            BlockPos torchBlockRight = new BlockPos(rightBlock.getX(), rightBlock.getY() + 1, rightBlock.getZ());
            BlockPos rightBlockZ = new BlockPos(firstBlock.getX(), firstBlock.getY(), firstBlock.getZ() + 1);
            BlockPos leftBlockZ = new BlockPos(firstBlock.getX(), firstBlock.getY(), firstBlock.getZ() - 1);
            BlockPos torchBlockLeftZ = new BlockPos(leftBlockZ.getX(), leftBlockZ.getY() + 1, leftBlockZ.getZ());
            BlockPos torchBlockRightZ = new BlockPos(rightBlockZ.getX(), rightBlockZ.getY() + 1, rightBlockZ.getZ());
            if (level.getBlockState(firstBlock).getBlock().equals(Blocks.QUARTZ_BLOCK)
                    && level.getBlockState(rightBlock).getBlock().equals(Blocks.QUARTZ_BLOCK) && level.getBlockState(leftBlock).getBlock().equals(Blocks.QUARTZ_BLOCK)
                    && level.getBlockState(torchBlockLeft).getBlock().equals(Blocks.TORCH) && level.getBlockState(torchBlockRight).getBlock().equals(Blocks.TORCH)) {
                ItemEntity itemGod = new ItemEntity(level, firstBlock.getX(), firstBlock.getY(), firstBlock.getZ(), new ItemStack(ModItems.GOD_INGOT.get(), 1));
                ExtendedEntityPlayer.get(context.getPlayer()).addMana(10);
                level.addFreshEntity(itemGod);
                context.getPlayer().getInventory().removeItem(context.getItemInHand());
            } else if (level.getBlockState(firstBlock).getBlock().equals(Blocks.QUARTZ_BLOCK)
                    && level.getBlockState(rightBlockZ).getBlock().equals(Blocks.QUARTZ_BLOCK) && level.getBlockState(leftBlockZ).getBlock().equals(Blocks.QUARTZ_BLOCK)
                    && level.getBlockState(torchBlockLeftZ).getBlock().equals(Blocks.TORCH) && level.getBlockState(torchBlockRightZ).getBlock().equals(Blocks.TORCH)) {
                ItemEntity itemGod = new ItemEntity(level, firstBlock.getX(), firstBlock.getY(), firstBlock.getZ(), new ItemStack(ModItems.GOD_INGOT.get(), 1));
                ExtendedEntityPlayer.get(context.getPlayer()).addMana(10);
                level.addFreshEntity(itemGod);
                context.getPlayer().getInventory().removeItem(context.getItemInHand());
            }
        }
        return super.useOn(context);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity livingEntity, LivingEntity entity) {
        itemStack.hurtAndBreak(1, entity, (p_43414_) -> {
            p_43414_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    public boolean mineBlock(ItemStack itemStack, Level level, BlockState blockState, BlockPos blockPos, LivingEntity livingEntity) {
        return false;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_43383_) {
        return p_43383_ == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(p_43383_);
    }

    public int getEnchantmentValue() {
        return 1;
    }
}
