package com.sxilverr.worldofstone.registry;

import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.api.enums.DecorativeSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.ModdedSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.OreVariant;
import com.sxilverr.worldofstone.api.enums.StrataVariant;
import com.sxilverr.worldofstone.api.enums.TerracottaSpeleothemVariant;
import com.sxilverr.worldofstone.api.enums.VanillaOreHost;
import com.sxilverr.worldofstone.api.enums.VanillaSpeleothemVariant;
import com.sxilverr.worldofstone.config.WosConfig;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;

public final class WosCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(ModInfo.MODID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> WOS_TAB = TABS.register("wos_tab", () -> CreativeTabRegistry.create(builder -> builder
            .title(Component.translatable("itemGroup.worldofstone"))
            .icon(() -> new ItemStack(StrataBlock.STONE.get(StrataVariant.ADAMELLITE)))
            .displayItems((parameters, output) -> {
                if (WosConfig.stoneReplacementEnabled) {
                    for (StrataVariant v : StrataVariant.ALL) {
                        for (StrataBlock kind : StrataBlock.values()) {
                            if (shown(kind, v)) output.accept(kind.get(v));
                        }
                        if (WosConfig.replaceVanillaOres) {
                            for (OreVariant ore : OreVariant.VALUES) output.accept(WosBlocks.ore(v.toString(), ore));
                        }
                    }
                }
                if (WosConfig.replaceVanillaOres) {
                    for (VanillaOreHost host : VanillaOreHost.VALUES) {
                        if (!shown(host)) continue;
                        for (OreVariant ore : OreVariant.VALUES) output.accept(WosBlocks.ore(host.toString(), ore));
                    }
                }
                for (String host : WosBlocks.VANILLA_HOSTS) {
                    if (WosConfig.allowOvergrownVanillaVariants) output.accept(WosBlocks.VANILLA_HOST_OVERGROWN.get(host).get());
                    if (WosConfig.allowSnowedVanillaVariants) output.accept(WosBlocks.VANILLA_HOST_SNOWED.get(host).get());
                }
                for (Map.Entry<VanillaSpeleothemVariant, RegistrySupplier<Block>> e : WosBlocks.VANILLA_SPELEOTHEM.entrySet()) {
                    if (WosConfig.isSpeleothemEnabled(e.getKey())) output.accept(e.getValue().get());
                }
                for (Map.Entry<TerracottaSpeleothemVariant, RegistrySupplier<Block>> e : WosBlocks.TERRACOTTA_SPELEOTHEM.entrySet()) {
                    if (WosConfig.isSpeleothemEnabled(e.getKey())) output.accept(e.getValue().get());
                }
                for (Map.Entry<ModdedSpeleothemVariant, RegistrySupplier<Block>> e : WosBlocks.MODDED_SPELEOTHEM.entrySet()) {
                    if (WosConfig.isSpeleothemEnabled(e.getKey())) output.accept(e.getValue().get());
                }
                for (Map.Entry<DecorativeSpeleothemVariant, RegistrySupplier<Block>> e : WosBlocks.DECORATIVE_SPELEOTHEM.entrySet()) {
                    if (WosConfig.isCreativeSpeleothemEnabled(e.getKey())) output.accept(e.getValue().get());
                }
                if (WosConfig.speleothemsEnabled && WosBlocks.MIMIC_SPELEOTHEM != null) {
                    for (String blockId : WosConfig.mimicSpeleothemBlocks) {
                        ResourceLocation rl = ResourceLocation.tryParse(blockId);
                        Block source = rl == null ? Blocks.AIR : BuiltInRegistries.BLOCK.getOptional(rl).orElse(Blocks.AIR);
                        if (source != Blocks.AIR) output.accept(mimicStack(rl, source));
                    }
                }
                if (WosConfig.stoneReplacementEnabled) output.accept(WosItems.LIGNITE_COAL.get());
                if (WosConfig.fossilsEnabled) WosItems.FOSSILS.values().forEach(fossil -> output.accept(fossil.get()));
            })));

    public static boolean shown(StrataBlock kind, StrataVariant v) {
        if (StrataBlock.INFESTED_BLOCKS.contains(kind)) return WosConfig.enableInfestedBlocks;
        return switch (kind) {
            case SPELEOTHEM -> WosConfig.isSpeleothemEnabled(v);
            case SANDSTONE_SPELEOTHEM -> WosConfig.isSandstoneSpeleothemEnabled(v);
            case OVERGROWN -> WosConfig.allowOvergrownStrata;
            case SNOWED -> WosConfig.allowSnowedStrata;
            default -> true;
        };
    }

    public static boolean shown(VanillaOreHost host) {
        return switch (host.dimension) {
            case NETHER -> WosConfig.allowVanillaOresInNether;
            case END -> WosConfig.allowVanillaOresInEnd;
            case ANY -> WosConfig.allowObsidianOreVariants;
            case OVERWORLD -> true;
        };
    }

    private static ItemStack mimicStack(ResourceLocation id, Block source) {
        ItemStack stack = new ItemStack(WosBlocks.MIMIC_SPELEOTHEM.get());
        CompoundTag tag = new CompoundTag();
        tag.putString("Source", id.toString());
        Component name = Component.literal(source.getName().getString() + " Speleothem");
        //? if >=1.21.1 {
        /*stack.set(net.minecraft.core.component.DataComponents.BLOCK_ENTITY_DATA, net.minecraft.world.item.component.CustomData.of(tag));
        stack.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME, name);
        *///?} else {
        stack.addTagElement("BlockEntityTag", tag);
        stack.setHoverName(name);
        //?}
        return stack;
    }

    private WosCreativeTabs() {
    }
}
