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
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static de.melanx.recipeprinter.jei.PrinterJEI.REG;

public class JeiCommand implements Command<CommandSource> {
	@Override
	public int run(CommandContext<CommandSource> context) {
		if (PrinterJEI.REG == null)
			return 0;
		RecipeSelector sel = context.getArgument("recipes", RecipeSelector.class);
		RecipeManager rm = Minecraft.getInstance().getIntegratedServer().getRecipeManager();
		List<ResourceLocation> recipes = sel.getRecipes(rm);
		ImmutableList<IRecipeCategory<?>> categories = REG.getRecipeCategories();

		AtomicInteger i = new AtomicInteger();
		AtomicInteger matches = new AtomicInteger();
		for (ResourceLocation rl : recipes) {
			rm.getRecipe(rl).ifPresent(iRecipe -> categories.stream().filter(iRecipeCategory -> iRecipeCategory.getRecipeClass().isAssignableFrom(iRecipe.getClass())).forEach(iRecipeCategory -> {
				matches.getAndIncrement();
				Path path = context.getSource().getServer().getDataDirectory().toPath()
					.resolve(RecipePrinter.getInstance().modid)
					.resolve("jei")
					.resolve(rl.getNamespace())
					.resolve(iRecipeCategory.getClass().getSimpleName())
					// .resolve(rl.toString().replaceAll("/|:", Matcher.quoteReplacement(String.valueOf(File.separatorChar))))
					.resolve(rl.getPath().replaceAll("([^/]*/)*", "") + ".png");
				if (!Files.exists(path.getParent())) {
					try {
						Files.createDirectories(path.getParent());
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}

				RecipeLayout<?> layout;
				try {
					layout = RecipeLayout.create(-1, (IRecipeCategory<IRecipe<?>>) iRecipeCategory, iRecipe, null, REG.getJeiHelpers().getModIdHelper(), 0, 0);
					if (layout != null) {
						ImageHelper.addRenderJob(iRecipeCategory.getBackground().getWidth(), iRecipeCategory.getBackground().getHeight(), Config.scale.get() * 2., (matrixStack, buffer) -> {
							RecipePrinter.getInstance().logger.debug("Printing {} {} {}%", iRecipeCategory.getUid(), iRecipe.getId(), MathHelper.floor(100. * i.getAndIncrement() / matches.get()));
							layout.drawRecipe(matrixStack, -10, -10);
						}, path, true);
					}
				} catch (RuntimeException e) {
					RecipePrinter.getInstance().logger.error("Could not print recipe {}: {}", iRecipe.getId(), e.getMessage());
				}
			}));
		}

		return 0;
	}
}
