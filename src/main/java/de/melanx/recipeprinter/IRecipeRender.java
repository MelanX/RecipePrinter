package de.melanx.recipeprinter;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;

public interface IRecipeRender<T extends Recipe<?>> {

    Class<T> getRecipeClass();

    @Nullable
    RecipeType<? super T> getRecipeType();

    int getRecipeWidth();

    int getRecipeHeight();

    void render(T recipe, GuiGraphics guiGraphics);

    default double getScaleFactor() {
        return ModConfig.scale;
    }
}
