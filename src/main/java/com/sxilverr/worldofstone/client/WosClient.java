package com.sxilverr.worldofstone.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.ModdedSpeleothemVariant;
import com.sxilverr.worldofstone.registry.StrataBlock;
import com.sxilverr.worldofstone.registry.WosBlocks;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public final class WosClient {

    private static final Set<DecorativeSpeleothemVariant> TRANSLUCENT = EnumSet.of(
            DecorativeSpeleothemVariant.TINTED_GLASS, DecorativeSpeleothemVariant.WHITE_STAINED_GLASS, DecorativeSpeleothemVariant.ORANGE_STAINED_GLASS,
            DecorativeSpeleothemVariant.MAGENTA_STAINED_GLASS, DecorativeSpeleothemVariant.LIGHT_BLUE_STAINED_GLASS, DecorativeSpeleothemVariant.YELLOW_STAINED_GLASS,
            DecorativeSpeleothemVariant.LIME_STAINED_GLASS, DecorativeSpeleothemVariant.PINK_STAINED_GLASS, DecorativeSpeleothemVariant.GRAY_STAINED_GLASS,
            DecorativeSpeleothemVariant.LIGHT_GRAY_STAINED_GLASS, DecorativeSpeleothemVariant.CYAN_STAINED_GLASS, DecorativeSpeleothemVariant.PURPLE_STAINED_GLASS,
            DecorativeSpeleothemVariant.BLUE_STAINED_GLASS, DecorativeSpeleothemVariant.BROWN_STAINED_GLASS, DecorativeSpeleothemVariant.GREEN_STAINED_GLASS,
            DecorativeSpeleothemVariant.RED_STAINED_GLASS, DecorativeSpeleothemVariant.BLACK_STAINED_GLASS, DecorativeSpeleothemVariant.SLIME,
            DecorativeSpeleothemVariant.HONEY);
    private static final List<DecorativeSpeleothemVariant> FOLIAGE = List.of(DecorativeSpeleothemVariant.OAK_LEAVES, DecorativeSpeleothemVariant.JUNGLE_LEAVES,
            DecorativeSpeleothemVariant.ACACIA_LEAVES, DecorativeSpeleothemVariant.DARK_OAK_LEAVES, DecorativeSpeleothemVariant.MANGROVE_LEAVES);
    private static final int MYALITE_PURPLE = 0xFFAE51DA;

    private static Map<Block, Integer> constantTints;

    public static void renderTypes() {
        List<Block> cutout = new ArrayList<>();
        for (StrataBlock kind : List.of(StrataBlock.OVERGROWN, StrataBlock.SNOWED, StrataBlock.SPELEOTHEM)) cutout.addAll(kind.all());
        WosBlocks.VANILLA_HOST_OVERGROWN.values().forEach(b -> cutout.add(b.get()));
        WosBlocks.VANILLA_HOST_SNOWED.values().forEach(b -> cutout.add(b.get()));
        WosBlocks.VANILLA_SPELEOTHEM.values().forEach(b -> cutout.add(b.get()));
        WosBlocks.MODDED_SPELEOTHEM.forEach((v, b) -> {
            if (v.modId.equals("quark")) cutout.add(b.get());
        });
        WosBlocks.DECORATIVE_SPELEOTHEM.forEach((v, b) -> {
            if (TRANSLUCENT.contains(v)) RenderTypeRegistry.register(RenderType.translucent(), b.get());
            else cutout.add(b.get());
        });
        if (WosBlocks.MIMIC_SPELEOTHEM != null && WosBlocks.MIMIC_SPELEOTHEM.isPresent()) cutout.add(WosBlocks.MIMIC_SPELEOTHEM.get());
        RenderTypeRegistry.register(RenderType.cutoutMipped(), cutout.toArray(Block[]::new));
        RenderTypeRegistry.register(RenderType.translucent(), StrataBlock.GLASS.all().toArray(Block[]::new));
        RenderTypeRegistry.register(RenderType.translucent(), StrataBlock.GLASS_PANE.all().toArray(Block[]::new));
    }

    public static void blockColors(BiConsumer<BlockColor, Block> register) {
        BlockColor grass = (state, level, pos, tint) -> tint != 0 ? -1 : level != null && pos != null ? BiomeColors.getAverageGrassColor(level, pos) : GrassColor.get(0.5D, 1.0D);
        overgrown().forEach(block -> register.accept(grass, block));
        constantTints().forEach((block, color) -> register.accept((s, l, p, t) -> color, block));
        BlockColor foliage = (state, level, pos, tint) -> tint != 0 ? -1 : level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.getDefaultColor();
        FOLIAGE.forEach(v -> register.accept(foliage, decorative(v)));
        int evergreen = FoliageColor.getEvergreenColor();
        register.accept((s, l, p, t) -> t == 0 ? evergreen : -1, decorative(DecorativeSpeleothemVariant.SPRUCE_LEAVES));
        int birch = FoliageColor.getBirchColor();
        register.accept((s, l, p, t) -> t == 0 ? birch : -1, decorative(DecorativeSpeleothemVariant.BIRCH_LEAVES));
    }

    public static void itemColors(BiConsumer<ItemColor, ItemLike> register) {
        ItemColor grass = (stack, tint) -> tint == 0 ? GrassColor.get(0.5D, 1.0D) : -1;
        overgrown().forEach(block -> register.accept(grass, block));
        constantTints().forEach((block, color) -> register.accept((stack, tint) -> color, block));
        int foliage = FoliageColor.getDefaultColor();
        FOLIAGE.forEach(v -> register.accept((stack, tint) -> tint == 0 ? foliage : -1, decorative(v)));
        int evergreen = FoliageColor.getEvergreenColor();
        register.accept((stack, tint) -> tint == 0 ? evergreen : -1, decorative(DecorativeSpeleothemVariant.SPRUCE_LEAVES));
        int birch = FoliageColor.getBirchColor();
        register.accept((stack, tint) -> tint == 0 ? birch : -1, decorative(DecorativeSpeleothemVariant.BIRCH_LEAVES));
    }

    private static List<Block> overgrown() {
        List<Block> blocks = new ArrayList<>(StrataBlock.OVERGROWN.all());
        WosBlocks.VANILLA_HOST_OVERGROWN.values().forEach(b -> blocks.add(b.get()));
        return blocks;
    }

    private static Block decorative(DecorativeSpeleothemVariant v) {
        return WosBlocks.DECORATIVE_SPELEOTHEM.get(v).get();
    }

    private static Map<Block, Integer> constantTints() {
        if (constantTints != null) return constantTints;
        constantTints = new LinkedHashMap<>();
        try (InputStream in = WosClient.class.getResourceAsStream("/data/worldofstone/sand_tints.json")) {
            if (in != null) {
                JsonObject tints = JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
                for (String name : tints.keySet()) {
                    JsonObject rgb = tints.getAsJsonObject(name);
                    int color = 0xFF000000 | (rgb.get("r").getAsInt() << 16) | (rgb.get("g").getAsInt() << 8) | rgb.get("b").getAsInt();
                    BuiltInRegistries.BLOCK.getOptional(ModInfo.rl(name)).ifPresent(block -> constantTints.put(block, color));
                }
            }
        } catch (Exception ignored) {
        }
        RegistrySupplier<Block> myalite = WosBlocks.MODDED_SPELEOTHEM.get(ModdedSpeleothemVariant.QUARK_MYALITE);
        if (myalite != null) constantTints.put(myalite.get(), MYALITE_PURPLE);
        return constantTints;
    }

    private WosClient() {
    }
}
