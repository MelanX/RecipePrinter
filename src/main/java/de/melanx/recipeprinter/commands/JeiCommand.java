package de.melanx.recipeprinter.commands;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import de.melanx.recipeprinter.Config;
import de.melanx.recipeprinter.RecipePrinter;
import de.melanx.recipeprinter.jei.PrinterJEI;
import de.melanx.recipeprinter.util.ImageHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.gui.recipes.RecipeLayout;
import net.minecraft.command.CommandSource;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static de.melanx.recipeprinter.jei.PrinterJEI.REG;

public class JeiCommand implements Command<CommandSource> {
	@Override
	public int run(CommandContext<CommandSource> context) {
		if (PrinterJEI.REG == null)
			return 0;
		RecipeSelector sel = context.getArgument("recipes", RecipeSelector.class);
		RecipeManager rm = context.getSource().getServer().getRecipeManager();
		List<ResourceLocation> recipes = sel.getRecipes(rm);
		ImmutableList<IRecipeCategory<?>> categories = REG.getRecipeCategories();

		for (ResourceLocation rl : recipes) {
			rm.getRecipe(rl).ifPresent(iRecipe -> categories.stream().filter(iRecipeCategory -> iRecipeCategory.getRecipeClass().isAssignableFrom(iRecipe.getClass())).forEach(iRecipeCategory -> {
				RecipePrinter.getInstance().logger.debug("Printing " + iRecipeCategory + " " + iRecipe);
				Path path = context.getSource().getServer().getDataDirectory().toPath().resolve(RecipePrinter.getInstance().modid).resolve("recipes").resolve(rl.getNamespace()).resolve(rl.getPath().replace('/', File.separatorChar) + ".png");
				if (!Files.exists(path.getParent())) {
					try {
						Files.createDirectories(path.getParent());
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}

				IRecipeCategory<IRecipe<?>> category = (IRecipeCategory<IRecipe<?>>) iRecipeCategory;
				RecipeLayout<?> layout = RecipeLayout.create(-1, category, iRecipe, null, REG.getJeiHelpers().getModIdHelper(), 0, 0);
				if (layout != null)
					ImageHelper.addRenderJob(category.getBackground().getWidth(), category.getBackground().getHeight(), Config.scale.get() * 2., (matrixStack, buffer) -> layout.drawRecipe(matrixStack, 0, 0), path, true);
			}));
		}

		return 0;
	}
}
