package com.nemonotfound.nemos.enchantments.utils;

import com.nemonotfound.nemos.enchantments.enchantment.NemosEnchantments;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;

import static com.nemonotfound.nemos.enchantments.Constants.MOD_ID;

public final class HeadHunterUtils {

    private static final float DROP_CHANCE_PER_LEVEL = 0.0084F;
    public static final ResourceKey<LootTable> HEAD_HUNTER_LOOT_TABLE = ResourceKey.create(
            Registries.LOOT_TABLE,
            Identifier.fromNamespaceAndPath(MOD_ID, "head_hunter")
    );

    private HeadHunterUtils() {
    }

    public static void tryDropHead(ServerLevel level, LivingEntity victim, DamageSource damageSource) {
        ItemStack weapon = damageSource.getWeaponItem();
        if ((weapon == null || weapon.isEmpty()) && damageSource.getEntity() != null) {
            weapon = damageSource.getEntity().getWeaponItem();
        }
        if (weapon == null || weapon.isEmpty()) {
            return;
        }

        int enchantmentLevel = EnchantmentUtils.getEnchantmentLevel(level, NemosEnchantments.HEAD_HUNTER, weapon);
        if (enchantmentLevel > 0 && level.getRandom().nextFloat() < DROP_CHANCE_PER_LEVEL * enchantmentLevel) {
            victim.dropFromLootTable(
                    level,
                    damageSource,
                    false,
                    HEAD_HUNTER_LOOT_TABLE,
                    head -> victim.spawnAtLocation(level, head)
            );
        }
    }
}
