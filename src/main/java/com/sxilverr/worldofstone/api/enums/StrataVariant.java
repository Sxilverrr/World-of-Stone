package com.sxilverr.worldofstone.api.enums;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public enum StrataVariant {
    ADAMELLITE(Family.IGNEOUS, 1.7F, 1.42F),
    PYROXENITE(Family.IGNEOUS, 1.6F, 1.39F),
    RHYOLITE(Family.IGNEOUS, 1.3F, 1.26F),
    TONALITE(Family.IGNEOUS, 1.4F, 1.31F),
    GABBRO(Family.IGNEOUS, 1.0F, 1.0F),
    LATITE(Family.IGNEOUS, 1.4F, 1.31F),
    KOMATIITE(Family.IGNEOUS, 1.5F, 1.35F),
    DACITE(Family.IGNEOUS, 1.2F, 1.2F),
    GNEISS(Family.METAMORPHIC, 1.1F, 1.11F),
    ECLOGITE(Family.METAMORPHIC, 1.0F, 1.0F),
    MARBLE(Family.METAMORPHIC, 1.1F, 1.11F),
    QUARTZITE(Family.METAMORPHIC, 1.3F, 1.26F),
    BLUESCHIST(Family.METAMORPHIC, 0.7F, 0.54F),
    GREENSCHIST(Family.METAMORPHIC, 0.7F, 0.54F),
    SOAPSTONE(Family.METAMORPHIC, 0.4F, 0.2F),
    MIGMATITE(Family.METAMORPHIC, 0.9F, 0.86F),
    LIMESTONE(Family.SEDIMENTARY, 0.5F, 0.29F),
    CHALK(Family.SEDIMENTARY, 0.5F, 0.29F),
    SHALE(Family.SEDIMENTARY, 0.5F, 0.29F),
    SILTSTONE(Family.SEDIMENTARY, 0.6F, 0.4F),
    LIGNITE(Family.SEDIMENTARY, 0.5F, 0.29F),
    DOLOMITE(Family.SEDIMENTARY, 0.5F, 0.29F),
    GREYWACKE(Family.SEDIMENTARY, 1.0F, 1.0F),
    CHERT(Family.SEDIMENTARY, 0.9F, 0.86F);

    public static final List<StrataVariant> ALL = List.of(values());
    private static final Map<Family, List<StrataVariant>> BY_FAMILY = new EnumMap<>(Family.class);

    static {
        for (Family f : Family.values()) {
            BY_FAMILY.put(f, ALL.stream().filter(v -> v.family == f).toList());
        }
    }

    public final Family family;
    public final float hardness;
    public final float resistance;
    private final String id = name().toLowerCase();

    StrataVariant(Family family, float hardness, float resistance) {
        this.family = family;
        this.hardness = hardness * family.hardnessScale;
        this.resistance = resistance * family.resistanceScale;
    }

    @Override
    public String toString() {
        return id;
    }

    public enum Family {
        IGNEOUS(1.0F, 1.0F),
        METAMORPHIC(1.0F, 1.0F),
        SEDIMENTARY(1.5F, 1.66F);

        private final float hardnessScale;
        private final float resistanceScale;

        Family(float hardnessScale, float resistanceScale) {
            this.hardnessScale = hardnessScale;
            this.resistanceScale = resistanceScale;
        }

        public List<StrataVariant> variants() {
            return BY_FAMILY.get(this);
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
