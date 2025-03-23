package com.nemonotfound.nemos.enchantments.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.nemonotfound.nemos.enchantments.entity.attribute.EntityAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract double getAttributeValue(RegistryEntry<EntityAttribute> attribute);

    private LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyReturnValue(method = "createLivingAttributes", at = @At("RETURN"))
    private static DefaultAttributeContainer.Builder createLivingAttributes(DefaultAttributeContainer.Builder original) {
        return original.add(EntityAttributes.CLIMBING_EFFICIENCY);
    }

    @ModifyVariable(method = "applyMovementInput", at = @At(value = "STORE", ordinal = 1), ordinal = 1)
    private Vec3d modifyMovementDistance(Vec3d vec3d) {
        var climbingEfficiency = getAttributeValue(EntityAttributes.CLIMBING_EFFICIENCY);

        return new Vec3d(vec3d.x, climbingEfficiency, vec3d.z);
    }

    @ModifyVariable(method = "applyClimbingSpeed", at = @At(value = "STORE"), ordinal = 2)
    private double modifyMovementDistance(double y, @Local(argsOnly = true) Vec3d motion) {
        var climbingEfficiency = getAttributeValue(EntityAttributes.CLIMBING_EFFICIENCY);

        return Math.max(motion.y, -climbingEfficiency + 0.05F);
    }
}