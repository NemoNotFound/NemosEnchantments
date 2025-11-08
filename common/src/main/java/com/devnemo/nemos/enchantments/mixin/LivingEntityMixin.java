package com.devnemo.nemos.enchantments.mixin;

import com.devnemo.nemos.enchantments.entity.attribute.NemosAttributes;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract double getAttributeValue(Holder<Attribute> attribute);

    private LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @ModifyReturnValue(method = "createLivingAttributes", at = @At("RETURN"))
    private static AttributeSupplier.Builder createLivingAttributes(AttributeSupplier.Builder original) {
        return original.add(NemosAttributes.CLIMBING_EFFICIENCY.get());
    }

    @ModifyVariable(method = "handleRelativeFrictionAndCalculateMovement", at = @At(value = "STORE", ordinal = 1), ordinal = 1)
    private Vec3 modifyMovementDistance(Vec3 vec3) {
        var climbingEfficiency = getAttributeValue(NemosAttributes.CLIMBING_EFFICIENCY.get());

        return new Vec3(vec3.x, climbingEfficiency, vec3.z);
    }

    @ModifyVariable(method = "handleOnClimbable", at = @At(value = "STORE"), ordinal = 2)
    private double modifyMovementDistance(double y, @Local(argsOnly = true) Vec3 vec3) {
        var climbingEfficiency = getAttributeValue(NemosAttributes.CLIMBING_EFFICIENCY.get());

        return Math.max(vec3.y, -climbingEfficiency + 0.05F);
    }
}