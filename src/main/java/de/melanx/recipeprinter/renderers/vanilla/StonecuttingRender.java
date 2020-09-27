package de.melanx.recipeprinter.renderers.vanilla;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.OverlayIcon;
import de.melanx.recipeprinter.util.RenderHelper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.StonecuttingRecipe;

import javax.annotation.Nullable;

public class StonecuttingRender implements IRecipeRender<StonecuttingRecipe> {

    @Override
    public Class<StonecuttingRecipe> getRecipeClass() {
        return StonecuttingRecipe.class;
    }

    @Nullable
    @Override
    public IRecipeType<? super StonecuttingRecipe> getRecipeType() {
        return IRecipeType.STONECUTTING;
    }

    @Override
    public int getRecipeWidth() {
        return 70;
    }

    @Override
    public int getRecipeHeight() {
        return 26;
    }

    @Override
    public void render(StonecuttingRecipe recipe, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        RenderHelper.renderDefaultBackground(matrixStack, buffer, 70, 26);
        RenderHelper.renderSlot(matrixStack, buffer, 5, 5);
        RenderHelper.renderIngredient(matrixStack, buffer, recipe.getIngredients().get(0), 5, 5);
        RenderHelper.renderSlot(matrixStack, buffer, 49, 5);
        RenderHelper.renderItem(matrixStack, buffer, recipe.getRecipeOutput(), 49, 5);
        RenderHelper.render(OverlayIcon.ARROW, matrixStack, buffer, 24, 5);
        matrixStack.push();
        matrixStack.translate(30, 8,0);
        matrixStack.scale(10f/16f, 10f/16f, 10f/16f);
        RenderHelper.renderItem(matrixStack, buffer, new ItemStack(Items.STONECUTTER), 0, 0);
        matrixStack.pop();
    }
}
