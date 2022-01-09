package net.dofmine.minedofmod.items.weapon;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.dofmine.minedofmod.items.ModItems;
import net.dofmine.minedofmod.job.ExtendedEntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
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
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ThorHammer extends Item implements Vanishable {
    public static final int THROW_THRESHOLD_TIME = 10;
    public static final float BASE_DAMAGE = 8.0F;
    public static final float SHOOT_POWER = 2.5F;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public ThorHammer(Properties p_43381_) {
        super(p_43381_);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 8.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", (double)-2.9F, AttributeModifier.Operation.ADDITION));
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

    public void releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int p_43397_) {
        if (livingEntity instanceof Player) {
            Player player = (Player)livingEntity;
            int i = this.getUseDuration(itemStack) - p_43397_;
            if (i >= 10) {
                int j = EnchantmentHelper.getRiptide(itemStack);
                if (j <= 0 || player.isInWaterOrRain()) {
                    if (!level.isClientSide) {
                        itemStack.hurtAndBreak(1, player, (p_43388_) -> {
                            p_43388_.broadcastBreakEvent(livingEntity.getUsedItemHand());
                        });
                        if (j == 0) {
                            ThrownTrident throwntrident = new ThrownTrident(level, player, itemStack);
                            throwntrident.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 2.5F + (float)j * 0.5F, 1.0F);
                            if (player.getAbilities().instabuild) {
                                throwntrident.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                            }

                            level.addFreshEntity(throwntrident);
                            level.playSound((Player)null, throwntrident, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                            if (!player.getAbilities().instabuild) {
                                player.getInventory().removeItem(itemStack);
                            }
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    if (j > 0) {
                        float f7 = player.getYRot();
                        float f = player.getXRot();
                        float f1 = -Mth.sin(f7 * ((float)Math.PI / 180F)) * Mth.cos(f * ((float)Math.PI / 180F));
                        float f2 = -Mth.sin(f * ((float)Math.PI / 180F));
                        float f3 = Mth.cos(f7 * ((float)Math.PI / 180F)) * Mth.cos(f * ((float)Math.PI / 180F));
                        float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
                        float f5 = 3.0F * ((1.0F + (float)j) / 4.0F);
                        f1 = f1 * (f5 / f4);
                        f2 = f2 * (f5 / f4);
                        f3 = f3 * (f5 / f4);
                        player.push((double)f1, (double)f2, (double)f3);
                        player.startAutoSpinAttack(20);
                        if (player.isOnGround()) {
                            float f6 = 1.1999999F;
                            player.move(MoverType.SELF, new Vec3(0.0D, (double)1.1999999F, 0.0D));
                        }

                        SoundEvent soundevent;
                        if (j >= 3) {
                            soundevent = SoundEvents.TRIDENT_RIPTIDE_3;
                        } else if (j == 2) {
                            soundevent = SoundEvents.TRIDENT_RIPTIDE_2;
                        } else {
                            soundevent = SoundEvents.TRIDENT_RIPTIDE_1;
                        }

                        level.playSound((Player)null, player, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }

                }
            }
        }
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
                player.startUsingItem(interactionHand);
                return InteractionResultHolder.consume(itemstack);
            }
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
                ExtendedEntityPlayer.get().addMana(10);
                level.addFreshEntity(itemGod);
                context.getPlayer().getInventory().removeItem(context.getItemInHand());
            } else if (level.getBlockState(firstBlock).getBlock().equals(Blocks.QUARTZ_BLOCK)
                    && level.getBlockState(rightBlockZ).getBlock().equals(Blocks.QUARTZ_BLOCK) && level.getBlockState(leftBlockZ).getBlock().equals(Blocks.QUARTZ_BLOCK)
                    && level.getBlockState(torchBlockLeftZ).getBlock().equals(Blocks.TORCH) && level.getBlockState(torchBlockRightZ).getBlock().equals(Blocks.TORCH)) {
                ItemEntity itemGod = new ItemEntity(level, firstBlock.getX(), firstBlock.getY(), firstBlock.getZ(), new ItemStack(ModItems.GOD_INGOT.get(), 1));
                ExtendedEntityPlayer.get().addMana(10);
                level.addFreshEntity(itemGod);
                context.getPlayer().getInventory().removeItem(context.getItemInHand());
            }
        }
        return super.useOn(context);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int p_41407_, boolean p_41408_) {
        if (!itemStack.isEnchanted()) {
            itemStack.enchant(Enchantments.LOYALTY, 3);
        }
        super.inventoryTick(itemStack, level, entity, p_41407_, p_41408_);
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
