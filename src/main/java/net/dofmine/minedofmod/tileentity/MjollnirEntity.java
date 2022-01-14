package net.dofmine.minedofmod.tileentity;

import net.dofmine.minedofmod.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.TridentLoyaltyEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Iterator;

public class MjollnirEntity extends AbstractArrow {

    private int nbEntityShoot;
    private boolean firstEntity;
    public int clientSideReturnTridentTickCount;
    private boolean canReturn;
    private final int MAX_ENTITY_DAMAGE = 4;

    public MjollnirEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public MjollnirEntity(double x, double y, double z, Level level) {
        super(ModEntity.MJOLLNIR_PROJECTILE.get(), x, y, z, level);
    }

    public MjollnirEntity(LivingEntity livingEntity, Level level, int nbEntityShoot) {
        super(ModEntity.MJOLLNIR_PROJECTILE.get(), livingEntity, level);
        this.nbEntityShoot = nbEntityShoot;
    }

    @Override
    protected ItemStack getPickupItem() {
        return ModItems.THOR_HAMMER.get().getDefaultInstance();
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (entity != null && canReturn) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level.isClientSide && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015D * (double)3, this.getZ());
                if (this.level.isClientSide) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05D * (double)3;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vec3.normalize().scale(d0)));
                if (this.clientSideReturnTridentTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.clientSideReturnTridentTickCount;
            }
        }

        super.tick();
    }


    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        if (nbEntityShoot < MAX_ENTITY_DAMAGE) {
            findOtherEntity(entityHitResult.getEntity());
        }
        if (firstEntity) {
            canReturn = true;
        }
    }

    private void findOtherEntity(Entity entityHit) {
        if (level instanceof ServerLevel serverLevel) {
            float closest = 500;
            Entity thisOne=null;
            Iterator<Entity> iterator = serverLevel.getAllEntities().iterator();
            while (iterator.hasNext()) {
                Entity entity = iterator.next();
                float distanceTo = entity.distanceTo(entityHit);
                if (distanceTo < closest && !entity.equals(entityHit)) {
                    if (entity instanceof Monster) {
                        closest = distanceTo;
                        thisOne = entity;
                    }
                }
            }
            if (thisOne != null) {
                nbEntityShoot++;
                MjollnirEntity mjollnirEntity = new MjollnirEntity((LivingEntity) getOwner(), level, nbEntityShoot);
                double d0 = thisOne.getX() - this.getX();
                double d1 = thisOne.getY(0.3333333333333333D) - mjollnirEntity.getY();
                double d2 = thisOne.getZ() - this.getZ();
                double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                mjollnirEntity.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
                level.addFreshEntity(mjollnirEntity);
            }
            if (nbEntityShoot == MAX_ENTITY_DAMAGE || thisOne == null) {
                firstEntity = true;
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult blockHitResult) {
        super.onHitBlock(blockHitResult);
        if (firstEntity) {
            canReturn = true;
        }
    }
}
