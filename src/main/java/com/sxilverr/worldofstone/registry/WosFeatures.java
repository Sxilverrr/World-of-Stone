package com.sxilverr.worldofstone.registry;

import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.world.BlobReplacerFeature;
import com.sxilverr.worldofstone.world.NoiseReplacerFeature;
import com.sxilverr.worldofstone.world.OtherDimensionOreFeature;
import com.sxilverr.worldofstone.world.SpeleothemFeature;
import com.sxilverr.worldofstone.world.StrataReplacerFeature;
import com.sxilverr.worldofstone.world.VanillaHostSurfaceFeature;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;

public final class WosFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ModInfo.MODID, Registries.FEATURE);

    static {
        FEATURES.register("strata_replacer", StrataReplacerFeature::new);
        FEATURES.register("speleothems", SpeleothemFeature::new);
        FEATURES.register("blob_replacer", BlobReplacerFeature::new);
        FEATURES.register("noise_replacer", NoiseReplacerFeature::new);
        FEATURES.register("other_dimension_ores", OtherDimensionOreFeature::new);
        FEATURES.register("vanilla_host_surface", VanillaHostSurfaceFeature::new);
    }

    private WosFeatures() {
    }
}
