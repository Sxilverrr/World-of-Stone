package com.sxilverr.worldofstone.api.enums;

import net.minecraft.util.valueproviders.UniformInt;

public enum OreVariant {
    COAL("coal_ore", 0, 2),
    IRON("iron_ore", 0, 0),
    GOLD("gold_ore", 0, 0),
    DIAMOND("diamond_ore", 3, 7),
    EMERALD("emerald_ore", 3, 7),
    REDSTONE("redstone_ore", 1, 5),
    LAPIS("lapis_ore", 2, 5),
    COPPER("copper_ore", 0, 2);

    public static final OreVariant[] VALUES = values();

    public final String suffix;
    public final int xpMin;
    public final int xpMax;

    OreVariant(String suffix, int xpMin, int xpMax) {
        this.suffix = suffix;
        this.xpMin = xpMin;
        this.xpMax = xpMax;
    }

    public UniformInt getXpProvider() {
        return UniformInt.of(xpMin, xpMax);
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
