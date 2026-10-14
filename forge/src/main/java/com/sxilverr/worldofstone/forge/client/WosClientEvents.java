package com.sxilverr.worldofstone.forge.client;

import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.client.WosClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = ModInfo.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class WosClientEvents {

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(WosClient::renderTypes);
    }

    @SubscribeEvent
    static void onBlockColors(RegisterColorHandlersEvent.Block event) {
        WosClient.blockColors(event::register);
    }

    @SubscribeEvent
    static void onItemColors(RegisterColorHandlersEvent.Item event) {
        WosClient.itemColors(event::register);
    }

    @SubscribeEvent
    static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        event.getModels().replaceAll((id, model) -> id.getNamespace().equals(ModInfo.MODID) && id.getPath().equals("mimic_speleothem")
                ? new WosMimicSpeleothemBakedModel(model) : model);
    }

    private WosClientEvents() {
    }
}
