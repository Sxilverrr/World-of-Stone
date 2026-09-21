package com.sxilverr.worldofstone.mixin;

import com.sxilverr.worldofstone.config.WosConfig;
import com.sxilverr.worldofstone.registry.WosBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InfestedBlock.class)
public abstract class InfestedBlockMixin {

    @Inject(method = "isCompatibleHostBlock", at = @At("HEAD"), cancellable = true)
    private static void wos$gateInfestedConversion(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (WosConfig.enableInfestedBlocks) return;
        Block host = state.getBlock();
        if (WosBlocks.isInfestedHost(host)) {
            cir.setReturnValue(false);
        }
    }
}
