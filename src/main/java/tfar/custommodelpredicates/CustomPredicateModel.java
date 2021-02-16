package tfar.custommodelpredicates;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.Direction;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.IModelLoader;
import net.minecraftforge.client.model.geometry.IModelGeometry;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class CustomPredicateModel implements IModelGeometry<CustomPredicateModel> {

    private final BlockModel baseModel;
    private final List<Pair<Predicate<ItemStack>, BlockModel>> predicates;

    public CustomPredicateModel(BlockModel baseModel, List<Pair<Predicate<ItemStack>, BlockModel>> predicates) {
        this.baseModel = baseModel;
        this.predicates = predicates;
    }

    @Override
    public IBakedModel bake(IModelConfiguration owner, ModelBakery bakery, Function<RenderMaterial, TextureAtlasSprite> spriteGetter, IModelTransform modelTransform, ItemOverrideList overrides, ResourceLocation modelLocation) {

        List<Pair<Predicate<ItemStack>, IBakedModel>> modelPredicates = new ArrayList<>();

        for (Pair<Predicate<ItemStack>,BlockModel> modelPair : predicates) {
            IBakedModel model = modelPair.getSecond().bakeModel(bakery, modelPair.getSecond(), spriteGetter, modelTransform, modelLocation, owner.isSideLit());
            modelPredicates.add(new Pair<>(modelPair.getFirst(),model));
        }

        return new BakedModel(
                owner.useSmoothLighting(), owner.isShadedInGui(), owner.isSideLit(),
                spriteGetter.apply(owner.resolveTexture("particle")), overrides,
                baseModel.bakeModel(bakery, baseModel, spriteGetter, modelTransform, modelLocation, owner.isSideLit()),
                modelPredicates);
    }

    @Override
    public Collection<RenderMaterial> getTextures(IModelConfiguration owner, Function<ResourceLocation, IUnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        Set<RenderMaterial> textures = Sets.newHashSet();
        textures.addAll(baseModel.getTextures(modelGetter, missingTextureErrors));

        for (Pair<Predicate<ItemStack>,BlockModel> modelPair : predicates) {
            textures.addAll(modelPair.getSecond().getTextures(modelGetter, missingTextureErrors));
        }
        return textures;
    }


    public static class BakedModel implements IBakedModel {
        private final boolean isAmbientOcclusion;
        private final boolean isGui3d;
        private final boolean isSideLit;
        private final TextureAtlasSprite particle;
        private final ItemOverrideList overrides;
        private final IBakedModel baseModel;
        private final List<Pair<Predicate<ItemStack>, IBakedModel>> predicates;

        public BakedModel(boolean isAmbientOcclusion, boolean isGui3d, boolean isSideLit, TextureAtlasSprite particle, ItemOverrideList overrides, IBakedModel baseModel, List<Pair<Predicate<ItemStack>, IBakedModel>> predicates) {
            this.isAmbientOcclusion = isAmbientOcclusion;
            this.isGui3d = isGui3d;
            this.isSideLit = isSideLit;
            this.particle = particle;
            this.overrides = overrides;
            this.baseModel = baseModel;
            this.predicates = predicates;
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
            return Collections.emptyList();
        }

        @Override
        public boolean isAmbientOcclusion() {
            return isAmbientOcclusion;
        }

        @Override
        public boolean isGui3d() {
            return isGui3d;
        }

        @Override
        public boolean isSideLit() {
            return isSideLit;
        }

        @Override
        public boolean isBuiltInRenderer() {
            return false;
        }

        @Override
        public TextureAtlasSprite getParticleTexture() {
            return particle;
        }

        @Override
        public ItemOverrideList getOverrides() {
            return overrides;
        }

        @Override
        public boolean doesHandlePerspectives() {
            return true;
        }

        @Override
        public ItemCameraTransforms getItemCameraTransforms() {
            return ItemCameraTransforms.DEFAULT;
        }

        @Override
        public IBakedModel handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, MatrixStack mat) {
            return baseModel.handlePerspective(cameraTransformType, mat);
        }

        public IBakedModel handleOverrides(IBakedModel oldModel, ItemStack stack, ClientWorld world, LivingEntity livingEntity) {
            IBakedModel newModel = null;
            for (Pair<Predicate<ItemStack>,IBakedModel> modelPair : predicates) {
                if (modelPair.getFirst().test(stack)) {
                    newModel = modelPair.getSecond();
                    break;
                }
            }
            return newModel;
        }
    }
}
