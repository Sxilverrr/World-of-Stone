package com.sxilverr.worldofstone.neoforge;

import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.config.WosConfigBuilder;
import com.sxilverr.worldofstone.config.WosConfigSchema;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

@EventBusSubscriber(modid = ModInfo.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class WosConfigSpec {

    public static final ModConfigSpec COMMON_SPEC;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
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

        private final ModConfigSpec.Builder builder;

        private Adapter(ModConfigSpec.Builder builder) {
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
            ModConfigSpec.ConfigValue<List<? extends String>> value = builder.defineListAllowEmpty(path, () -> defaultValue, () -> "", elementValidator);
            return value::get;
        }
    }

    private WosConfigSpec() {
    }
}
