package com.nemonotfound.nemos.enchantments.mixin;

import com.nemonotfound.nemos.enchantments.entity.attribute.NemosAttributes;
import com.nemonotfound.nemos.enchantments.access.LivingEntityAccess;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.nemonotfound.nemos.enchantments.utils.HeadHunterUtils;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingEntityAccess {

    @Shadow public abstract double getAttributeValue(Holder<Attribute> attribute);

    private LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    @Invoker("calculateFallDamage")
    public abstract int nemosEnchantments$calculateFallDamage(double fallDistance, float damageMultiplier);

    @ModifyReturnValue(method = "createLivingAttributes", at = @At("RETURN"))
    private static AttributeSupplier.Builder createLivingAttributes(AttributeSupplier.Builder original) {
        return original.add(NemosAttributes.CLIMBING_EFFICIENCY);
    }

    @ModifyVariable(method = "handleRelativeFrictionAndCalculateMovement", at = @At(value = "STORE", ordinal = 1), name = "movement")
    private Vec3 modifyMovementDistance(Vec3 movement) {
        var climbingEfficiency = getAttributeValue(NemosAttributes.CLIMBING_EFFICIENCY);

        return new Vec3(movement.x, climbingEfficiency, movement.z);
    }

    @ModifyVariable(method = "handleOnClimbable", at = @At(value = "STORE"), name = "yd")
    private double modifyMovementDistance(double y, @Local(argsOnly = true, name = "delta") Vec3 delta) {
        var climbingEfficiency = getAttributeValue(NemosAttributes.CLIMBING_EFFICIENCY);

        return Math.max(delta.y, -climbingEfficiency + 0.05F);
    }

    @Inject(method = "dropAllDeathLoot", at = @At("TAIL"))
    private void nemosEnchantments$dropHead(ServerLevel level, DamageSource source, CallbackInfo ci) {
        HeadHunterUtils.tryDropHead(level, (LivingEntity) (Object) this, source);
    }
}
