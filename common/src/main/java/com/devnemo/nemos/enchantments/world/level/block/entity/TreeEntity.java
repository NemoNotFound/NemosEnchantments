package com.devnemo.nemos.enchantments.world.level.block.entity;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TreeEntity extends BlockEntity {

    public static final Codec<List<BlockPos>> LIST_CODEC = BlockPos.CODEC.listOf();
    private final List<BlockPos> treeBlocks = new ArrayList<>();

    public TreeEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        this.treeBlocks.clear();
        input.read("treeBlocks", LIST_CODEC)
                .orElse(List.of())
                .forEach(this::setTreeBlocks);
    }

    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);
        output.store("treeBlocks", LIST_CODEC, this.getTreeBlocks());
    }

    private void setTreeBlocks(BlockPos blockPos) {
        treeBlocks.add(blockPos);
    }

    private List<BlockPos> getTreeBlocks() {
        return treeBlocks;
    }

    public List<BlockPos> getTreeBlocksToDestroy() {
        return treeBlocks;
        //TODO: Adapt
    }
}
