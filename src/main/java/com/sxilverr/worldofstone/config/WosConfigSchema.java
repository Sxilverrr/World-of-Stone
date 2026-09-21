package com.sxilverr.worldofstone.config;

import com.sxilverr.worldofstone.api.enums.IgneousVariant;
import com.sxilverr.worldofstone.api.enums.MetamorphicVariant;
import com.sxilverr.worldofstone.api.enums.NoiseType;
import com.sxilverr.worldofstone.api.enums.QuarkSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.SedimentaryVariant;
import com.sxilverr.worldofstone.api.enums.TerracottaSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.VanillaOreHost;
import com.sxilverr.worldofstone.api.enums.VanillaSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.WorldgenSystem;
import dev.architectury.platform.Platform;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public final class WosConfigSchema {

    public static Supplier<Boolean> STONE_REPLACEMENT_ENABLED;
    public static Supplier<WorldgenSystem> WORLDGEN_SYSTEM;

    public static Supplier<Boolean> REPLACE_STONE_ABOVE_GROUND;
    public static Supplier<Boolean> REPLACE_SAND;
    public static Supplier<Boolean> REPLACE_SANDSTONE;
    public static Supplier<Boolean> REPLACE_GRAVEL;
    public static Supplier<Boolean> REPLACE_CLAY;
    public static Supplier<Boolean> REPLACE_RED_SAND_AND_SANDSTONE;
    public static Supplier<Integer> VARIANT_CHANCE;
    public static Supplier<Double> STRATA_BOTTOM_RATIO;
    public static Supplier<Double> STRATA_MIDDLE_RATIO;
    public static Supplier<Boolean> IGNORE_STRATA_HEIGHT_RESTRICTIONS;
    public static Supplier<Boolean> ALLOW_STRATA_IN_DEEPSLATE;
    public static Supplier<Boolean> ALLOW_VANILLA_ORES_IN_NETHER;
    public static Supplier<Boolean> ALLOW_VANILLA_ORES_IN_END;
    public static Supplier<Boolean> ALLOW_OBSIDIAN_ORE_VARIANTS;
    public static Supplier<Double> NETHER_ORE_ATTEMPTS_MULTIPLIER;
    public static Supplier<Double> END_ORE_ATTEMPTS_MULTIPLIER;
    public static Supplier<Boolean> ALLOW_OVERGROWN_STRATA;
    public static Supplier<Boolean> ALLOW_SNOWED_STRATA;
    public static Supplier<Boolean> ALLOW_OVERGROWN_VANILLA_VARIANTS;
    public static Supplier<Boolean> ALLOW_SNOWED_VANILLA_VARIANTS;

    public static Supplier<Integer> PATCH_SIZE;
    public static Supplier<Boolean> REPLACE_VANILLA_ORES;
    public static Supplier<Boolean> ENABLE_STRUCTURE_VARIANT_GENERATION;
    public static Supplier<Integer> STRATA_XZ_DITHER;
    public static Supplier<Integer> STRATA_Y_DITHER;

    public static Supplier<Integer> BLOBS_PER_CHUNK;
    public static Supplier<Integer> BLOB_SIZE;

    public static Supplier<Integer> NOISE_SCALE;
    public static Supplier<NoiseType> NOISE_TYPE;
    public static Supplier<Integer> NOISE_OCTAVES;

    public static Supplier<Boolean> SPELEOTHEMS_ENABLED;
    public static Supplier<Boolean> SPELEOTHEMS_GENERATE;
    public static Supplier<Boolean> SANDSTONE_SPELEOTHEMS_ENABLED;
    public static Supplier<Integer> SPELEOTHEM_RARITY;
    public static Supplier<Integer> SPELEOTHEM_ATTEMPTS_PER_CHUNK;
    public static Supplier<Boolean> SPELEOTHEMS_WATERLOGGABLE;
    public static Supplier<Boolean> SPELEOTHEMS_GENERATE_IN_WATER;
    public static Supplier<Boolean> SPELEOTHEMS_GENERATE_IN_LAVA;
    public static Supplier<Boolean> SPELEOTHEMS_BREAK_BY_WATER;
    public static Supplier<Boolean> SPELEOTHEMS_BREAK_BY_LAVA;
    public static Supplier<Boolean> SPELEOTHEMS_DROP_WITHOUT_SILK_TOUCH;
    public static Supplier<Boolean> SPELEOTHEMS_DRIPSTONE_DAMAGE;
    public static Supplier<Boolean> SPELEOTHEMS_CAN_FALL;
    public static Supplier<Boolean> SPELEOTHEMS_DROP_ON_FALL;
    public static Supplier<Boolean> SPELEOTHEMS_ALLOW_SKY_VIEW;
    public static Supplier<Boolean> SPELEOTHEMS_ALLOW_SKY_VIEW_END;
    public static Supplier<Boolean> SPELEOTHEMS_ALLOW_ABOVE_GROUND;
    public static Supplier<Boolean> SPELEOTHEMS_ALLOW_POINTING_UP_ABOVE_GROUND;
    public static Supplier<Boolean> FOSSILS_ENABLED;
    public static Supplier<Double> FOSSIL_DROP_CHANCE;
    public static Supplier<List<? extends String>> FOSSIL_DROP_BLOCKS;
    public static Supplier<Boolean> MASON_TRADES_ENABLED;
    public static Supplier<Boolean> ALLOW_PILLAR_VARIANT_TRADES;
    public static Supplier<Boolean> STONE_DROPS_COBBLESTONE;
    public static Supplier<Boolean> LIGNITE_DROPS_LIGNITE_COBBLESTONE;
    public static Supplier<Boolean> ENABLE_INFESTED_BLOCKS;
    public static Supplier<List<? extends String>> MIMIC_SPELEOTHEM_BLOCKS;
    public static Supplier<List<? extends String>> MIMIC_SPELEOTHEM_WORLDGEN_BLOCKS;

    public static final Map<IgneousVariant, Supplier<Boolean>> IGNEOUS_VARIANT_ENABLED = new EnumMap<>(IgneousVariant.class);
    public static final Map<MetamorphicVariant, Supplier<Boolean>> METAMORPHIC_VARIANT_ENABLED = new EnumMap<>(MetamorphicVariant.class);
    public static final Map<SedimentaryVariant, Supplier<Boolean>> SEDIMENTARY_VARIANT_ENABLED = new EnumMap<>(SedimentaryVariant.class);

    public static final Map<IgneousVariant, Supplier<Integer>> IGNEOUS_VARIANT_WEIGHT = new EnumMap<>(IgneousVariant.class);
    public static final Map<MetamorphicVariant, Supplier<Integer>> METAMORPHIC_VARIANT_WEIGHT = new EnumMap<>(MetamorphicVariant.class);
    public static final Map<SedimentaryVariant, Supplier<Integer>> SEDIMENTARY_VARIANT_WEIGHT = new EnumMap<>(SedimentaryVariant.class);

    public static final Map<IgneousVariant, Supplier<Boolean>> IGNEOUS_SPELEOTHEM = new EnumMap<>(IgneousVariant.class);
    public static final Map<MetamorphicVariant, Supplier<Boolean>> METAMORPHIC_SPELEOTHEM = new EnumMap<>(MetamorphicVariant.class);
    public static final Map<SedimentaryVariant, Supplier<Boolean>> SEDIMENTARY_SPELEOTHEM = new EnumMap<>(SedimentaryVariant.class);
    public static final Map<IgneousVariant, Supplier<Boolean>> IGNEOUS_SANDSTONE_SPELEOTHEM = new EnumMap<>(IgneousVariant.class);
    public static final Map<MetamorphicVariant, Supplier<Boolean>> METAMORPHIC_SANDSTONE_SPELEOTHEM = new EnumMap<>(MetamorphicVariant.class);
    public static final Map<SedimentaryVariant, Supplier<Boolean>> SEDIMENTARY_SANDSTONE_SPELEOTHEM = new EnumMap<>(SedimentaryVariant.class);
    public static final Map<VanillaSpeleothemVariant, Supplier<Boolean>> VANILLA_SPELEOTHEM = new EnumMap<>(VanillaSpeleothemVariant.class);
    public static final Map<QuarkSpeleothemVariant, Supplier<Boolean>> QUARK_SPELEOTHEM = new EnumMap<>(QuarkSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.UndergardenSpeleothemVariant, Supplier<Boolean>> UNDERGARDEN_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.UndergardenSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.CreateSpeleothemVariant, Supplier<Boolean>> CREATE_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.CreateSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.BetterEndSpeleothemVariant, Supplier<Boolean>> BETTEREND_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.BetterEndSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.GalosphereSpeleothemVariant, Supplier<Boolean>> GALOSPHERE_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.GalosphereSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.BwgSpeleothemVariant, Supplier<Boolean>> BWG_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.BwgSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.TwilightForestSpeleothemVariant, Supplier<Boolean>> TWILIGHTFOREST_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.TwilightForestSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.AetherSpeleothemVariant, Supplier<Boolean>> AETHER_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.AetherSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.BlueSkiesSpeleothemVariant, Supplier<Boolean>> BLUE_SKIES_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.BlueSkiesSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.SpelunkerySpeleothemVariant, Supplier<Boolean>> SPELUNKERY_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.SpelunkerySpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.IceAndFireSpeleothemVariant, Supplier<Boolean>> ICEANDFIRE_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.IceAndFireSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.MysticalAgricultureSpeleothemVariant, Supplier<Boolean>> MYSTICALAGRICULTURE_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.MysticalAgricultureSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.BiomesOPlentySpeleothemVariant, Supplier<Boolean>> BIOMESOPLENTY_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.BiomesOPlentySpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.ForbiddenArcanusSpeleothemVariant, Supplier<Boolean>> FORBIDDEN_ARCANUS_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.ForbiddenArcanusSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.AlexsCavesSpeleothemVariant, Supplier<Boolean>> ALEXSCAVES_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.AlexsCavesSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.ArsNouveauSpeleothemVariant, Supplier<Boolean>> ARS_NOUVEAU_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.ArsNouveauSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.CataclysmSpeleothemVariant, Supplier<Boolean>> CATACLYSM_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.CataclysmSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.TwigsSpeleothemVariant, Supplier<Boolean>> TWIGS_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.TwigsSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.ArchitectsPaletteSpeleothemVariant, Supplier<Boolean>> ARCHITECTS_PALETTE_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.ArchitectsPaletteSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.OuterEndSpeleothemVariant, Supplier<Boolean>> OUTER_END_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.OuterEndSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.BotaniaSpeleothemVariant, Supplier<Boolean>> BOTANIA_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.BotaniaSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.AdAstraSpeleothemVariant, Supplier<Boolean>> AD_ASTRA_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.AdAstraSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.DeepAetherSpeleothemVariant, Supplier<Boolean>> DEEP_AETHER_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.DeepAetherSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.CavernsAndChasmsSpeleothemVariant, Supplier<Boolean>> CAVERNS_AND_CHASMS_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.CavernsAndChasmsSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.AtmosphericSpeleothemVariant, Supplier<Boolean>> ATMOSPHERIC_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.AtmosphericSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.EndergeticSpeleothemVariant, Supplier<Boolean>> ENDERGETIC_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.EndergeticSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.WilderWildsSpeleothemVariant, Supplier<Boolean>> WILDER_WILDS_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.WilderWildsSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.RegionsUnexploredSpeleothemVariant, Supplier<Boolean>> REGIONS_UNEXPLORED_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.RegionsUnexploredSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.BornInChaosSpeleothemVariant, Supplier<Boolean>> BORN_IN_CHAOS_V1_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.BornInChaosSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.NaturalistSpeleothemVariant, Supplier<Boolean>> NATURALIST_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.NaturalistSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.YungsCaveBiomesSpeleothemVariant, Supplier<Boolean>> YUNGSCAVEBIOMES_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.YungsCaveBiomesSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.NaturesSpiritSpeleothemVariant, Supplier<Boolean>> NATURES_SPIRIT_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.NaturesSpiritSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.NetherExpSpeleothemVariant, Supplier<Boolean>> NETHEREXP_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.NetherExpSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.DeeperDarkerSpeleothemVariant, Supplier<Boolean>> DEEPERDARKER_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.DeeperDarkerSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.TheDeepVoidSpeleothemVariant, Supplier<Boolean>> THE_DEEP_VOID_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.TheDeepVoidSpeleothemVariant.class);
    public static final Map<com.sxilverr.worldofstone.api.enums.DefiledLandsSpeleothemVariant, Supplier<Boolean>> DEFILED_LANDS_PREBORN_SPELEOTHEM = new EnumMap<>(com.sxilverr.worldofstone.api.enums.DefiledLandsSpeleothemVariant.class);
    public static final Map<TerracottaSpeleothemVariant, Supplier<Boolean>> TERRACOTTA_SPELEOTHEM_ENABLED_MAP = new EnumMap<>(TerracottaSpeleothemVariant.class);
    public static final Map<VanillaOreHost, Supplier<Boolean>> VANILLA_ORE_HOST_ENABLED = new EnumMap<>(VanillaOreHost.class);
    public static Supplier<Boolean> TERRACOTTA_SPELEOTHEMS_ENABLED;
    public static Supplier<Boolean> ICE_SPELEOTHEM_ENABLED;
    public static Supplier<Boolean> CREATIVE_SPELEOTHEMS_ENABLED;

    public static void define(WosConfigBuilder builder) {
        builder.push("worldgen");
        STONE_REPLACEMENT_ENABLED = builder
                .comment("Master toggle for stone replacement.")
                .define("stoneReplacementEnabled", true);
        WORLDGEN_SYSTEM = builder
                .comment("Worldgen system. NOISE = 3D noise field. BLOB = vanilla-style ore-vein blobs. CHUNK = patch regions.")
                .defineEnum("worldgenSystem", WorldgenSystem.NOISE);

        builder.push("shared");
        REPLACE_STONE_ABOVE_GROUND = builder
                .comment("Replace stone above sea level.")
                .define("replaceStoneAboveGround", false);
        REPLACE_SAND = builder
                .comment("Replace vanilla sand with variants.")
                .define("replaceSand", true);
        REPLACE_SANDSTONE = builder
                .comment("Replace vanilla sandstone with variants.")
                .define("replaceSandstone", true);
        REPLACE_GRAVEL = builder
                .comment("Replace vanilla gravel with variants.")
                .define("replaceGravel", true);
        REPLACE_CLAY = builder
                .comment("Replace vanilla clay with variants.")
                .define("replaceClay", true);
        REPLACE_RED_SAND_AND_SANDSTONE = builder
                .comment("Replace red sand and red sandstone with variants.")
                .define("replaceRedSandAndSandstone", false);
        REPLACE_VANILLA_ORES = builder
                .comment("Convert vanilla ores into variant ores.")
                .define("replaceVanillaOres", true);
        VARIANT_CHANCE = builder
                .comment("Chance (0-100) of variant generation per placement.")
                .defineInRange("variantChance", 70, 0, 100);
        STRATA_BOTTOM_RATIO = builder
                .comment("Y ratio below which the bottom (igneous) stratum generates.")
                .defineInRange("strataBottomRatio", 0.33D, 0.0D, 1.0D);
        STRATA_MIDDLE_RATIO = builder
                .comment("Y ratio below which the middle (metamorphic) stratum generates.")
                .defineInRange("strataMiddleRatio", 0.66D, 0.0D, 1.0D);
        IGNORE_STRATA_HEIGHT_RESTRICTIONS = builder
                .comment("Ignore strata height restrictions. When enabled, all 24 variants generate at every Y level above deepslate.")
                .define("ignoreStrataHeightRestrictions", false);
        ALLOW_STRATA_IN_DEEPSLATE = builder
                .comment("Allow strata to generate in deepslate layers.")
                .define("allowStrataInDeepslate", false);
        ALLOW_VANILLA_ORES_IN_NETHER = builder
                .comment("Allow ore generation in The Nether.")
                .define("allowVanillaOresInNether", false);
        ALLOW_VANILLA_ORES_IN_END = builder
                .comment("Allow ore generation in The End.")
                .define("allowVanillaOresInEnd", false);
        ALLOW_OBSIDIAN_ORE_VARIANTS = builder
                .comment("Allow obsidian ore variants to generate where possible.")
                .define("allowObsidianOreVariants", false);
        NETHER_ORE_ATTEMPTS_MULTIPLIER = builder
                .comment("Nether ore attempts multiplier.")
                .defineInRange("netherOreAttemptsMultiplier", 1.0D, 0.0D, 10.0D);
        END_ORE_ATTEMPTS_MULTIPLIER = builder
                .comment("End ore attempts multiplier.")
                .defineInRange("endOreAttemptsMultiplier", 1.0D, 0.0D, 10.0D);
        ALLOW_OVERGROWN_STRATA = builder
                .comment("Allow overgrown strata to generate.")
                .define("allowOvergrownStrata", true);
        ALLOW_SNOWED_STRATA = builder
                .comment("Allow snowed strata to generate.")
                .define("allowSnowedStrata", true);
        ALLOW_OVERGROWN_VANILLA_VARIANTS = builder
                .comment("Allow overgrown vanilla stone variant generation.")
                .define("allowOvergrownVanillaVariants", true);
        ALLOW_SNOWED_VANILLA_VARIANTS = builder
                .comment("Allow snowed vanilla stone variant generation.")
                .define("allowSnowedVanillaVariants", true);
        builder.pop();

        builder.push("chunk");
        PATCH_SIZE = builder
                .comment("Patch size in chunks.")
                .defineInRange("patchSize", 8, 1, 64);
        ENABLE_STRUCTURE_VARIANT_GENERATION = builder
                .comment("Replace structure blocks with variant equivalents.")
                .define("enableStructureVariantGeneration", true);
        STRATA_XZ_DITHER = builder
                .comment("Maximum horizontal dither at patch boundaries.")
                .defineInRange("xzDither", 4, 0, 32);
        STRATA_Y_DITHER = builder
                .comment("Maximum vertical dither at stratum boundaries.")
                .defineInRange("yDither", 6, 0, 64);
        builder.pop();

        builder.push("blob");
        BLOBS_PER_CHUNK = builder
                .comment("Number of variant blobs spawned per chunk.")
                .defineInRange("blobsPerChunk", 20, 0, 10000);
        BLOB_SIZE = builder
                .comment("Blob diameter in blocks. Higher values increase chunk-gen time.")
                .defineInRange("blobSize", 35, 1, 100);
        builder.pop();

        builder.push("noise");
        NOISE_SCALE = builder
                .comment("Noise feature size in blocks.")
                .defineInRange("noiseScale", 16, 1, 128);
        NOISE_TYPE = builder
                .comment("Noise function type.")
                .defineEnum("noiseType", NoiseType.FRACTIONAL_BROWNIAN_MOTION);
        NOISE_OCTAVES = builder
                .comment("Detail octaves. Higher = slower.")
                .defineInRange("octaves", 4, 1, 8);
        builder.pop();

        builder.push("variants");
        builder.push("igneous");
        for (IgneousVariant v : IgneousVariant.VALUES) {
            builder.push(v.toString());
            IGNEOUS_VARIANT_ENABLED.put(v, builder.comment("Whether this variant generates").define("enabled", true));
            IGNEOUS_VARIANT_WEIGHT.put(v, builder.comment("Relative spawn weight").defineInRange("weight", 10, 0, 1000));
            builder.pop();
        }
        builder.pop();
        builder.push("metamorphic");
        for (MetamorphicVariant v : MetamorphicVariant.VALUES) {
            builder.push(v.toString());
            METAMORPHIC_VARIANT_ENABLED.put(v, builder.comment("Whether this variant generates").define("enabled", true));
            METAMORPHIC_VARIANT_WEIGHT.put(v, builder.comment("Relative spawn weight").defineInRange("weight", 10, 0, 1000));
            builder.pop();
        }
        builder.pop();
        builder.push("sedimentary");
        for (SedimentaryVariant v : SedimentaryVariant.VALUES) {
            builder.push(v.toString());
            SEDIMENTARY_VARIANT_ENABLED.put(v, builder.comment("Whether this variant generates").define("enabled", true));
            SEDIMENTARY_VARIANT_WEIGHT.put(v, builder.comment("Relative spawn weight").defineInRange("weight", 10, 0, 1000));
            builder.pop();
        }
        builder.pop();
        builder.pop();

        builder.pop();

        builder.push("speleothems");
        SPELEOTHEMS_ENABLED = builder
                .comment("Master toggle for speleothems.")
                .define("enabled", true);
        SPELEOTHEMS_GENERATE = builder
                .comment("Allow speleothems to generate naturally.")
                .define("speleothemsGenerate", true);
        SPELEOTHEM_RARITY = builder
                .comment("Speleothem spawn rarity. Higher numbers are more rare.")
                .defineInRange("rarity", 1, 1, 256);
        SPELEOTHEM_ATTEMPTS_PER_CHUNK = builder
                .comment("Speleothem density. Default 24 = 768 attempts per chunk.")
                .defineInRange("attemptsPerChunk", 24, 1, 128);
        SPELEOTHEMS_WATERLOGGABLE = builder
                .comment("Allow speleothems to be waterlogged.")
                .define("waterloggable", false);
        SPELEOTHEMS_GENERATE_IN_WATER = builder
                .comment("Allow speleothems to spawn in water during worldgen. Forces 'waterloggable' on.")
                .define("generateInWater", false);
        SPELEOTHEMS_GENERATE_IN_LAVA = builder
                .comment("Allow speleothems to spawn in lava during worldgen.")
                .define("generateInLava", false);
        SPELEOTHEMS_BREAK_BY_WATER = builder
                .comment("Speleothems break when water touches them (ignored if waterloggable is enabled).")
                .define("breakByWater", true);
        SPELEOTHEMS_BREAK_BY_LAVA = builder
                .comment("Speleothems break when lava touches them.")
                .define("breakByLava", true);
        SPELEOTHEMS_DROP_WITHOUT_SILK_TOUCH = builder
                .comment("Speleothems drop themselves even when broken without silk touch.")
                .define("dropWithoutSilkTouch", false);
        SPELEOTHEMS_DRIPSTONE_DAMAGE = builder
                .comment("Speleothems damage entities like dripstone.")
                .define("dripstoneDamage", false);
        SPELEOTHEMS_CAN_FALL = builder
                .comment("Speleothems fall when their support is removed.")
                .define("canFall", false);
        SPELEOTHEMS_DROP_ON_FALL = builder
                .comment("A falling speleothem drops its item when it lands.")
                .define("dropOnFall", false);
        SPELEOTHEMS_ALLOW_SKY_VIEW = builder
                .comment("Allow speleothems to generate at positions that can see the sky.")
                .define("allowSkyView", false);
        SPELEOTHEMS_ALLOW_SKY_VIEW_END = builder
                .comment("Allow speleothems to generate at positions with sky view in The End.")
                .define("allowSkyViewEnd", false);
        SPELEOTHEMS_ALLOW_ABOVE_GROUND = builder
                .comment("Allow speleothems to generate above sea level.")
                .define("allowAboveGround", true);
        SPELEOTHEMS_ALLOW_POINTING_UP_ABOVE_GROUND = builder
                .comment("Allow stalagmites (pointing up) above sea level.")
                .define("allowPointingUpAboveGround", false);

        builder.push("igneous");
        for (IgneousVariant v : IgneousVariant.VALUES) {
            IGNEOUS_SPELEOTHEM.put(v, builder.define(v.toString(), true));
        }
        builder.pop();

        builder.push("metamorphic");
        for (MetamorphicVariant v : MetamorphicVariant.VALUES) {
            METAMORPHIC_SPELEOTHEM.put(v, builder.define(v.toString(), true));
        }
        builder.pop();

        builder.push("sedimentary");
        for (SedimentaryVariant v : SedimentaryVariant.VALUES) {
            SEDIMENTARY_SPELEOTHEM.put(v, builder.define(v.toString(), true));
        }
        builder.pop();

        builder.push("sandstone");
        SANDSTONE_SPELEOTHEMS_ENABLED = builder
                .comment("Enable sandstone speleothem generation.")
                .define("enabled", true);
        builder.push("igneous");
        for (IgneousVariant v : IgneousVariant.VALUES) {
            IGNEOUS_SANDSTONE_SPELEOTHEM.put(v, builder.define(v.toString(), true));
        }
        builder.pop();
        builder.push("metamorphic");
        for (MetamorphicVariant v : MetamorphicVariant.VALUES) {
            METAMORPHIC_SANDSTONE_SPELEOTHEM.put(v, builder.define(v.toString(), true));
        }
        builder.pop();
        builder.push("sedimentary");
        for (SedimentaryVariant v : SedimentaryVariant.VALUES) {
            SEDIMENTARY_SANDSTONE_SPELEOTHEM.put(v, builder.define(v.toString(), true));
        }
        builder.pop();
        builder.pop();

        builder.push("minecraft");
        for (VanillaSpeleothemVariant v : VanillaSpeleothemVariant.VALUES) {
            VANILLA_SPELEOTHEM.put(v, builder.define(v.toString(), true));
        }
        builder.pop();

        builder.push("modded");
        if (Platform.isModLoaded("quark")) {
            builder.push("quark");
            for (QuarkSpeleothemVariant v : QuarkSpeleothemVariant.VALUES) {
                QUARK_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("undergarden")) {
            builder.push("undergarden");
            for (com.sxilverr.worldofstone.api.enums.UndergardenSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.UndergardenSpeleothemVariant.VALUES) {
                UNDERGARDEN_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("create")) {
            builder.push("create");
            for (com.sxilverr.worldofstone.api.enums.CreateSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.CreateSpeleothemVariant.VALUES) {
                CREATE_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("betterend")) {
            builder.push("betterend");
            for (com.sxilverr.worldofstone.api.enums.BetterEndSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.BetterEndSpeleothemVariant.VALUES) {
                BETTEREND_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("galosphere")) {
            builder.push("galosphere");
            for (com.sxilverr.worldofstone.api.enums.GalosphereSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.GalosphereSpeleothemVariant.VALUES) {
                GALOSPHERE_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("biomeswevegone")) {
            builder.push("biomeswevegone");
            for (com.sxilverr.worldofstone.api.enums.BwgSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.BwgSpeleothemVariant.VALUES) {
                BWG_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("twilightforest")) {
            builder.push("twilightforest");
            for (com.sxilverr.worldofstone.api.enums.TwilightForestSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.TwilightForestSpeleothemVariant.VALUES) {
                TWILIGHTFOREST_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("aether")) {
            builder.push("aether");
            for (com.sxilverr.worldofstone.api.enums.AetherSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.AetherSpeleothemVariant.VALUES) {
                AETHER_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("blue_skies")) {
            builder.push("blue_skies");
            for (com.sxilverr.worldofstone.api.enums.BlueSkiesSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.BlueSkiesSpeleothemVariant.VALUES) {
                BLUE_SKIES_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("spelunkery")) {
            builder.push("spelunkery");
            for (com.sxilverr.worldofstone.api.enums.SpelunkerySpeleothemVariant v : com.sxilverr.worldofstone.api.enums.SpelunkerySpeleothemVariant.VALUES) {
                SPELUNKERY_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("iceandfire")) {
            builder.push("iceandfire");
            for (com.sxilverr.worldofstone.api.enums.IceAndFireSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.IceAndFireSpeleothemVariant.VALUES) {
                ICEANDFIRE_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("mysticalagriculture")) {
            builder.push("mysticalagriculture");
            for (com.sxilverr.worldofstone.api.enums.MysticalAgricultureSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.MysticalAgricultureSpeleothemVariant.VALUES) {
                MYSTICALAGRICULTURE_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("biomesoplenty")) {
            builder.push("biomesoplenty");
            for (com.sxilverr.worldofstone.api.enums.BiomesOPlentySpeleothemVariant v : com.sxilverr.worldofstone.api.enums.BiomesOPlentySpeleothemVariant.VALUES) {
                BIOMESOPLENTY_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("forbidden_arcanus")) {
            builder.push("forbidden_arcanus");
            for (com.sxilverr.worldofstone.api.enums.ForbiddenArcanusSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.ForbiddenArcanusSpeleothemVariant.VALUES) {
                FORBIDDEN_ARCANUS_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("alexscaves")) {
            builder.push("alexscaves");
            for (com.sxilverr.worldofstone.api.enums.AlexsCavesSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.AlexsCavesSpeleothemVariant.VALUES) {
                ALEXSCAVES_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("ars_nouveau")) {
            builder.push("ars_nouveau");
            for (com.sxilverr.worldofstone.api.enums.ArsNouveauSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.ArsNouveauSpeleothemVariant.VALUES) {
                ARS_NOUVEAU_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("cataclysm")) {
            builder.push("cataclysm");
            for (com.sxilverr.worldofstone.api.enums.CataclysmSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.CataclysmSpeleothemVariant.VALUES) {
                CATACLYSM_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("twigs")) {
            builder.push("twigs");
            for (com.sxilverr.worldofstone.api.enums.TwigsSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.TwigsSpeleothemVariant.VALUES) {
                TWIGS_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("architects_palette")) {
            builder.push("architects_palette");
            for (com.sxilverr.worldofstone.api.enums.ArchitectsPaletteSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.ArchitectsPaletteSpeleothemVariant.VALUES) {
                ARCHITECTS_PALETTE_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("outer_end")) {
            builder.push("outer_end");
            for (com.sxilverr.worldofstone.api.enums.OuterEndSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.OuterEndSpeleothemVariant.VALUES) {
                OUTER_END_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("botania")) {
            builder.push("botania");
            for (com.sxilverr.worldofstone.api.enums.BotaniaSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.BotaniaSpeleothemVariant.VALUES) {
                BOTANIA_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("ad_astra")) {
            builder.push("ad_astra");
            for (com.sxilverr.worldofstone.api.enums.AdAstraSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.AdAstraSpeleothemVariant.VALUES) {
                AD_ASTRA_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("deep_aether")) {
            builder.push("deep_aether");
            for (com.sxilverr.worldofstone.api.enums.DeepAetherSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.DeepAetherSpeleothemVariant.VALUES) {
                DEEP_AETHER_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("caverns_and_chasms")) {
            builder.push("caverns_and_chasms");
            for (com.sxilverr.worldofstone.api.enums.CavernsAndChasmsSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.CavernsAndChasmsSpeleothemVariant.VALUES) {
                CAVERNS_AND_CHASMS_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("atmospheric")) {
            builder.push("atmospheric");
            for (com.sxilverr.worldofstone.api.enums.AtmosphericSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.AtmosphericSpeleothemVariant.VALUES) {
                ATMOSPHERIC_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("endergetic")) {
            builder.push("endergetic");
            for (com.sxilverr.worldofstone.api.enums.EndergeticSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.EndergeticSpeleothemVariant.VALUES) {
                ENDERGETIC_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("wilder_wilds")) {
            builder.push("wilder_wilds");
            for (com.sxilverr.worldofstone.api.enums.WilderWildsSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.WilderWildsSpeleothemVariant.VALUES) {
                WILDER_WILDS_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("regions_unexplored")) {
            builder.push("regions_unexplored");
            for (com.sxilverr.worldofstone.api.enums.RegionsUnexploredSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.RegionsUnexploredSpeleothemVariant.VALUES) {
                REGIONS_UNEXPLORED_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("born_in_chaos_v1")) {
            builder.push("born_in_chaos_v1");
            for (com.sxilverr.worldofstone.api.enums.BornInChaosSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.BornInChaosSpeleothemVariant.VALUES) {
                BORN_IN_CHAOS_V1_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("naturalist")) {
            builder.push("naturalist");
            for (com.sxilverr.worldofstone.api.enums.NaturalistSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.NaturalistSpeleothemVariant.VALUES) {
                NATURALIST_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("yungscavebiomes")) {
            builder.push("yungscavebiomes");
            for (com.sxilverr.worldofstone.api.enums.YungsCaveBiomesSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.YungsCaveBiomesSpeleothemVariant.VALUES) {
                YUNGSCAVEBIOMES_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("natures_spirit")) {
            builder.push("natures_spirit");
            for (com.sxilverr.worldofstone.api.enums.NaturesSpiritSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.NaturesSpiritSpeleothemVariant.VALUES) {
                NATURES_SPIRIT_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("netherexp")) {
            builder.push("netherexp");
            for (com.sxilverr.worldofstone.api.enums.NetherExpSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.NetherExpSpeleothemVariant.VALUES) {
                NETHEREXP_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("deeperdarker")) {
            builder.push("deeperdarker");
            for (com.sxilverr.worldofstone.api.enums.DeeperDarkerSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.DeeperDarkerSpeleothemVariant.VALUES) {
                DEEPERDARKER_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("the_deep_void")) {
            builder.push("the_deep_void");
            for (com.sxilverr.worldofstone.api.enums.TheDeepVoidSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.TheDeepVoidSpeleothemVariant.VALUES) {
                THE_DEEP_VOID_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        if (Platform.isModLoaded("defiled_lands_preborn")) {
            builder.push("defiled_lands_preborn");
            for (com.sxilverr.worldofstone.api.enums.DefiledLandsSpeleothemVariant v : com.sxilverr.worldofstone.api.enums.DefiledLandsSpeleothemVariant.VALUES) {
                DEFILED_LANDS_PREBORN_SPELEOTHEM.put(v, builder.define(v.toString(), true));
            }
            builder.pop();
        }
        builder.pop();

        ICE_SPELEOTHEM_ENABLED = builder
                .comment("Enable generation of Ice, Packed Ice, and Blue Ice speleothems.")
                .define("iceSpeleothemEnabled", false);

        CREATIVE_SPELEOTHEMS_ENABLED = builder
                .comment("Enable decorative creative speleothems.")
                .define("creativeSpeleothemsEnabled", true);

        TERRACOTTA_SPELEOTHEMS_ENABLED = builder
                .comment("Toggle for terracotta speleothem generation.")
                .define("terracottaEnabled", false);
        builder.push("terracotta");
        for (TerracottaSpeleothemVariant v : TerracottaSpeleothemVariant.VALUES) {
            if (v.isNaturalGeneration()) {
                TERRACOTTA_SPELEOTHEM_ENABLED_MAP.put(v, builder.define(v.toString(), true));
            }
        }
        builder.pop();

        builder.pop();

        builder.push("fossils");
        FOSSILS_ENABLED = builder
                .comment("Enable fossils.")
                .define("fossilsEnabled", true);
        FOSSIL_DROP_CHANCE = builder
                .comment("Chance of a fossil dropping when a defined block is broken without silk touch")
                .defineInRange("dropChance", 0.05, 0.0, 1.0);
        FOSSIL_DROP_BLOCKS = builder
                .comment("Block IDs that have a chance to drop fossils when broken.")
                .defineListAllowEmpty("extraBlocks", List.of(),
                        obj -> obj instanceof String);
        builder.pop();

        builder.push("mimicSpeleothems");
        MIMIC_SPELEOTHEM_BLOCKS = builder
                .comment("Block IDs to show as mimic speleothem presets. Each entry creates a creative-tab item that places a mimic speleothem mimicking that block.")
                .defineListAllowEmpty("mimicBlocks", List.of(), obj -> obj instanceof String);
        MIMIC_SPELEOTHEM_WORLDGEN_BLOCKS = builder
                .comment("mimicBlocks that should generate naturally as speleothems on their host block. must also be in mimicBlocks.")
                .defineListAllowEmpty("worldgenBlocks", List.of(), obj -> obj instanceof String);
        builder.pop();

        builder.push("villager");
        MASON_TRADES_ENABLED = builder
                .comment("Add World of Stone variants to Mason villager trades.")
                .define("masonTradesEnabled", true);
        ALLOW_PILLAR_VARIANT_TRADES = builder
                .comment("Allow pillar variant trades.")
                .define("allowPillarVariantTrades", true);
        builder.pop();

        builder.push("gameplay");
        STONE_DROPS_COBBLESTONE = builder
                .comment("Strata stones drop cobblestone unless mined with silk touch.")
                .define("stoneDropsCobblestone", true);
        LIGNITE_DROPS_LIGNITE_COBBLESTONE = builder
                .comment("Lignite drops lignite cobblestone instead of lignite coal.")
                .define("ligniteDropsLigniteCobblestone", false);
        ENABLE_INFESTED_BLOCKS = builder
                .comment("Enable infested block variants.")
                .define("enableInfestedBlocks", true);
        builder.pop();

        builder.push("vanillaStoneOres");
        for (VanillaOreHost host : VanillaOreHost.VALUES) {
            VANILLA_ORE_HOST_ENABLED.put(host, builder.define(host.getRegistryName(), true));
        }
        builder.pop();
    }

    private static <K extends Enum<K>> void bakeBoolMap(Map<K, Supplier<Boolean>> spec, Map<K, Boolean> baked) {
        baked.clear();
        for (Map.Entry<K, Supplier<Boolean>> e : spec.entrySet()) {
            baked.put(e.getKey(), e.getValue().get());
        }
    }

    private static <K extends Enum<K>> void bakeIntMap(Map<K, Supplier<Integer>> spec, Map<K, Integer> baked) {
        baked.clear();
        for (Map.Entry<K, Supplier<Integer>> e : spec.entrySet()) {
            baked.put(e.getKey(), e.getValue().get());
        }
    }

    public static void bake() {
        WosConfig.stoneReplacementEnabled = STONE_REPLACEMENT_ENABLED.get();
        WosConfig.patchSize = PATCH_SIZE.get();
        WosConfig.replaceVanillaOres = REPLACE_VANILLA_ORES.get();
        WosConfig.strataBottomRatio = STRATA_BOTTOM_RATIO.get();
        WosConfig.strataMiddleRatio = STRATA_MIDDLE_RATIO.get();
        WosConfig.strataXZDither = STRATA_XZ_DITHER.get();
        WosConfig.strataYDither = STRATA_Y_DITHER.get();
        WosConfig.speleothemsEnabled = SPELEOTHEMS_ENABLED.get();
        WosConfig.speleothemsGenerate = SPELEOTHEMS_GENERATE.get();
        WosConfig.sandstoneSpeleothemsEnabled = SANDSTONE_SPELEOTHEMS_ENABLED.get();
        WosConfig.speleothemRarity = SPELEOTHEM_RARITY.get();
        WosConfig.speleothemAttemptsPerChunk = SPELEOTHEM_ATTEMPTS_PER_CHUNK.get();
        WosConfig.speleothemsGenerateInWater = SPELEOTHEMS_GENERATE_IN_WATER.get();
        WosConfig.speleothemsWaterloggable = SPELEOTHEMS_WATERLOGGABLE.get() || WosConfig.speleothemsGenerateInWater;
        WosConfig.speleothemsGenerateInLava = SPELEOTHEMS_GENERATE_IN_LAVA.get();
        WosConfig.speleothemsBreakByWater = SPELEOTHEMS_BREAK_BY_WATER.get();
        WosConfig.speleothemsBreakByLava = SPELEOTHEMS_BREAK_BY_LAVA.get();
        WosConfig.speleothemsDropWithoutSilkTouch = SPELEOTHEMS_DROP_WITHOUT_SILK_TOUCH.get();
        WosConfig.speleothemsDripstoneDamage = SPELEOTHEMS_DRIPSTONE_DAMAGE.get();
        WosConfig.speleothemsCanFall = SPELEOTHEMS_CAN_FALL.get();
        WosConfig.speleothemsDropOnFall = SPELEOTHEMS_DROP_ON_FALL.get();
        WosConfig.speleothemsAllowSkyView = SPELEOTHEMS_ALLOW_SKY_VIEW.get();
        WosConfig.speleothemsAllowSkyViewEnd = SPELEOTHEMS_ALLOW_SKY_VIEW_END.get();
        WosConfig.speleothemsAllowAboveGround = SPELEOTHEMS_ALLOW_ABOVE_GROUND.get();
        WosConfig.speleothemsAllowPointingUpAboveGround = SPELEOTHEMS_ALLOW_POINTING_UP_ABOVE_GROUND.get();
        WosConfig.replaceStoneAboveGround = REPLACE_STONE_ABOVE_GROUND.get();
        WosConfig.ignoreStrataHeightRestrictions = IGNORE_STRATA_HEIGHT_RESTRICTIONS.get();
        WosConfig.allowStrataInDeepslate = ALLOW_STRATA_IN_DEEPSLATE.get();
        WosConfig.allowVanillaOresInNether = ALLOW_VANILLA_ORES_IN_NETHER.get();
        WosConfig.allowVanillaOresInEnd = ALLOW_VANILLA_ORES_IN_END.get();
        WosConfig.allowObsidianOreVariants = ALLOW_OBSIDIAN_ORE_VARIANTS.get();
        WosConfig.netherOreAttemptsMultiplier = NETHER_ORE_ATTEMPTS_MULTIPLIER.get();
        WosConfig.endOreAttemptsMultiplier = END_ORE_ATTEMPTS_MULTIPLIER.get();
        WosConfig.allowOvergrownStrata = ALLOW_OVERGROWN_STRATA.get();
        WosConfig.allowSnowedStrata = ALLOW_SNOWED_STRATA.get();
        WosConfig.allowOvergrownVanillaVariants = ALLOW_OVERGROWN_VANILLA_VARIANTS.get();
        WosConfig.allowSnowedVanillaVariants = ALLOW_SNOWED_VANILLA_VARIANTS.get();
        WosConfig.replaceSand = REPLACE_SAND.get();
        WosConfig.replaceSandstone = REPLACE_SANDSTONE.get();
        WosConfig.replaceGravel = REPLACE_GRAVEL.get();
        WosConfig.replaceClay = REPLACE_CLAY.get();
        WosConfig.replaceRedSandAndSandstone = REPLACE_RED_SAND_AND_SANDSTONE.get();
        WosConfig.variantChance = VARIANT_CHANCE.get();
        WosConfig.enableStructureVariantGeneration = ENABLE_STRUCTURE_VARIANT_GENERATION.get();
        WosConfig.worldgenSystem = WORLDGEN_SYSTEM.get();
        WosConfig.blobsPerChunk = BLOBS_PER_CHUNK.get();
        WosConfig.blobSize = BLOB_SIZE.get();
        WosConfig.noiseScale = NOISE_SCALE.get();
        WosConfig.noiseType = NOISE_TYPE.get();
        WosConfig.noiseOctaves = NOISE_OCTAVES.get();
        WosConfig.terracottaSpeleothemsEnabled = TERRACOTTA_SPELEOTHEMS_ENABLED.get();
        WosConfig.iceSpeleothemEnabled = ICE_SPELEOTHEM_ENABLED.get();
        WosConfig.creativeSpeleothemsEnabled = CREATIVE_SPELEOTHEMS_ENABLED.get();
        WosConfig.fossilsEnabled = FOSSILS_ENABLED.get();
        WosConfig.fossilDropChance = FOSSIL_DROP_CHANCE.get();
        WosConfig.fossilDropBlocks = new HashSet<>(FOSSIL_DROP_BLOCKS.get());
        WosConfig.mimicSpeleothemBlocks = new ArrayList<>(MIMIC_SPELEOTHEM_BLOCKS.get());
        WosConfig.mimicSpeleothemWorldgenBlocks = new HashSet<>(MIMIC_SPELEOTHEM_WORLDGEN_BLOCKS.get());
        WosConfig.masonTradesEnabled = MASON_TRADES_ENABLED.get();
        WosConfig.allowPillarVariantTrades = ALLOW_PILLAR_VARIANT_TRADES.get();
        WosConfig.stoneDropsCobblestone = STONE_DROPS_COBBLESTONE.get();
        WosConfig.ligniteDropsLigniteCobblestone = LIGNITE_DROPS_LIGNITE_COBBLESTONE.get();
        WosConfig.enableInfestedBlocks = ENABLE_INFESTED_BLOCKS.get();

        bakeBoolMap(IGNEOUS_VARIANT_ENABLED, WosConfig.IGNEOUS_VARIANT_ENABLED);
        bakeBoolMap(METAMORPHIC_VARIANT_ENABLED, WosConfig.METAMORPHIC_VARIANT_ENABLED);
        bakeBoolMap(SEDIMENTARY_VARIANT_ENABLED, WosConfig.SEDIMENTARY_VARIANT_ENABLED);
        bakeIntMap(IGNEOUS_VARIANT_WEIGHT, WosConfig.IGNEOUS_VARIANT_WEIGHT);
        bakeIntMap(METAMORPHIC_VARIANT_WEIGHT, WosConfig.METAMORPHIC_VARIANT_WEIGHT);
        bakeIntMap(SEDIMENTARY_VARIANT_WEIGHT, WosConfig.SEDIMENTARY_VARIANT_WEIGHT);
        bakeBoolMap(IGNEOUS_SPELEOTHEM, WosConfig.IGNEOUS_SPELEOTHEM);
        bakeBoolMap(METAMORPHIC_SPELEOTHEM, WosConfig.METAMORPHIC_SPELEOTHEM);
        bakeBoolMap(SEDIMENTARY_SPELEOTHEM, WosConfig.SEDIMENTARY_SPELEOTHEM);
        bakeBoolMap(IGNEOUS_SANDSTONE_SPELEOTHEM, WosConfig.IGNEOUS_SANDSTONE_SPELEOTHEM);
        bakeBoolMap(METAMORPHIC_SANDSTONE_SPELEOTHEM, WosConfig.METAMORPHIC_SANDSTONE_SPELEOTHEM);
        bakeBoolMap(SEDIMENTARY_SANDSTONE_SPELEOTHEM, WosConfig.SEDIMENTARY_SANDSTONE_SPELEOTHEM);
        bakeBoolMap(VANILLA_SPELEOTHEM, WosConfig.VANILLA_SPELEOTHEM);
        bakeBoolMap(QUARK_SPELEOTHEM, WosConfig.QUARK_SPELEOTHEM);
        bakeBoolMap(UNDERGARDEN_SPELEOTHEM, WosConfig.UNDERGARDEN_SPELEOTHEM);
        bakeBoolMap(CREATE_SPELEOTHEM, WosConfig.CREATE_SPELEOTHEM);
        bakeBoolMap(BETTEREND_SPELEOTHEM, WosConfig.BETTEREND_SPELEOTHEM);
        bakeBoolMap(GALOSPHERE_SPELEOTHEM, WosConfig.GALOSPHERE_SPELEOTHEM);
        bakeBoolMap(BWG_SPELEOTHEM, WosConfig.BWG_SPELEOTHEM);
        bakeBoolMap(TWILIGHTFOREST_SPELEOTHEM, WosConfig.TWILIGHTFOREST_SPELEOTHEM);
        bakeBoolMap(AETHER_SPELEOTHEM, WosConfig.AETHER_SPELEOTHEM);
        bakeBoolMap(BLUE_SKIES_SPELEOTHEM, WosConfig.BLUE_SKIES_SPELEOTHEM);
        bakeBoolMap(SPELUNKERY_SPELEOTHEM, WosConfig.SPELUNKERY_SPELEOTHEM);
        bakeBoolMap(ICEANDFIRE_SPELEOTHEM, WosConfig.ICEANDFIRE_SPELEOTHEM);
        bakeBoolMap(MYSTICALAGRICULTURE_SPELEOTHEM, WosConfig.MYSTICALAGRICULTURE_SPELEOTHEM);
        bakeBoolMap(BIOMESOPLENTY_SPELEOTHEM, WosConfig.BIOMESOPLENTY_SPELEOTHEM);
        bakeBoolMap(FORBIDDEN_ARCANUS_SPELEOTHEM, WosConfig.FORBIDDEN_ARCANUS_SPELEOTHEM);
        bakeBoolMap(ALEXSCAVES_SPELEOTHEM, WosConfig.ALEXSCAVES_SPELEOTHEM);
        bakeBoolMap(ARS_NOUVEAU_SPELEOTHEM, WosConfig.ARS_NOUVEAU_SPELEOTHEM);
        bakeBoolMap(CATACLYSM_SPELEOTHEM, WosConfig.CATACLYSM_SPELEOTHEM);
        bakeBoolMap(TWIGS_SPELEOTHEM, WosConfig.TWIGS_SPELEOTHEM);
        bakeBoolMap(ARCHITECTS_PALETTE_SPELEOTHEM, WosConfig.ARCHITECTS_PALETTE_SPELEOTHEM);
        bakeBoolMap(OUTER_END_SPELEOTHEM, WosConfig.OUTER_END_SPELEOTHEM);
        bakeBoolMap(BOTANIA_SPELEOTHEM, WosConfig.BOTANIA_SPELEOTHEM);
        bakeBoolMap(AD_ASTRA_SPELEOTHEM, WosConfig.AD_ASTRA_SPELEOTHEM);
        bakeBoolMap(DEEP_AETHER_SPELEOTHEM, WosConfig.DEEP_AETHER_SPELEOTHEM);
        bakeBoolMap(CAVERNS_AND_CHASMS_SPELEOTHEM, WosConfig.CAVERNS_AND_CHASMS_SPELEOTHEM);
        bakeBoolMap(ATMOSPHERIC_SPELEOTHEM, WosConfig.ATMOSPHERIC_SPELEOTHEM);
        bakeBoolMap(ENDERGETIC_SPELEOTHEM, WosConfig.ENDERGETIC_SPELEOTHEM);
        bakeBoolMap(WILDER_WILDS_SPELEOTHEM, WosConfig.WILDER_WILDS_SPELEOTHEM);
        bakeBoolMap(REGIONS_UNEXPLORED_SPELEOTHEM, WosConfig.REGIONS_UNEXPLORED_SPELEOTHEM);
        bakeBoolMap(BORN_IN_CHAOS_V1_SPELEOTHEM, WosConfig.BORN_IN_CHAOS_V1_SPELEOTHEM);
        bakeBoolMap(NATURALIST_SPELEOTHEM, WosConfig.NATURALIST_SPELEOTHEM);
        bakeBoolMap(YUNGSCAVEBIOMES_SPELEOTHEM, WosConfig.YUNGSCAVEBIOMES_SPELEOTHEM);
        bakeBoolMap(NATURES_SPIRIT_SPELEOTHEM, WosConfig.NATURES_SPIRIT_SPELEOTHEM);
        bakeBoolMap(NETHEREXP_SPELEOTHEM, WosConfig.NETHEREXP_SPELEOTHEM);
        bakeBoolMap(DEEPERDARKER_SPELEOTHEM, WosConfig.DEEPERDARKER_SPELEOTHEM);
        bakeBoolMap(THE_DEEP_VOID_SPELEOTHEM, WosConfig.THE_DEEP_VOID_SPELEOTHEM);
        bakeBoolMap(DEFILED_LANDS_PREBORN_SPELEOTHEM, WosConfig.DEFILED_LANDS_PREBORN_SPELEOTHEM);
        bakeBoolMap(TERRACOTTA_SPELEOTHEM_ENABLED_MAP, WosConfig.TERRACOTTA_SPELEOTHEM_ENABLED_MAP);
        bakeBoolMap(VANILLA_ORE_HOST_ENABLED, WosConfig.VANILLA_ORE_HOST_ENABLED);

        WosConfigSync.afterBake();
    }

    private WosConfigSchema() {
    }
}
