package de.melanx.recipeprinter.renderers.vanilla;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelperMod;
import de.melanx.recipeprinter.util.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import org.jetbrains.annotations.Nullable;

public class SmithingTrimRender implements IRecipeRender<SmithingTrimRecipe> {
    @Override
    public Class<SmithingTrimRecipe> getRecipeClass() {
        return SmithingTrimRecipe.class;
    }

    @Nullable
    @Override
    public RecipeType<? super SmithingTrimRecipe> getRecipeType() {
        return RecipeType.SMITHING;
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
    public void render(SmithingTrimRecipe recipe, PoseStack poseStack, MultiBufferSource buffer) {
        RenderHelperMod.renderBackground(SmithingTransformRender.BACKGROUND_TEXTURE, poseStack, buffer, 15, 5, 140, 63, true);
        poseStack.translate(0, 0, 10);
        RenderHelperMod.renderSlot(poseStack, buffer, 61, 42);
        RenderHelperMod.renderIngredient(poseStack, buffer, recipe.base, 12, 42);
        RenderHelperMod.renderIngredient(poseStack, buffer, recipe.addition, 61, 42);
        RenderHelperMod.renderItem(poseStack, buffer, Util.getResultItem(recipe), 119, 42);
    }
}
