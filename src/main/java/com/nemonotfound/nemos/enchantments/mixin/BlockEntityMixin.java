package com.nemonotfound.nemos.enchantments.mixin;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTypes;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.storage.TagValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {

    @Inject(method = "collectImplicitComponents", at = @At("TAIL"))
    private void nemosEnchantments$exposeSpawnerData(DataComponentMap.Builder components, CallbackInfo ci) {
        if (!((Object) this instanceof SpawnerBlockEntity spawner) || spawner.getLevel() == null) {
            return;
        }

        TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, spawner.getLevel().registryAccess());
        spawner.saveCustomOnly(output);
        components.set(
                DataComponents.BLOCK_ENTITY_DATA,
                TypedEntityData.of(BlockEntityTypes.MOB_SPAWNER, output.buildResult())
        );
    }
}
