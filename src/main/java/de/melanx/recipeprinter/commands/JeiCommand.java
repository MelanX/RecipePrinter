package de.melanx.recipeprinter.commands;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import de.melanx.recipeprinter.util.PrinterJob;
import de.melanx.recipeprinter.util.RenderHelperMod;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IIngredientVisibility;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.moddingx.libx.render.target.ImageHelper;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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
        CompletableFuture<NativeImage> render = ImageHelper.render(new PrinterJob(400, 400, 1, guiGraphics -> {
            RenderHelperMod.renderItem(guiGraphics, new ItemStack(Items.DIAMOND_BLOCK), 20, 20);
        }));

        try {
            render.get().writeToFile(Paths.get("Test.png"));
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
//        if (PrinterJEI.REG == null)
//            return 0;
//        RecipeSelector sel = context.getArgument("recipes", RecipeSelector.class);
//        //noinspection ConstantConditions
//        RecipeManager rm = Minecraft.getInstance().getSingleplayerServer().getRecipeManager();
//        List<ResourceLocation> recipes = sel.getRecipes(rm);
//        List<IRecipeCategory<?>> categories = REG.getRecipeCategories();
//
//        AtomicInteger i = new AtomicInteger();
//        AtomicInteger matches = new AtomicInteger();
//        for (ResourceLocation rl : recipes) {
//            rm.byKey(rl).ifPresent(iRecipe -> categories.stream().filter(iRecipeCategory -> iRecipeCategory.getRecipeType().getRecipeClass().isAssignableFrom(iRecipe.getClass())).forEach(recipeCategory -> {
//                matches.getAndIncrement();
//                Path path = context.getSource().getServer().getServerDirectory().toPath()
//                        .resolve(RecipePrinter.getInstance().modid)
//                        .resolve("jei")
//                        .resolve(rl.getNamespace())
//                        .resolve(recipeCategory.getClass().getSimpleName())
//                        // .resolve(rl.toString().replaceAll("/|:", Matcher.quoteReplacement(String.valueOf(File.separatorChar))))
//                        .resolve(rl.getPath().replaceAll("([^/]*/)*", "") + ".png");
//                if (!Files.exists(path.getParent())) {
//                    try {
//                        Files.createDirectories(path.getParent());
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
//
//                Optional<IRecipeLayoutDrawable<Recipe<?>>> optLayout;
//                try {
//                    //noinspection unchecked
//                    optLayout = RecipeLayout.create((IRecipeCategory<Recipe<?>>) recipeCategory, Collections.emptyList(), iRecipe, FocusGroup.EMPTY, REG.getJeiHelpers().getIngredientManager(), INGREDIENT_VISIBILITY, REG.getJeiHelpers().getModIdHelper(), Internal.getTextures());
//                    optLayout.ifPresent(layout -> {
//                        CompletableFuture<NativeImage> img = ImageHelper.render(new PrinterJob(recipeCategory.getBackground().getWidth() + 8, recipeCategory.getBackground().getHeight() + 8, ModConfig.scale,
//                                guiGraphics -> {
//                                    RecipePrinter.getInstance().logger.debug("Printing {} {} {}%", recipeCategory.getRecipeType().getUid(), iRecipe.getId(), Mth.floor(100. * i.getAndIncrement() / matches.get()));
//                                    guiGraphics.pose().translate(4, 4, 0);
//                                    layout.drawRecipe(guiGraphics, -10, -10);
//                                }));
//                        try {
//                            img.get().writeToFile(path);
//                        } catch (InterruptedException | IOException | ExecutionException e) {
//                            throw new RuntimeException(e);
//                        }
//                    });
//                } catch (Exception e) {
//                    RecipePrinter.getInstance().logger.error("Could not print recipe {}: {}", iRecipe.getId(), e.getMessage());
//                }
//            }));
//
//            context.getSource().sendSuccess(() -> Component.literal("Successfully printed " + rl), false);
//        }

        return 0;
    }
}
