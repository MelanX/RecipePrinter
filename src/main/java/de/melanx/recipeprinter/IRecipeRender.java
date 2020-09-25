package de.melanx.recipeprinter;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

import javax.annotation.Nullable;

public interface IRecipeRender<T extends IRecipe<?>> {

    Class<T> getRecipeClass();

    @Nullable
    IRecipeType<? super T> getRecipeType();

    int getRecipeWidth();
    int getRecipeHeight();

    void render(T recipe, MatrixStack matrixStack, IRenderTypeBuffer buffer);

    default double getScaleFactor() {
        return 5;
    }
}
