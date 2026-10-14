package com.sxilverr.worldofstone.mixin;

import com.sxilverr.worldofstone.api.enums.OreVariant;
import com.sxilverr.worldofstone.api.enums.StrataVariant;
import com.sxilverr.worldofstone.api.enums.VanillaOreHost;
import com.sxilverr.worldofstone.registry.WosBlocks;
import com.mojang.logging.LogUtils;
import jeresources.jei.worldgen.WorldGenWrapper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(value = WorldGenWrapper.class, remap = false)
public abstract class JERWorldGenWrapperMixin {
    private static final Logger WOS$LOGGER = LogUtils.getLogger();

    private static volatile Map<Block, OreVariant> WOS$VANILLA_ORES;
    private static volatile boolean WOS$LOGGED_ONCE;

    private static Map<Block, OreVariant> wos$vanillaOres() {
        Map<Block, OreVariant> m = WOS$VANILLA_ORES;
        if (m != null) return m;
        m = new HashMap<>();
        m.put(Blocks.COAL_ORE, OreVariant.COAL);
        m.put(Blocks.IRON_ORE, OreVariant.IRON);
        m.put(Blocks.GOLD_ORE, OreVariant.GOLD);
        m.put(Blocks.DIAMOND_ORE, OreVariant.DIAMOND);
        m.put(Blocks.EMERALD_ORE, OreVariant.EMERALD);
        m.put(Blocks.REDSTONE_ORE, OreVariant.REDSTONE);
        m.put(Blocks.LAPIS_ORE, OreVariant.LAPIS);
        m.put(Blocks.COPPER_ORE, OreVariant.COPPER);
        WOS$VANILLA_ORES = m;
        return m;
    }

    @Inject(method = "getBlocks", at = @At("RETURN"), cancellable = true, remap = false)
    private void wos$appendVariants(CallbackInfoReturnable<List<ItemStack>> cir) {
        List<ItemStack> blocks = cir.getReturnValue();
        if (blocks == null || blocks.isEmpty()) return;
        ItemStack primary = blocks.get(0);
        Block block = Block.byItem(primary.getItem());
        OreVariant ore = wos$vanillaOres().get(block);
        if (ore == null) return;

        int before = blocks.size();
        for (StrataVariant v : StrataVariant.ALL) wos$add(blocks, v.toString(), ore);
        for (VanillaOreHost h : VanillaOreHost.VALUES) {
            if (h.dimension == VanillaOreHost.HostDimension.OVERWORLD) wos$add(blocks, h.toString(), ore);
        }
        if (!WOS$LOGGED_ONCE) {
            WOS$LOGGED_ONCE = true;
            WOS$LOGGER.info("[WorldOfStone] JER mixin appended {} variants for {}", blocks.size() - before, ore);
        }
    }

    private static void wos$add(List<ItemStack> blocks, String hostName, OreVariant ore) {
        Block variant = WosBlocks.ore(hostName, ore);
        if (variant != null) blocks.add(new ItemStack(variant));
    }
}
