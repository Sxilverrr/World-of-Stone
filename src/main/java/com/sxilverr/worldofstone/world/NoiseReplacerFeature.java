package com.sxilverr.worldofstone.world;

import com.sxilverr.worldofstone.api.enums.NoiseType;
import com.sxilverr.worldofstone.api.enums.OreVariant;
import com.sxilverr.worldofstone.api.enums.StrataVariant;
import com.sxilverr.worldofstone.api.enums.WorldgenSystem;
import com.sxilverr.worldofstone.config.WosConfig;
import com.sxilverr.worldofstone.registry.StrataBlock;
import com.sxilverr.worldofstone.registry.WosBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class NoiseReplacerFeature extends Feature<NoneFeatureConfiguration> {

    private static final long GATE_SALT = 0xA5A5C3C3L;

    public NoiseReplacerFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        if (!WosConfig.stoneReplacementEnabled || WosConfig.worldgenSystem != WorldgenSystem.NOISE) return false;

        WorldGenLevel level = ctx.level();
        ChunkAccess chunk = level.getChunk(ctx.origin());
        ChunkPos cpos = chunk.getPos();
        int minY = level.getMinBuildHeight();
        int seaLevel = level.getSeaLevel();
        if (seaLevel - minY <= 0) return false;
        int strataBottomY = WosConfig.allowStrataInDeepslate ? minY : 0;
        int strataRange = Math.max(1, seaLevel - strataBottomY);
        int loopMaxY = WosConfig.replaceStoneAboveGround ? level.getMaxBuildHeight() : seaLevel;
        double bottomRatio = WosConfig.strataBottomRatio;
        double middleRatio = Math.max(bottomRatio, WosConfig.strataMiddleRatio);
        int variantChance = Mth.clamp(WosConfig.variantChance, 0, 100);
        if (variantChance == 0) return false;

        int scale = Math.max(1, WosConfig.noiseScale);
        long seed = level.getSeed();
        NoiseType noiseType = WosConfig.noiseType;
        int octaves = Mth.clamp(WosConfig.noiseOctaves, 1, 8);

        int[] totals = new int[StrataWorldgen.ANY + 1];
        for (int i = 0; i < StrataWorldgen.ANY; i++) {
            totals[i] = StrataWorldgen.totalWeight(StrataWorldgen.pool(i));
            totals[StrataWorldgen.ANY] += totals[i];
        }

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                int wx = cpos.getMinBlockX() + dx;
                int wz = cpos.getMinBlockZ() + dz;
                for (int y = minY; y < loopMaxY; y++) {
                    pos.set(wx, y, wz);
                    BlockState state = chunk.getBlockState(pos);
                    StrataBlock kind = StrataWorldgen.kind(state);
                    OreVariant ore = kind == null && WosConfig.replaceVanillaOres ? StrataWorldgen.VANILLA_ORES.get(state.getBlock()) : null;
                    if (kind == null && ore == null) continue;

                    if (variantChance < 100 && sample(noiseType, octaves, wx, y, wz, scale, seed ^ GATE_SALT) * 100.0 >= variantChance) continue;

                    if (ore != null) {
                        Block hostOre = StrataWorldgen.hostOre(chunk, wx, y, wz, ore);
                        if (hostOre != null) {
                            chunk.setBlockState(pos, hostOre.defaultBlockState(), false);
                            continue;
                        }
                        if (!WosConfig.allowStrataInDeepslate && StrataWorldgen.DEEPSLATE_ORES.contains(state.getBlock())) continue;
                    }

                    int index = StrataWorldgen.poolIndex((double) (y - strataBottomY) / strataRange, bottomRatio, middleRatio);
                    int total = totals[index];
                    if (total <= 0) continue;
                    double n = sample(noiseType, octaves, wx, y, wz, scale, seed ^ ((index + 1) * 0x1111L));
                    StrataVariant variant = StrataWorldgen.pick(StrataWorldgen.pool(index), Mth.clamp((int) Math.floor(n * total), 0, total - 1));
                    if (variant == null) continue;

                    Block target = ore != null ? WosBlocks.ore(variant.toString(), ore) : StrataWorldgen.block(level, pos, kind, variant);
                    if (target != null) {
                        chunk.setBlockState(pos, target.defaultBlockState(), false);
                    }
                }
            }
        }
        return true;
    }

    private static double sample(NoiseType type, int octaves, int x, int y, int z, int scale, long salt) {
        double sx = (double) x / scale;
        double sy = (double) y / scale;
        double sz = (double) z / scale;
        if (octaves <= 1) {
            return Mth.clamp(baseAt(type, sx, sy, sz, salt), 0.0, 1.0);
        }
        double total = 0.0;
        double maxAmp = 0.0;
        double amplitude = 1.0;
        double frequency = 1.0;
        for (int o = 0; o < octaves; o++) {
            total += amplitude * baseAt(type, sx * frequency, sy * frequency, sz * frequency, salt + (long) o * 7919L);
            maxAmp += amplitude;
            amplitude *= 0.5;
            frequency *= 2.0;
        }
        return Mth.clamp(total / Math.max(0.001, maxAmp), 0.0, 1.0);
    }

    private static double baseAt(NoiseType type, double sx, double sy, double sz, long salt) {
        switch (type) {
            case PERLIN:
                return (perlinD(sx, sy, sz, salt) + 1.0) * 0.5;
            case SIMPLEX:
                return (simplexD(sx, sy, sz, salt) + 1.0) * 0.5;
            case RIDGED:
            case RIDGED_MULTIFRACTAL:
                return 1.0 - Math.abs(perlinD(sx, sy, sz, salt));
            case BILLOW:
                return Math.abs(perlinD(sx, sy, sz, salt));
            case CELLULAR:
                return cellularAt(sx, sy, sz, salt);
            case DOMAIN_WARPED:
                return domainWarpedAt(sx, sy, sz, salt);
            case VALUE:
            case FRACTIONAL_BROWNIAN_MOTION:
            default:
                return valueAt(sx, sy, sz, salt);
        }
    }

    private static double valueAt(double sx, double sy, double sz, long salt) {
        int xi = (int) Math.floor(sx);
        int yi = (int) Math.floor(sy);
        int zi = (int) Math.floor(sz);
        double xf = sx - xi;
        double yf = sy - yi;
        double zf = sz - zi;
        double sxx = xf * xf * (3.0 - 2.0 * xf);
        double syy = yf * yf * (3.0 - 2.0 * yf);
        double szz = zf * zf * (3.0 - 2.0 * zf);
        double v000 = StrataWorldgen.hash01(xi, yi, zi, salt);
        double v100 = StrataWorldgen.hash01(xi + 1, yi, zi, salt);
        double v010 = StrataWorldgen.hash01(xi, yi + 1, zi, salt);
        double v110 = StrataWorldgen.hash01(xi + 1, yi + 1, zi, salt);
        double v001 = StrataWorldgen.hash01(xi, yi, zi + 1, salt);
        double v101 = StrataWorldgen.hash01(xi + 1, yi, zi + 1, salt);
        double v011 = StrataWorldgen.hash01(xi, yi + 1, zi + 1, salt);
        double v111 = StrataWorldgen.hash01(xi + 1, yi + 1, zi + 1, salt);
        double a00 = v000 + sxx * (v100 - v000);
        double a10 = v010 + sxx * (v110 - v010);
        double a01 = v001 + sxx * (v101 - v001);
        double a11 = v011 + sxx * (v111 - v011);
        double b0 = a00 + syy * (a10 - a00);
        double b1 = a01 + syy * (a11 - a01);
        return b0 + szz * (b1 - b0);
    }

    private static double perlinD(double sx, double sy, double sz, long salt) {
        int xi = (int) Math.floor(sx);
        int yi = (int) Math.floor(sy);
        int zi = (int) Math.floor(sz);
        double xf = sx - xi;
        double yf = sy - yi;
        double zf = sz - zi;
        double u = Mth.smoothstep(xf);
        double v = Mth.smoothstep(yf);
        double w = Mth.smoothstep(zf);
        double n000 = grad3D(StrataWorldgen.hash(xi, yi, zi, salt), xf, yf, zf);
        double n100 = grad3D(StrataWorldgen.hash(xi + 1, yi, zi, salt), xf - 1, yf, zf);
        double n010 = grad3D(StrataWorldgen.hash(xi, yi + 1, zi, salt), xf, yf - 1, zf);
        double n110 = grad3D(StrataWorldgen.hash(xi + 1, yi + 1, zi, salt), xf - 1, yf - 1, zf);
        double n001 = grad3D(StrataWorldgen.hash(xi, yi, zi + 1, salt), xf, yf, zf - 1);
        double n101 = grad3D(StrataWorldgen.hash(xi + 1, yi, zi + 1, salt), xf - 1, yf, zf - 1);
        double n011 = grad3D(StrataWorldgen.hash(xi, yi + 1, zi + 1, salt), xf, yf - 1, zf - 1);
        double n111 = grad3D(StrataWorldgen.hash(xi + 1, yi + 1, zi + 1, salt), xf - 1, yf - 1, zf - 1);
        double a00 = Mth.lerp(u, n000, n100);
        double a10 = Mth.lerp(u, n010, n110);
        double a01 = Mth.lerp(u, n001, n101);
        double a11 = Mth.lerp(u, n011, n111);
        double b0 = Mth.lerp(v, a00, a10);
        double b1 = Mth.lerp(v, a01, a11);
        double n = Mth.lerp(w, b0, b1);
        return Mth.clamp(n, -1.0, 1.0);
    }

    private static double simplexD(double sx, double sy, double sz, long salt) {
        final double F3 = 1.0 / 3.0;
        final double G3 = 1.0 / 6.0;
        double s = (sx + sy + sz) * F3;
        int i = (int) Math.floor(sx + s);
        int j = (int) Math.floor(sy + s);
        int k = (int) Math.floor(sz + s);
        double t = (i + j + k) * G3;
        double X0 = i - t;
        double Y0 = j - t;
        double Z0 = k - t;
        double x0 = sx - X0;
        double y0 = sy - Y0;
        double z0 = sz - Z0;

        int i1, j1, k1, i2, j2, k2;
        if (x0 >= y0) {
            if (y0 >= z0) { i1 = 1; j1 = 0; k1 = 0; i2 = 1; j2 = 1; k2 = 0; }
            else if (x0 >= z0) { i1 = 1; j1 = 0; k1 = 0; i2 = 1; j2 = 0; k2 = 1; }
            else { i1 = 0; j1 = 0; k1 = 1; i2 = 1; j2 = 0; k2 = 1; }
        } else {
            if (y0 < z0) { i1 = 0; j1 = 0; k1 = 1; i2 = 0; j2 = 1; k2 = 1; }
            else if (x0 < z0) { i1 = 0; j1 = 1; k1 = 0; i2 = 0; j2 = 1; k2 = 1; }
            else { i1 = 0; j1 = 1; k1 = 0; i2 = 1; j2 = 1; k2 = 0; }
        }

        double x1 = x0 - i1 + G3;
        double y1 = y0 - j1 + G3;
        double z1 = z0 - k1 + G3;
        double x2 = x0 - i2 + 2.0 * G3;
        double y2 = y0 - j2 + 2.0 * G3;
        double z2 = z0 - k2 + 2.0 * G3;
        double x3 = x0 - 1.0 + 3.0 * G3;
        double y3 = y0 - 1.0 + 3.0 * G3;
        double z3 = z0 - 1.0 + 3.0 * G3;

        double n0 = simplexCorner(i, j, k, x0, y0, z0, salt);
        double n1 = simplexCorner(i + i1, j + j1, k + k1, x1, y1, z1, salt);
        double n2 = simplexCorner(i + i2, j + j2, k + k2, x2, y2, z2, salt);
        double n3 = simplexCorner(i + 1, j + 1, k + 1, x3, y3, z3, salt);

        double n = 32.0 * (n0 + n1 + n2 + n3);
        return Mth.clamp(n, -1.0, 1.0);
    }

    private static double simplexCorner(int i, int j, int k, double x, double y, double z, long salt) {
        double t = 0.6 - x * x - y * y - z * z;
        if (t < 0) return 0.0;
        int h = StrataWorldgen.hash(i, j, k, salt);
        return t * t * t * t * grad3D(h, x, y, z);
    }

    private static double cellularAt(double sx, double sy, double sz, long salt) {
        int xi = (int) Math.floor(sx);
        int yi = (int) Math.floor(sy);
        int zi = (int) Math.floor(sz);
        double minDist2 = Double.MAX_VALUE;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dz = -1; dz <= 1; dz++) {
                    int cx = xi + dx;
                    int cy = yi + dy;
                    int cz = zi + dz;
                    double fx = cx + StrataWorldgen.hash01(cx, cy, cz, salt);
                    double fy = cy + StrataWorldgen.hash01(cx, cy, cz, salt + 7L);
                    double fz = cz + StrataWorldgen.hash01(cx, cy, cz, salt + 13L);
                    double ddx = fx - sx;
                    double ddy = fy - sy;
                    double ddz = fz - sz;
                    double d2 = ddx * ddx + ddy * ddy + ddz * ddz;
                    if (d2 < minDist2) minDist2 = d2;
                }
            }
        }
        return Math.min(1.0, Math.sqrt(minDist2) * 1.15);
    }

    private static double domainWarpedAt(double sx, double sy, double sz, long salt) {
        double wx = perlinD(sx, sy, sz, salt + 100L) * 0.5;
        double wy = perlinD(sx, sy, sz, salt + 200L) * 0.5;
        double wz = perlinD(sx, sy, sz, salt + 300L) * 0.5;
        return (perlinD(sx + wx, sy + wy, sz + wz, salt) + 1.0) * 0.5;
    }

    private static double grad3D(int hash, double x, double y, double z) {
        int h = hash & 15;
        double u = h < 8 ? x : y;
        double v;
        if (h < 4) v = y;
        else if (h == 12 || h == 14) v = x;
        else v = z;
        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }
}
