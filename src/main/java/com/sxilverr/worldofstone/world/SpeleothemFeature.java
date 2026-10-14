package com.sxilverr.worldofstone.world;

import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.ModdedSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.StrataVariant;
import com.sxilverr.worldofstone.api.enums.TerracottaSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.VanillaSpeleothemVariant;
import com.sxilverr.worldofstone.common.block.WosSpeleothemBlock;
import com.sxilverr.worldofstone.config.WosConfig;
import com.sxilverr.worldofstone.registry.StrataBlock;
import com.sxilverr.worldofstone.registry.WosBlocks;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.FluidState;

import java.util.HashMap;
import java.util.Map;

public class SpeleothemFeature extends Feature<NoneFeatureConfiguration> {

    private static volatile Map<Block, Block> hosts;

    public SpeleothemFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        if (!WosConfig.speleothemsEnabled || !WosConfig.speleothemsGenerate) return false;
        WorldGenLevel level = ctx.level();
        BlockPos origin = ctx.origin();
        RandomSource rand = ctx.random();

        Map<Block, Block> stoneToSpeleothem = hosts();
        if (stoneToSpeleothem.isEmpty()) return false;

        ChunkAccess chunk = level.getChunk(origin);
        int chunkX = chunk.getPos().getMinBlockX();
        int chunkZ = chunk.getPos().getMinBlockZ();
        int minY = level.getMinBuildHeight() + 4;
        ResourceKey<Level> dim = level.getLevel().dimension();
        int seaLevel = level.getSeaLevel();
        int dimensionCap;
        if (dim == Level.NETHER) dimensionCap = 120;
        else if (dim == Level.END) dimensionCap = 200;
        else dimensionCap = WosConfig.speleothemsAllowAboveGround ? level.getMaxBuildHeight() - 4 : 80;
        int maxY = Math.min(level.getMaxBuildHeight() - 4, dimensionCap);
        int yRange = Math.max(1, maxY - minY);
        boolean isOverworld = dim == Level.OVERWORLD;
        boolean isEnd = dim == Level.END;

        int attempts = Math.max(1, WosConfig.speleothemAttemptsPerChunk) * 32;
        int rarity = Math.max(1, WosConfig.speleothemRarity);

        int placedClusters = 0;
        for (int i = 0; i < attempts; i++) {
            if (rarity > 1 && rand.nextInt(rarity) != 0) continue;

            int x = chunkX + rand.nextInt(16);
            int z = chunkZ + rand.nextInt(16);
            int y = minY + rand.nextInt(yRange);
            BlockPos pos = new BlockPos(x, y, z);

            BlockState atPos = level.getBlockState(pos);
            FluidState atFluid = atPos.getFluidState();
            boolean isAir = atPos.isAir();
            boolean isWater = !isAir && atFluid.is(FluidTags.WATER);
            boolean isLava = !isAir && atFluid.is(FluidTags.LAVA);
            boolean canPlaceHere = isAir
                    || (isWater && WosConfig.speleothemsGenerateInWater)
                    || (isLava && WosConfig.speleothemsGenerateInLava);
            if (!canPlaceHere) continue;

            int heightmapY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, pos.getX(), pos.getZ());
            boolean exposed = pos.getY() >= heightmapY;
            if (exposed) {
                boolean allowSky = isEnd ? WosConfig.speleothemsAllowSkyViewEnd : WosConfig.speleothemsAllowSkyView;
                if (!allowSky) continue;
            }

            boolean aboveGround = isOverworld && y > seaLevel;
            if (aboveGround && !WosConfig.speleothemsAllowAboveGround) continue;

            BlockPos above = pos.above();
            BlockPos below = pos.below();
            BlockState aboveState = level.getBlockState(above);
            BlockState belowState = level.getBlockState(below);
            boolean ceilingSturdy = aboveState.isFaceSturdy(level, above, Direction.DOWN);
            boolean floorSturdy = belowState.isFaceSturdy(level, below, Direction.UP);
            if (!ceilingSturdy && !floorSturdy) continue;

            boolean blockPointingUp = aboveGround && !WosConfig.speleothemsAllowPointingUpAboveGround;

            Direction tip;
            Block surrounding;
            if (ceilingSturdy && floorSturdy) {
                if (blockPointingUp) {
                    tip = Direction.DOWN;
                    surrounding = aboveState.getBlock();
                } else if (rand.nextBoolean()) {
                    tip = Direction.DOWN;
                    surrounding = aboveState.getBlock();
                } else {
                    tip = Direction.UP;
                    surrounding = belowState.getBlock();
                }
            } else if (ceilingSturdy) {
                tip = Direction.DOWN;
                surrounding = aboveState.getBlock();
            } else {
                if (blockPointingUp) continue;
                tip = Direction.UP;
                surrounding = belowState.getBlock();
            }

            Block speleothem = stoneToSpeleothem.get(surrounding);
            if (speleothem == null) continue;

            int length = (rand.nextInt(10) < 7) ? 3 : 2;

