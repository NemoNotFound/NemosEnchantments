package com.nemonotfound.nemos.enchantments.tree;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public final class TreeBlockHelper {

    private TreeBlockHelper() {
    }

    public static boolean isTreeBlock(BlockState state) {
        return state.is(BlockTags.LOGS)
                || state.is(Blocks.MANGROVE_ROOTS)
                || state.is(Blocks.MUDDY_MANGROVE_ROOTS);
    }

    public static boolean connectsLogs(BlockState state) {
        return state.is(BlockTags.LEAVES) || state.is(BlockTags.WART_BLOCKS);
    }

    public static boolean isNaturalCanopy(BlockState state) {
        return connectsLogs(state)
                && (state.is(BlockTags.WART_BLOCKS) || !state.hasProperty(LeavesBlock.PERSISTENT)
                || !state.getValue(LeavesBlock.PERSISTENT));
    }
}
