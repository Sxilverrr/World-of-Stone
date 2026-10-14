package com.sxilverr.worldofstone.api.enums;

import net.minecraft.util.valueproviders.UniformInt;

public enum OreVariant {
    COAL("coal_ore", 0, 2, 6, 4, 12),
    IRON("iron_ore", 0, 0, 8, 6, 6),
    GOLD("gold_ore", 0, 0, 2, 1, 5),
    DIAMOND("diamond_ore", 3, 7, 1, 1, 4),
    EMERALD("emerald_ore", 3, 7, 1, 1, 3),
    REDSTONE("redstone_ore", 1, 5, 2, 1, 5),
    LAPIS("lapis_ore", 2, 5, 1, 1, 4),
    COPPER("copper_ore", 0, 2, 4, 3, 12);

    public static final OreVariant[] VALUES = values();

    public final String suffix;
    public final int xpMin;
    public final int xpMax;
    public final int netherAttempts;
    public final int endAttempts;
    public final int veinSize;

    OreVariant(String suffix, int xpMin, int xpMax, int netherAttempts, int endAttempts, int veinSize) {
        this.suffix = suffix;
        this.xpMin = xpMin;
        this.xpMax = xpMax;
        this.netherAttempts = netherAttempts;
        this.endAttempts = endAttempts;
        this.veinSize = veinSize;
    }

    public UniformInt getXpProvider() {
        return UniformInt.of(xpMin, xpMax);
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
