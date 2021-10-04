package de.melanx.recipeprinter.renderers.vanilla;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelperMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.UpgradeRecipe;

import javax.annotation.Nullable;

public class SmithingRender implements IRecipeRender<UpgradeRecipe> {

    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/smithing.png");

    @Override
    public Class<UpgradeRecipe> getRecipeClass() {
        return UpgradeRecipe.class;
    }

    @Nullable
    @Override
    public RecipeType<? super UpgradeRecipe> getRecipeType() {
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
    public void render(UpgradeRecipe recipe, PoseStack poseStack, MultiBufferSource buffer) {
        RenderHelperMod.renderBackground(BACKGROUND_TEXTURE, poseStack, buffer, 15, 5, 140, 63);
        poseStack.translate(0, 0, 10);
        RenderHelperMod.renderSlot(poseStack, buffer, 61, 42);
        RenderHelperMod.renderIngredient(poseStack, buffer, recipe.base, 12, 42);
        RenderHelperMod.renderIngredient(poseStack, buffer, recipe.addition, 61, 42);
        RenderHelperMod.renderItem(poseStack, buffer, recipe.getResultItem(), 119, 42);
    }
}
