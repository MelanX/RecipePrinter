package de.melanx.recipedrawer.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.recipedrawer.IRecipeRender;
import de.melanx.recipedrawer.RecipeDrawer;
import de.melanx.recipedrawer.util.OverlayIcon;
import de.melanx.recipedrawer.util.RenderHelper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

public class ShapelessRender implements IRecipeRender<ShapelessRecipe> {

    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(RecipeDrawer.MODID, "textures/gui/crafting_gui.png");

    @Override
    public Class<ShapelessRecipe> getRecipeClass() {
        return ShapelessRecipe.class;
    }

    @Nullable
    @Override
    public IRecipeType<? super ShapelessRecipe> getRecipeType() {
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
    public void render(ShapelessRecipe recipe, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        RenderHelper.renderDefaultBackground(matrixStack, buffer, 124, 62);
        RenderHelper.renderBackground(BACKGROUND_TEXTURE, matrixStack, buffer, 0, 0, 124, 62);
        matrixStack.push();
        matrixStack.translate(108, 4, 0);
        matrixStack.scale(1f / 3, 1f / 3, 1);
        RenderHelper.render(OverlayIcon.SHAPELESS, matrixStack, buffer, 0, 0);
        matrixStack.pop();
        RenderHelper.renderItem(matrixStack, buffer, recipe.getRecipeOutput(), 99, 23);
        List<Ingredient> ingredients = recipe.getIngredients();
        int max = ingredients.size() > 4 ? 3 : 2;
        for (int x = 0; x < max; x++) {
            for (int y = 0; y < max; y++) {
                int idx = x + (max * y);
                if (idx < ingredients.size()) {
                    RenderHelper.renderIngredient(matrixStack, buffer, ingredients.get(idx), 5 + (x * 18), 5 + (y * 18));
                }
            }
        }
    }
}
