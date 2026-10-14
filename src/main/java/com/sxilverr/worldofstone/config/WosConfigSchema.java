package com.sxilverr.worldofstone.config;

import com.sxilverr.worldofstone.api.enums.ModdedSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.NoiseType;
import com.sxilverr.worldofstone.api.enums.StrataVariant;
import com.sxilverr.worldofstone.api.enums.TerracottaSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.VanillaOreHost;
import com.sxilverr.worldofstone.api.enums.VanillaSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.WorldgenSystem;
import com.sxilverr.worldofstone.world.SpeleothemFeature;
import dev.architectury.platform.Platform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class WosConfigSchema {

    private static final List<Runnable> BAKERS = new ArrayList<>();

    public static void define(WosConfigBuilder b) {
        b.push("worldgen");
        bind(b.comment("Master toggle for stone replacement.").define("stoneReplacementEnabled", true), v -> WosConfig.stoneReplacementEnabled = v);
        bind(b.comment("Worldgen system. NOISE = 3D noise field. BLOB = vanilla-style ore-vein blobs. CHUNK = patch regions.").defineEnum("worldgenSystem", WorldgenSystem.NOISE), v -> WosConfig.worldgenSystem = v);

        b.push("shared");
        bind(b.comment("Replace stone above sea level.").define("replaceStoneAboveGround", false), v -> WosConfig.replaceStoneAboveGround = v);
        bind(b.comment("Replace vanilla sand with variants.").define("replaceSand", true), v -> WosConfig.replaceSand = v);
        bind(b.comment("Replace vanilla sandstone with variants.").define("replaceSandstone", true), v -> WosConfig.replaceSandstone = v);
        bind(b.comment("Replace vanilla gravel with variants.").define("replaceGravel", true), v -> WosConfig.replaceGravel = v);
        bind(b.comment("Replace vanilla clay with variants.").define("replaceClay", true), v -> WosConfig.replaceClay = v);
        bind(b.comment("Replace red sand and red sandstone with variants.").define("replaceRedSandAndSandstone", false), v -> WosConfig.replaceRedSandAndSandstone = v);
        bind(b.comment("Convert vanilla ores into variant ores.").define("replaceVanillaOres", true), v -> WosConfig.replaceVanillaOres = v);
        bind(b.comment("Chance (0-100) of variant generation per placement.").defineInRange("variantChance", 70, 0, 100), v -> WosConfig.variantChance = v);
        bind(b.comment("Y ratio below which the bottom (igneous) stratum generates.").defineInRange("strataBottomRatio", 0.33D, 0.0D, 1.0D), v -> WosConfig.strataBottomRatio = v);
        bind(b.comment("Y ratio below which the middle (metamorphic) stratum generates.").defineInRange("strataMiddleRatio", 0.66D, 0.0D, 1.0D), v -> WosConfig.strataMiddleRatio = v);
        bind(b.comment("Ignore strata height restrictions. When enabled, all 24 variants generate at every Y level above deepslate.").define("ignoreStrataHeightRestrictions", false), v -> WosConfig.ignoreStrataHeightRestrictions = v);
        bind(b.comment("Allow strata to generate in deepslate layers.").define("allowStrataInDeepslate", false), v -> WosConfig.allowStrataInDeepslate = v);
        bind(b.comment("Allow ore generation in The Nether.").define("allowVanillaOresInNether", false), v -> WosConfig.allowVanillaOresInNether = v);
        bind(b.comment("Allow ore generation in The End.").define("allowVanillaOresInEnd", false), v -> WosConfig.allowVanillaOresInEnd = v);
        bind(b.comment("Allow obsidian ore variants to generate where possible.").define("allowObsidianOreVariants", false), v -> WosConfig.allowObsidianOreVariants = v);
        bind(b.comment("Nether ore attempts multiplier.").defineInRange("netherOreAttemptsMultiplier", 1.0D, 0.0D, 10.0D), v -> WosConfig.netherOreAttemptsMultiplier = v);
        bind(b.comment("End ore attempts multiplier.").defineInRange("endOreAttemptsMultiplier", 1.0D, 0.0D, 10.0D), v -> WosConfig.endOreAttemptsMultiplier = v);
        bind(b.comment("Allow overgrown strata to generate.").define("allowOvergrownStrata", true), v -> WosConfig.allowOvergrownStrata = v);
        bind(b.comment("Allow snowed strata to generate.").define("allowSnowedStrata", true), v -> WosConfig.allowSnowedStrata = v);
        bind(b.comment("Allow overgrown vanilla stone variant generation.").define("allowOvergrownVanillaVariants", true), v -> WosConfig.allowOvergrownVanillaVariants = v);
        bind(b.comment("Allow snowed vanilla stone variant generation.").define("allowSnowedVanillaVariants", true), v -> WosConfig.allowSnowedVanillaVariants = v);
        b.pop();

        b.push("chunk");
        bind(b.comment("Patch size in chunks.").defineInRange("patchSize", 8, 1, 64), v -> WosConfig.patchSize = v);
        bind(b.comment("Replace structure blocks with variant equivalents.").define("enableStructureVariantGeneration", true), v -> WosConfig.enableStructureVariantGeneration = v);
        bind(b.comment("Maximum horizontal dither at patch boundaries.").defineInRange("xzDither", 4, 0, 32), v -> WosConfig.strataXZDither = v);
        bind(b.comment("Maximum vertical dither at stratum boundaries.").defineInRange("yDither", 6, 0, 64), v -> WosConfig.strataYDither = v);
        b.pop();

        b.push("blob");
        bind(b.comment("Number of variant blobs spawned per chunk.").defineInRange("blobsPerChunk", 20, 0, 10000), v -> WosConfig.blobsPerChunk = v);
        bind(b.comment("Blob diameter in blocks. Higher values increase chunk-gen time.").defineInRange("blobSize", 35, 1, 100), v -> WosConfig.blobSize = v);
        b.pop();

        b.push("noise");
        bind(b.comment("Noise feature size in blocks.").defineInRange("noiseScale", 16, 1, 128), v -> WosConfig.noiseScale = v);
        bind(b.comment("Noise function type.").defineEnum("noiseType", NoiseType.FRACTIONAL_BROWNIAN_MOTION), v -> WosConfig.noiseType = v);
        bind(b.comment("Detail octaves. Higher = slower.").defineInRange("octaves", 4, 1, 8), v -> WosConfig.noiseOctaves = v);
        b.pop();

        b.push("variants");
        for (StrataVariant.Family family : StrataVariant.Family.values()) {
            b.push(family.toString());
            for (StrataVariant variant : family.variants()) {
                b.push(variant.toString());
                bind(b.comment("Whether this variant generates").define("enabled", true), v -> WosConfig.VARIANT_ENABLED.put(variant, v));
                bind(b.comment("Relative spawn weight").defineInRange("weight", 10, 0, 1000), v -> WosConfig.VARIANT_WEIGHT.put(variant, v));
                b.pop();
            }
            b.pop();
        }
        b.pop();
        b.pop();

        b.push("speleothems");
        bind(b.comment("Master toggle for speleothems.").define("enabled", true), v -> WosConfig.speleothemsEnabled = v);
        bind(b.comment("Allow speleothems to generate naturally.").define("speleothemsGenerate", true), v -> WosConfig.speleothemsGenerate = v);
        bind(b.comment("Speleothem spawn rarity. Higher numbers are more rare.").defineInRange("rarity", 1, 1, 256), v -> WosConfig.speleothemRarity = v);
        bind(b.comment("Speleothem density. Default 24 = 768 attempts per chunk.").defineInRange("attemptsPerChunk", 24, 1, 128), v -> WosConfig.speleothemAttemptsPerChunk = v);
        bind(b.comment("Allow speleothems to be waterlogged.").define("waterloggable", false), v -> WosConfig.speleothemsWaterloggable = v);
        bind(b.comment("Allow speleothems to spawn in water during worldgen. Forces 'waterloggable' on.").define("generateInWater", false), v -> {
            WosConfig.speleothemsGenerateInWater = v;
            WosConfig.speleothemsWaterloggable |= v;
        });
        bind(b.comment("Allow speleothems to spawn in lava during worldgen.").define("generateInLava", false), v -> WosConfig.speleothemsGenerateInLava = v);
        bind(b.comment("Speleothems break when water touches them (ignored if waterloggable is enabled).").define("breakByWater", true), v -> WosConfig.speleothemsBreakByWater = v);
        bind(b.comment("Speleothems break when lava touches them.").define("breakByLava", true), v -> WosConfig.speleothemsBreakByLava = v);
        bind(b.comment("Speleothems drop themselves even when broken without silk touch.").define("dropWithoutSilkTouch", false), v -> WosConfig.speleothemsDropWithoutSilkTouch = v);
        bind(b.comment("Speleothems damage entities like dripstone.").define("dripstoneDamage", false), v -> WosConfig.speleothemsDripstoneDamage = v);
        bind(b.comment("Speleothems fall when their support is removed.").define("canFall", false), v -> WosConfig.speleothemsCanFall = v);
        bind(b.comment("A falling speleothem drops its item when it lands.").define("dropOnFall", false), v -> WosConfig.speleothemsDropOnFall = v);
        bind(b.comment("Allow speleothems to generate at positions that can see the sky.").define("allowSkyView", false), v -> WosConfig.speleothemsAllowSkyView = v);
        bind(b.comment("Allow speleothems to generate at positions with sky view in The End.").define("allowSkyViewEnd", false), v -> WosConfig.speleothemsAllowSkyViewEnd = v);
        bind(b.comment("Allow speleothems to generate above sea level.").define("allowAboveGround", true), v -> WosConfig.speleothemsAllowAboveGround = v);
        bind(b.comment("Allow stalagmites (pointing up) above sea level.").define("allowPointingUpAboveGround", false), v -> WosConfig.speleothemsAllowPointingUpAboveGround = v);
        familyFlags(b, WosConfig.STRATA_SPELEOTHEM);

        b.push("sandstone");
        bind(b.comment("Enable sandstone speleothem generation.").define("enabled", true), v -> WosConfig.sandstoneSpeleothemsEnabled = v);
        familyFlags(b, WosConfig.SANDSTONE_SPELEOTHEM);
        b.pop();

        b.push("minecraft");
        flags(b, List.of(VanillaSpeleothemVariant.values()), WosConfig.VANILLA_SPELEOTHEM);
        b.pop();

        b.push("modded");
        String mod = null;
        for (ModdedSpeleothemVariant variant : ModdedSpeleothemVariant.values()) {
            if (!Platform.isModLoaded(variant.modId)) continue;
            if (!variant.modId.equals(mod)) {
                if (mod != null) b.pop();
                b.push(mod = variant.modId);
            }
            flags(b, List.of(variant), WosConfig.MODDED_SPELEOTHEM);
        }
        if (mod != null) b.pop();
        b.pop();

        bind(b.comment("Enable generation of Ice, Packed Ice, and Blue Ice speleothems.").define("iceSpeleothemEnabled", false), v -> WosConfig.iceSpeleothemEnabled = v);
        bind(b.comment("Enable decorative creative speleothems.").define("creativeSpeleothemsEnabled", true), v -> WosConfig.creativeSpeleothemsEnabled = v);
        bind(b.comment("Toggle for terracotta speleothem generation.").define("terracottaEnabled", false), v -> WosConfig.terracottaSpeleothemsEnabled = v);
        b.push("terracotta");
        flags(b, Arrays.stream(TerracottaSpeleothemVariant.values()).filter(v -> v.naturalGeneration).toList(), WosConfig.TERRACOTTA_SPELEOTHEM);
        b.pop();
        b.pop();

        b.push("fossils");
        bind(b.comment("Enable fossils.").define("fossilsEnabled", true), v -> WosConfig.fossilsEnabled = v);
        bind(b.comment("Chance of a fossil dropping when a defined block is broken without silk touch").defineInRange("dropChance", 0.05, 0.0, 1.0), v -> WosConfig.fossilDropChance = v);
        bind(b.comment("Block IDs that have a chance to drop fossils when broken.").defineListAllowEmpty("extraBlocks", List.of(), obj -> obj instanceof String), v -> WosConfig.fossilDropBlocks = new HashSet<>(v));
        b.pop();

        b.push("mimicSpeleothems");
        bind(b.comment("Block IDs to show as mimic speleothem presets. Each entry creates a creative-tab item that places a mimic speleothem mimicking that block.").defineListAllowEmpty("mimicBlocks", List.of(), obj -> obj instanceof String), v -> WosConfig.mimicSpeleothemBlocks = new ArrayList<>(v));
        bind(b.comment("mimicBlocks that should generate naturally as speleothems on their host block. must also be in mimicBlocks.").defineListAllowEmpty("worldgenBlocks", List.of(), obj -> obj instanceof String), v -> WosConfig.mimicSpeleothemWorldgenBlocks = new HashSet<>(v));
        b.pop();

        b.push("villager");
        bind(b.comment("Add World of Stone variants to Mason villager trades.").define("masonTradesEnabled", true), v -> WosConfig.masonTradesEnabled = v);
        bind(b.comment("Allow pillar variant trades.").define("allowPillarVariantTrades", true), v -> WosConfig.allowPillarVariantTrades = v);
        b.pop();

        b.push("gameplay");
        bind(b.comment("Strata stones drop cobblestone unless mined with silk touch.").define("stoneDropsCobblestone", true), v -> WosConfig.stoneDropsCobblestone = v);
        bind(b.comment("Lignite drops lignite cobblestone instead of lignite coal.").define("ligniteDropsLigniteCobblestone", false), v -> WosConfig.ligniteDropsLigniteCobblestone = v);
        bind(b.comment("Enable infested block variants.").define("enableInfestedBlocks", true), v -> WosConfig.enableInfestedBlocks = v);
        b.pop();

        b.push("vanillaStoneOres");
        flags(b, List.of(VanillaOreHost.VALUES), WosConfig.VANILLA_ORE_HOST_ENABLED);
        b.pop();
    }

    public static void bake() {
        for (Map<?, ?> map : List.of(WosConfig.VARIANT_ENABLED, WosConfig.VARIANT_WEIGHT, WosConfig.STRATA_SPELEOTHEM, WosConfig.SANDSTONE_SPELEOTHEM,
                WosConfig.VANILLA_SPELEOTHEM, WosConfig.MODDED_SPELEOTHEM, WosConfig.TERRACOTTA_SPELEOTHEM, WosConfig.VANILLA_ORE_HOST_ENABLED)) {
            map.clear();
        }
        BAKERS.forEach(Runnable::run);
        SpeleothemFeature.invalidate();
        WosConfigSync.afterBake();
    }

    private static <T> void bind(Supplier<T> value, Consumer<T> target) {
        BAKERS.add(() -> target.accept(value.get()));
    }

    private static <K> void flags(WosConfigBuilder b, List<K> keys, Map<K, Boolean> target) {
        for (K key : keys) {
            bind(b.define(key.toString(), true), v -> target.put(key, v));
        }
    }

    private static void familyFlags(WosConfigBuilder b, Map<StrataVariant, Boolean> target) {
        for (StrataVariant.Family family : StrataVariant.Family.values()) {
            b.push(family.toString());
            flags(b, family.variants(), target);
            b.pop();
        }
    }

    private WosConfigSchema() {
    }
}
