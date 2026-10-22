package com.sxilverr.worldofstone.neoforge.client;

import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.api.enums.TextureStyle;
import com.sxilverr.worldofstone.client.WosClient;
import com.sxilverr.worldofstone.config.WosConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;

import java.nio.file.Path;
import java.util.Optional;

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
    static void onAddPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.CLIENT_RESOURCES) return;
        Path path = ModList.get().getModFileById(ModInfo.MODID).getFile().findResource("resourcepacks", "legacy");
        PackLocationInfo info = new PackLocationInfo(ModInfo.MODID + "/legacy", Component.literal("World of Stone Legacy Textures"), PackSource.BUILT_IN, Optional.empty());
        Pack pack = Pack.readMetaAndCreate(info, BuiltInPackSource.fromName(location -> new PathPackResources(location, path)),
                PackType.CLIENT_RESOURCES, new PackSelectionConfig(true, Pack.Position.TOP, false));
        event.addRepositorySource(packs -> {
            if (pack != null && WosConfig.textureStyle == TextureStyle.LEGACY) packs.accept(pack);
        });
    }

    @SubscribeEvent
    static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        event.getModels().replaceAll((id, model) -> id.id().getNamespace().equals(ModInfo.MODID) && id.id().getPath().equals("mimic_speleothem")
                ? new WosMimicSpeleothemBakedModel(model) : model);
    }

    private WosClientEvents() {
    }
}
