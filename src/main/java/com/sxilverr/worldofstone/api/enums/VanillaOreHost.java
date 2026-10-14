package com.sxilverr.worldofstone.api.enums;

public enum VanillaOreHost {
    GRANITE(HostDimension.OVERWORLD),
    DIORITE(HostDimension.OVERWORLD),
    ANDESITE(HostDimension.OVERWORLD),
    TUFF(HostDimension.OVERWORLD),
    NETHERRACK(HostDimension.NETHER),
    BLACKSTONE(HostDimension.NETHER),
    BASALT(HostDimension.NETHER),
    END_STONE(HostDimension.END),
    OBSIDIAN(HostDimension.ANY);

    public enum HostDimension { OVERWORLD, NETHER, END, ANY }

    public static final VanillaOreHost[] VALUES = values();

    public final HostDimension dimension;

    VanillaOreHost(HostDimension dimension) {
        this.dimension = dimension;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
