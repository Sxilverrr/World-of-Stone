package com.sxilverr.worldofstone.forge;

import com.sxilverr.worldofstone.WorldOfStone;
import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.common.WosEvents;
import com.sxilverr.worldofstone.compat.jer.WorldOfStoneJERPlugin;
import com.sxilverr.worldofstone.forge.block.WosLoaderBlocks;
import dev.architectury.platform.forge.EventBuses;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModInfo.MODID)
public final class WorldOfStoneForge {

    @SuppressWarnings("removal")
    public WorldOfStoneForge(FMLJavaModLoadingContext context) {
        IEventBus modBus = context.getModEventBus();
        EventBuses.registerModEventBus(ModInfo.MODID, modBus);
        WosLoaderBlocks.register();
        WorldOfStone.init();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, WosConfigSpec.COMMON_SPEC);
        modBus.addListener((FMLCommonSetupEvent event) -> {
            if (ModList.get().isLoaded("jeresources")) event.enqueueWork(WorldOfStoneJERPlugin::register);
        });
        MinecraftForge.EVENT_BUS.addListener((VillagerTradesEvent event) -> {
            if (event.getType() == VillagerProfession.MASON) WosEvents.addMasonTrades(event.getTrades());
        });
    }
}
