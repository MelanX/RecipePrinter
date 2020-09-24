package de.melanx.recipedrawer.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.recipedrawer.IRecipeRender;
import de.melanx.recipedrawer.RecipeDrawer;
import de.melanx.recipedrawer.util.RenderHelper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;

import javax.annotation.Nullable;
import java.util.List;

public class ShapedRender implements IRecipeRender<ShapedRecipe> {

    @Override
    public Class<ShapedRecipe> getRecipeClass() {
        return ShapedRecipe.class;
    }

    @Nullable
    @Override
    public IRecipeType<? super ShapedRecipe> getRecipeType() {
        return IRecipeType.CRAFTING;
    }

    @Override
    public int getRecipeWidth() {
        return 124;
    }

    @Override
    public int getRecipeHeight() {
        return 62;
    }

    @Override
    public int getProtectionAreaX() {
        return 29;
    }

    @Override
    public int getProtectionAreaY() {
        return 16;
    }

    @Override
    public int getProtectionAreaX2() {
        return 145;
    }

    @Override
    public int getProtectionAreaY2() {
        return 70;
    }

    @Override
    public void render(ShapedRecipe recipe, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        RenderHelper.renderBackground(ShapelessRender.BACKGROUND_TEXTURE, matrixStack, buffer, 25, 12, 124, 62);
        RenderHelper.renderItem(matrixStack, buffer, recipe.getRecipeOutput(), 99, 23);
        List<Ingredient> ingredients = recipe.getIngredients();
        for (int x = 0; x < recipe.getRecipeWidth(); x++) {
            for (int y = 0; y < recipe.getRecipeHeight(); y++) {
                int idx = x + (recipe.getRecipeWidth() * y);
                if (idx < ingredients.size()) {
                    RenderHelper.renderIngredient(matrixStack, buffer, ingredients.get(idx), 5 + (x * 18), 5 + (y * 18));
                }
            }
        }
    }
}
