package com.sxilverr.worldofstone.neoforge.client;

import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.client.WosClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

@EventBusSubscriber(modid = ModInfo.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
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
        event.getModels().replaceAll((id, model) -> id.id().getNamespace().equals(ModInfo.MODID) && id.id().getPath().equals("mimic_speleothem")
                ? new WosMimicSpeleothemBakedModel(model) : model);
    }

    private WosClientEvents() {
    }
}
