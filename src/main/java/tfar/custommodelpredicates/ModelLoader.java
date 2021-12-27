package tfar.custommodelpredicates;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraftforge.client.model.IModelLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ModelLoader implements IModelLoader<CustomPredicateModel> {
    public static final ModelLoader INSTANCE = new ModelLoader();

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        // Not used
    }

    @Override
    public CustomPredicateModel read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
        BlockModel baseModel = deserializationContext.deserialize(JSONUtils.getJsonObject(modelContents, "base"), BlockModel.class);

        List<Pair<Predicate<ItemStack>,BlockModel>> predicateList = new ArrayList<>();

        JsonArray predicateData = JSONUtils.getJsonArray(modelContents, "predicates");

        for (JsonElement predicate : predicateData) {
            JsonObject pred = predicate.getAsJsonObject();

            String type = pred.get("type").getAsString();

            if (type != null) {
                BlockModel blockModel = deserializationContext.deserialize(JSONUtils.getJsonObject(pred, "model"), BlockModel.class);
                switch (type) {
                    case "name":
                        predicateList.add(new Pair<>(ItemPredicateList.createNamePredicate(pred.get("name").getAsString()), blockModel));
                        break;
                    case "count":
                        predicateList.add(new Pair<>(ItemPredicateList.createCountPredicate(pred.get("count").getAsString()), blockModel));
                        break;
                    case "nbt":
                        predicateList.add(new Pair<>(
                                ItemPredicateList.createNBTPredicate(pred.get("tag_name").getAsString(), pred.get("tag_type").getAsString(), pred.get("require").getAsString()), blockModel));
                        break;
                    case "modid":
                        predicateList.add(new Pair<>(
                                ItemPredicateList.createModidPredicate(pred.get("modid").getAsString()), blockModel));
                        break;
                    case "enchantment":
                        predicateList.add(new Pair<>(
                                ItemPredicateList.createEnchantmentPredicate(pred.get("enchant").getAsString()), blockModel));
                        break;
                }
            }
        }
        return new CustomPredicateModel(baseModel, predicateList);
    }
}
