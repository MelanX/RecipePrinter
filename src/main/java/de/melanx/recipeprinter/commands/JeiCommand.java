package de.melanx.recipeprinter.commands;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import de.melanx.recipeprinter.ModConfig;
import de.melanx.recipeprinter.RecipePrinter;
import de.melanx.recipeprinter.jei.PrinterJEI;
import de.melanx.recipeprinter.util.ImageHelper;
import mezz.jei.Internal;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.gui.recipes.RecipeLayout;
import mezz.jei.recipes.FocusGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static de.melanx.recipeprinter.jei.PrinterJEI.REG;

public class JeiCommand implements Command<CommandSourceStack> {
	@Override
	public int run(CommandContext<CommandSourceStack> context) {
		if (PrinterJEI.REG == null)
			return 0;
		RecipeSelector sel = context.getArgument("recipes", RecipeSelector.class);
		//noinspection ConstantConditions
		RecipeManager rm = Minecraft.getInstance().getSingleplayerServer().getRecipeManager();
		List<ResourceLocation> recipes = sel.getRecipes(rm);
		ImmutableList<IRecipeCategory<?>> categories = REG.getRecipeCategories();

		AtomicInteger i = new AtomicInteger();
		AtomicInteger matches = new AtomicInteger();
		for (ResourceLocation rl : recipes) {
			rm.byKey(rl).ifPresent(iRecipe -> categories.stream().filter(iRecipeCategory -> iRecipeCategory.getRecipeClass().isAssignableFrom(iRecipe.getClass())).forEach(recipeCategory -> {
				matches.getAndIncrement();
				Path path = context.getSource().getServer().getServerDirectory().toPath()
						.resolve(RecipePrinter.getInstance().modid)
						.resolve("jei")
						.resolve(rl.getNamespace())
						.resolve(recipeCategory.getClass().getSimpleName())
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
					//noinspection unchecked
					layout = RecipeLayout.create(-1, (IRecipeCategory<Recipe<?>>) recipeCategory, iRecipe, FocusGroup.EMPTY, Internal.getRegisteredIngredients(), REG.getJeiHelpers().getModIdHelper(), 0, 0);
					if (layout != null) {
						ImageHelper.addRenderJob(recipeCategory.getBackground().getWidth() + 8, recipeCategory.getBackground().getHeight() + 8, ModConfig.scale * 2., (poseStack, buffer) -> {
							RecipePrinter.getInstance().logger.debug("Printing {} {} {}%", recipeCategory.getUid(), iRecipe.getId(), Mth.floor(100. * i.getAndIncrement() / matches.get()));
							poseStack.translate(4, 4, 0);
							layout.drawRecipe(poseStack, -10, -10);
						}, path);
					}
				} catch (Exception e) {
					RecipePrinter.getInstance().logger.error("Could not print recipe {}: {}", iRecipe.getId(), e.getMessage());
				}
			}));

			context.getSource().sendSuccess(new TextComponent("Successfully printed " + rl), false);
		}


		return 0;
	}
}
