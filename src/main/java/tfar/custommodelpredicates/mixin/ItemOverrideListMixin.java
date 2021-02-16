package tfar.custommodelpredicates.mixin;

import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.custommodelpredicates.CustomPredicateModel;

@Mixin(ItemOverrideList.class)
public class ItemOverrideListMixin {
	@Inject(at = @At("HEAD"), method = "getOverrideModel",cancellable = true)
	private void init(IBakedModel model, ItemStack stack, ClientWorld world, LivingEntity livingEntity, CallbackInfoReturnable<IBakedModel> cir) {
		if (model instanceof CustomPredicateModel.BakedModel) {
			IBakedModel newModel = ((CustomPredicateModel.BakedModel)model).handleOverrides(model,stack,world,livingEntity);
			if (newModel != null) {
				cir.setReturnValue(newModel);
			}
		}
	}
}
