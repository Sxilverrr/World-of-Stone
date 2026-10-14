package com.sxilverr.worldofstone.world;

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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.HashMap;
import java.util.Map;

public class StrataReplacerFeature extends Feature<NoneFeatureConfiguration> {

    private static final Map<Block, StrataBlock> STRUCTURE = new HashMap<>();
    private static final Map<Block, StrataBlock> RED_SANDSTONE_STRUCTURE = new HashMap<>();

    static {
        STRUCTURE.put(Blocks.COBBLESTONE, StrataBlock.COBBLE);
        STRUCTURE.put(Blocks.COBBLESTONE_STAIRS, StrataBlock.COBBLE_STAIRS);
        STRUCTURE.put(Blocks.COBBLESTONE_SLAB, StrataBlock.COBBLE_SLAB);
        STRUCTURE.put(Blocks.COBBLESTONE_WALL, StrataBlock.COBBLE_WALL);
        STRUCTURE.put(Blocks.MOSSY_COBBLESTONE, StrataBlock.MOSSY_COBBLE);
        STRUCTURE.put(Blocks.MOSSY_COBBLESTONE_STAIRS, StrataBlock.MOSSY_COBBLE_STAIRS);
        STRUCTURE.put(Blocks.MOSSY_COBBLESTONE_SLAB, StrataBlock.MOSSY_COBBLE_SLAB);
        STRUCTURE.put(Blocks.MOSSY_COBBLESTONE_WALL, StrataBlock.MOSSY_COBBLE_WALL);
        STRUCTURE.put(Blocks.STONE_BRICKS, StrataBlock.BRICK);
        STRUCTURE.put(Blocks.STONE_BRICK_STAIRS, StrataBlock.BRICK_STAIRS);
        STRUCTURE.put(Blocks.STONE_BRICK_SLAB, StrataBlock.BRICK_SLAB);
        STRUCTURE.put(Blocks.STONE_BRICK_WALL, StrataBlock.BRICK_WALL);
        STRUCTURE.put(Blocks.MOSSY_STONE_BRICKS, StrataBlock.MOSSY_BRICK);
        STRUCTURE.put(Blocks.MOSSY_STONE_BRICK_STAIRS, StrataBlock.MOSSY_BRICK_STAIRS);
        STRUCTURE.put(Blocks.MOSSY_STONE_BRICK_SLAB, StrataBlock.MOSSY_BRICK_SLAB);
        STRUCTURE.put(Blocks.MOSSY_STONE_BRICK_WALL, StrataBlock.MOSSY_BRICK_WALL);
        STRUCTURE.put(Blocks.SANDSTONE_STAIRS, StrataBlock.SANDSTONE_STAIRS);
        STRUCTURE.put(Blocks.SANDSTONE_SLAB, StrataBlock.SANDSTONE_SLAB);
        STRUCTURE.put(Blocks.SANDSTONE_WALL, StrataBlock.SANDSTONE_WALL);
        STRUCTURE.put(Blocks.CUT_SANDSTONE, StrataBlock.SANDSTONE_CUT);
        STRUCTURE.put(Blocks.CUT_SANDSTONE_SLAB, StrataBlock.SANDSTONE_CUT_SLAB);
        STRUCTURE.put(Blocks.CHISELED_SANDSTONE, StrataBlock.SANDSTONE_CHISELED);
        STRUCTURE.put(Blocks.SMOOTH_SANDSTONE, StrataBlock.SANDSTONE_SMOOTH);
        STRUCTURE.put(Blocks.SMOOTH_SANDSTONE_STAIRS, StrataBlock.SANDSTONE_SMOOTH_STAIRS);
        STRUCTURE.put(Blocks.SMOOTH_SANDSTONE_SLAB, StrataBlock.SANDSTONE_SMOOTH_SLAB);
        STRUCTURE.put(Blocks.SMOOTH_STONE, StrataBlock.SMOOTH);
        STRUCTURE.put(Blocks.SMOOTH_STONE_SLAB, StrataBlock.SMOOTH_SLAB);

        RED_SANDSTONE_STRUCTURE.put(Blocks.RED_SANDSTONE_STAIRS, StrataBlock.SANDSTONE_STAIRS);
        RED_SANDSTONE_STRUCTURE.put(Blocks.RED_SANDSTONE_SLAB, StrataBlock.SANDSTONE_SLAB);
        RED_SANDSTONE_STRUCTURE.put(Blocks.RED_SANDSTONE_WALL, StrataBlock.SANDSTONE_WALL);
        RED_SANDSTONE_STRUCTURE.put(Blocks.CUT_RED_SANDSTONE, StrataBlock.SANDSTONE_CUT);
        RED_SANDSTONE_STRUCTURE.put(Blocks.CUT_RED_SANDSTONE_SLAB, StrataBlock.SANDSTONE_CUT_SLAB);
        RED_SANDSTONE_STRUCTURE.put(Blocks.CHISELED_RED_SANDSTONE, StrataBlock.SANDSTONE_CHISELED);
        RED_SANDSTONE_STRUCTURE.put(Blocks.SMOOTH_RED_SANDSTONE, StrataBlock.SANDSTONE_SMOOTH);
        RED_SANDSTONE_STRUCTURE.put(Blocks.SMOOTH_RED_SANDSTONE_STAIRS, StrataBlock.SANDSTONE_SMOOTH_STAIRS);
        RED_SANDSTONE_STRUCTURE.put(Blocks.SMOOTH_RED_SANDSTONE_SLAB, StrataBlock.SANDSTONE_SMOOTH_SLAB);
    }

