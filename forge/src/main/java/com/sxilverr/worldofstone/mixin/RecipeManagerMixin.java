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
        Map<ResourceLocation, Recipe<?>> keep = new HashMap<>();
        for (Map.Entry<ResourceLocation, Recipe<?>> entry : this.byName.entrySet()) {
            if (!wos$isRemoved(entry.getKey(), entry.getValue())) {
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

    private static boolean wos$isRemoved(ResourceLocation id, Recipe<?> recipe) {
        ItemStack result;
        try {
            result = recipe.getResultItem(RegistryAccess.EMPTY);
        } catch (Throwable t) {
            result = ItemStack.EMPTY;
        }
        ResourceLocation key = result == null || result.isEmpty() ? null : BuiltInRegistries.ITEM.getKey(result.getItem());
        return WosConfig.isRecipeDisabled(id, key);
    }
}
