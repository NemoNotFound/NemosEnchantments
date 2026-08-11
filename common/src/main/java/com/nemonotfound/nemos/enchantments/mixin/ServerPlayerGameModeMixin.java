package com.nemonotfound.nemos.enchantments.mixin;

import com.nemonotfound.nemos.enchantments.enchantment.NemosEnchantments;
import com.nemonotfound.nemos.enchantments.utils.TreeFellingUtils;
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

    @Inject(method = "destroyBlock", at = @At("HEAD"))
    private void destroyBlockHead(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (nemosEnchantments$felling) {
            return;
        }

        ItemStack axe = player.getMainHandItem();
        if (!TreeFellingUtils.isTreeBlock(level.getBlockState(pos))
                || !axe.is(ItemTags.AXES)
                || !hasEnchantment(level, NemosEnchantments.FELLING, axe)) {
            nemosEnchantments$logsToFell = List.of();
            return;
        }

        nemosEnchantments$logsToFell = TreeFellingUtils.findTreeLogs(level, pos);
    }

    @Inject(method = "destroyBlock", at = @At("RETURN"))
    private void destroyBlockReturn(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (nemosEnchantments$felling || !cir.getReturnValue() || nemosEnchantments$logsToFell.isEmpty()) {
            return;
        }

        nemosEnchantments$felling = true;
        try {
            for (BlockPos logPos : nemosEnchantments$logsToFell) {
                ItemStack axe = player.getMainHandItem();
                if (!axe.is(ItemTags.AXES) || !hasEnchantment(level, NemosEnchantments.FELLING, axe)) {
                    break;
                }
                destroyBlock(logPos);
            }
        } finally {
            nemosEnchantments$logsToFell = List.of();
            nemosEnchantments$felling = false;
        }
    }

}
