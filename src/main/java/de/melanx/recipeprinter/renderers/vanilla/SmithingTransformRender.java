package de.melanx.recipeprinter.renderers.vanilla;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelperMod;
import de.melanx.recipeprinter.util.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.*;

import javax.annotation.Nullable;

public class SmithingTransformRender implements IRecipeRender<SmithingRecipe> {

    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/smithing.png");

    @Override
    public Class<SmithingRecipe> getRecipeClass() {
        return SmithingRecipe.class;
    }

    @Nullable
    @Override
    public RecipeType<? super SmithingRecipe> getRecipeType() {
        return RecipeType.SMITHING;
    }

    @Override
    public int getRecipeWidth() {
        return 116;
    }

    @Override
    public int getRecipeHeight() {
        return 65;
    }

    @Override
    public void render(SmithingRecipe recipe, PoseStack poseStack, MultiBufferSource buffer) {
        RenderHelperMod.renderBackground(BACKGROUND_TEXTURE, poseStack, buffer, 3, 4, 116, 65, true);
        poseStack.translate(0, 0, 10);
        RenderHelperMod.renderIngredient(poseStack, buffer, getTemplate(recipe), 5, 44);
        RenderHelperMod.renderIngredient(poseStack, buffer, getBase(recipe), 23, 44);
        RenderHelperMod.renderIngredient(poseStack, buffer, getAddition(recipe), 41, 44);
        RenderHelperMod.renderItem(poseStack, buffer, Util.getResultItem(recipe), 95, 44);
    }

    private static Ingredient getTemplate(SmithingRecipe recipe) {
        if (recipe instanceof SmithingTransformRecipe transformRecipe) {
            return transformRecipe.template;
        }

        if (recipe instanceof SmithingTrimRecipe trimRecipe) {
            return trimRecipe.template;
        }

        return Ingredient.EMPTY;
    }

    private static Ingredient getBase(SmithingRecipe recipe) {
        //noinspection removal
        if (recipe instanceof LegacyUpgradeRecipe legacyRecipe) {
            //noinspection removal
            return legacyRecipe.base;
        }

        if (recipe instanceof SmithingTransformRecipe transformRecipe) {
            return transformRecipe.base;
        }

        if (recipe instanceof SmithingTrimRecipe trimRecipe) {
            return trimRecipe.base;
        }

        return Ingredient.EMPTY;
    }

    private static Ingredient getAddition(SmithingRecipe recipe) {
        //noinspection removal
        if (recipe instanceof LegacyUpgradeRecipe legacyRecipe) {
            //noinspection removal
            return legacyRecipe.addition;
        }

        if (recipe instanceof SmithingTransformRecipe transformRecipe) {
            return transformRecipe.addition;
        }

        if (recipe instanceof SmithingTrimRecipe trimRecipe) {
            return trimRecipe.addition;
        }

        return Ingredient.EMPTY;
    }
}
