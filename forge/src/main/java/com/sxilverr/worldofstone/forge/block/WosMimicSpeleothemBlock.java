package com.sxilverr.worldofstone.forge.block;

import com.sxilverr.worldofstone.common.block.WosSpeleothemBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

public class WosMimicSpeleothemBlock extends WosSpeleothemBlock implements EntityBlock {

    public WosMimicSpeleothemBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WosMimicSpeleothemBlockEntity(pos, state);
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        if (level.getBlockEntity(pos) instanceof WosMimicSpeleothemBlockEntity mimic) {
            BlockState source = mimic.getSourceState();
            return source.getBlock().getSoundType(source, level, pos, entity);
        }
        return super.getSoundType(state, level, pos, entity);
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof WosMimicSpeleothemBlockEntity mimic) {
            return mimic.getSourceState().getDestroyProgress(player, level, pos);
        }
        return super.getDestroyProgress(state, player, level, pos);
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        if (level.getBlockEntity(pos) instanceof WosMimicSpeleothemBlockEntity mimic) {
            return mimic.getSourceState().canHarvestBlock(level, pos, player);
        }
        if (!state.requiresCorrectToolForDrops()) return true;
        return player.getMainHandItem().isCorrectToolForDrops(state);
    }

    @Override
    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        if (level.getBlockEntity(pos) instanceof WosMimicSpeleothemBlockEntity mimic) {
            return mimic.getSourceState().getExplosionResistance(level, pos, explosion);
        }
        return super.getExplosionResistance(state, level, pos, explosion);
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof WosMimicSpeleothemBlockEntity mimic) {
            return mimic.getSourceState().getLightEmission(level, pos);
        }
        return super.getLightEmission(state, level, pos);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        CompoundTag tag = stack.getTagElement("BlockEntityTag");
        if (tag != null && level.getBlockEntity(pos) instanceof WosMimicSpeleothemBlockEntity mimic) {
            WosMimicSpeleothemBlockEntity.readSource(tag).ifPresent(mimic::setSource);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        ItemStack stack = super.getCloneItemStack(state, target, level, pos, player);
        if (level.getBlockEntity(pos) instanceof WosMimicSpeleothemBlockEntity mimic) {
            ResourceLocation id = BuiltInRegistries.BLOCK.getKey(mimic.getSourceState().getBlock());
            CompoundTag tag = new CompoundTag();
            tag.putString("Source", id.toString());
            stack.addTagElement("BlockEntityTag", tag);
        }
        return stack;
    }
}
