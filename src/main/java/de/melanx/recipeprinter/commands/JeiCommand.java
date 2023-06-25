package de.melanx.recipeprinter.commands;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import de.melanx.recipeprinter.ModConfig;
import de.melanx.recipeprinter.RecipePrinter;
import de.melanx.recipeprinter.jei.PrinterJEI;
import de.melanx.recipeprinter.util.PrinterJob;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IIngredientVisibility;
import mezz.jei.common.Internal;
import mezz.jei.library.focus.FocusGroup;
import mezz.jei.library.gui.recipes.RecipeLayout;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import org.moddingx.libx.render.target.ImageHelper;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import static de.melanx.recipeprinter.jei.PrinterJEI.REG;

public class JeiCommand implements Command<CommandSourceStack> {

    private static final IIngredientVisibility INGREDIENT_VISIBILITY = new IIngredientVisibility() {

        @Override
        public <V> boolean isIngredientVisible(@Nonnull IIngredientType<V> ingredientType, @Nonnull V ingredient) {
            return true;
        }

        @Override
        public <V> boolean isIngredientVisible(@Nonnull ITypedIngredient<V> typedIngredient) {
            return true;
        }

        @Override
        public void registerListener(@Nonnull IListener listener) {
            // NO-OP
        }
    };

    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        if (PrinterJEI.REG == null)
            return 0;
        RecipeSelector sel = context.getArgument("recipes", RecipeSelector.class);
        //noinspection ConstantConditions
        RecipeManager rm = Minecraft.getInstance().getSingleplayerServer().getRecipeManager();
        List<ResourceLocation> recipes = sel.getRecipes(rm);
        List<IRecipeCategory<?>> categories = REG.getRecipeCategories();

        AtomicInteger i = new AtomicInteger();
        AtomicInteger matches = new AtomicInteger();
        for (ResourceLocation rl : recipes) {
            rm.byKey(rl).ifPresent(iRecipe -> categories.stream().filter(iRecipeCategory -> iRecipeCategory.getRecipeType().getRecipeClass().isAssignableFrom(iRecipe.getClass())).forEach(recipeCategory -> {
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

                Optional<IRecipeLayoutDrawable<Recipe<?>>> optLayout;
                try {
                    //noinspection unchecked
                    optLayout = RecipeLayout.create((IRecipeCategory<Recipe<?>>) recipeCategory, iRecipe, FocusGroup.EMPTY, REG.getJeiHelpers().getIngredientManager(), INGREDIENT_VISIBILITY, REG.getJeiHelpers().getModIdHelper(), Internal.getTextures());
                    optLayout.ifPresent(layout -> {
                        CompletableFuture<NativeImage> img = ImageHelper.render(new PrinterJob(recipeCategory.getBackground().getWidth() + 8, recipeCategory.getBackground().getHeight() + 8, ModConfig.scale,
                                (poseStack, buffer) -> {
                                    RecipePrinter.getInstance().logger.debug("Printing {} {} {}%", recipeCategory.getRecipeType().getUid(), iRecipe.getId(), Mth.floor(100. * i.getAndIncrement() / matches.get()));
                                    poseStack.translate(4, 4, 0);
                                    layout.drawRecipe(poseStack, -10, -10);
                                }));
                        try {
                            img.get().writeToFile(path);
                        } catch (InterruptedException | IOException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (Exception e) {
                    RecipePrinter.getInstance().logger.error("Could not print recipe {}: {}", iRecipe.getId(), e.getMessage());
                }
            }));

            context.getSource().sendSuccess(Component.literal("Successfully printed " + rl), false);
        }

        return 0;
    }
}
