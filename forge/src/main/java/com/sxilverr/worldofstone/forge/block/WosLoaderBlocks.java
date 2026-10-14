package com.sxilverr.worldofstone.forge.block;

import com.sxilverr.worldofstone.api.ModInfo;
import com.sxilverr.worldofstone.registry.WosBlocks;
import com.sxilverr.worldofstone.world.MimicHook;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class WosLoaderBlocks {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ModInfo.MODID, Registries.BLOCK_ENTITY_TYPE);
    public static final RegistrySupplier<BlockEntityType<WosMimicSpeleothemBlockEntity>> MIMIC_ENTITY = BLOCK_ENTITIES.register("mimic_speleothem",
            () -> BlockEntityType.Builder.of(WosMimicSpeleothemBlockEntity::new, WosBlocks.MIMIC_SPELEOTHEM.get()).build(null));

    private static final SoundType DEV_SOUNDS = new SoundType(1.0F, 1.0F, SoundEvents.CAT_DEATH, SoundEvents.CAT_AMBIENT,
            SoundEvents.CAT_STRAY_AMBIENT, SoundEvents.CAT_AMBIENT, SoundEvents.CAT_AMBIENT);

    public static void register() {
        WosBlocks.DEV_SPELEOTHEM = WosBlocks.register("dev_speleothem", () -> new WosDevSpeleothemBlock(BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL).sound(DEV_SOUNDS).strength(50.0F, 1200.0F).noOcclusion().dynamicShape().requiresCorrectToolForDrops()));
        WosBlocks.MIMIC_SPELEOTHEM = WosBlocks.register("mimic_speleothem", () -> new WosMimicSpeleothemBlock(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE).sound(SoundType.STONE).strength(1.5F, 6.0F).noOcclusion().dynamicShape().requiresCorrectToolForDrops()));
        BLOCK_ENTITIES.register();
        MimicHook.INSTANCE = (level, pos, hostState) -> {
            if (level.getBlockEntity(pos) instanceof WosMimicSpeleothemBlockEntity mimic) mimic.setSource(hostState);
        };
    }

    private WosLoaderBlocks() {
    }
}
