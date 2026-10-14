package com.sxilverr.worldofstone.registry;

import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.ModdedSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.OreVariant;
import com.sxilverr.worldofstone.api.enums.StrataVariant;
import com.sxilverr.worldofstone.api.enums.TerracottaSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.VanillaOreHost;
import com.sxilverr.worldofstone.api.enums.VanillaSpeleothemVariant;
import com.sxilverr.worldofstone.common.block.WosHoneySpeleothemBlock;
import com.sxilverr.worldofstone.common.block.WosMagmaSpeleothemBlock;
import com.sxilverr.worldofstone.common.block.WosSlimeSpeleothemBlock;
import com.sxilverr.worldofstone.common.block.WosSoulSandSpeleothemBlock;
import com.sxilverr.worldofstone.common.block.WosSpeleothemBlock;
import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class WosBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ModInfo.MODID, Registries.BLOCK);

    public static final Map<VanillaSpeleothemVariant, RegistrySupplier<Block>> VANILLA_SPELEOTHEM = new EnumMap<>(VanillaSpeleothemVariant.class);
    public static final Map<ModdedSpeleothemVariant, RegistrySupplier<Block>> MODDED_SPELEOTHEM = new EnumMap<>(ModdedSpeleothemVariant.class);
    public static final Map<TerracottaSpeleothemVariant, RegistrySupplier<Block>> TERRACOTTA_SPELEOTHEM = new EnumMap<>(TerracottaSpeleothemVariant.class);
    public static final Map<DecorativeSpeleothemVariant, RegistrySupplier<Block>> DECORATIVE_SPELEOTHEM = new EnumMap<>(DecorativeSpeleothemVariant.class);
    public static final Map<String, RegistrySupplier<Block>> ORES = new HashMap<>();
    public static final Map<String, RegistrySupplier<Block>> VANILLA_HOST_OVERGROWN = new HashMap<>();
    public static final Map<String, RegistrySupplier<Block>> VANILLA_HOST_SNOWED = new HashMap<>();
    public static final String[] VANILLA_HOSTS = {"stone", "granite", "diorite", "andesite", "tuff"};

    public static RegistrySupplier<Block> DEV_SPELEOTHEM;
    public static RegistrySupplier<Block> MIMIC_SPELEOTHEM;

    private static Set<Block> infestedHosts;

    static {
        for (StrataVariant v : StrataVariant.ALL) {
            for (StrataBlock kind : StrataBlock.values()) {
                kind.register(v);
            }
        }
        for (VanillaSpeleothemVariant v : VanillaSpeleothemVariant.values()) {
            VANILLA_SPELEOTHEM.put(v, register(v.getRegistryName(), () -> speleothem(host(v.host).orElseThrow())));
        }
        for (ModdedSpeleothemVariant v : ModdedSpeleothemVariant.values()) {
            if (Platform.isModLoaded(v.modId)) {
                MODDED_SPELEOTHEM.put(v, register(v.getRegistryName(), () -> host(v.host).map(WosBlocks::speleothem)
                        .orElseGet(() -> new WosSpeleothemBlock(speleothemProps(1.5F, 6.0F)))));
            }
        }
        for (TerracottaSpeleothemVariant v : TerracottaSpeleothemVariant.values()) {
            TERRACOTTA_SPELEOTHEM.put(v, register(v.getRegistryName(), () -> new WosSpeleothemBlock(speleothemProps(1.25F, 4.2F))));
        }
        for (DecorativeSpeleothemVariant v : DecorativeSpeleothemVariant.values()) {
            DECORATIVE_SPELEOTHEM.put(v, register(v.getRegistryName(), () -> decorative(v)));
        }
        for (StrataVariant v : StrataVariant.ALL) {
            registerOres(v.toString());
        }
        for (VanillaOreHost host : VanillaOreHost.VALUES) {
            registerOres(host.toString());
        }
        for (String host : VANILLA_HOSTS) {
            VANILLA_HOST_OVERGROWN.put(host, register("overgrown_" + host, () -> new Block(stoneProps(1.5F, 6.0F))));
            VANILLA_HOST_SNOWED.put(host, register("snowed_" + host, () -> new Block(stoneProps(1.5F, 6.0F))));
        }
    }

    public static RegistrySupplier<Block> register(String name, Supplier<Block> block) {
        RegistrySupplier<Block> registered = BLOCKS.register(name, block);
        WosItems.ITEMS.register(name, () -> new BlockItem(registered.get(), new Item.Properties()));
        return registered;
    }

    public static Block ore(String host, OreVariant ore) {
        RegistrySupplier<Block> block = ORES.get(host + "_" + ore.suffix);
        return block == null ? null : block.get();
    }

    public static boolean isInfestedHost(Block block) {
        if (infestedHosts == null) {
            infestedHosts = StrataBlock.INFESTED_BLOCKS.stream()
                    .flatMap(kind -> kind.all().stream())
                    .map(b -> ((InfestedBlock) b).getHostBlock())
                    .collect(Collectors.toSet());
        }
        return infestedHosts.contains(block);
    }

    private static void registerOres(String host) {
        boolean obsidian = host.equals("obsidian");
        for (OreVariant ore : OreVariant.VALUES) {
            String name = host + "_" + ore.suffix;
            ORES.put(name, register(name, () -> {
                BlockBehaviour.Properties props = obsidian ? stoneProps(50.0F, 1200.0F) : stoneProps(3.0F, 3.0F);
                return new DropExperienceBlock(/*? if >=1.21.1 {*//*ore.getXpProvider(), props*//*?} else {*/props, ore.getXpProvider()/*?}*/);
            }));
        }
    }

    private static Optional<Block> host(String id) {
        return BuiltInRegistries.BLOCK.getOptional(ModInfo.id(id));
    }

    static Block speleothem(Block host) {
        return new WosSpeleothemBlock(hostProps(host, host.defaultBlockState().getLightEmission()));
    }

    private static Block decorative(DecorativeSpeleothemVariant v) {
        BlockBehaviour.Properties props = switch (v) {
            case BEDROCK -> speleothemProps(-1.0F, 3600000.0F);
            case END_PORTAL_FRAME -> speleothemProps(-1.0F, 3600000.0F).mapColor(MapColor.COLOR_BLACK).lightLevel(state -> v.light);
            default -> hostProps(host(v.getHostBlock()).orElseThrow(), v.light);
        };
        return switch (v) {
            case MAGMA -> new WosMagmaSpeleothemBlock(props);
            case SLIME -> new WosSlimeSpeleothemBlock(props);
            case HONEY -> new WosHoneySpeleothemBlock(props);
            case SOUL_SAND -> new WosSoulSandSpeleothemBlock(props);
            default -> new WosSpeleothemBlock(props);
        };
    }

    private static BlockBehaviour.Properties hostProps(Block host, int light) {
        return (/*? if >=1.21.1 {*//*BlockBehaviour.Properties.ofFullCopy(host)*//*?} else {*/BlockBehaviour.Properties.copy(host)/*?}*/)
                .mapColor(host.defaultMapColor())
                .sound(host.defaultBlockState().getSoundType())
                .lightLevel(state -> light)
                .emissiveRendering((s, l, p) -> false)
                .hasPostProcess((s, l, p) -> false)
                .isRedstoneConductor((s, l, p) -> false)
                .isSuffocating((s, l, p) -> false)
                .isViewBlocking((s, l, p) -> false)
                .noOcclusion()
                .dynamicShape();
    }

    static BlockBehaviour.Properties stoneProps(float hardness, float resistance) {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .sound(SoundType.STONE)
                .strength(hardness, resistance)
                .requiresCorrectToolForDrops();
    }

    private static BlockBehaviour.Properties speleothemProps(float hardness, float resistance) {
        return stoneProps(hardness, resistance).noOcclusion().dynamicShape();
    }

    static Block sand() {
        return /*? if >=1.21.1 {*//*new net.minecraft.world.level.block.ColoredFallingBlock(new net.minecraft.util.ColorRGBA(0xDDD2A3),*//*?} else {*/new net.minecraft.world.level.block.SandBlock(0xDDD2A3,/*?}*/
                BlockBehaviour.Properties.of().mapColor(MapColor.SAND).sound(SoundType.SAND).strength(0.5F, 0.5F));
    }

    static Block gravel() {
        return /*? if >=1.21.1 {*//*new net.minecraft.world.level.block.ColoredFallingBlock(new net.minecraft.util.ColorRGBA(-8356741),*//*?} else {*/new net.minecraft.world.level.block.GravelBlock(/*?}*/
                BlockBehaviour.Properties.of().mapColor(MapColor.STONE).sound(SoundType.GRAVEL).strength(0.6F, 0.6F));
    }

    static Block clay() {
        return new Block(BlockBehaviour.Properties.of().mapColor(MapColor.CLAY).sound(SoundType.GRAVEL).strength(0.6F, 0.6F));
    }

    static Block button() {
        BlockBehaviour.Properties props = BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .noCollission()
                .strength(0.5F)
                .pushReaction(PushReaction.DESTROY);
        return new ButtonBlock(/*? if >=1.21.1 {*//*BlockSetType.STONE, 20, props*//*?} else {*/props, BlockSetType.STONE, 20, false/*?}*/);
    }

    private WosBlocks() {
    }
}
