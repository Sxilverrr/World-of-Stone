package com.sxilverr.worldofstone.world;

import com.sxilverr.worldofstone.api.enums.OreVariant;
import com.sxilverr.worldofstone.config.WosConfig;
import com.sxilverr.worldofstone.registry.WosBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class OtherDimensionOreFeature extends Feature<NoneFeatureConfiguration> {

    public static final int OBSIDIAN_ATTEMPTS_PER_CHUNK = 16;

    private static final Map<Block, String> NETHER_HOSTS = Map.of(Blocks.NETHERRACK, "netherrack", Blocks.BLACKSTONE, "blackstone", Blocks.BASALT, "basalt");

    public OtherDimensionOreFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    public static int attempts(OreVariant ore, boolean nether) {
        double attempts = nether ? ore.netherAttempts * WosConfig.netherOreAttemptsMultiplier : ore.endAttempts * WosConfig.endOreAttemptsMultiplier;
        return Math.max(0, (int) Math.round(attempts));
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel level = ctx.level();
        BlockPos origin = ctx.origin();
        RandomSource rand = ctx.random();
        ResourceKey<Level> dim = level.getLevel().dimension();
        boolean isNether = dim == Level.NETHER;
        boolean isEnd = dim == Level.END;
        boolean genOres = (isNether && WosConfig.allowVanillaOresInNether) || (isEnd && WosConfig.allowVanillaOresInEnd);
        boolean genObsidian = WosConfig.allowObsidianOreVariants;
        if (!genOres && !genObsidian) return false;

        int placed = 0;
        if (genOres) {
            int yMax = isNether ? 128 : level.getMaxBuildHeight();
            for (OreVariant ore : OreVariant.VALUES) {
                int attempts = attempts(ore, isNether);
                for (int i = 0; i < attempts; i++) {
                    int x = origin.getX() + rand.nextInt(16);
                    int z = origin.getZ() + rand.nextInt(16);
                    int y = rand.nextInt(Math.max(1, yMax));
                    if (placeVein(level, new BlockPos(x, y, z), ore, isNether, rand)) placed++;
                }
            }
        }

        if (genObsidian) {
            for (int i = 0; i < OBSIDIAN_ATTEMPTS_PER_CHUNK; i++) {
                int x = origin.getX() + rand.nextInt(16);
                int z = origin.getZ() + rand.nextInt(16);
                int y = level.getMinBuildHeight() + rand.nextInt(Math.max(1, level.getMaxBuildHeight() - level.getMinBuildHeight()));
                BlockPos pos = new BlockPos(x, y, z);
                if (level.getBlockState(pos).is(Blocks.OBSIDIAN)) {
                    Block ore = WosBlocks.ore("obsidian", OreVariant.VALUES[rand.nextInt(OreVariant.VALUES.length)]);
                    if (ore != null) {
                        level.setBlock(pos, ore.defaultBlockState(), 2);
                        placed++;
                    }
                }
            }
        }
        return placed > 0;
    }

    private static boolean placeVein(WorldGenLevel level, BlockPos center, OreVariant ore, boolean isNether, RandomSource rand) {
        int radius = Math.max(1, (int) Math.ceil(Math.cbrt(ore.veinSize)));
        List<BlockPos> candidates = new ArrayList<>();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (dx * dx + dy * dy + dz * dz <= radius * radius) candidates.add(center.offset(dx, dy, dz));
                }
            }
        }
        Collections.shuffle(candidates, new Random(rand.nextLong()));
        int placed = 0;
        for (BlockPos p : candidates) {
            if (placed >= ore.veinSize) break;
            Block here = level.getBlockState(p).getBlock();
            String host = isNether ? NETHER_HOSTS.get(here) : here == Blocks.END_STONE ? "end_stone" : null;
            if (host == null) continue;
            Block oreBlock = WosBlocks.ore(host, ore);
            if (oreBlock == null) continue;
            level.setBlock(p, oreBlock.defaultBlockState(), 2);
            placed++;
        }
        return placed > 0;
    }
}
