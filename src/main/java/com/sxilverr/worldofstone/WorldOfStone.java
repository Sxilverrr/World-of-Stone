package com.sxilverr.worldofstone;

import com.sxilverr.worldofstone.common.WosEvents;
import com.sxilverr.worldofstone.config.WosConfigSync;
import com.sxilverr.worldofstone.registry.WosBlocks;
import com.sxilverr.worldofstone.registry.WosCreativeTabs;
import com.sxilverr.worldofstone.registry.WosFeatures;
import com.sxilverr.worldofstone.registry.WosItems;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.registry.fuel.FuelRegistry;

public final class WorldOfStone {

    public static void init() {
        WosBlocks.BLOCKS.register();
        WosItems.ITEMS.register();
        WosFeatures.FEATURES.register();
        WosCreativeTabs.TABS.register();
        WosItems.LIGNITE_COAL.listen(item -> FuelRegistry.register(1600, item));
        BlockEvent.BREAK.register(WosEvents::onBlockBreak);
        WosConfigSync.init();
    }

    private WorldOfStone() {
    }
}
