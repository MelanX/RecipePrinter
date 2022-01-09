package de.melanx.recipeprinter.renderers.vanilla;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelperMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;

import javax.annotation.Nullable;
import java.util.List;

public class ShapedRender implements IRecipeRender<ShapedRecipe> {

    @Override
    public Class<ShapedRecipe> getRecipeClass() {
        return ShapedRecipe.class;
    }

    @Nullable
    @Override
    public RecipeType<? super ShapedRecipe> getRecipeType() {
        return RecipeType.CRAFTING;
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
    public void render(ShapedRecipe recipe, PoseStack poseStack, MultiBufferSource buffer) {
        RenderHelperMod.renderBackground(ShapelessRender.BACKGROUND_TEXTURE, poseStack, buffer, 25, 12, 124, 62, true);
        RenderHelperMod.renderItem(poseStack, buffer, recipe.getResultItem(), 99, 23);
        List<Ingredient> ingredients = recipe.getIngredients();
        for (int x = 0; x < recipe.getRecipeWidth(); x++) {
            for (int y = 0; y < recipe.getRecipeHeight(); y++) {
                int idx = x + (recipe.getRecipeWidth() * y);
                if (idx < ingredients.size()) {
                    RenderHelperMod.renderIngredient(poseStack, buffer, ingredients.get(idx), 5 + (x * 18), 5 + (y * 18));
                }
            }
        }
    }
}
