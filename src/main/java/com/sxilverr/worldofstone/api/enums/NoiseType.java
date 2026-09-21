package com.sxilverr.worldofstone.api.enums;

public enum NoiseType {
    VALUE,
    PERLIN,
    SIMPLEX,
    FRACTIONAL_BROWNIAN_MOTION,
    RIDGED,
    BILLOW,
    CELLULAR,
    DOMAIN_WARPED,
    RIDGED_MULTIFRACTAL;

    public static final NoiseType[] VALUES = values();
}
