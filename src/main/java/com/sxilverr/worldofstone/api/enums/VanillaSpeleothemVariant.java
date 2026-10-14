package com.sxilverr.worldofstone.api.enums;

public enum VanillaSpeleothemVariant {
    STONE("stone"),
    GRANITE("granite"),
    DIORITE("diorite"),
    ANDESITE("andesite"),
    DEEPSLATE("deepslate"),
    TUFF("tuff"),
    CALCITE("calcite"),
    DRIPSTONE("dripstone_block"),
    NETHERRACK("netherrack"),
    BASALT("basalt"),
    SMOOTH_BASALT("smooth_basalt"),
    BLACKSTONE("blackstone"),
    END_STONE("end_stone"),
    SANDSTONE("sandstone"),
    RED_SANDSTONE("red_sandstone");

    public final String host;

    VanillaSpeleothemVariant(String host) {
        this.host = "minecraft:" + host;
    }

    public String getRegistryName() {
        return this + "_speleothem";
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
