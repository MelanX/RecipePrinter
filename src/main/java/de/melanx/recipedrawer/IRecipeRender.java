package de.melanx.recipedrawer;

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
    int getProtectionAreaX();
    int getProtectionAreaY();
    int getProtectionAreaX2();
    int getProtectionAreaY2();

    void render(T recipe, MatrixStack matrixStack, IRenderTypeBuffer buffer);

    default boolean isProtected(int x, int y) {
        return (x >= this.getProtectionAreaX() && x <= this.getProtectionAreaX2()) || (y >= this.getProtectionAreaY() && y <= this.getProtectionAreaY2());
    }

    default double getScaleFactor() {
        return 5;
    }
}
