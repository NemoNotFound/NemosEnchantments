package com.devnemo.nemos.enchantments.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(TreeFeature.class)
public class TreeFeatureMixin {

//    @Inject(method = "method_49238", at = @At(value = "TAIL"))
//    private static void addBlockEntityToRoots(Set set, WorldGenLevel worldGenLevel, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
//
//    }
//
//    @Inject(method = "method_43162", at = @At(value = "TAIL"))
//    private static void addBlockEntityToLogs(Set set, WorldGenLevel worldGenLevel, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
//
//    }
//
//    @Inject(method = "method_35364", at = @At(value = "TAIL"))
//    private static void addBlockEntityToLeaves(Set set, WorldGenLevel worldGenLevel, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
//
//    }

    @Inject(method = "place", at = @At("TAIL"))
    private void addBlockEntityToTree(FeaturePlaceContext<TreeConfiguration> context, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) Set<BlockPos> rootSet, @Local(ordinal = 1) Set<BlockPos> logSet, @Local(ordinal = 2) Set<BlockPos> leavesSet) {
        //TODO: Add logic
    }
}
