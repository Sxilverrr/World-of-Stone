package com.sxilverr.worldofstone.neoforge;

import com.sxilverr.worldofstone.WorldOfStone;
import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.common.WosEvents;
import com.sxilverr.worldofstone.compat.jer.WorldOfStoneJERPlugin;
import com.sxilverr.worldofstone.neoforge.block.WosLoaderBlocks;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

@Mod(ModInfo.MODID)
public final class WorldOfStoneNeoForge {

    public WorldOfStoneNeoForge(IEventBus modBus, ModContainer container) {
        WosLoaderBlocks.register();
        WorldOfStone.init();
        container.registerConfig(ModConfig.Type.COMMON, WosConfigSpec.COMMON_SPEC);
        modBus.addListener((FMLCommonSetupEvent event) -> {
            if (ModList.get().isLoaded("jeresources")) event.enqueueWork(WorldOfStoneJERPlugin::register);
        });
        NeoForge.EVENT_BUS.addListener((VillagerTradesEvent event) -> {
            if (event.getType() == VillagerProfession.MASON) WosEvents.addMasonTrades(event.getTrades());
        });
    }
}
