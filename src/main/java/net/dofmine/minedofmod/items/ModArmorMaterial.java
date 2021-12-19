package net.dofmine.minedofmod.items;

import net.dofmine.minedofmod.MinedofMod;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ModArmorMaterial implements ArmorMaterial {

    TITANIUM("titanium", 50, new int[]{3, 6, 8, 3}, 5, SoundEvents.ARMOR_EQUIP_NETHERITE,
            4.0F, 0.2F, () -> {
        return Ingredient.of(ModItems.TITANIUM_INGOT.get());
    }),
    RUBY("ruby", 75, new int[]{4, 7, 9, 4}, 6, SoundEvents.ARMOR_EQUIP_NETHERITE,
            4.0F, 0.2F, () -> {
        return Ingredient.of(ModItems.RUBIS_INGOT.get());
    }),
    LAPIS("lapis", 100, new int[]{5, 9, 10, 5}, 7, SoundEvents.ARMOR_EQUIP_NETHERITE,
            4.0F, 0.2F, () -> {
        return Ingredient.of(ModItems.LAPIS_INGOT.get());
    }),
    DARK("dark", 150, new int[]{7, 11, 12, 7}, 9, SoundEvents.ARMOR_EQUIP_NETHERITE,
            4.0F, 0.6F, () -> {
        return Ingredient.of(ModItems.DARK_INGOT.get());
    }),
    GOD("god", 175, new int[]{8, 12, 13, 8}, 10, SoundEvents.ARMOR_EQUIP_NETHERITE,
            4.0F, 0.6F, () -> {
        return Ingredient.of(ModItems.GOD_INGOT.get());
    }),
    ICE("ice", 175, new int[]{8, 12, 13, 8}, 10, SoundEvents.ARMOR_EQUIP_NETHERITE,
            4.0F, 0.6F, () -> {
        return Ingredient.of(ModItems.GOD_INGOT.get());
    }),
    LAVA("lava", 175, new int[]{8, 12, 13, 8}, 10, SoundEvents.ARMOR_EQUIP_NETHERITE,
            4.0F, 0.6F, () -> {
        return Ingredient.of(ModItems.GOD_INGOT.get());
    });

    private static final int[] HEALTH_PER_SLOT = new int[]{13, 15, 16, 11};
    private final String name;
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    ModArmorMaterial(String name, int durability, int[] slotProtection, int enchantmentValue,
                     SoundEvent soundEvent, float toughness, float knockBackResistance, Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durability;
        this.slotProtections = slotProtection;
        this.enchantmentValue = enchantmentValue;
        this.sound = soundEvent;
        this.toughness = toughness;
        this.knockbackResistance = knockBackResistance;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
    }

    public int getDurabilityForSlot(EquipmentSlot pSlot) {
        return HEALTH_PER_SLOT[pSlot.getIndex()] * this.durabilityMultiplier;
    }

    public int getDefenseForSlot(EquipmentSlot pSlot) {
        return this.slotProtections[pSlot.getIndex()];
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public SoundEvent getEquipSound() {
        return this.sound;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    public String getName() {
        return MinedofMod.MODS_ID + ":" + this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    /**
     * Gets the percentage of knockback resistance provided by armor of the material.
     */
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}