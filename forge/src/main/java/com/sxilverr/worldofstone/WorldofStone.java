package com.sxilverr.worldofstone;

import com.mojang.logging.LogUtils;
import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.config.WosConfigSync;
import com.sxilverr.worldofstone.forge.WosConfigSpec;
import com.sxilverr.worldofstone.forge.registry.WosBlockEntities;
import com.sxilverr.worldofstone.registry.WosBlocks;
import com.sxilverr.worldofstone.forge.registry.WosCreativeTabs;
import com.sxilverr.worldofstone.registry.WosFeatures;
import com.sxilverr.worldofstone.registry.WosItems;
import com.sxilverr.worldofstone.forge.registry.WosLoaderBlocks;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(ModInfo.MODID)
public class WorldofStone {

    public static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("removal")
    public WorldofStone(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        EventBuses.registerModEventBus(ModInfo.MODID, modEventBus);

        WosLoaderBlocks.register();

        WosBlocks.init();
        WosItems.init();
        WosBlockEntities.init();
        WosCreativeTabs.init();
        WosFeatures.init();

        WosBlocks.BLOCKS.register();
        WosItems.ITEMS.register();
        WosBlockEntities.BES.register(modEventBus);
        WosCreativeTabs.TABS.register(modEventBus);
        WosFeatures.FEATURES.register();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WosConfigSpec.COMMON_SPEC);
        WosConfigSync.init();

        modEventBus.addListener(this::onCommonSetup);

        LOGGER.info("World of Stone loaded");
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        if (ModList.get().isLoaded("jeresources")) {
            event.enqueueWork(com.sxilverr.worldofstone.compat.jer.WorldOfStoneJERPlugin::register);
        }
    }
}
