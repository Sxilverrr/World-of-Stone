package com.sxilverr.worldofstone.world;

import com.sxilverr.worldofstone.config.WosConfig;
import com.sxilverr.worldofstone.registry.WosBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Map;

public class VanillaHostSurfaceFeature extends Feature<NoneFeatureConfiguration> {

    private static final Map<Block, String> HOSTS = Map.of(Blocks.GRANITE, "granite", Blocks.DIORITE, "diorite", Blocks.ANDESITE, "andesite", Blocks.TUFF, "tuff");

    public VanillaHostSurfaceFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        boolean snowed = WosConfig.allowSnowedVanillaVariants;
        boolean overgrown = WosConfig.allowOvergrownVanillaVariants;
        if (!snowed && !overgrown) return false;

        WorldGenLevel level = ctx.level();
        ChunkAccess chunk = level.getChunk(ctx.origin());
        ChunkPos cpos = chunk.getPos();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                for (int y = level.getMinBuildHeight(); y < level.getMaxBuildHeight(); y++) {
                    pos.set(cpos.getMinBlockX() + dx, y, cpos.getMinBlockZ() + dz);
                    String host = HOSTS.get(chunk.getBlockState(pos).getBlock());
                    if (host == null || !StrataWorldgen.atSurface(level, pos)) continue;
                    Block target = StrataWorldgen.surfaceVariant(level, pos,
                            snowed ? WosBlocks.VANILLA_HOST_SNOWED.get(host).get() : null,
                            overgrown ? WosBlocks.VANILLA_HOST_OVERGROWN.get(host).get() : null);
                    if (target != null) {
                        chunk.setBlockState(pos, target.defaultBlockState(), false);
                    }
                }
            }
        }
        return true;
    }
}
