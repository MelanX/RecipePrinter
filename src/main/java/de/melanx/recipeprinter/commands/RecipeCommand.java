package de.melanx.recipeprinter.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.RecipePrinter;
import de.melanx.recipeprinter.RecipeRenderers;
import de.melanx.recipeprinter.util.ImageHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class RecipeCommand implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        RecipeSelector sel = context.getArgument("recipes", RecipeSelector.class);
        //noinspection ConstantConditions
        RecipeManager rm = Minecraft.getInstance().getSingleplayerServer().getRecipeManager();
        List<ResourceLocation> rls = sel.getRecipes(rm);

        Set<RecipeType<?>> typesNotSupported = new HashSet<>();
        AtomicInteger recipesTotal = new AtomicInteger();
        AtomicInteger recipesStarted = new AtomicInteger();
        for (ResourceLocation rl : rls) {
            rm.byKey(rl).ifPresent(recipe -> {
                recipesTotal.addAndGet(1);
                IRecipeRender<?> render = RecipeRenderers.getRecipeRender(recipe);
                if (render == null) {
                    typesNotSupported.add(recipe.getType());
                } else {
                    recipesStarted.addAndGet(1);
                    Path path = context.getSource().getServer().getServerDirectory().toPath().resolve(RecipePrinter.getInstance().modid).resolve("recipes").resolve(rl.getNamespace()).resolve(rl.getPath().replace('/', File.separatorChar) + ".png");
                    if (!Files.exists(path.getParent())) {
                        try {
                            Files.createDirectories(path.getParent());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    //noinspection unchecked
                    ImageHelper.addRenderJob(render.getRecipeWidth(), render.getRecipeHeight(), render.getScaleFactor(), (matrixStack, buffer) -> ((IRecipeRender<Recipe<?>>) render).render(recipe, matrixStack, buffer), path);
                }
            });
        }

        if (typesNotSupported.isEmpty()) {
            context.getSource().sendSuccess(new TextComponent("Started rendering " + recipesTotal.get() + " recipes."), true);
        } else {
            context.getSource().sendSuccess(new TextComponent("Started rendering " + recipesStarted.get() + " out of " + recipesTotal.get() + " recipes."), true);
            for (RecipeType<?> rt : typesNotSupported) {
                context.getSource().sendSuccess(new TextComponent("Could not render recipes: " + rt.toString()), true);
            }
        }

        return 0;
    }
}
