package de.melanx.recipedrawer;

import de.melanx.recipedrawer.commands.FilteredResourceLocationArgument;
import de.melanx.recipedrawer.commands.RecipeDrawerCommands;
import de.melanx.recipedrawer.commands.RecipeSelectorArgument;
import de.melanx.recipedrawer.renderers.*;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(RecipeDrawer.MODID)
public class RecipeDrawer {

    public static final String MODID = "recipedrawer";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public RecipeDrawer() {
        try {
            Class.forName("net.minecraft.client.main.Main"); // Luckily this class is never renamed.
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            throw new IllegalStateException("RecipeDrawer can only run in singleplayer.", e);
        }

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(RecipeDrawerCommands::register);
    }

    private void setup(FMLCommonSetupEvent event) {
        ArgumentTypes.register(MODID + "_recipeselector", RecipeSelectorArgument.class, new RecipeSelectorArgument.Serializer());
        ArgumentTypes.register(MODID + "_resourceselector", FilteredResourceLocationArgument.class, new FilteredResourceLocationArgument.Serializer());

        RecipeRenderers.registerRecipeRender(new ShapelessRender());
        RecipeRenderers.registerRecipeRender(new ShapedRender());
        RecipeRenderers.registerRecipeRender(new SmeltingRender());
        RecipeRenderers.registerRecipeRender(new BlastingRender());
        RecipeRenderers.registerRecipeRender(new SmokingRender());
        RecipeRenderers.registerRecipeRender(new CampfireRender());
        RecipeRenderers.registerRecipeRender(new SmithingRender());
    }
}
