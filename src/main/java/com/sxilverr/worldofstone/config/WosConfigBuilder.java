package com.sxilverr.worldofstone.config;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface WosConfigBuilder {

    WosConfigBuilder comment(String comment);

    WosConfigBuilder push(String path);

    WosConfigBuilder pop();

    Supplier<Boolean> define(String path, boolean defaultValue);

    Supplier<Integer> defineInRange(String path, int defaultValue, int min, int max);

    Supplier<Double> defineInRange(String path, double defaultValue, double min, double max);

    <V extends Enum<V>> Supplier<V> defineEnum(String path, V defaultValue);

    Supplier<List<? extends String>> defineListAllowEmpty(String path, List<? extends String> defaultValue, Predicate<Object> elementValidator);
}
