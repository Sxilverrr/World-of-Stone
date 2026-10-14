package com.sxilverr.worldofstone.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.config.WosConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(RecipeManager.class)
public abstract class RecipeManagerMixin {

    @Shadow private Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipes;
    @Shadow private Map<ResourceLocation, Recipe<?>> byName;

    @Inject(method = "apply", at = @At("TAIL"))
    private void wos$filterRecipes(Map<ResourceLocation, JsonElement> object, ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfo ci) {
        if (!wos$filteringActive()) {
            return;
        }

        Map<ResourceLocation, Recipe<?>> keep = new HashMap<>();
        for (Map.Entry<ResourceLocation, Recipe<?>> entry : this.byName.entrySet()) {
            if (!wos$isRemoved(entry.getValue())) {
                keep.put(entry.getKey(), entry.getValue());
            }
        }
        if (keep.size() == this.byName.size()) {
            return;
        }

        Map<RecipeType<?>, ImmutableMap.Builder<ResourceLocation, Recipe<?>>> byType = new HashMap<>();
        for (Map.Entry<ResourceLocation, Recipe<?>> entry : keep.entrySet()) {
            byType.computeIfAbsent(entry.getValue().getType(), t -> ImmutableMap.builder()).put(entry.getKey(), entry.getValue());
        }

        ImmutableMap.Builder<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> rebuilt = ImmutableMap.builder();
        byType.forEach((type, b) -> rebuilt.put(type, b.build()));

        this.recipes = rebuilt.build();
        this.byName = ImmutableMap.copyOf(keep);
    }

    private static boolean wos$filteringActive() {
        return !WosConfig.stoneReplacementEnabled
                || !WosConfig.allowOvergrownStrata
                || !WosConfig.allowSnowedStrata
                || !WosConfig.allowOvergrownVanillaVariants
                || !WosConfig.allowSnowedVanillaVariants;
    }

    private static boolean wos$isRemoved(Recipe<?> recipe) {
        ItemStack result;
        try {
            result = recipe.getResultItem(RegistryAccess.EMPTY);
        } catch (Throwable t) {
            return false;
        }
        if (result == null || result.isEmpty()) {
            return false;
        }
        ResourceLocation key = BuiltInRegistries.ITEM.getKey(result.getItem());
        if (key == null || !ModInfo.MODID.equals(key.getNamespace())) {
            return false;
        }
        String path = key.getPath();
        boolean strata = WosConfig.isStrataVariantPath(path);
        if (!WosConfig.stoneReplacementEnabled && strata) {
            return true;
        }
        if (path.startsWith("overgrown_")) {
            return strata ? !WosConfig.allowOvergrownStrata : !WosConfig.allowOvergrownVanillaVariants;
        }
        if (path.startsWith("snowed_")) {
            return strata ? !WosConfig.allowSnowedStrata : !WosConfig.allowSnowedVanillaVariants;
        }
        return false;
    }
}
