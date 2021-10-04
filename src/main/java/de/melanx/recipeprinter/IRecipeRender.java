package de.melanx.recipeprinter;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;

public interface IRecipeRender<T extends Recipe<?>> {

    Class<T> getRecipeClass();

    @Nullable
    RecipeType<? super T> getRecipeType();

    int getRecipeWidth();

    int getRecipeHeight();

    void render(T recipe, PoseStack matrixStack, MultiBufferSource buffer);

    default double getScaleFactor() {
        return Config.scale.get();
    }
}