    public StrataReplacerFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        if (!WosConfig.stoneReplacementEnabled || WosConfig.worldgenSystem != WorldgenSystem.CHUNK) return false;
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

        int patchBlockSize = Math.max(16, WosConfig.patchSize * 16);
        int patchX = Math.floorDiv(cpos.getMinBlockX(), patchBlockSize);
        int patchZ = Math.floorDiv(cpos.getMinBlockZ(), patchBlockSize);
        int xzDither = Math.max(0, WosConfig.strataXZDither);
        double yDitherRatio = (double) Math.max(0, WosConfig.strataYDither) / strataRange;
        int variantChance = Mth.clamp(WosConfig.variantChance, 0, 100);

        StrataVariant[][][] picks = new StrataVariant[3][3][StrataWorldgen.ANY + 1];
        boolean[][] patchHasVariants = new boolean[3][3];
        for (int ddx = -1; ddx <= 1; ddx++) {
            for (int ddz = -1; ddz <= 1; ddz++) {
                int px = patchX + ddx;
                int pz = patchZ + ddz;
                patchHasVariants[ddx + 1][ddz + 1] = variantChance >= 100 || StrataWorldgen.hash(px, pz, 0, 7777L) % 100 < variantChance;
                for (int i = 0; i <= StrataWorldgen.ANY; i++) {
                    int total = StrataWorldgen.totalWeight(StrataWorldgen.pool(i));
                    if (total > 0) picks[ddx + 1][ddz + 1][i] = StrataWorldgen.pick(StrataWorldgen.pool(i), StrataWorldgen.hash(px, pz, 0, i + 1) % total);
                }
            }
        }
        if (!patchHasVariants[1][1] && xzDither == 0) return false;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int dx = 0; dx < 16; dx++) {
            for (int dz = 0; dz < 16; dz++) {
                int wx = cpos.getMinBlockX() + dx;
                int wz = cpos.getMinBlockZ() + dz;

                int offX = 0;
                int offZ = 0;
                if (xzDither > 0) {
                    int xJitter = StrataWorldgen.hash(wx, wz, 0, 100L) % (2 * xzDither + 1) - xzDither;
                    int zJitter = StrataWorldgen.hash(wx, wz, 0, 200L) % (2 * xzDither + 1) - xzDither;
                    offX = Mth.clamp(Math.floorDiv(wx + xJitter, patchBlockSize) - patchX, -1, 1);
                    offZ = Mth.clamp(Math.floorDiv(wz + zJitter, patchBlockSize) - patchZ, -1, 1);
                }
                if (!patchHasVariants[offX + 1][offZ + 1]) continue;
                StrataVariant[] patch = picks[offX + 1][offZ + 1];

                for (int y = minY; y < loopMaxY; y++) {
                    pos.set(wx, y, wz);
                    BlockState state = chunk.getBlockState(pos);
                    Block block = state.getBlock();

                    StrataBlock kind = StrataWorldgen.kind(state);
                    StrataBlock structure = null;
                    if (kind == null && WosConfig.enableStructureVariantGeneration) {
                        structure = STRUCTURE.get(block);
                        if (structure == null && WosConfig.replaceRedSandAndSandstone) structure = RED_SANDSTONE_STRUCTURE.get(block);
                    }
                    OreVariant ore = kind == null && structure == null && WosConfig.replaceVanillaOres ? StrataWorldgen.VANILLA_ORES.get(block) : null;
                    if (kind == null && structure == null && ore == null) continue;

                    if (ore != null) {
                        Block hostOre = StrataWorldgen.hostOre(chunk, wx, y, wz, ore);
                        if (hostOre != null) {
                            chunk.setBlockState(pos, hostOre.defaultBlockState(), false);
                            continue;
                        }
                        if (!WosConfig.allowStrataInDeepslate && StrataWorldgen.DEEPSLATE_ORES.contains(block)) continue;
                    }

                    double ratio = (double) (y - strataBottomY) / strataRange;
                    if (yDitherRatio > 0.0) {
                        ratio += (StrataWorldgen.hash01(wx, y, wz, 999L) * 2.0 - 1.0) * yDitherRatio;
                    }
                    StrataVariant variant = patch[StrataWorldgen.poolIndex(ratio, bottomRatio, middleRatio)];
                    if (variant == null) continue;

                    Block target;
                    if (structure != null) target = structure.get(variant);
                    else if (ore != null) target = WosBlocks.ore(variant.toString(), ore);
                    else target = StrataWorldgen.block(level, pos, kind, variant);
                    if (target != null) {
                        BlockState newState = structure != null ? copyMatchingProperties(target.defaultBlockState(), state) : target.defaultBlockState();
                        chunk.setBlockState(pos, newState, false);
                    }
                }
            }
        }
        return true;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static BlockState copyMatchingProperties(BlockState target, BlockState source) {
        BlockState result = target;
        for (Property<?> prop : source.getProperties()) {
            if (target.hasProperty(prop)) {
                result = result.setValue((Property) prop, source.getValue(prop));
            }
        }
        return result;
    }
}