            if (placeCluster(level, pos, tip, speleothem, length, surrounding)) {
                placedClusters++;
            }
        }
        return placedClusters > 0;
    }

    private boolean placeCluster(WorldGenLevel level, BlockPos start, Direction tip, Block speleothem, int length, Block hostBlock) {
        WosSpeleothemBlock.Size[] order = {
                WosSpeleothemBlock.Size.LARGE,
                WosSpeleothemBlock.Size.MEDIUM,
                WosSpeleothemBlock.Size.SMALL
        };
        boolean placedAny = false;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int i = 0; i < length; i++) {
            int yOffset = (tip == Direction.DOWN) ? -i : i;
            pos.set(start.getX(), start.getY() + yOffset, start.getZ());
            BlockState atPos = level.getBlockState(pos);
            FluidState atFluid = atPos.getFluidState();
            boolean atAir = atPos.isAir();
            boolean atWater = !atAir && atFluid.is(FluidTags.WATER);
            boolean atLava = !atAir && atFluid.is(FluidTags.LAVA);
            if (i > 0) {
                boolean canPlace = atAir
                        || (atWater && WosConfig.speleothemsGenerateInWater)
                        || (atLava && WosConfig.speleothemsGenerateInLava);
                if (!canPlace) break;
            }
            boolean waterlogged = atWater && WosConfig.speleothemsWaterloggable;
            WosSpeleothemBlock.Size size = order[3 - length + i];
            BlockState state = speleothem.defaultBlockState()
                    .setValue(WosSpeleothemBlock.TIP_DIRECTION, tip)
                    .setValue(WosSpeleothemBlock.SIZE, size)
                    .setValue(WosSpeleothemBlock.WATERLOGGED, waterlogged);
            if (level.setBlock(pos, state, 3)) {
                placedAny = true;
                if (WosBlocks.MIMIC_SPELEOTHEM != null && speleothem == WosBlocks.MIMIC_SPELEOTHEM.get()) {
                    MimicHook.INSTANCE.setSource(level, pos, hostBlock.defaultBlockState());
                }
            }
        }
        return placedAny;
    }

    public static void invalidate() {
        hosts = null;
    }

    private static Map<Block, Block> hosts() {
        Map<Block, Block> map = hosts;
        if (map != null) return map;
        map = new HashMap<>();
        for (StrataVariant v : StrataVariant.ALL) {
            if (WosConfig.isSpeleothemEnabled(v)) map.put(StrataBlock.STONE.get(v), StrataBlock.SPELEOTHEM.get(v));
            if (WosConfig.isSandstoneSpeleothemEnabled(v)) map.put(StrataBlock.SANDSTONE.get(v), StrataBlock.SANDSTONE_SPELEOTHEM.get(v));
        }
        for (Map.Entry<VanillaSpeleothemVariant, RegistrySupplier<Block>> e : WosBlocks.VANILLA_SPELEOTHEM.entrySet()) {
            if (WosConfig.isSpeleothemEnabled(e.getKey())) put(map, e.getKey().host, e.getValue());
        }
        for (Map.Entry<TerracottaSpeleothemVariant, RegistrySupplier<Block>> e : WosBlocks.TERRACOTTA_SPELEOTHEM.entrySet()) {
            if (WosConfig.isSpeleothemEnabled(e.getKey())) put(map, e.getKey().getHostBlock(), e.getValue());
        }
        if (WosConfig.iceSpeleothemEnabled) {
            for (DecorativeSpeleothemVariant v : new DecorativeSpeleothemVariant[]{DecorativeSpeleothemVariant.ICE, DecorativeSpeleothemVariant.PACKED_ICE, DecorativeSpeleothemVariant.BLUE_ICE}) {
                put(map, v.getHostBlock(), WosBlocks.DECORATIVE_SPELEOTHEM.get(v));
            }
        }
        for (Map.Entry<ModdedSpeleothemVariant, RegistrySupplier<Block>> e : WosBlocks.MODDED_SPELEOTHEM.entrySet()) {
            if (WosConfig.isSpeleothemEnabled(e.getKey())) put(map, e.getKey().host, e.getValue());
        }
        if (WosBlocks.MIMIC_SPELEOTHEM != null) {
            for (String blockId : WosConfig.mimicSpeleothemWorldgenBlocks) {
                ResourceLocation rl = ResourceLocation.tryParse(blockId);
                if (rl != null) put(map, rl, WosBlocks.MIMIC_SPELEOTHEM);
            }
        }
        hosts = map;
        return map;
    }

    private static void put(Map<Block, Block> map, String hostId, RegistrySupplier<Block> speleothem) {
        put(map, ModInfo.id(hostId), speleothem);
    }

    private static void put(Map<Block, Block> map, ResourceLocation hostId, RegistrySupplier<Block> speleothem) {
        BuiltInRegistries.BLOCK.getOptional(hostId).filter(host -> host != Blocks.AIR).ifPresent(host -> map.put(host, speleothem.get()));
    }
}
