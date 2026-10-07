package com.nemonotfound.nemos.enchantments.utils;

import com.nemonotfound.nemos.enchantments.tree.ConnectedTreeLogSearch;
import com.nemonotfound.nemos.enchantments.tree.TrackedTreeLogSearch;
import com.nemonotfound.nemos.enchantments.tree.TreeBlockHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO: Rethink and refactor
public final class TreeFellingUtils {

    private static final int MAX_TREE_BLOCKS = 256;
    private static final int CANOPY_SEARCH_RADIUS = 2;
    private static final int MAX_REQUIRED_CANOPY_BLOCKS = 8;
    private static final int LOGS_PER_REQUIRED_CANOPY_BLOCK = 4;

    private TreeFellingUtils() {
    }

    public static List<BlockPos> findTreeLogs(ServerLevel level, BlockPos origin) {
        if (!level.hasChunkAt(origin) || !TreeBlockHelper.isTreeBlock(level.getBlockState(origin))) {
            return List.of();
        }

        var result = new TrackedTreeLogSearch(level, origin, MAX_TREE_BLOCKS).findLogs();

        return switch (result.status()) {
            case UNTRACKED -> findUntrackedTreeLogs(level, origin);
            case READY -> result.logs();
            case REJECTED -> List.of();
        };
    }

    private static List<BlockPos> findUntrackedTreeLogs(ServerLevel level, BlockPos origin) {
        var search = new ConnectedTreeLogSearch(
                chunkPos -> level.getChunkSource().getChunkNow(ChunkPos.getX(chunkPos), ChunkPos.getZ(chunkPos)),
                null, MAX_TREE_BLOCKS);

        List<BlockPos> treeBlocks = search.findLogs(origin);

        if (treeBlocks.isEmpty() || !hasEnoughCanopy(level, treeBlocks)) {
            return List.of();
        }

        treeBlocks.remove(origin);

        return treeBlocks;
    }

    private static boolean hasEnoughCanopy(ServerLevel level, List<BlockPos> treeBlocks) {
        Set<BlockPos> canopyBlocks = new HashSet<>();

        for (BlockPos treeBlock : treeBlocks) {
            collectCanopy(level, treeBlock, canopyBlocks);
        }

        return canopyBlocks.size() >= requiredCanopyCount(treeBlocks.size());
    }

    private static int requiredCanopyCount(int logCount) {
        return Math.min(
                MAX_REQUIRED_CANOPY_BLOCKS,
                Math.max(2, (logCount + LOGS_PER_REQUIRED_CANOPY_BLOCK - 1) / LOGS_PER_REQUIRED_CANOPY_BLOCK)
        );
    }

    private static void collectCanopy(ServerLevel level, BlockPos treeBlock, Set<BlockPos> canopyBlocks) {
        BlockPos start = treeBlock.offset(-CANOPY_SEARCH_RADIUS, -CANOPY_SEARCH_RADIUS, -CANOPY_SEARCH_RADIUS);
        BlockPos end = treeBlock.offset(CANOPY_SEARCH_RADIUS, CANOPY_SEARCH_RADIUS, CANOPY_SEARCH_RADIUS);

        for (BlockPos canopyPos : BlockPos.betweenClosed(start, end)) {
            if (level.hasChunkAt(canopyPos) && TreeBlockHelper.isNaturalCanopy(level.getBlockState(canopyPos))) {
                canopyBlocks.add(canopyPos.immutable());
            }
        }
    }

}
