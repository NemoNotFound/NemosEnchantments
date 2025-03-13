package com.nemonotfound.nemos.enchantments.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    private LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyVariable(method = "applyMovementInput", at = @At(value = "STORE", ordinal = 1), ordinal = 1)
    private Vec3d modifyMovementDistance(Vec3d vec3d) {
        return new Vec3d(vec3d.x, 100000, vec3d.z);
    }
}