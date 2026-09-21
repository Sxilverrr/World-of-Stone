package com.sxilverr.worldofstone.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.QuarkSpeleothemVariant;
import com.sxilverr.worldofstone.registry.WosBlocks;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import dev.architectury.registry.registries.RegistrySupplier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ModInfo.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class WosClient {

    private static Map<String, Integer> sandTints = null;

    private WosClient() {
    }

    @SuppressWarnings("removal")
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            WosBlocks.IGNEOUS_OVERGROWN.values().forEach(ro -> setCutoutMipped(ro.get()));
            WosBlocks.IGNEOUS_OVERGROWN_SNOWED.values().forEach(ro -> setCutoutMipped(ro.get()));
            WosBlocks.METAMORPHIC_OVERGROWN.values().forEach(ro -> setCutoutMipped(ro.get()));
            WosBlocks.METAMORPHIC_OVERGROWN_SNOWED.values().forEach(ro -> setCutoutMipped(ro.get()));
            WosBlocks.SEDIMENTARY_OVERGROWN.values().forEach(ro -> setCutoutMipped(ro.get()));
            WosBlocks.SEDIMENTARY_OVERGROWN_SNOWED.values().forEach(ro -> setCutoutMipped(ro.get()));
            WosBlocks.VANILLA_HOST_OVERGROWN.values().forEach(ro -> setCutoutMipped(ro.get()));
            WosBlocks.VANILLA_HOST_SNOWED.values().forEach(ro -> setCutoutMipped(ro.get()));
            WosBlocks.IGNEOUS_SPELEOTHEM.values().forEach(ro -> setCutoutMipped(ro.get()));
            WosBlocks.METAMORPHIC_SPELEOTHEM.values().forEach(ro -> setCutoutMipped(ro.get()));
            WosBlocks.SEDIMENTARY_SPELEOTHEM.values().forEach(ro -> setCutoutMipped(ro.get()));
            WosBlocks.VANILLA_SPELEOTHEM.values().forEach(ro -> setCutoutMipped(ro.get()));
            WosBlocks.QUARK_SPELEOTHEM.values().forEach(ro -> {
                if (ro != null) setCutoutMipped(ro.get());
            });
            java.util.Set<com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant> translucent = java.util.EnumSet.of(
                    com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant.TINTED_GLASS,
                    com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant.WHITE_STAINED_GLASS,
                    com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant.ORANGE_STAINED_GLASS,
                    com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant.MAGENTA_STAINED_GLASS,
                    com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant.LIGHT_BLUE_STAINED_GLASS,
                    com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant.YELLOW_STAINED_GLASS,
                    com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant.LIME_STAINED_GLASS,
                    com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant.PINK_STAINED_GLASS,
                    com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant.GRAY_STAINED_GLASS,
                    com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant.LIGHT_GRAY_STAINED_GLASS,
                    com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant.CYAN_STAINED_GLASS,
                    com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant.PURPLE_STAINED_GLASS,
                    com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant.BLUE_STAINED_GLASS,
                    com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant.BROWN_STAINED_GLASS,
                    com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant.GREEN_STAINED_GLASS,
                    com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant.RED_STAINED_GLASS,
                    com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant.BLACK_STAINED_GLASS,
                    com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant.SLIME,
                    com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant.HONEY
            );
            WosBlocks.DECORATIVE_SPELEOTHEM.forEach((variant, ro) -> {
                if (ro != null && ro.isPresent()) {
                    if (translucent.contains(variant)) {
                        ItemBlockRenderTypes.setRenderLayer(ro.get(), RenderType.translucent());
                    } else {
                        setCutoutMipped(ro.get());
                    }
                }
            });
            if (WosBlocks.MIMIC_SPELEOTHEM.isPresent()) setCutoutMipped(WosBlocks.MIMIC_SPELEOTHEM.get());
        });
    }

    @SuppressWarnings("removal")
    private static void setCutoutMipped(Block block) {
        ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutoutMipped());
    }

    @SubscribeEvent
    public static void onModifyBakingResult(net.minecraftforge.client.event.ModelEvent.ModifyBakingResult event) {
        java.util.Map<ResourceLocation, net.minecraft.client.resources.model.BakedModel> models = event.getModels();
        java.util.List<ResourceLocation> keys = new java.util.ArrayList<>(models.keySet());
        for (ResourceLocation rl : keys) {
            if (rl.getNamespace().equals(ModInfo.MODID) && rl.getPath().equals("mimic_speleothem")) {
                net.minecraft.client.resources.model.BakedModel original = models.get(rl);
                if (original != null) {
                    models.put(rl, new WosMimicSpeleothemBakedModel(original));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterBlockColors(RegisterColorHandlersEvent.Block event) {
        BlockColor grass = (state, world, pos, tintIndex) -> {
            if (tintIndex != 0) return -1;
            if (world != null && pos != null) return BiomeColors.getAverageGrassColor(world, pos);
            return GrassColor.get(0.5D, 1.0D);
        };
        registerAllBlock(event, grass, WosBlocks.IGNEOUS_OVERGROWN);
        registerAllBlock(event, grass, WosBlocks.METAMORPHIC_OVERGROWN);
        registerAllBlock(event, grass, WosBlocks.SEDIMENTARY_OVERGROWN);
        registerAllBlock(event, grass, WosBlocks.VANILLA_HOST_OVERGROWN);

        Map<String, Integer> tints = getSandTints();
        for (Map.Entry<String, Integer> e : tints.entrySet()) {
            Block block = ForgeRegistries.BLOCKS.getValue(ModInfo.rl(e.getKey()));
            if (block != null) {
                final int color = e.getValue();
                event.register((s, w, p, t) -> color, block);
            }
        }

        RegistrySupplier<Block> myaliteRo = WosBlocks.QUARK_SPELEOTHEM.get(QuarkSpeleothemVariant.QUARK_MYALITE);
        if (myaliteRo != null) {
            final int myalitePurple = 0xAE51DA;
            event.register((s, w, p, t) -> myalitePurple, myaliteRo.get());
        }

        BlockColor foliage = (state, world, pos, tintIndex) -> {
            if (tintIndex != 0) return -1;
            if (world != null && pos != null) return BiomeColors.getAverageFoliageColor(world, pos);
            return FoliageColor.getDefaultColor();
        };
        for (DecorativeSpeleothemVariant v : new DecorativeSpeleothemVariant[]{
                DecorativeSpeleothemVariant.OAK_LEAVES, DecorativeSpeleothemVariant.JUNGLE_LEAVES,
                DecorativeSpeleothemVariant.ACACIA_LEAVES, DecorativeSpeleothemVariant.DARK_OAK_LEAVES,
                DecorativeSpeleothemVariant.MANGROVE_LEAVES}) {
            RegistrySupplier<Block> ro = WosBlocks.DECORATIVE_SPELEOTHEM.get(v);
            if (ro != null && ro.isPresent()) event.register(foliage, ro.get());
        }
        RegistrySupplier<Block> spruceLeavesRo = WosBlocks.DECORATIVE_SPELEOTHEM.get(DecorativeSpeleothemVariant.SPRUCE_LEAVES);
        if (spruceLeavesRo != null && spruceLeavesRo.isPresent()) {
            final int evergreen = FoliageColor.getEvergreenColor();
            event.register((s, w, p, t) -> t == 0 ? evergreen : -1, spruceLeavesRo.get());
        }
        RegistrySupplier<Block> birchLeavesRo = WosBlocks.DECORATIVE_SPELEOTHEM.get(DecorativeSpeleothemVariant.BIRCH_LEAVES);
        if (birchLeavesRo != null && birchLeavesRo.isPresent()) {
            final int birch = FoliageColor.getBirchColor();
            event.register((s, w, p, t) -> t == 0 ? birch : -1, birchLeavesRo.get());
        }
    }

    @SubscribeEvent
    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        ItemColor grass = (stack, tintIndex) -> {
            if (tintIndex != 0) return -1;
            return GrassColor.get(0.5D, 1.0D);
        };
        registerAllItem(event, grass, WosBlocks.IGNEOUS_OVERGROWN);
        registerAllItem(event, grass, WosBlocks.METAMORPHIC_OVERGROWN);
        registerAllItem(event, grass, WosBlocks.SEDIMENTARY_OVERGROWN);
        registerAllItem(event, grass, WosBlocks.VANILLA_HOST_OVERGROWN);

        Map<String, Integer> tints = getSandTints();
        for (Map.Entry<String, Integer> e : tints.entrySet()) {
            Block block = ForgeRegistries.BLOCKS.getValue(ModInfo.rl(e.getKey()));
            if (block != null) {
                final int color = e.getValue();
                event.register((stack, t) -> color, block.asItem());
            }
        }

        RegistrySupplier<Block> myaliteRo = WosBlocks.QUARK_SPELEOTHEM.get(QuarkSpeleothemVariant.QUARK_MYALITE);
        if (myaliteRo != null) {
            final int myalitePurple = 0xAE51DA;
            event.register((stack, t) -> myalitePurple, myaliteRo.get().asItem());
        }

        final int defaultFoliage = FoliageColor.getDefaultColor();
        ItemColor foliageItem = (stack, tintIndex) -> tintIndex == 0 ? defaultFoliage : -1;
        for (DecorativeSpeleothemVariant v : new DecorativeSpeleothemVariant[]{
                DecorativeSpeleothemVariant.OAK_LEAVES, DecorativeSpeleothemVariant.JUNGLE_LEAVES,
                DecorativeSpeleothemVariant.ACACIA_LEAVES, DecorativeSpeleothemVariant.DARK_OAK_LEAVES,
                DecorativeSpeleothemVariant.MANGROVE_LEAVES}) {
            RegistrySupplier<Block> ro = WosBlocks.DECORATIVE_SPELEOTHEM.get(v);
            if (ro != null && ro.isPresent()) event.register(foliageItem, ro.get().asItem());
        }
        RegistrySupplier<Block> spruceLeavesRo = WosBlocks.DECORATIVE_SPELEOTHEM.get(DecorativeSpeleothemVariant.SPRUCE_LEAVES);
        if (spruceLeavesRo != null && spruceLeavesRo.isPresent()) {
            final int evergreen = FoliageColor.getEvergreenColor();
            event.register((stack, t) -> t == 0 ? evergreen : -1, spruceLeavesRo.get().asItem());
        }
        RegistrySupplier<Block> birchLeavesRo = WosBlocks.DECORATIVE_SPELEOTHEM.get(DecorativeSpeleothemVariant.BIRCH_LEAVES);
        if (birchLeavesRo != null && birchLeavesRo.isPresent()) {
            final int birch = FoliageColor.getBirchColor();
            event.register((stack, t) -> t == 0 ? birch : -1, birchLeavesRo.get().asItem());
        }
    }

    private static <K> void registerAllBlock(RegisterColorHandlersEvent.Block event, BlockColor color,
                                             java.util.Map<K, RegistrySupplier<Block>> blocks) {
        for (RegistrySupplier<Block> ro : blocks.values()) {
            event.register(color, ro.get());
        }
    }

    private static <K> void registerAllItem(RegisterColorHandlersEvent.Item event, ItemColor color,
                                            java.util.Map<K, RegistrySupplier<Block>> blocks) {
        for (RegistrySupplier<Block> ro : blocks.values()) {
            BlockItem item = (BlockItem) ro.get().asItem();
            event.register(color, item);
        }
    }

    private static Map<String, Integer> getSandTints() {
        if (sandTints != null) return sandTints;
        sandTints = new HashMap<>();
        try (InputStream is = WosClient.class.getResourceAsStream("/data/worldofstone/sand_tints.json")) {
            if (is == null) return sandTints;
            Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
            for (Map.Entry<String, JsonElement> e : obj.entrySet()) {
                JsonObject rgb = e.getValue().getAsJsonObject();
                int r = rgb.get("r").getAsInt();
                int g = rgb.get("g").getAsInt();
                int b = rgb.get("b").getAsInt();
                sandTints.put(e.getKey(), (r << 16) | (g << 8) | b);
            }
        } catch (Exception ignored) {
        }
        return sandTints;
    }
}
