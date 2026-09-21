package com.sxilverr.worldofstone.config;

import dev.architectury.event.events.client.ClientPlayerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.CreativeModeTabs;

final class WosConfigSyncClient {

    static void init() {
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(player -> WosConfigSync.clearRemote());
        WosConfigSync.addListener(WosConfigSyncClient::rebuildCreativeTabs);
    }

    private static void rebuildCreativeTabs() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.level == null) return;
        FeatureFlagSet features = minecraft.player.connection.enabledFeatures();
        RegistryAccess registries = minecraft.level.registryAccess();
        boolean operatorTab = minecraft.options.operatorItemsTab().get() && minecraft.player.canUseGameMasterBlocks();
        CreativeModeTabs.tryRebuildTabContents(features, !operatorTab, registries);
        CreativeModeTabs.tryRebuildTabContents(features, operatorTab, registries);
    }

    private WosConfigSyncClient() {
    }
}
