package com.sxilverr.worldofstone.registry;

import com.sxilverr.worldofstone.api.enums.StrataVariant;
import com.sxilverr.worldofstone.common.block.StrataStoneBlock;
import com.sxilverr.worldofstone.common.block.WosInfestedBlock;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

public enum StrataBlock {
    STONE("{}", StrataBlock::stone),
    COBBLE("{}_cobblestone", block(Strength.COBBLE)),
    COBBLE_STAIRS("{}_cobblestone_stairs", stairs(COBBLE, Strength.COBBLE)),
    COBBLE_SLAB("{}_cobblestone_slab", slab(Strength.COBBLE)),
    COBBLE_WALL("{}_cobblestone_wall", wall(Strength.COBBLE)),
    MOSSY_COBBLE("mossy_{}_cobblestone", block(Strength.COBBLE)),
    MOSSY_COBBLE_STAIRS("mossy_{}_cobblestone_stairs", stairs(MOSSY_COBBLE, Strength.COBBLE)),
    MOSSY_COBBLE_SLAB("mossy_{}_cobblestone_slab", slab(Strength.COBBLE)),
    MOSSY_COBBLE_WALL("mossy_{}_cobblestone_wall", wall(Strength.COBBLE)),
    BRICK("{}_bricks", block(Strength.STONE)),
    BRICK_STAIRS("{}_brick_stairs", stairs(BRICK, Strength.STONE)),
    BRICK_SLAB("{}_brick_slab", slab(Strength.STONE)),
    BRICK_WALL("{}_brick_wall", wall(Strength.STONE)),
    MOSSY_BRICK("mossy_{}_bricks", block(Strength.STONE)),
    MOSSY_BRICK_STAIRS("mossy_{}_brick_stairs", stairs(MOSSY_BRICK, Strength.STONE)),
    MOSSY_BRICK_SLAB("mossy_{}_brick_slab", slab(Strength.STONE)),
    MOSSY_BRICK_WALL("mossy_{}_brick_wall", wall(Strength.STONE)),
    CRACKED_BRICK("cracked_{}_bricks", block(Strength.STONE)),
    CHISELED_BRICK("chiseled_{}_bricks", block(Strength.STONE)),
    INFESTED("infested_{}", infested(STONE)),
    INFESTED_COBBLE("infested_{}_cobblestone", infested(COBBLE)),
    INFESTED_BRICK("infested_{}_bricks", infested(BRICK)),
    INFESTED_MOSSY_BRICK("infested_mossy_{}_bricks", infested(MOSSY_BRICK)),
    INFESTED_CRACKED_BRICK("infested_cracked_{}_bricks", infested(CRACKED_BRICK)),
    INFESTED_CHISELED_BRICK("infested_chiseled_{}_bricks", infested(CHISELED_BRICK)),
    BUTTON("{}_button", v -> WosBlocks.button()),
    SAND("{}_sand", v -> WosBlocks.sand()),
    SANDSTONE("{}_sandstone", block(Strength.SANDSTONE)),
    SANDSTONE_STAIRS("{}_sandstone_stairs", stairs(SANDSTONE, Strength.SANDSTONE)),
    SANDSTONE_SLAB("{}_sandstone_slab", slab(Strength.SANDSTONE)),
    SANDSTONE_WALL("{}_sandstone_wall", wall(Strength.SANDSTONE)),
    SANDSTONE_CHISELED("chiseled_{}_sandstone", block(Strength.SANDSTONE)),
    SANDSTONE_CUT("cut_{}_sandstone", block(Strength.SANDSTONE)),
    SANDSTONE_CUT_SLAB("cut_{}_sandstone_slab", slab(Strength.SANDSTONE)),
    SANDSTONE_SMOOTH("smooth_{}_sandstone", block(Strength.SMOOTH)),
    SANDSTONE_SMOOTH_STAIRS("smooth_{}_sandstone_stairs", stairs(SANDSTONE_SMOOTH, Strength.SMOOTH)),
    SANDSTONE_SMOOTH_SLAB("smooth_{}_sandstone_slab", slab(Strength.SMOOTH)),
    GRAVEL("{}_gravel", v -> WosBlocks.gravel()),
    CLAY("{}_clay", v -> WosBlocks.clay()),
    SPELEOTHEM("{}_speleothem", v -> WosBlocks.speleothem(STONE.get(v))),
    SANDSTONE_SPELEOTHEM("{}_sandstone_speleothem", v -> WosBlocks.speleothem(SANDSTONE.get(v))),
    OVERGROWN("overgrown_{}", block(Strength.STONE)),
    SNOWED("snowed_{}", block(Strength.STONE)),
    SMOOTH("smooth_{}", block(Strength.SMOOTH)),
    SMOOTH_SLAB("smooth_{}_slab", slab(Strength.SMOOTH)),
    POLISHED("polished_{}", block(Strength.STONE)),
    POLISHED_STAIRS("polished_{}_stairs", stairs(POLISHED, Strength.STONE)),
    POLISHED_SLAB("polished_{}_slab", slab(Strength.STONE)),
    POLISHED_WALL("polished_{}_wall", wall(Strength.STONE)),
    TILE("{}_tiles", block(Strength.STONE)),
    CRACKED_TILE("cracked_{}_tiles", block(Strength.STONE)),
    TILE_STAIRS("{}_tile_stairs", stairs(TILE, Strength.STONE)),
    TILE_SLAB("{}_tile_slab", slab(Strength.STONE)),
    TILE_WALL("{}_tile_wall", wall(Strength.STONE)),
    PILLAR("{}_pillar", v -> new RotatedPillarBlock(Strength.STONE.props(v)));

