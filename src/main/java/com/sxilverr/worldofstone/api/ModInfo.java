package com.sxilverr.worldofstone.api;

import net.minecraft.resources.ResourceLocation;

public final class ModInfo {

    public static final String MODID = "worldofstone";

    public static ResourceLocation rl(String path) {
        //? if >=1.21.1 {
        /*return ResourceLocation.fromNamespaceAndPath(MODID, path);
        *///?} else {
        return new ResourceLocation(MODID, path);
        //?}
    }

    public static ResourceLocation id(String fullId) {
        //? if >=1.21.1 {
        /*return ResourceLocation.parse(fullId);
        *///?} else {
        return new ResourceLocation(fullId);
        //?}
    }

    private ModInfo() {
    }
}
