package de.melanx.recipedrawer.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import de.melanx.recipedrawer.IRecipeRender;
import de.melanx.recipedrawer.RecipeDrawer;
import de.melanx.recipedrawer.RecipeRenderers;
import de.melanx.recipedrawer.util.ImageHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class RecipeDrawerCommand implements Command<CommandSource> {

    @Override
    public int run(CommandContext<CommandSource> context) {
        RecipeSelector sel = context.getArgument("recipes", RecipeSelector.class);
        //noinspection ConstantConditions
        RecipeManager rm = Minecraft.getInstance().getIntegratedServer().getRecipeManager();
        List<ResourceLocation> rls = sel.getRecipes(rm);

        Set<IRecipeType<?>> typesNotSupported = new HashSet<>();
        AtomicInteger recipesTotal = new AtomicInteger();
        AtomicInteger recipesStarted = new AtomicInteger();
        for (ResourceLocation rl : rls) {
            rm.getRecipe(rl).ifPresent(recipe -> {
                recipesTotal.addAndGet(1);
                IRecipeRender<?> render = RecipeRenderers.getRecipeRender(recipe);
                if (render == null) {
                    typesNotSupported.add(recipe.getType());
                } else {
                    recipesStarted.addAndGet(1);
                    Path path = context.getSource().getServer().getDataDirectory().toPath().resolve(RecipeDrawer.MODID).resolve(rl.getNamespace()).resolve(rl.getPath().replace('/', '-') + ".png");
                    if (!Files.exists(path.getParent())) {
                        try {
                            Files.createDirectories(path.getParent());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    //noinspection unchecked
                    ImageHelper.addRenderJob(render.getRecipeWidth(), render.getRecipeHeight(), render.getScaleFactor(), (matrixStack, buffer) -> ((IRecipeRender<IRecipe<?>> ) render).render(recipe, matrixStack, buffer), path);
                    ImageHelper.replaceBackground(render, path);
                }
            });
        }

        if (typesNotSupported.isEmpty()) {
            context.getSource().sendFeedback(new StringTextComponent("Started rendering " + recipesTotal.get() + " recipes."), true);
        } else {
            context.getSource().sendFeedback(new StringTextComponent("Started rendering " + recipesStarted.get() + " out of " + recipesTotal.get() + " recipes."), true);
            for (IRecipeType<?> rt : typesNotSupported) {
                context.getSource().sendFeedback(new StringTextComponent("Could not render recipes: " + rt.toString()), true);
            }
        }

        return 0;
    }
}
