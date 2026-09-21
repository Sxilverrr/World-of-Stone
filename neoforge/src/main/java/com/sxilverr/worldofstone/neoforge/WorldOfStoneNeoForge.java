package com.sxilverr.worldofstone.neoforge;

import com.sxilverr.worldofstone.config.WosConfigSync;
import com.sxilverr.worldofstone.neoforge.compat.jer.WorldOfStoneJERPlugin;
import com.sxilverr.worldofstone.neoforge.registry.WosBlockEntities;
import com.sxilverr.worldofstone.neoforge.registry.WosCreativeTabs;
import com.sxilverr.worldofstone.neoforge.registry.WosLoaderBlocks;
import com.sxilverr.worldofstone.registry.WosBlocks;
import com.sxilverr.worldofstone.registry.WosFeatures;
import com.sxilverr.worldofstone.registry.WosItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod("worldofstone")
public final class WorldOfStoneNeoForge {

    public WorldOfStoneNeoForge(IEventBus modBus, ModContainer container) {
        WosLoaderBlocks.register();

        WosBlocks.init();
        WosItems.init();
        WosFeatures.init();
        WosBlockEntities.init();
        WosCreativeTabs.init();

        WosBlocks.BLOCKS.register();
        WosItems.ITEMS.register();
        WosFeatures.FEATURES.register();
        WosBlockEntities.BES.register(modBus);
        WosCreativeTabs.TABS.register(modBus);

        container.registerConfig(ModConfig.Type.COMMON, WosConfigSpec.COMMON_SPEC);
        WosConfigSync.init();

        modBus.addListener(this::onCommonSetup);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        if (ModList.get().isLoaded("jeresources")) {
            event.enqueueWork(WorldOfStoneJERPlugin::register);
        }
    }
}
