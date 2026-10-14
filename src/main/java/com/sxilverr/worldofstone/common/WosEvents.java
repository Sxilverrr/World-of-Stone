package com.sxilverr.worldofstone.common;

import com.sxilverr.worldofstone.api.enums.FossilVariant;
import com.sxilverr.worldofstone.api.enums.StrataVariant;
import com.sxilverr.worldofstone.common.block.StrataStoneBlock;
import com.sxilverr.worldofstone.config.WosConfig;
import com.sxilverr.worldofstone.registry.StrataBlock;
import com.sxilverr.worldofstone.registry.WosItems;
import dev.architectury.event.EventResult;
import dev.architectury.utils.value.IntValue;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Random;

public final class WosEvents {

    private static final Random RANDOM = new Random();
    private static final List<StrataBlock> MASON_SELLS = List.of(StrataBlock.BRICK, StrataBlock.SANDSTONE, StrataBlock.SANDSTONE_CHISELED,
            StrataBlock.SANDSTONE_SMOOTH, StrataBlock.MOSSY_BRICK);

    public static EventResult onBlockBreak(Level level, BlockPos pos, BlockState state, ServerPlayer player, IntValue xp) {
        if (!WosConfig.fossilsEnabled || WosConfig.fossilDropBlocks.isEmpty()) return EventResult.pass();
        if (player == null || player.isCreative() || !(level instanceof ServerLevel)) return EventResult.pass();
        if (StrataStoneBlock.hasSilkTouch(player.getMainHandItem(), level)) return EventResult.pass();
        if (!WosConfig.fossilDropBlocks.contains(BuiltInRegistries.BLOCK.getKey(state.getBlock()).toString())) return EventResult.pass();
        if (RANDOM.nextDouble() >= WosConfig.fossilDropChance) return EventResult.pass();
        ItemEntity drop = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(WosItems.FOSSILS.get(randomFossil()).get()));
        drop.setDefaultPickUpDelay();
        level.addFreshEntity(drop);
        return EventResult.pass();
    }

    private static FossilVariant randomFossil() {
        int roll = RANDOM.nextInt(9);
        if (roll < 3) return FossilVariant.BONE_FOSSIL;
        if (roll < 5) return FossilVariant.RIB_FOSSIL;
        if (roll < 7) return FossilVariant.SHELL_FOSSIL;
        if (roll < 8) return FossilVariant.AMMONITE_FOSSIL;
        return FossilVariant.SKULL_FOSSIL;
    }

    public static void addMasonTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trades) {
        if (!WosConfig.masonTradesEnabled || !WosConfig.stoneReplacementEnabled) return;
        for (StrataVariant v : StrataVariant.ALL) {
            trades.get(2).add(offer(new ItemStack(Items.EMERALD), new ItemStack(StrataBlock.CHISELED_BRICK.get(v), 4), 12, 5, 0.05F));
        }
        for (StrataVariant v : StrataVariant.ALL) {
            trades.get(3).add(offer(new ItemStack(StrataBlock.COBBLE.get(v), 16), new ItemStack(Items.EMERALD), 16, 10, 0.05F));
            trades.get(3).add(offer(new ItemStack(StrataBlock.STONE.get(v), 16), new ItemStack(Items.EMERALD), 16, 10, 0.05F));
        }
        for (StrataVariant v : StrataVariant.ALL) {
            for (StrataBlock kind : MASON_SELLS) {
                trades.get(3).add(offer(new ItemStack(Items.EMERALD), new ItemStack(kind.get(v), 4), 12, 10, 0.05F));
            }
        }
        if (WosConfig.allowPillarVariantTrades) {
            for (StrataVariant v : StrataVariant.ALL) {
                trades.get(5).add(offer(new ItemStack(Items.EMERALD), new ItemStack(StrataBlock.PILLAR.get(v)), 12, 30, 0.2F));
            }
        }
    }

    private static VillagerTrades.ItemListing offer(ItemStack cost, ItemStack result, int maxUses, int xp, float priceMultiplier) {
        return (trader, random) -> new MerchantOffer(
                /*? if >=1.21.1 {*//*new net.minecraft.world.item.trading.ItemCost(cost.getItem(), cost.getCount())*//*?} else {*/cost.copy()/*?}*/,
                result.copy(), maxUses, xp, priceMultiplier);
    }

    private WosEvents() {
    }
}
