package com.sxilverr.worldofstone.neoforge.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import dev.architectury.registry.registries.RegistrySupplier;
import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.OreVariant;
import com.sxilverr.worldofstone.api.enums.VanillaOreHost;
import com.sxilverr.worldofstone.config.WosConfig;
import com.sxilverr.worldofstone.config.WosConfigSync;
import com.sxilverr.worldofstone.registry.WosBlocks;
import com.sxilverr.worldofstone.registry.WosItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JeiPlugin
public class WorldOfStoneJEIPlugin implements IModPlugin {

    private static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(ModInfo.MODID, "jei");

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    private static IJeiRuntime runtime;
    private static List<ItemStack> hidden = List.of();
    private static boolean listening;

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
        hidden = List.of();
        if (!listening) {
            listening = true;
            WosConfigSync.addListener(WorldOfStoneJEIPlugin::refreshHidden);
        }
        refreshHidden();
    }

    @Override
    public void onRuntimeUnavailable() {
        runtime = null;
        hidden = List.of();
    }

    private static void refreshHidden() {
        if (runtime == null) {
            return;
        }

        List<ItemStack> hide = new ArrayList<>();

        if (!WosConfig.stoneReplacementEnabled) {
            addTag(hide, "stone_variants");
            add(hide, WosItems.LIGNITE_COAL);
        }

        if (!WosConfig.enableInfestedBlocks) {
            addBlockMaps(hide,
                    WosBlocks.IGNEOUS_INFESTED, WosBlocks.IGNEOUS_INFESTED_COBBLE, WosBlocks.IGNEOUS_INFESTED_BRICK,
                    WosBlocks.IGNEOUS_INFESTED_MOSSY_BRICK, WosBlocks.IGNEOUS_INFESTED_CRACKED_BRICK, WosBlocks.IGNEOUS_INFESTED_CHISELED_BRICK,
                    WosBlocks.METAMORPHIC_INFESTED, WosBlocks.METAMORPHIC_INFESTED_COBBLE, WosBlocks.METAMORPHIC_INFESTED_BRICK,
                    WosBlocks.METAMORPHIC_INFESTED_MOSSY_BRICK, WosBlocks.METAMORPHIC_INFESTED_CRACKED_BRICK, WosBlocks.METAMORPHIC_INFESTED_CHISELED_BRICK,
                    WosBlocks.SEDIMENTARY_INFESTED, WosBlocks.SEDIMENTARY_INFESTED_COBBLE, WosBlocks.SEDIMENTARY_INFESTED_BRICK,
                    WosBlocks.SEDIMENTARY_INFESTED_MOSSY_BRICK, WosBlocks.SEDIMENTARY_INFESTED_CRACKED_BRICK, WosBlocks.SEDIMENTARY_INFESTED_CHISELED_BRICK);
        }

        if (!WosConfig.isFossilsEnabled()) {
            for (RegistrySupplier<Item> ro : WosItems.FOSSILS.values()) {
                add(hide, ro);
            }
        }

        for (DecorativeSpeleothemVariant v : DecorativeSpeleothemVariant.VALUES) {
            if (!WosConfig.isCreativeSpeleothemEnabled(v)) {
                addDecorative(hide, v);
            }
        }

        if (!WosConfig.allowVanillaOresInNether) {
            addHostOres(hide, VanillaOreHost.HostDimension.NETHER);
        }
        if (!WosConfig.allowVanillaOresInEnd) {
            addHostOres(hide, VanillaOreHost.HostDimension.END);
        }
        if (!WosConfig.allowObsidianOreVariants) {
            addHostOres(hide, VanillaOreHost.HostDimension.ANY);
        }

        if (!WosConfig.replaceVanillaOres) {
            addBlockMaps(hide, WosBlocks.ORES);
        }

        if (!WosConfig.allowOvergrownStrata) {
            addBlockMaps(hide, WosBlocks.IGNEOUS_OVERGROWN, WosBlocks.METAMORPHIC_OVERGROWN, WosBlocks.SEDIMENTARY_OVERGROWN);
        }
        if (!WosConfig.allowSnowedStrata) {
            addBlockMaps(hide, WosBlocks.IGNEOUS_OVERGROWN_SNOWED, WosBlocks.METAMORPHIC_OVERGROWN_SNOWED, WosBlocks.SEDIMENTARY_OVERGROWN_SNOWED);
        }
        if (!WosConfig.allowOvergrownVanillaVariants) {
            addBlockMaps(hide, WosBlocks.VANILLA_HOST_OVERGROWN);
        }
        if (!WosConfig.allowSnowedVanillaVariants) {
            addBlockMaps(hide, WosBlocks.VANILLA_HOST_SNOWED);
        }

        if (!WosConfig.speleothemsEnabled) {
            addTag(hide, "speleothem");
            add(hide, WosBlocks.MIMIC_SPELEOTHEM);
        }

        IIngredientManager manager = runtime.getIngredientManager();
        if (!hidden.isEmpty()) {
            manager.addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, hidden);
        }
        if (!hide.isEmpty()) {
            manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, hide);
        }
        hidden = hide;
    }

    private static void addTag(List<ItemStack> hide, String tagPath) {
        TagKey<Item> tag = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ModInfo.MODID, tagPath));
        BuiltInRegistries.ITEM.getTag(tag).ifPresent(named -> {
            for (Holder<Item> h : named) {
                hide.add(new ItemStack(h.value()));
            }
        });
    }

    private static void add(List<ItemStack> hide, RegistrySupplier<? extends ItemLike> ro) {
        if (ro != null && ro.isPresent()) {
            hide.add(new ItemStack(ro.get()));
        }
    }

    private static void addDecorative(List<ItemStack> hide, DecorativeSpeleothemVariant v) {
        RegistrySupplier<Block> ro = WosBlocks.DECORATIVE_SPELEOTHEM.get(v);
        if (ro != null && ro.isPresent()) {
            hide.add(new ItemStack(ro.get()));
        }
    }

    @SafeVarargs
    private static void addBlockMaps(List<ItemStack> hide, Map<?, RegistrySupplier<Block>>... maps) {
        for (Map<?, RegistrySupplier<Block>> map : maps) {
            for (RegistrySupplier<Block> ro : map.values()) {
                if (ro != null && ro.isPresent()) {
                    hide.add(new ItemStack(ro.get()));
                }
            }
        }
    }

    private static void addHostOres(List<ItemStack> hide, VanillaOreHost.HostDimension dimension) {
        for (VanillaOreHost host : VanillaOreHost.VALUES) {
            if (host.getDimension() != dimension) {
                continue;
            }
            for (OreVariant ore : OreVariant.VALUES) {
                RegistrySupplier<Block> ro = WosBlocks.ORES.get(host.getRegistryName() + "_" + ore.suffix);
                if (ro != null && ro.isPresent()) {
                    hide.add(new ItemStack(ro.get()));
                }
            }
        }
    }
}
