package com.sxilverr.worldofstone.api.enums;

public enum TerracottaSpeleothemVariant {
    TERRACOTTA("terracotta", true),
    WHITE("white_terracotta", true),
    ORANGE("orange_terracotta", true),
    YELLOW("yellow_terracotta", true),
    LIGHT_GRAY("light_gray_terracotta", true),
    RED("red_terracotta", true),
    BROWN("brown_terracotta", true),
    MAGENTA("magenta_terracotta", false),
    LIGHT_BLUE("light_blue_terracotta", false),
    LIME("lime_terracotta", false),
    PINK("pink_terracotta", false),
    GRAY("gray_terracotta", false),
    CYAN("cyan_terracotta", false),
    PURPLE("purple_terracotta", false),
    BLUE("blue_terracotta", false),
    GREEN("green_terracotta", false),
    BLACK("black_terracotta", false);

    private final String name;
    public final boolean naturalGeneration;

    TerracottaSpeleothemVariant(String name, boolean naturalGeneration) {
        this.name = name;
        this.naturalGeneration = naturalGeneration;
    }

    public String getRegistryName() {
        return name + "_speleothem";
    }

    public String getHostBlock() {
        return "minecraft:" + name;
    }

    @Override
    public String toString() {
        return name;
    }
}
