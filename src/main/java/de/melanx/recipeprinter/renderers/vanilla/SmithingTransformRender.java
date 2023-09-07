package de.melanx.recipeprinter.renderers.vanilla;

import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.RecipePrinter;
import de.melanx.recipeprinter.util.RenderHelperMod;
import de.melanx.recipeprinter.util.Util;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;

import javax.annotation.Nullable;

public class SmithingTransformRender implements IRecipeRender<SmithingRecipe> {

    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/smithing.png");

    @Override
    public Class<SmithingRecipe> getRecipeClass() {
        return SmithingRecipe.class;
    }

    @Nullable
    @Override
    public RecipeType<? super SmithingRecipe> getRecipeType() {
        return RecipeType.SMITHING;
    }

    @Override
    public int getRecipeWidth() {
        return 116;
    }

    @Override
    public int getRecipeHeight() {
        return 65;
    }

    @Override
    public void render(SmithingRecipe recipe, GuiGraphics guiGraphics) {
        RenderHelperMod.renderBackground(BACKGROUND_TEXTURE, guiGraphics, 3, 4, 116, 65, true);
        guiGraphics.pose().translate(0, 0, 10);
        RenderHelperMod.renderIngredient(guiGraphics, getTemplate(recipe), 5, 44);
        RenderHelperMod.renderIngredient(guiGraphics, getBase(recipe), 23, 44);
        RenderHelperMod.renderIngredient(guiGraphics, getAddition(recipe), 41, 44);
        RenderHelperMod.renderItem(guiGraphics, Util.getResultItem(recipe), 95, 44);
    }

    private static Ingredient getTemplate(SmithingRecipe recipe) {
        if (recipe instanceof SmithingTransformRecipe transformRecipe) {
            return transformRecipe.template;
        }

        if (recipe instanceof SmithingTrimRecipe trimRecipe) {
            return trimRecipe.template;
        }

        return Ingredient.EMPTY;
    }

    private static Ingredient getBase(SmithingRecipe recipe) {
        if (recipe instanceof SmithingTransformRecipe transformRecipe) {
            return transformRecipe.base;
        }

        if (recipe instanceof SmithingTrimRecipe trimRecipe) {
            return trimRecipe.base;
        }

        RecipePrinter.getInstance().logger.warn("Unknown smithing recipe: " + recipe.getClass().getCanonicalName());
        return Ingredient.EMPTY;
    }

    private static Ingredient getAddition(SmithingRecipe recipe) {
        if (recipe instanceof SmithingTransformRecipe transformRecipe) {
            return transformRecipe.addition;
        }

        if (recipe instanceof SmithingTrimRecipe trimRecipe) {
            return trimRecipe.addition;
        }

        RecipePrinter.getInstance().logger.warn("Unknown smithing recipe: " + recipe.getClass().getCanonicalName());
        return Ingredient.EMPTY;
    }
}
