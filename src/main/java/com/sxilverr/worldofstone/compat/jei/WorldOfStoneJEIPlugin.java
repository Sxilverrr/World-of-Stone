package com.sxilverr.worldofstone.compat.jei;

import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.OreVariant;
import com.sxilverr.worldofstone.api.enums.VanillaOreHost;
import com.sxilverr.worldofstone.config.WosConfig;
import com.sxilverr.worldofstone.config.WosConfigSync;
import com.sxilverr.worldofstone.registry.StrataBlock;
import com.sxilverr.worldofstone.registry.WosBlocks;
import com.sxilverr.worldofstone.registry.WosCreativeTabs;
import com.sxilverr.worldofstone.registry.WosItems;
import dev.architectury.registry.registries.RegistrySupplier;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@JeiPlugin
public class WorldOfStoneJEIPlugin implements IModPlugin {

    private static final ResourceLocation UID = ModInfo.rl("jei");

    private static IJeiRuntime runtime;
    private static List<ItemStack> hidden = List.of();
    private static boolean listening;

    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

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
        if (runtime == null) return;
        List<ItemStack> hide = new ArrayList<>();

        if (!WosConfig.stoneReplacementEnabled) {
            addTag(hide, "stone_variants");
            add(hide, WosItems.LIGNITE_COAL.get());
        }
        if (!WosConfig.enableInfestedBlocks) {
            StrataBlock.INFESTED_BLOCKS.forEach(kind -> addAll(hide, kind.all()));
        }
        if (!WosConfig.fossilsEnabled) {
            WosItems.FOSSILS.values().forEach(fossil -> add(hide, fossil.get()));
        }
        for (DecorativeSpeleothemVariant v : DecorativeSpeleothemVariant.values()) {
            if (!WosConfig.isCreativeSpeleothemEnabled(v)) add(hide, WosBlocks.DECORATIVE_SPELEOTHEM.get(v).get());
        }
        for (VanillaOreHost host : VanillaOreHost.VALUES) {
            if (WosCreativeTabs.shown(host)) continue;
            for (OreVariant ore : OreVariant.VALUES) add(hide, WosBlocks.ore(host.toString(), ore));
        }
        if (!WosConfig.replaceVanillaOres) {
            addAll(hide, WosBlocks.ORES.values().stream().map(RegistrySupplier::get).toList());
        }
        if (!WosConfig.allowOvergrownStrata) addAll(hide, StrataBlock.OVERGROWN.all());
        if (!WosConfig.allowSnowedStrata) addAll(hide, StrataBlock.SNOWED.all());
        if (!WosConfig.enableStrataGlass) {
            addAll(hide, StrataBlock.GLASS.all());
            addAll(hide, StrataBlock.GLASS_PANE.all());
        }
        if (!WosConfig.allowOvergrownVanillaVariants) addAll(hide, WosBlocks.VANILLA_HOST_OVERGROWN.values().stream().map(RegistrySupplier::get).toList());
        if (!WosConfig.allowSnowedVanillaVariants) addAll(hide, WosBlocks.VANILLA_HOST_SNOWED.values().stream().map(RegistrySupplier::get).toList());
        if (!WosConfig.speleothemsEnabled) {
            addTag(hide, "speleothem");
            if (WosBlocks.MIMIC_SPELEOTHEM != null && WosBlocks.MIMIC_SPELEOTHEM.isPresent()) add(hide, WosBlocks.MIMIC_SPELEOTHEM.get());
        }

        IIngredientManager manager = runtime.getIngredientManager();
        if (!hidden.isEmpty()) manager.addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, hidden);
        if (!hide.isEmpty()) manager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, hide);
        hidden = hide;
    }

    private static void addTag(List<ItemStack> hide, String tagPath) {
        BuiltInRegistries.ITEM.getTag(TagKey.create(Registries.ITEM, ModInfo.rl(tagPath)))
                .ifPresent(tag -> tag.forEach(item -> hide.add(new ItemStack(item.value()))));
    }

    private static void add(List<ItemStack> hide, ItemLike item) {
        hide.add(new ItemStack(item));
    }

    private static void addAll(List<ItemStack> hide, Collection<Block> blocks) {
        blocks.forEach(block -> add(hide, block));
    }
}
