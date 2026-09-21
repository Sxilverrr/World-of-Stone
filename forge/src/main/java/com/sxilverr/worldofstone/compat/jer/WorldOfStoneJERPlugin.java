package com.sxilverr.worldofstone.compat.jer;

import com.sxilverr.worldofstone.api.enums.FossilVariant;
import com.sxilverr.worldofstone.api.enums.OreVariant;
import com.sxilverr.worldofstone.api.enums.SedimentaryVariant;
import com.sxilverr.worldofstone.api.enums.VanillaOreHost;
import com.sxilverr.worldofstone.config.WosConfig;
import com.sxilverr.worldofstone.registry.WosBlocks;
import com.sxilverr.worldofstone.registry.WosItems;
import com.sxilverr.worldofstone.world.OtherDimensionOreFeature;
import net.minecraft.world.item.Item;
import com.mojang.logging.LogUtils;
import jeresources.api.IJERAPI;
import jeresources.api.IWorldGenRegistry;
import jeresources.api.conditionals.Conditional;
import jeresources.api.distributions.DistributionBase;
import jeresources.api.distributions.DistributionSquare;
import jeresources.api.drop.LootDrop;
import jeresources.api.restrictions.DimensionRestriction;
import jeresources.api.restrictions.Restriction;
import jeresources.compatibility.api.JERAPI;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import dev.architectury.registry.registries.RegistrySupplier;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public final class WorldOfStoneJERPlugin {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int OVERWORLD_TOP_NO_ABOVE_GROUND = 63;
    private static final int OVERWORLD_TOP_ABOVE_GROUND = 200;
    private static final int NETHER_MAX_Y = 128;
    private static final int END_MAX_Y = 128;
    private static final int OBSIDIAN_MIN_Y = -64;
    private static final int OBSIDIAN_MAX_Y = 320;

    private WorldOfStoneJERPlugin() {
    }

    public static void register() {
        IJERAPI api;
        try {
            api = JERAPI.getInstance();
        } catch (Throwable t) {
            LOGGER.warn("[WorldOfStone] JER API unavailable: {}", t.toString());
            return;
        }
        if (api == null) {
            LOGGER.warn("[WorldOfStone] JER API was null");
            return;
        }

        IWorldGenRegistry registry = api.getWorldGenRegistry();
        int registered = 0;
        registered += registerOverworldSedimentary(registry);
        registered += registerNonOverworldVariants(registry);
        LOGGER.info("[WorldOfStone] JER plugin: registered {} entries (lignite/fossils + non-overworld variants)", registered);
    }

    private static int registerOverworldSedimentary(IWorldGenRegistry registry) {
        if (!WosConfig.stoneReplacementEnabled) {
            return 0;
        }
        Restriction overworld = new Restriction(DimensionRestriction.OVERWORLD);
        int yMin = sedimentaryMinY();
        int yMax = sedimentaryMaxY();
        float perVariantChance = 1.0f / (float) SedimentaryVariant.VALUES.length;
        DistributionBase sedimentaryDist = new DistributionSquare(yMin, yMax, perVariantChance);

        int registered = 0;
        RegistrySupplier<Block> lignite = WosBlocks.SEDIMENTARY_STONE.get(SedimentaryVariant.LIGNITE);
        if (lignite != null && lignite.isPresent() && WosItems.LIGNITE_COAL.isPresent()) {
            try {
                registry.register(
                        new ItemStack(lignite.get()),
                        sedimentaryDist,
                        overworld,
                        new LootDrop(new ItemStack(WosItems.LIGNITE_COAL.get()), 1, 1)
                );
                registered++;
            } catch (Throwable t) {
                LOGGER.warn("[WorldOfStone] failed to register lignite distribution: {}", t.toString());
            }
        }

        if (!WosConfig.isFossilsEnabled()) {
            return registered;
        }

        SedimentaryVariant[] fossilStones = {
                SedimentaryVariant.CHALK,
                SedimentaryVariant.LIMESTONE,
                SedimentaryVariant.DOLOMITE,
                SedimentaryVariant.SHALE
        };
        float fossilChance = clampChance((float) WosConfig.fossilDropChance);
        List<LootDrop> fossilDrops = new ArrayList<>();
        for (FossilVariant fv : FossilVariant.VALUES) {
            RegistrySupplier<Item> fossil = WosItems.FOSSILS.get(fv);
            if (fossil != null && fossil.isPresent()) {
                fossilDrops.add(new LootDrop(new ItemStack(fossil.get()), fossilChance, 0));
            }
        }
        if (!fossilDrops.isEmpty()) {
            LootDrop[] dropsArr = fossilDrops.toArray(new LootDrop[0]);
            for (SedimentaryVariant v : fossilStones) {
                RegistrySupplier<Block> stone = WosBlocks.SEDIMENTARY_STONE.get(v);
                if (stone != null && stone.isPresent()) {
                    try {
                        registry.register(new ItemStack(stone.get()), sedimentaryDist, overworld, dropsArr);
                        registered++;
                    } catch (Throwable t) {
                        LOGGER.warn("[WorldOfStone] failed to register fossil drops for {}: {}", v, t.toString());
                    }
                }
            }
        }
        return registered;
    }

    private static int registerNonOverworldVariants(IWorldGenRegistry registry) {
        if (!WosConfig.replaceVanillaOres) {
            return 0;
        }
        Map<OreVariant, LootDrop> drops = oreDrops();
        Function<OreVariant, DistributionBase> netherDist = netherDistribution();
        Function<OreVariant, DistributionBase> endDist = endDistribution();
        Function<OreVariant, DistributionBase> obsidianDist = obsidianDistribution();

        boolean netherEnabled = WosConfig.allowVanillaOresInNether;
        boolean endEnabled = WosConfig.allowVanillaOresInEnd;
        boolean obsidianEnabled = WosConfig.allowObsidianOreVariants;

        int registered = 0;
        for (VanillaOreHost host : VanillaOreHost.VALUES) {
            switch (host.getDimension()) {
                case NETHER:
                    if (netherEnabled) {
                        registered += registerHostOres(registry, host, drops, new Restriction(DimensionRestriction.NETHER), netherDist);
                    }
                    break;
                case END:
                    if (endEnabled) {
                        registered += registerHostOres(registry, host, drops, new Restriction(DimensionRestriction.END), endDist);
                    }
                    break;
                case ANY:
                    if (obsidianEnabled) {
                        registered += registerHostOres(registry, host, drops, new Restriction(DimensionRestriction.NONE), obsidianDist);
                    }
                    break;
                case OVERWORLD:
                default:
                    break;
            }
        }
        return registered;
    }

    private static int registerHostOres(
            IWorldGenRegistry registry,
            VanillaOreHost host,
            Map<OreVariant, LootDrop> drops,
            Restriction restriction,
            Function<OreVariant, DistributionBase> distFn) {
        int count = 0;
        for (OreVariant ore : OreVariant.VALUES) {
            RegistrySupplier<Block> block = WosBlocks.ORES.get(host.getRegistryName() + "_" + ore.suffix);
            LootDrop drop = drops.get(ore);
            if (block == null || !block.isPresent() || drop == null) continue;
            try {
                registry.register(new ItemStack(block.get()), distFn.apply(ore), restriction, drop);
                count++;
            } catch (Throwable t) {
                LOGGER.warn("[WorldOfStone] failed to register {}_{}: {}", host.getRegistryName(), ore.suffix, t.toString());
            }
        }
        return count;
    }

    private static Function<OreVariant, DistributionBase> netherDistribution() {
        return ore -> {
            int attempts = OtherDimensionOreFeature.attemptsForOreNether(ore);
            int veinSize = OtherDimensionOreFeature.veinSizeForOre(ore);
            float chance = clampChance(attempts * (float) veinSize / (16f * 16f * (float) NETHER_MAX_Y));
            return new DistributionSquare(0, NETHER_MAX_Y, chance);
        };
    }

    private static Function<OreVariant, DistributionBase> endDistribution() {
        return ore -> {
            int attempts = OtherDimensionOreFeature.attemptsForOreEnd(ore);
            int veinSize = OtherDimensionOreFeature.veinSizeForOre(ore);
            float chance = clampChance(attempts * (float) veinSize / (16f * 16f * (float) END_MAX_Y));
            return new DistributionSquare(0, END_MAX_Y, chance);
        };
    }

    private static Function<OreVariant, DistributionBase> obsidianDistribution() {
        return ore -> {
            int yRange = OBSIDIAN_MAX_Y - OBSIDIAN_MIN_Y;
            float chance = clampChance(OtherDimensionOreFeature.OBSIDIAN_ATTEMPTS_PER_CHUNK / (16f * 16f * (float) yRange));
            return new DistributionSquare(OBSIDIAN_MIN_Y, OBSIDIAN_MAX_Y, chance);
        };
    }

    private static float clampChance(float chance) {
        if (chance < 0f) return 0f;
        if (chance > 1f) return 1f;
        return chance;
    }

    private static int sedimentaryMinY() {
        boolean deepslate = WosConfig.allowStrataInDeepslate;
        boolean aboveGround = WosConfig.replaceStoneAboveGround;
        int bottom = deepslate ? -64 : 0;
        int top = aboveGround ? OVERWORLD_TOP_ABOVE_GROUND : OVERWORLD_TOP_NO_ABOVE_GROUND;
        double ratio = WosConfig.strataMiddleRatio;
        return bottom + (int) Math.round((top - bottom) * ratio);
    }

    private static int sedimentaryMaxY() {
        return WosConfig.replaceStoneAboveGround ? OVERWORLD_TOP_ABOVE_GROUND : OVERWORLD_TOP_NO_ABOVE_GROUND;
    }

    private static Map<OreVariant, LootDrop> oreDrops() {
        Map<OreVariant, LootDrop> drops = new EnumMap<>(OreVariant.class);
        drops.put(OreVariant.COAL, new LootDrop(new ItemStack(Items.COAL), 1, 1, Conditional.affectedByFortune));
        drops.put(OreVariant.IRON, new LootDrop(new ItemStack(Items.RAW_IRON), 1, 1, Conditional.affectedByFortune));
        drops.put(OreVariant.GOLD, new LootDrop(new ItemStack(Items.RAW_GOLD), 1, 1, Conditional.affectedByFortune));
        drops.put(OreVariant.DIAMOND, new LootDrop(new ItemStack(Items.DIAMOND), 1, 1, Conditional.affectedByFortune));
        drops.put(OreVariant.EMERALD, new LootDrop(new ItemStack(Items.EMERALD), 1, 1, Conditional.affectedByFortune));
        drops.put(OreVariant.REDSTONE, new LootDrop(new ItemStack(Items.REDSTONE), 4, 5, Conditional.affectedByFortune));
        drops.put(OreVariant.LAPIS, new LootDrop(new ItemStack(Items.LAPIS_LAZULI), 4, 9, Conditional.affectedByFortune));
        drops.put(OreVariant.COPPER, new LootDrop(new ItemStack(Items.RAW_COPPER), 2, 5, Conditional.affectedByFortune));
        return drops;
    }
}
