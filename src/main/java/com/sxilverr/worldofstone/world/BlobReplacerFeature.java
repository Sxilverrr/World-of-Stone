package com.sxilverr.worldofstone.world;

import com.sxilverr.worldofstone.api.enums.OreVariant;
import com.sxilverr.worldofstone.api.enums.StrataVariant;
import com.sxilverr.worldofstone.api.enums.WorldgenSystem;
import com.sxilverr.worldofstone.config.WosConfig;
import com.sxilverr.worldofstone.registry.StrataBlock;
import com.sxilverr.worldofstone.registry.WosBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.List;

public class BlobReplacerFeature extends Feature<NoneFeatureConfiguration> {

    public BlobReplacerFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        if (!WosConfig.stoneReplacementEnabled || WosConfig.worldgenSystem != WorldgenSystem.BLOB) return false;

        WorldGenLevel level = ctx.level();
        RandomSource rand = ctx.random();
        ChunkPos cpos = level.getChunk(ctx.origin()).getPos();
        int minY = level.getMinBuildHeight();
        int seaLevel = level.getSeaLevel();
        if (seaLevel - minY <= 0) return false;
        int strataBottomY = WosConfig.allowStrataInDeepslate ? minY : 0;
        int strataRange = Math.max(1, seaLevel - strataBottomY);
        int blobSize = Math.max(1, WosConfig.blobSize);
        int yScanRange = Math.max(1, (WosConfig.replaceStoneAboveGround ? level.getMaxBuildHeight() : seaLevel) - minY);
        double bottomRatio = WosConfig.strataBottomRatio;
        double middleRatio = Math.max(bottomRatio, WosConfig.strataMiddleRatio);
        int variantChance = Mth.clamp(WosConfig.variantChance, 0, 100);

        for (int i = 0; i < Math.max(0, WosConfig.blobsPerChunk); i++) {
            if (variantChance < 100 && rand.nextInt(100) >= variantChance) continue;
            int x = cpos.getMinBlockX() + rand.nextInt(16);
            int z = cpos.getMinBlockZ() + rand.nextInt(16);
            int y = minY + rand.nextInt(yScanRange);

            List<StrataVariant> pool = StrataWorldgen.pool(StrataWorldgen.poolIndex((double) (y - strataBottomY) / strataRange, bottomRatio, middleRatio));
            int total = StrataWorldgen.totalWeight(pool);
            if (total <= 0) continue;
            StrataVariant variant = StrataWorldgen.pick(pool, rand.nextInt(total));
            if (variant == null) continue;
            placeBlob(level, cpos, new BlockPos(x, y, z), blobSize, variant, rand);
        }
        return true;
    }

    private static void placeBlob(WorldGenLevel level, ChunkPos cpos, BlockPos center, int size, StrataVariant variant, RandomSource rand) {
        double radius = Math.max(1.0, size / 2.0);
        int r = (int) Math.ceil(radius);
        double rSq = radius * radius;
        double edgeSq = (radius - 0.7) * (radius - 0.7);

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int dx = -r; dx <= r; dx++) {
            for (int dy = -r; dy <= r; dy++) {
                for (int dz = -r; dz <= r; dz++) {
                    double distSq = dx * dx + dy * dy + dz * dz;
                    if (distSq > rSq) continue;
                    if (distSq > edgeSq && rand.nextFloat() < 0.45f) continue;
                    int px = center.getX() + dx;
                    int py = center.getY() + dy;
                    int pz = center.getZ() + dz;
                    if (px < cpos.getMinBlockX() || px > cpos.getMaxBlockX() || pz < cpos.getMinBlockZ() || pz > cpos.getMaxBlockZ()) continue;
                    if (py < level.getMinBuildHeight() || py >= level.getMaxBuildHeight()) continue;
                    pos.set(px, py, pz);
                    BlockState state = level.getBlockState(pos);
                    StrataBlock kind = StrataWorldgen.kind(state);
                    Block target = null;
                    if (kind != null) {
                        target = StrataWorldgen.block(level, pos, kind, variant);
                    } else if (WosConfig.replaceVanillaOres && (WosConfig.allowStrataInDeepslate || !StrataWorldgen.DEEPSLATE_ORES.contains(state.getBlock()))) {
                        OreVariant ore = StrataWorldgen.VANILLA_ORES.get(state.getBlock());
                        if (ore != null) target = WosBlocks.ore(variant.toString(), ore);
                    }
                    if (target != null) {
                        level.setBlock(pos, target.defaultBlockState(), 2);
                    }
                }
            }
        }
    }
}
