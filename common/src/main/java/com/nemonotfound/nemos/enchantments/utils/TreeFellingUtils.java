package com.nemonotfound.nemos.enchantments.utils;

import com.nemonotfound.nemos.enchantments.mixin.AxeItemAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class TreeFellingUtils {

    private static final int MAX_TREE_BLOCKS = 256;
    private static final int LEAF_SEARCH_RADIUS = 2;
    private static final int MAX_REQUIRED_LEAVES = 8;
    private static final int LOGS_PER_REQUIRED_LEAF = 4;
    private static final List<String> WOOD_SUFFIXES = List.of("_leaves", "_roots", "_log", "_wood", "_stem", "_hyphae");
    private static final WoodFamily OAK = new WoodFamily("minecraft", "oak");

    private TreeFellingUtils() {
    }

    public static boolean isTreeBlock(BlockState state) {
        return state.is(BlockTags.LOGS)
                || state.is(Blocks.MANGROVE_ROOTS)
                || state.is(Blocks.MUDDY_MANGROVE_ROOTS);
    }

    public static List<BlockPos> findTreeLogs(ServerLevel level, BlockPos origin) {
        WoodFamily family = woodFamily(level.getBlockState(origin).getBlock());
        List<BlockPos> treeBlocks = findConnectedTreeBlocks(level, origin, family);

        if (!hasEnoughNaturalLeaves(level, treeBlocks, family)) {
            return List.of();
        }

        treeBlocks.remove(origin);
        return treeBlocks;
    }

    private static List<BlockPos> findConnectedTreeBlocks(ServerLevel level, BlockPos origin, WoodFamily family) {
        BlockPos immutableOrigin = origin.immutable();
        ArrayDeque<BlockPos> pending = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        List<BlockPos> connected = new ArrayList<>();
        pending.add(immutableOrigin);
        visited.add(immutableOrigin);
        connected.add(immutableOrigin);

        while (!pending.isEmpty() && connected.size() < MAX_TREE_BLOCKS) {
            addMatchingNeighbors(level, pending.removeFirst(), family, pending, visited, connected);
        }
        return connected;
    }

    private static void addMatchingNeighbors(
            ServerLevel level,
            BlockPos current,
            WoodFamily family,
            ArrayDeque<BlockPos> pending,
            Set<BlockPos> visited,
            List<BlockPos> connected
    ) {
        BlockPos start = current.offset(-1, -1, -1);
        BlockPos end = current.offset(1, 1, 1);

        for (BlockPos neighbor : BlockPos.betweenClosed(start, end)) {
            if (connected.size() == MAX_TREE_BLOCKS) {
                return;
            }
            if (neighbor.equals(current)) {
                continue;
            }

            BlockPos immutableNeighbor = neighbor.immutable();
            if (isUnvisitedMatchingTreeBlock(level, immutableNeighbor, family, visited)) {
                visited.add(immutableNeighbor);
                pending.addLast(immutableNeighbor);
                connected.add(immutableNeighbor);
            }
        }
    }

    private static boolean isUnvisitedMatchingTreeBlock(
            ServerLevel level,
            BlockPos pos,
            WoodFamily family,
            Set<BlockPos> visited
    ) {
        return !visited.contains(pos)
                && level.hasChunkAt(pos)
                && isMatchingTreeBlock(level.getBlockState(pos), family);
    }

    private static boolean hasEnoughNaturalLeaves(ServerLevel level, List<BlockPos> treeBlocks, WoodFamily family) {
        Set<BlockPos> leaves = new HashSet<>();
        for (BlockPos treeBlock : treeBlocks) {
            collectNaturalLeaves(level, treeBlock, family, leaves);
        }

        int requiredLeaves = Math.min(
                MAX_REQUIRED_LEAVES,
                Math.max(2, (treeBlocks.size() + LOGS_PER_REQUIRED_LEAF - 1) / LOGS_PER_REQUIRED_LEAF)
        );
        return leaves.size() >= requiredLeaves;
    }

    private static void collectNaturalLeaves(ServerLevel level, BlockPos treeBlock, WoodFamily family, Set<BlockPos> leaves) {
        BlockPos start = treeBlock.offset(-LEAF_SEARCH_RADIUS, -LEAF_SEARCH_RADIUS, -LEAF_SEARCH_RADIUS);
        BlockPos end = treeBlock.offset(LEAF_SEARCH_RADIUS, LEAF_SEARCH_RADIUS, LEAF_SEARCH_RADIUS);

        for (BlockPos leafPos : BlockPos.betweenClosed(start, end)) {
            if (level.hasChunkAt(leafPos) && isMatchingNaturalLeaf(level.getBlockState(leafPos), family)) {
                leaves.add(leafPos.immutable());
            }
        }
    }

    private static boolean isMatchingNaturalLeaf(BlockState state, WoodFamily family) {
        return state.is(BlockTags.LEAVES)
                && state.hasProperty(LeavesBlock.PERSISTENT)
                && !state.getValue(LeavesBlock.PERSISTENT)
                && isMatchingLeaf(state.getBlock(), family);
    }

    private static boolean isMatchingTreeBlock(BlockState state, WoodFamily family) {
        return isTreeBlock(state) && woodFamily(state.getBlock()).equals(family);
    }

    private static boolean isMatchingLeaf(Block leaf, WoodFamily family) {
        return family.equals(OAK) && (leaf == Blocks.AZALEA_LEAVES || leaf == Blocks.FLOWERING_AZALEA_LEAVES)
                || woodFamily(leaf).equals(family);
    }

    private static WoodFamily woodFamily(Block block) {
        Block strippedVariant = AxeItemAccessor.nemosEnchantments$getStrippables().getOrDefault(block, block);
        Identifier id = BuiltInRegistries.BLOCK.getKey(strippedVariant);
        String path = removePrefix(id.getPath(), "stripped_");
        path = removePrefix(path, "muddy_");

        for (String suffix : WOOD_SUFFIXES) {
            if (path.endsWith(suffix)) {
                path = path.substring(0, path.length() - suffix.length());
                break;
            }
        }
        return new WoodFamily(id.getNamespace(), path);
    }

    private static String removePrefix(String value, String prefix) {
        return value.startsWith(prefix) ? value.substring(prefix.length()) : value;
    }

    private record WoodFamily(String namespace, String name) {
    }
}
