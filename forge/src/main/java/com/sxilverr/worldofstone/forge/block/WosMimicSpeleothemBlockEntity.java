package com.sxilverr.worldofstone.forge.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class WosMimicSpeleothemBlockEntity extends BlockEntity {

    public static final ModelProperty<BlockState> SOURCE = new ModelProperty<>();

    private BlockState sourceState = Blocks.STONE.defaultBlockState();

    public WosMimicSpeleothemBlockEntity(BlockPos pos, BlockState state) {
        super(WosLoaderBlocks.MIMIC_ENTITY.get(), pos, state);
    }

    public static Optional<BlockState> readSource(CompoundTag tag) {
        if (!tag.contains("Source")) return Optional.empty();
        return Optional.ofNullable(ResourceLocation.tryParse(tag.getString("Source")))
                .flatMap(BuiltInRegistries.BLOCK::getOptional)
                .filter(block -> block != Blocks.AIR)
                .map(block -> block.defaultBlockState());
    }

    public BlockState getSourceState() {
        return sourceState;
    }

    public void setSource(BlockState state) {
        if (state == null || state.isAir() || sourceState == state) return;
        BlockState old = sourceState;
        sourceState = state;
        setChanged();
        requestModelDataUpdate();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            if (state.getLightEmission() != old.getLightEmission()) {
                level.getLightEngine().checkBlock(getBlockPos());
            }
        }
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        readSource(tag).ifPresent(state -> sourceState = state);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("Source", BuiltInRegistries.BLOCK.getKey(sourceState.getBlock()).toString());
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull ModelData getModelData() {
        return ModelData.builder().with(SOURCE, sourceState).build();
    }
}
