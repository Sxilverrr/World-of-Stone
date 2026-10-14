package com.sxilverr.worldofstone.registry;

import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.api.enums.FossilVariant;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

import java.util.EnumMap;
import java.util.Map;

public final class WosItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ModInfo.MODID, Registries.ITEM);

    public static final Map<FossilVariant, RegistrySupplier<Item>> FOSSILS = new EnumMap<>(FossilVariant.class);

    public static final RegistrySupplier<Item> LIGNITE_COAL = ITEMS.register("lignite_coal",
            () -> new Item(new Item.Properties()));

    static {
        for (FossilVariant v : FossilVariant.VALUES) {
            FOSSILS.put(v, ITEMS.register(v.toString(),
                    () -> new Item(new Item.Properties())));
        }
    }

    private WosItems() {
    }
}
