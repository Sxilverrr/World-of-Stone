package com.sxilverr.worldofstone.compat.jer;

import com.mojang.logging.LogUtils;
import com.sxilverr.worldofstone.api.enums.OreVariant;
import com.sxilverr.worldofstone.api.enums.StrataVariant;
import com.sxilverr.worldofstone.api.enums.VanillaOreHost;
import com.sxilverr.worldofstone.config.WosConfig;
import com.sxilverr.worldofstone.registry.StrataBlock;
import com.sxilverr.worldofstone.registry.WosBlocks;
import com.sxilverr.worldofstone.registry.WosCreativeTabs;
import com.sxilverr.worldofstone.registry.WosItems;
import com.sxilverr.worldofstone.world.OtherDimensionOreFeature;
import jeresources.api.IJERAPI;
import jeresources.api.IWorldGenRegistry;
import jeresources.api.conditionals.Conditional;
import jeresources.api.distributions.DistributionBase;
import jeresources.api.distributions.DistributionSquare;
import jeresources.api.drop.LootDrop;
import jeresources.api.restrictions.DimensionRestriction;
import jeresources.api.restrictions.Restriction;
import jeresources.compatibility.api.JERAPI;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

public final class WorldOfStoneJERPlugin {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int OVERWORLD_TOP_NO_ABOVE_GROUND = 63;
    private static final int OVERWORLD_TOP_ABOVE_GROUND = 200;
    private static final int OTHER_DIMENSION_MAX_Y = 128;
    private static final int OBSIDIAN_MIN_Y = -64;
    private static final int OBSIDIAN_MAX_Y = 320;
    private static final List<StrataVariant> FOSSIL_STONES = List.of(StrataVariant.CHALK, StrataVariant.LIMESTONE, StrataVariant.DOLOMITE, StrataVariant.SHALE);
    private static final Map<OreVariant, Drop> DROPS = Map.of(
            OreVariant.COAL, new Drop(Items.COAL, 1, 1), OreVariant.IRON, new Drop(Items.RAW_IRON, 1, 1),
            OreVariant.GOLD, new Drop(Items.RAW_GOLD, 1, 1), OreVariant.DIAMOND, new Drop(Items.DIAMOND, 1, 1),
            OreVariant.EMERALD, new Drop(Items.EMERALD, 1, 1), OreVariant.REDSTONE, new Drop(Items.REDSTONE, 4, 5),
            OreVariant.LAPIS, new Drop(Items.LAPIS_LAZULI, 4, 9), OreVariant.COPPER, new Drop(Items.RAW_COPPER, 2, 5));

    private record Drop(Item item, int min, int max) {
        LootDrop loot() {
            return new LootDrop(new ItemStack(item), min, max, Conditional.affectedByFortune);
        }
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
        int registered = registerOverworldSedimentary(registry) + registerNonOverworldVariants(registry);
        LOGGER.info("[WorldOfStone] JER plugin: registered {} entries (lignite/fossils + non-overworld variants)", registered);
    }

    private static int registerOverworldSedimentary(IWorldGenRegistry registry) {
        if (!WosConfig.stoneReplacementEnabled) return 0;
        Restriction overworld = new Restriction(DimensionRestriction.OVERWORLD);
        int top = WosConfig.replaceStoneAboveGround ? OVERWORLD_TOP_ABOVE_GROUND : OVERWORLD_TOP_NO_ABOVE_GROUND;
        int bottom = WosConfig.allowStrataInDeepslate ? -64 : 0;
        int yMin = bottom + (int) Math.round((top - bottom) * WosConfig.strataMiddleRatio);
        DistributionBase sedimentary = new DistributionSquare(yMin, top, 1.0f / StrataVariant.Family.SEDIMENTARY.variants().size());

        int registered = 0;
        if (register(registry, StrataBlock.STONE.get(StrataVariant.LIGNITE), sedimentary, overworld, new LootDrop(new ItemStack(WosItems.LIGNITE_COAL.get()), 1, 1))) {
            registered++;
        }
        if (!WosConfig.fossilsEnabled) return registered;
        float fossilChance = Mth.clamp((float) WosConfig.fossilDropChance, 0f, 1f);
        LootDrop[] fossils = WosItems.FOSSILS.values().stream().map(f -> new LootDrop(new ItemStack(f.get()), fossilChance, 0)).toArray(LootDrop[]::new);
        for (StrataVariant v : FOSSIL_STONES) {
            if (register(registry, StrataBlock.STONE.get(v), sedimentary, overworld, fossils)) registered++;
        }
        return registered;
    }

    private static int registerNonOverworldVariants(IWorldGenRegistry registry) {
        if (!WosConfig.replaceVanillaOres) return 0;
        int registered = 0;
        for (VanillaOreHost host : VanillaOreHost.VALUES) {
            if (host.dimension == VanillaOreHost.HostDimension.OVERWORLD || !WosCreativeTabs.shown(host)) continue;
            Restriction restriction = new Restriction(switch (host.dimension) {
                case NETHER -> DimensionRestriction.NETHER;
                case END -> DimensionRestriction.END;
                default -> DimensionRestriction.NONE;
            });
            for (OreVariant ore : OreVariant.VALUES) {
                if (register(registry, WosBlocks.ore(host.toString(), ore), distribution(host, ore), restriction, DROPS.get(ore).loot())) registered++;
            }
        }
        return registered;
    }

    private static DistributionBase distribution(VanillaOreHost host, OreVariant ore) {
        if (host.dimension == VanillaOreHost.HostDimension.ANY) {
            float chance = OtherDimensionOreFeature.OBSIDIAN_ATTEMPTS_PER_CHUNK / (16f * 16f * (float) (OBSIDIAN_MAX_Y - OBSIDIAN_MIN_Y));
            return new DistributionSquare(OBSIDIAN_MIN_Y, OBSIDIAN_MAX_Y, Mth.clamp(chance, 0f, 1f));
        }
        int attempts = OtherDimensionOreFeature.attempts(ore, host.dimension == VanillaOreHost.HostDimension.NETHER);
        float chance = attempts * (float) ore.veinSize / (16f * 16f * (float) OTHER_DIMENSION_MAX_Y);
        return new DistributionSquare(0, OTHER_DIMENSION_MAX_Y, Mth.clamp(chance, 0f, 1f));
    }

    private static boolean register(IWorldGenRegistry registry, Block block, DistributionBase distribution, Restriction restriction, LootDrop... drops) {
        try {
            registry.register(new ItemStack(block), distribution, restriction, drops);
            return true;
        } catch (Throwable t) {
            LOGGER.warn("[WorldOfStone] failed to register JER entry for {}: {}", block, t.toString());
            return false;
        }
    }
}
