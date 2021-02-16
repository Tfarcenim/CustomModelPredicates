package tfar.custommodelpredicates;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CustomModelPredicates.MODID)
public class CustomModelPredicates {
    // Directly reference a log4j logger.

    public static final String MODID = "custommodelpredicates";

    public CustomModelPredicates() {
        if (FMLEnvironment.dist.isClient())
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(MODID, MODID), ModelLoader.INSTANCE);
    }
}
