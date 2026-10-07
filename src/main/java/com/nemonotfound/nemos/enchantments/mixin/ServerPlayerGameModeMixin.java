package com.nemonotfound.nemos.enchantments.mixin;

import com.nemonotfound.nemos.enchantments.enchantment.Enchantments;
import com.nemonotfound.nemos.enchantments.utils.TreeFellingUtils;
import com.nemonotfound.nemos.enchantments.tree.TreeBlockHelper;
import com.nemonotfound.nemos.enchantments.tree.TreeTracking;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.nemonotfound.nemos.enchantments.utils.EnchantmentUtils.hasEnchantment;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin {

    @Shadow
    protected ServerLevel level;

    @Shadow
    @Final
    protected ServerPlayer player;

    @Shadow
    public abstract boolean destroyBlock(BlockPos pos);

    @Unique
    private boolean nemosEnchantments$felling;

    @Unique
    private List<BlockPos> nemosEnchantments$logsToFell = List.of();

    @Unique
    private Optional<UUID> nemosEnchantments$treeId = Optional.empty();

    @Inject(method = "destroyBlock", at = @At("HEAD"))
    private void destroyBlockHead(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (nemosEnchantments$felling) {
            return;
        }

        nemosEnchantments$treeId = Optional.empty();

        if (!TreeBlockHelper.isTreeBlock(level.getBlockState(pos))
                || !nemosEnchantments$hasFellingAxe()) {
            nemosEnchantments$logsToFell = List.of();
            return;
        }

        nemosEnchantments$logsToFell = TreeFellingUtils.findTreeLogs(level, pos);
        nemosEnchantments$treeId = TreeTracking.getTreeId(level, pos);
    }

    @Inject(method = "destroyBlock", at = @At("RETURN"))
    private void destroyBlockReturn(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (nemosEnchantments$felling || !cir.getReturnValue() || nemosEnchantments$logsToFell.isEmpty()) {
            return;
        }

        nemosEnchantments$felling = true;

        try {
            nemosEnchantments$fellSelectedLogs();
        } finally {
            nemosEnchantments$logsToFell = List.of();
            nemosEnchantments$treeId = Optional.empty();
            nemosEnchantments$felling = false;
        }
    }

    @Unique
    private void nemosEnchantments$fellSelectedLogs() {
        for (BlockPos pos : nemosEnchantments$logsToFell) {
            if (!nemosEnchantments$hasFellingAxe()) {
                break;
            }

            if (nemosEnchantments$isSelectedTreeLog(pos)) {
                destroyBlock(pos);
            }
        }
    }

    @Unique
    private boolean nemosEnchantments$hasFellingAxe() {
        ItemStack axe = player.getMainHandItem();
        return axe.is(ItemTags.AXES) && hasEnchantment(level, Enchantments.FELLING, axe);
    }

    @Unique
    private boolean nemosEnchantments$isSelectedTreeLog(BlockPos pos) {
        return level.getChunkSource().getChunkNow(pos.getX() >> 4, pos.getZ() >> 4) != null
                && TreeBlockHelper.isTreeBlock(level.getBlockState(pos))
                && nemosEnchantments$treeId.equals(TreeTracking.getTreeId(level, pos));
    }
}
