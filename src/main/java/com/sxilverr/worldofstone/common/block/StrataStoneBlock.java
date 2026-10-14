package com.sxilverr.worldofstone.common.block;

import com.sxilverr.worldofstone.config.WosConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class StrataStoneBlock extends Block {

    private final Supplier<Block> snowedVariant;
    private final Supplier<Block> cobbleVariant;
    private final boolean isLignite;

    public StrataStoneBlock(Properties props, Supplier<Block> snowedVariant, Supplier<Block> cobbleVariant, boolean isLignite) {
        super(props);
        this.snowedVariant = snowedVariant;
        this.cobbleVariant = cobbleVariant;
        this.isLignite = isLignite;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource rand) {
        BlockState above = level.getBlockState(pos.above());
        if (above.is(Blocks.SNOW) || above.is(Blocks.SNOW_BLOCK)) {
            Block snowed = snowedVariant.get();
            if (snowed != null) {
                level.setBlockAndUpdate(pos, snowed.defaultBlockState());
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        ItemStack tool = params.getOptionalParameter(LootContextParams.TOOL);
        if (hasSilkTouch(tool, params.getLevel())) return super.getDrops(state, params);

        if (isLignite) {
            if (WosConfig.ligniteDropsLigniteCobblestone && cobbleVariant != null) {
                Block cobble = cobbleVariant.get();
                if (cobble != null) return Collections.singletonList(new ItemStack(cobble));
            }
            return super.getDrops(state, params);
        }

        if (!WosConfig.stoneDropsCobblestone) {
            List<ItemStack> drops = super.getDrops(state, params);
            Block cobble = cobbleVariant != null ? cobbleVariant.get() : null;
            if (cobble == null) {
                return Collections.singletonList(new ItemStack(this));
            }
            List<ItemStack> result = new ArrayList<>(drops.size());
            boolean replacedCobble = false;
            for (ItemStack stack : drops) {
                if (!replacedCobble && stack.is(cobble.asItem())) {
                    result.add(new ItemStack(this));
                    replacedCobble = true;
                } else {
                    result.add(stack);
                }
            }
            if (!replacedCobble) {
                result.add(new ItemStack(this));
            }
            return result;
        }

        return super.getDrops(state, params);
    }

    public static boolean hasSilkTouch(ItemStack tool, Level level) {
        if (tool == null) return false;
        //? if >=1.21.1 {
        /*net.minecraft.core.Holder<net.minecraft.world.item.enchantment.Enchantment> silk =
                level.registryAccess()
                        .lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT)
                        .getOrThrow(Enchantments.SILK_TOUCH);
        return EnchantmentHelper.getItemEnchantmentLevel(silk, tool) > 0;
        *///?} else {
        return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0;
        //?}
    }
}