    public static final Set<StrataBlock> INFESTED_BLOCKS = EnumSet.range(INFESTED, INFESTED_CHISELED_BRICK);

    private final String pattern;
    private final Function<StrataVariant, Block> factory;
    private final Map<StrataVariant, RegistrySupplier<Block>> blocks = new EnumMap<>(StrataVariant.class);

    StrataBlock(String pattern, Function<StrataVariant, Block> factory) {
        this.pattern = pattern;
        this.factory = factory;
    }

    void register(StrataVariant v) {
        blocks.put(v, WosBlocks.register(pattern.replace("{}", v.toString()), () -> factory.apply(v)));
    }

    public Block get(StrataVariant v) {
        return blocks.get(v).get();
    }

    public List<Block> all() {
        return blocks.values().stream().map(Supplier::get).toList();
    }

    private static Block stone(StrataVariant v) {
        return new StrataStoneBlock(Strength.STONE.props(v).randomTicks(), () -> SNOWED.get(v), () -> COBBLE.get(v), v == StrataVariant.LIGNITE);
    }

    private static Function<StrataVariant, Block> block(Strength s) {
        return v -> new Block(s.props(v));
    }

    private static Function<StrataVariant, Block> stairs(StrataBlock base, Strength s) {
        return v -> new StairBlock(base.get(v).defaultBlockState(), s.props(v));
    }

    private static Function<StrataVariant, Block> slab(Strength s) {
        return v -> new SlabBlock(s.props(v));
    }

    private static Function<StrataVariant, Block> wall(Strength s) {
        return v -> new WallBlock(s.props(v));
    }

    private static Function<StrataVariant, Block> infested(StrataBlock host) {
        return v -> new WosInfestedBlock(host.get(v), BlockBehaviour.Properties.of()
                .mapColor(host.get(v).defaultMapColor())
                .strength(0.0F, 0.75F));
    }

    private enum Strength {
        STONE(1.5F, 6.0F, true),
        COBBLE(2.0F, 6.0F, true),
        SANDSTONE(0.8F, 0.8F, false),
        SMOOTH(2.0F, 6.0F, false);

        private final float hardness;
        private final float resistance;
        private final boolean scaled;

        Strength(float hardness, float resistance, boolean scaled) {
            this.hardness = hardness;
            this.resistance = resistance;
            this.scaled = scaled;
        }

        BlockBehaviour.Properties props(StrataVariant v) {
            return scaled ? WosBlocks.stoneProps(hardness * v.hardness, resistance * v.resistance) : WosBlocks.stoneProps(hardness, resistance);
        }
    }
}
