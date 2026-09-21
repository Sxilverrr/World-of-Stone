package com.sxilverr.worldofstone.forge;

import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.config.WosConfigBuilder;
import com.sxilverr.worldofstone.config.WosConfigSchema;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ModInfo.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class WosConfigSpec {

    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        WosConfigSchema.define(new Adapter(builder));
        COMMON_SPEC = builder.build();
    }

    @SubscribeEvent
    static void onLoad(ModConfigEvent.Loading event) {
        WosConfigSchema.bake();
    }

    @SubscribeEvent
    static void onReload(ModConfigEvent.Reloading event) {
        WosConfigSchema.bake();
    }

    private static final class Adapter implements WosConfigBuilder {

        private final ForgeConfigSpec.Builder builder;

        private Adapter(ForgeConfigSpec.Builder builder) {
            this.builder = builder;
        }

        @Override
        public WosConfigBuilder comment(String comment) {
            builder.comment(comment);
            return this;
        }

        @Override
        public WosConfigBuilder push(String path) {
            builder.push(path);
            return this;
        }

        @Override
        public WosConfigBuilder pop() {
            builder.pop();
            return this;
        }

        @Override
        public Supplier<Boolean> define(String path, boolean defaultValue) {
            return builder.define(path, defaultValue)::get;
        }

        @Override
        public Supplier<Integer> defineInRange(String path, int defaultValue, int min, int max) {
            return builder.defineInRange(path, defaultValue, min, max)::get;
        }

        @Override
        public Supplier<Double> defineInRange(String path, double defaultValue, double min, double max) {
            return builder.defineInRange(path, defaultValue, min, max)::get;
        }

        @Override
        public <V extends Enum<V>> Supplier<V> defineEnum(String path, V defaultValue) {
            return builder.defineEnum(path, defaultValue)::get;
        }

        @Override
        public Supplier<List<? extends String>> defineListAllowEmpty(String path, List<? extends String> defaultValue, Predicate<Object> elementValidator) {
            ForgeConfigSpec.ConfigValue<List<? extends String>> value = builder.defineListAllowEmpty(path, defaultValue, elementValidator);
            return value::get;
        }
    }

    private WosConfigSpec() {
    }
}
