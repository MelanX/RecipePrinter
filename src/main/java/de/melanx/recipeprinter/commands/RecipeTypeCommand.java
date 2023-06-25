package de.melanx.recipeprinter.commands;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.RecipePrinter;
import de.melanx.recipeprinter.RecipeRenderers;
import de.melanx.recipeprinter.util.PrinterJob;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.moddingx.libx.render.target.ImageHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RecipeTypeCommand implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        String typeName = StringArgumentType.getString(context, "type");
        //noinspection deprecation
        RecipeType<?> type = BuiltInRegistries.RECIPE_TYPE.get(new ResourceLocation(typeName));
        List<Recipe<?>> list = context.getSource().getServer().getRecipeManager().recipes.get(type).values().stream().toList();
        if (list.isEmpty()) {
            return 0;
        }

        for (Recipe<?> recipe : list) {
            IRecipeRender<? extends Recipe<?>> render = RecipeRenderers.getRecipeRender(recipe);
            Path path = context.getSource().getServer().getServerDirectory().toPath().resolve(RecipePrinter.getInstance().modid).resolve("recipes").resolve(recipe.getId().getNamespace()).resolve(recipe.getId().getPath().replace('/', File.separatorChar) + ".png");
            if (!Files.exists(path.getParent())) {
                try {
                    Files.createDirectories(path.getParent());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            CompletableFuture<NativeImage> img = ImageHelper.render(new PrinterJob(render.getRecipeWidth(), render.getRecipeHeight(), (int) render.getScaleFactor(), (poseStack, buffer) -> {
                //noinspection unchecked
                ((IRecipeRender<Recipe<?>>) render).render(recipe, poseStack, buffer);
            }));

            try {
                img.get().writeToFile(path);
            } catch (IOException | InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        context.getSource().sendSuccess(Component.literal("Started rendering " + list.size() + " recipes."), true);

        return 0;
    }
}
