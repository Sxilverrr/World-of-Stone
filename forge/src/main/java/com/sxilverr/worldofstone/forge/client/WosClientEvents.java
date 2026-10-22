package com.sxilverr.worldofstone.forge.client;

import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.api.enums.TextureStyle;
import com.sxilverr.worldofstone.client.WosClient;
import com.sxilverr.worldofstone.config.WosConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.nio.file.Path;

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
    static void onAddPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.CLIENT_RESOURCES) return;
        Path path = ModList.get().getModFileById(ModInfo.MODID).getFile().findResource("resourcepacks", "legacy");
        Pack pack = Pack.readMetaAndCreate(ModInfo.MODID + "_legacy", Component.literal("World of Stone Legacy Textures"), true,
                id -> new PathPackResources(id, path, true), PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.BUILT_IN);
        event.addRepositorySource(packs -> {
            if (pack != null && WosConfig.textureStyle == TextureStyle.LEGACY) packs.accept(pack);
        });
    }

    @SubscribeEvent
    static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        event.getModels().replaceAll((id, model) -> id.getNamespace().equals(ModInfo.MODID) && id.getPath().equals("mimic_speleothem")
                ? new WosMimicSpeleothemBakedModel(model) : model);
    }

    private WosClientEvents() {
    }
}
