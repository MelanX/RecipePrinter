package de.melanx.recipeprinter;

import de.melanx.recipeprinter.commands.FilteredResourceLocationArgument;
import de.melanx.recipeprinter.commands.RecipePrinterCommands;
import de.melanx.recipeprinter.commands.RecipeSelectorArgument;
import de.melanx.recipeprinter.renderers.vanilla.*;
import io.github.noeppi_noeppi.libx.mod.ModX;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import javax.annotation.Nonnull;

@Mod("recipeprinter")
public final class RecipePrinter extends ModX {

    private static RecipePrinter instance;

    public RecipePrinter() {
        instance = this;

        try {
            Class.forName("net.minecraft.client.main.Main"); // Luckily this class is never renamed.
        } catch (ClassNotFoundException | NoClassDefFoundError e) {
            throw new IllegalStateException("Recipe Printer can only run in singleplayer.", e);
        }

        MinecraftForge.EVENT_BUS.addListener(RecipePrinterCommands::register);
    }

    @Nonnull
    public static RecipePrinter getInstance() {
        return instance;
    }

    @Override
    protected void setup(FMLCommonSetupEvent event) {
        ArgumentTypes.register(this.modid + "_recipeselector", RecipeSelectorArgument.class, new RecipeSelectorArgument.Serializer());
        ArgumentTypes.register(this.modid + "_resourceselector", FilteredResourceLocationArgument.class, new FilteredResourceLocationArgument.Serializer());

        RecipeRenderers.registerRecipeRender(new ShapelessRender());
        RecipeRenderers.registerRecipeRender(new ShapedRender());
        RecipeRenderers.registerRecipeRender(new SmeltingRender());
        RecipeRenderers.registerRecipeRender(new BlastingRender());
        RecipeRenderers.registerRecipeRender(new SmokingRender());
        RecipeRenderers.registerRecipeRender(new CampfireRender());
        RecipeRenderers.registerRecipeRender(new SmithingRender());
        RecipeRenderers.registerRecipeRender(new StonecuttingRender());
    }

    @Override
    protected void clientSetup(FMLClientSetupEvent event) {

    }
}
