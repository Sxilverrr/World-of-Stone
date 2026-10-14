package com.sxilverr.worldofstone.neoforge.client;

import com.sxilverr.worldofstone.neoforge.block.WosMimicSpeleothemBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WosMimicSpeleothemBakedModel implements IDynamicBakedModel {

    private static final int STRIDE = 8;
    private static final int UV_OFFSET = 4;

    private final BakedModel template;
    private final ItemOverrides itemOverrides;

    public WosMimicSpeleothemBakedModel(BakedModel template) {
        this.template = template;
        this.itemOverrides = new MimicItemOverrides(this);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType layer) {
        BlockState source = data.get(WosMimicSpeleothemBlockEntity.SOURCE);
        if (source == null) source = Blocks.STONE.defaultBlockState();
        TextureAtlasSprite sprite = sourceSprite(source);
        return retexture(template.getQuads(state, side, rand, data, layer), sprite);
    }

    @Override
    public boolean useAmbientOcclusion() { return template.useAmbientOcclusion(); }

    @Override
    public boolean isGui3d() { return template.isGui3d(); }

    @Override
    public boolean usesBlockLight() { return template.usesBlockLight(); }

    @Override
    public boolean isCustomRenderer() { return template.isCustomRenderer(); }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() { return template.getParticleIcon(); }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        BlockState source = data.get(WosMimicSpeleothemBlockEntity.SOURCE);
        if (source == null) return template.getParticleIcon(data);
        return sourceSprite(source);
    }

    @Override
    public @NotNull ItemTransforms getTransforms() { return template.getTransforms(); }

    @Override
    public @NotNull ItemOverrides getOverrides() { return itemOverrides; }

    static TextureAtlasSprite sourceSprite(BlockState source) {
        BakedModel m = Minecraft.getInstance().getBlockRenderer().getBlockModel(source);
        return m.getParticleIcon(ModelData.EMPTY);
    }

    static List<BakedQuad> retexture(List<BakedQuad> quads, TextureAtlasSprite newSprite) {
        List<BakedQuad> out = new ArrayList<>(quads.size());
        for (BakedQuad q : quads) out.add(retextureQuad(q, newSprite));
        return out;
    }

    private static BakedQuad retextureQuad(BakedQuad q, TextureAtlasSprite newSprite) {
        int[] data = q.getVertices().clone();
        TextureAtlasSprite oldSprite = q.getSprite();
        float oldU0 = oldSprite.getU0();
        float oldU1 = oldSprite.getU1();
        float oldV0 = oldSprite.getV0();
        float oldV1 = oldSprite.getV1();
        float oldUSpan = oldU1 - oldU0;
        float oldVSpan = oldV1 - oldV0;
        if (oldUSpan == 0 || oldVSpan == 0) {
            return new BakedQuad(data, q.getTintIndex(), q.getDirection(), newSprite, q.isShade());
        }
        float newU0 = newSprite.getU0();
        float newV0 = newSprite.getV0();
        float newUSpan = newSprite.getU1() - newU0;
        float newVSpan = newSprite.getV1() - newV0;
        for (int v = 0; v < 4; v++) {
            int base = v * STRIDE;
            float u = Float.intBitsToFloat(data[base + UV_OFFSET]);
            float vv = Float.intBitsToFloat(data[base + UV_OFFSET + 1]);
            float localU = (u - oldU0) / oldUSpan;
            float localV = (vv - oldV0) / oldVSpan;
            data[base + UV_OFFSET] = Float.floatToRawIntBits(newU0 + localU * newUSpan);
            data[base + UV_OFFSET + 1] = Float.floatToRawIntBits(newV0 + localV * newVSpan);
        }
        return new BakedQuad(data, q.getTintIndex(), q.getDirection(), newSprite, q.isShade());
    }

    private static BlockState readSourceFromStack(ItemStack stack) {
        CustomData data = stack.get(DataComponents.BLOCK_ENTITY_DATA);
        return data == null ? Blocks.STONE.defaultBlockState() : WosMimicSpeleothemBlockEntity.readSource(data.copyTag()).orElse(Blocks.STONE.defaultBlockState());
    }

    private static class MimicItemOverrides extends ItemOverrides {
        private final WosMimicSpeleothemBakedModel parent;

        MimicItemOverrides(WosMimicSpeleothemBakedModel parent) {
            this.parent = parent;
        }

        @Override
        public BakedModel resolve(BakedModel base, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
            BlockState source = readSourceFromStack(stack);
            return new BoundMimicModel(parent.template, source);
        }
    }

    private static class BoundMimicModel implements BakedModel {
        private final BakedModel template;
        private final TextureAtlasSprite sprite;

        BoundMimicModel(BakedModel template, BlockState source) {
            this.template = template;
            this.sprite = sourceSprite(source);
        }

        @Override
        public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
            return retexture(template.getQuads(state, side, rand), sprite);
        }

        @Override
        public boolean useAmbientOcclusion() { return template.useAmbientOcclusion(); }

        @Override
        public boolean isGui3d() { return template.isGui3d(); }

        @Override
        public boolean usesBlockLight() { return template.usesBlockLight(); }

        @Override
        public boolean isCustomRenderer() { return template.isCustomRenderer(); }

        @SuppressWarnings("deprecation")
        @Override
        public @NotNull TextureAtlasSprite getParticleIcon() { return sprite; }

        @Override
        public @NotNull ItemTransforms getTransforms() { return template.getTransforms(); }

        @Override
        public @NotNull ItemOverrides getOverrides() { return ItemOverrides.EMPTY; }
    }
}
