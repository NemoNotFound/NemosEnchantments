package com.nemonotfound.nemos.enchantments.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.nemonotfound.nemos.enchantments.tree.TreeTracking;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.HugeFungusFeature;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Captures the logs placed during vanilla tree and huge-fungus generation.
 *
 * <p>Both feature types use {@code place(...)} for world generation and player-triggered growth.
 * Wrapping that method gives {@link TreeTracking} one context for the complete placement, allowing
 * every affected chunk to store its local logs under the same tree ID.
 */
@Mixin({TreeFeature.class, HugeFungusFeature.class})
public abstract class TreeFeatureMixin {

    /**
     * Runs the original feature placement inside a tree-growth capture.
     *
     * <p>Block changes reported by {@code TreeChunkMixin} are collected while the original method
     * executes. The captured logs are saved only when placement returns {@code true}. The context
     * is always restored afterward, including when placement fails or throws an exception.
     */
    @WrapMethod(method = "place")
    private boolean nemosEnchantments$trackTree(WorldGenLevel level, ChunkGenerator generator,
                                               RandomSource random, BlockPos origin, Operation<Boolean> original) {
        return TreeTracking.capturePlacement(() -> original.call(level, generator, random, origin));
    }
}
