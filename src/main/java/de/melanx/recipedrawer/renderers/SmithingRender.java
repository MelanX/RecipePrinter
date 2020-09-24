package de.melanx.recipedrawer.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.recipedrawer.IRecipeRender;
import de.melanx.recipedrawer.util.RenderHelper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SmithingRecipe;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class SmithingRender implements IRecipeRender<SmithingRecipe> {

    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/smithing.png");

    @Override
    public Class<SmithingRecipe> getRecipeClass() {
        return SmithingRecipe.class;
    }

    @Nullable
    @Override
    public IRecipeType<? super SmithingRecipe> getRecipeType() {
        return IRecipeType.SMITHING;
    }

    @Override
    public int getRecipeWidth() {
        return 140;
    }

    @Override
    public int getRecipeHeight() {
        return 63;
    }

    @Override
    public void render(SmithingRecipe recipe, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        RenderHelper.renderBackground(BACKGROUND_TEXTURE, matrixStack, buffer, 15, 5, 140, 63);
        matrixStack.translate(0, 0, 10);
        RenderHelper.renderSlot(matrixStack, buffer, 61, 42);
        RenderHelper.renderIngredient(matrixStack, buffer, recipe.base, 12, 42);
        RenderHelper.renderIngredient(matrixStack, buffer, recipe.addition, 61, 42);
        RenderHelper.renderItem(matrixStack, buffer, recipe.getRecipeOutput(), 119, 42);
    }
}
