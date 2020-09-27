package de.melanx.recipeprinter;

import de.melanx.recipeprinter.commands.FilteredResourceLocationArgument;
import de.melanx.recipeprinter.commands.RecipePrinterCommands;
import de.melanx.recipeprinter.commands.RecipeSelectorArgument;
import de.melanx.recipeprinter.renderers.*;
import de.melanx.recipeprinter.renderers.botania.PureDaisyRender;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(RecipePrinter.MODID)
public class RecipePrinter {

    public static final String MODID = "recipeprinter";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public RecipePrinter() {
        try {
            Class.forName("net.minecraft.client.main.Main"); // Luckily this class is never renamed.
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            throw new IllegalStateException("RecipePrinter can only run in singleplayer.", e);
        }

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CONFIG);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(RecipePrinterCommands::register);
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
        RecipeRenderers.registerRecipeRender(new StonecuttingRender());

        if (ModList.get().isLoaded("botania")) {
            RecipeRenderers.registerRecipeRender(new PureDaisyRender());
        }
    }
}
