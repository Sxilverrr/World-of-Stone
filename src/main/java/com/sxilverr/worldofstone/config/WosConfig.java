package com.sxilverr.worldofstone.config;

import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.ModdedSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.NoiseType;
import com.sxilverr.worldofstone.api.enums.StrataVariant;
import com.sxilverr.worldofstone.api.enums.TerracottaSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.VanillaOreHost;
import com.sxilverr.worldofstone.api.enums.VanillaSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.WorldgenSystem;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class WosConfig {

    public static boolean stoneReplacementEnabled = true;
    public static int patchSize = 8;
    public static boolean replaceVanillaOres = true;
    public static double strataBottomRatio = 0.33;
    public static double strataMiddleRatio = 0.66;
    public static boolean ignoreStrataHeightRestrictions = false;
    public static boolean allowStrataInDeepslate = false;
    public static boolean allowVanillaOresInNether = false;
    public static boolean allowVanillaOresInEnd = false;
    public static boolean allowObsidianOreVariants = false;
    public static double netherOreAttemptsMultiplier = 1.0;
    public static double endOreAttemptsMultiplier = 1.0;
    public static boolean allowOvergrownStrata = true;
    public static boolean allowSnowedStrata = true;
    public static boolean allowOvergrownVanillaVariants = true;
    public static boolean allowSnowedVanillaVariants = true;
    public static int strataXZDither = 4;
    public static int strataYDither = 6;
    public static boolean speleothemsEnabled = true;
    public static boolean speleothemsGenerate = true;
    public static boolean sandstoneSpeleothemsEnabled = true;
    public static int speleothemRarity = 1;
    public static int speleothemAttemptsPerChunk = 24;
    public static boolean speleothemsWaterloggable = false;
    public static boolean speleothemsGenerateInWater = false;
    public static boolean speleothemsGenerateInLava = false;
    public static boolean speleothemsBreakByWater = true;
    public static boolean speleothemsBreakByLava = true;
    public static boolean speleothemsDropWithoutSilkTouch = false;
    public static boolean speleothemsDripstoneDamage = false;
    public static boolean speleothemsCanFall = false;
    public static boolean speleothemsDropOnFall = false;
    public static boolean speleothemsAllowSkyView = false;
    public static boolean speleothemsAllowSkyViewEnd = false;
    public static boolean speleothemsAllowAboveGround = true;
    public static boolean speleothemsAllowPointingUpAboveGround = false;
    public static boolean replaceStoneAboveGround = false;
    public static boolean replaceSand = true;
    public static boolean replaceSandstone = true;
    public static boolean replaceGravel = true;
    public static boolean replaceClay = true;
    public static boolean replaceRedSandAndSandstone = false;
    public static int variantChance = 70;
    public static boolean enableStructureVariantGeneration = true;
    public static WorldgenSystem worldgenSystem = WorldgenSystem.NOISE;
    public static int blobsPerChunk = 20;
    public static int blobSize = 35;
    public static int noiseScale = 16;
    public static NoiseType noiseType = NoiseType.FRACTIONAL_BROWNIAN_MOTION;
    public static int noiseOctaves = 4;
    public static boolean terracottaSpeleothemsEnabled = false;
    public static boolean iceSpeleothemEnabled = false;
    public static boolean creativeSpeleothemsEnabled = true;
    public static boolean fossilsEnabled = true;
    public static double fossilDropChance = 0.05;
    public static Set<String> fossilDropBlocks = Collections.emptySet();
    public static List<String> mimicSpeleothemBlocks = Collections.emptyList();
    public static Set<String> mimicSpeleothemWorldgenBlocks = Collections.emptySet();
    public static boolean masonTradesEnabled = true;
    public static boolean allowPillarVariantTrades = true;
    public static boolean stoneDropsCobblestone = true;
    public static boolean ligniteDropsLigniteCobblestone = false;
    public static boolean enableInfestedBlocks = true;
    public static boolean enableStrataGlass = false;

    private static final ResourceLocation PLAIN_GLASS_RECIPE = ModInfo.rl("glass_from_strata_sand");

    public static final Map<StrataVariant, Boolean> VARIANT_ENABLED = new EnumMap<>(StrataVariant.class);
    public static final Map<StrataVariant, Integer> VARIANT_WEIGHT = new EnumMap<>(StrataVariant.class);
    public static final Map<StrataVariant, Boolean> STRATA_SPELEOTHEM = new EnumMap<>(StrataVariant.class);
    public static final Map<StrataVariant, Boolean> SANDSTONE_SPELEOTHEM = new EnumMap<>(StrataVariant.class);
    public static final Map<VanillaSpeleothemVariant, Boolean> VANILLA_SPELEOTHEM = new EnumMap<>(VanillaSpeleothemVariant.class);
    public static final Map<ModdedSpeleothemVariant, Boolean> MODDED_SPELEOTHEM = new EnumMap<>(ModdedSpeleothemVariant.class);
    public static final Map<TerracottaSpeleothemVariant, Boolean> TERRACOTTA_SPELEOTHEM = new EnumMap<>(TerracottaSpeleothemVariant.class);
    public static final Map<VanillaOreHost, Boolean> VANILLA_ORE_HOST_ENABLED = new EnumMap<>(VanillaOreHost.class);

    public static boolean isVariantEnabled(StrataVariant v) {
        return VARIANT_ENABLED.getOrDefault(v, true);
    }

    public static int getVariantWeight(StrataVariant v) {
        return isVariantEnabled(v) ? VARIANT_WEIGHT.getOrDefault(v, 10) : 0;
    }

    public static boolean isSpeleothemEnabled(StrataVariant v) {
        return speleothemsEnabled && STRATA_SPELEOTHEM.getOrDefault(v, true);
    }

    public static boolean isSandstoneSpeleothemEnabled(StrataVariant v) {
        return speleothemsEnabled && sandstoneSpeleothemsEnabled && SANDSTONE_SPELEOTHEM.getOrDefault(v, true);
    }

    public static boolean isSpeleothemEnabled(VanillaSpeleothemVariant v) {
        boolean sandstone = v == VanillaSpeleothemVariant.SANDSTONE || v == VanillaSpeleothemVariant.RED_SANDSTONE;
        return speleothemsEnabled && (sandstoneSpeleothemsEnabled || !sandstone) && VANILLA_SPELEOTHEM.getOrDefault(v, true);
    }

    public static boolean isSpeleothemEnabled(ModdedSpeleothemVariant v) {
        return speleothemsEnabled && MODDED_SPELEOTHEM.getOrDefault(v, false);
    }

    public static boolean isSpeleothemEnabled(TerracottaSpeleothemVariant v) {
        return speleothemsEnabled && terracottaSpeleothemsEnabled && v.naturalGeneration && TERRACOTTA_SPELEOTHEM.getOrDefault(v, true);
    }

    public static boolean isCreativeSpeleothemEnabled(DecorativeSpeleothemVariant v) {
        if (!speleothemsEnabled) return false;
        return switch (v) {
            case ICE, PACKED_ICE, BLUE_ICE -> iceSpeleothemEnabled;
            default -> creativeSpeleothemsEnabled;
        };
    }

    public static boolean isStrataVariantPath(String path) {
        for (StrataVariant v : StrataVariant.ALL) {
            if (path.contains(v.toString())) return true;
        }
        return false;
    }

    public static boolean isRecipeDisabled(ResourceLocation id, ResourceLocation result) {
        if (id.equals(PLAIN_GLASS_RECIPE)) return enableStrataGlass;
        if (ModInfo.MODID.equals(id.getNamespace()) && id.getPath().endsWith("_from_strata_glass")) return !enableStrataGlass;
        if (result == null || !ModInfo.MODID.equals(result.getNamespace())) return false;
        String path = result.getPath();
        boolean strata = isStrataVariantPath(path);
        if (!stoneReplacementEnabled && strata) return true;
        if (path.startsWith("overgrown_")) return strata ? !allowOvergrownStrata : !allowOvergrownVanillaVariants;
        if (path.startsWith("snowed_")) return strata ? !allowSnowedStrata : !allowSnowedVanillaVariants;
        return strata && (path.endsWith("_glass") || path.endsWith("_glass_pane")) && !enableStrataGlass;
    }

    public static boolean isVanillaOreHostEnabled(VanillaOreHost host) {
        return VANILLA_ORE_HOST_ENABLED.getOrDefault(host, true);
    }

    private WosConfig() {
    }
}
