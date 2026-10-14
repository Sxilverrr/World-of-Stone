package com.sxilverr.worldofstone.world;

import com.sxilverr.worldofstone.api.enums.OreVariant;
import com.sxilverr.worldofstone.api.enums.StrataVariant;
import com.sxilverr.worldofstone.api.enums.VanillaOreHost;
import com.sxilverr.worldofstone.config.WosConfig;
import com.sxilverr.worldofstone.registry.StrataBlock;
import com.sxilverr.worldofstone.registry.WosBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.List;
import java.util.Map;
import java.util.Set;

final class StrataWorldgen {

    static final int ANY = 3;

    static final Map<Block, OreVariant> VANILLA_ORES = Map.ofEntries(
            Map.entry(Blocks.COAL_ORE, OreVariant.COAL), Map.entry(Blocks.DEEPSLATE_COAL_ORE, OreVariant.COAL),
            Map.entry(Blocks.IRON_ORE, OreVariant.IRON), Map.entry(Blocks.DEEPSLATE_IRON_ORE, OreVariant.IRON),
            Map.entry(Blocks.GOLD_ORE, OreVariant.GOLD), Map.entry(Blocks.DEEPSLATE_GOLD_ORE, OreVariant.GOLD),
            Map.entry(Blocks.DIAMOND_ORE, OreVariant.DIAMOND), Map.entry(Blocks.DEEPSLATE_DIAMOND_ORE, OreVariant.DIAMOND),
            Map.entry(Blocks.EMERALD_ORE, OreVariant.EMERALD), Map.entry(Blocks.DEEPSLATE_EMERALD_ORE, OreVariant.EMERALD),
            Map.entry(Blocks.REDSTONE_ORE, OreVariant.REDSTONE), Map.entry(Blocks.DEEPSLATE_REDSTONE_ORE, OreVariant.REDSTONE),
            Map.entry(Blocks.LAPIS_ORE, OreVariant.LAPIS), Map.entry(Blocks.DEEPSLATE_LAPIS_ORE, OreVariant.LAPIS),
            Map.entry(Blocks.COPPER_ORE, OreVariant.COPPER), Map.entry(Blocks.DEEPSLATE_COPPER_ORE, OreVariant.COPPER));

    static final Set<Block> DEEPSLATE_ORES = Set.of(Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.DEEPSLATE_EMERALD_ORE, Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.DEEPSLATE_LAPIS_ORE,
            Blocks.DEEPSLATE_COPPER_ORE);

    private static final Map<Block, VanillaOreHost> VANILLA_HOSTS = Map.of(
            Blocks.GRANITE, VanillaOreHost.GRANITE, Blocks.DIORITE, VanillaOreHost.DIORITE,
            Blocks.ANDESITE, VanillaOreHost.ANDESITE, Blocks.TUFF, VanillaOreHost.TUFF);

    private static final int[][] NEIGHBORS = {{-1, 0, 0}, {1, 0, 0}, {0, -1, 0}, {0, 1, 0}, {0, 0, -1}, {0, 0, 1}};

    static List<StrataVariant> pool(int index) {
        return index == ANY ? StrataVariant.ALL : StrataVariant.Family.values()[index].variants();
    }

    static int poolIndex(double ratio, double bottomRatio, double middleRatio) {
        if (WosConfig.ignoreStrataHeightRestrictions) return ANY;
        return ratio < bottomRatio ? 0 : ratio < middleRatio ? 1 : 2;
    }

    static int totalWeight(List<StrataVariant> pool) {
        int total = 0;
        for (StrataVariant v : pool) total += WosConfig.getVariantWeight(v);
        return total;
    }

    static StrataVariant pick(List<StrataVariant> pool, int roll) {
        int acc = 0;
        for (StrataVariant v : pool) {
            acc += WosConfig.getVariantWeight(v);
            if (roll < acc) return v;
        }
        return null;
    }

    static StrataBlock kind(BlockState state) {
        boolean red = WosConfig.replaceRedSandAndSandstone;
        if (state.is(Blocks.STONE) || (WosConfig.allowStrataInDeepslate && state.is(Blocks.DEEPSLATE))) return StrataBlock.STONE;
        if ((WosConfig.replaceSand && state.is(Blocks.SAND)) || (red && state.is(Blocks.RED_SAND))) return StrataBlock.SAND;
        if ((WosConfig.replaceSandstone && state.is(Blocks.SANDSTONE)) || (red && state.is(Blocks.RED_SANDSTONE))) return StrataBlock.SANDSTONE;
        if (WosConfig.replaceGravel && state.is(Blocks.GRAVEL)) return StrataBlock.GRAVEL;
        if (WosConfig.replaceClay && state.is(Blocks.CLAY)) return StrataBlock.CLAY;
        return null;
    }

    static Block block(WorldGenLevel level, BlockPos pos, StrataBlock kind, StrataVariant v) {
        if (kind == StrataBlock.STONE && atSurface(level, pos)) {
            Block surface = surfaceVariant(level, pos,
                    WosConfig.allowSnowedStrata ? StrataBlock.SNOWED.get(v) : null,
                    WosConfig.allowOvergrownStrata ? StrataBlock.OVERGROWN.get(v) : null);
            if (surface != null) return surface;
        }
        return kind.get(v);
    }

    static boolean atSurface(WorldGenLevel level, BlockPos pos) {
        BlockState above = level.getBlockState(pos.above());
        if (above.is(Blocks.SNOW) || above.is(Blocks.SNOW_BLOCK)) return true;
        return pos.getY() + 1 >= level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
    }

    static Block surfaceVariant(WorldGenLevel level, BlockPos pos, Block snowed, Block overgrown) {
        Biome biome = level.getBiome(pos).value();
        if (biome.getBaseTemperature() <= 0.15f) return snowed;
        return biome.getPrecipitationAt(pos) == Biome.Precipitation.RAIN ? overgrown : null;
    }

    static Block hostOre(ChunkAccess chunk, int x, int y, int z, OreVariant ore) {
        ChunkPos cpos = chunk.getPos();
        int[] counts = new int[VanillaOreHost.VALUES.length];
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int[] o : NEIGHBORS) {
            int nx = x + o[0];
            int nz = z + o[2];
            if (nx < cpos.getMinBlockX() || nx > cpos.getMaxBlockX() || nz < cpos.getMinBlockZ() || nz > cpos.getMaxBlockZ()) continue;
            VanillaOreHost host = VANILLA_HOSTS.get(chunk.getBlockState(pos.set(nx, y + o[1], nz)).getBlock());
            if (host != null) counts[host.ordinal()]++;
        }
        VanillaOreHost best = null;
        for (VanillaOreHost host : VanillaOreHost.VALUES) {
            if (counts[host.ordinal()] > (best == null ? 0 : counts[best.ordinal()])) best = host;
        }
        return best != null && WosConfig.isVanillaOreHostEnabled(best) ? WosBlocks.ore(best.toString(), ore) : null;
    }

    static int hash(int x, int y, int z, long salt) {
        long h = (long) x * 341873128712L + (long) y * 132897987541L + (long) z * 104395303L + salt * 7919L;
        h ^= h >>> 33;
        h *= 0xff51afd7ed558ccdL;
        h ^= h >>> 33;
        return (int) (h & 0x7FFFFFFF);
    }

    static double hash01(int x, int y, int z, long salt) {
        return (double) hash(x, y, z, salt) / (double) 0x7FFFFFFF;
    }

    private StrataWorldgen() {
    }
}
