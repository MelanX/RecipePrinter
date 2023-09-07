package de.melanx.recipeprinter.commands;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.RecipePrinter;
import de.melanx.recipeprinter.RecipeRenderers;
import de.melanx.recipeprinter.util.PrinterJob;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.moddingx.libx.render.target.ImageHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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

                    CompletableFuture<NativeImage> img = ImageHelper.render(new PrinterJob(render.getRecipeWidth(), render.getRecipeHeight(), (int) render.getScaleFactor(), guiGraphics -> {
                        //noinspection unchecked
                        ((IRecipeRender<Recipe<?>>) render).render(recipe, guiGraphics);
                    }));

                    try {
                        img.get().writeToFile(path);
                    } catch (IOException | InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        if (typesNotSupported.isEmpty()) {
            context.getSource().sendSuccess(() -> Component.literal("Started rendering " + recipesTotal.get() + " recipes."), true);
        } else {
            context.getSource().sendSuccess(() -> Component.literal("Started rendering " + recipesStarted.get() + " out of " + recipesTotal.get() + " recipes."), true);
            for (RecipeType<?> rt : typesNotSupported) {
                context.getSource().sendSuccess(() -> Component.literal("Could not render recipes: " + rt.toString()), true);
            }
        }

        return 0;
    }
}
