package de.melanx.recipeprinter.renderers.vanilla;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.OverlayIcon;
import de.melanx.recipeprinter.util.RenderHelperMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.StonecutterRecipe;

import javax.annotation.Nullable;

public class StonecuttingRender implements IRecipeRender<StonecutterRecipe> {

    @Override
    public Class<StonecutterRecipe> getRecipeClass() {
        return StonecutterRecipe.class;
    }

    @Nullable
    @Override
    public RecipeType<? super StonecutterRecipe> getRecipeType() {
        return RecipeType.STONECUTTING;
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
    public void render(StonecutterRecipe recipe, PoseStack poseStack, MultiBufferSource buffer) {
        RenderHelperMod.renderDefaultBackground(poseStack, buffer, 70, 26);
        RenderHelperMod.renderSlot(poseStack, buffer, 5, 5);
        RenderHelperMod.renderIngredient(poseStack, buffer, recipe.getIngredients().get(0), 5, 5);
        RenderHelperMod.renderSlot(poseStack, buffer, 49, 5);
        RenderHelperMod.renderItem(poseStack, buffer, recipe.getResultItem(), 49, 5);
        RenderHelperMod.render(OverlayIcon.ARROW, poseStack, buffer, 24, 5);
        poseStack.pushPose();
        poseStack.translate(30, 8, 0);
        poseStack.scale(10f / 16f, 10f / 16f, 10f / 16f);
        RenderHelperMod.renderItem(poseStack, buffer, new ItemStack(Items.STONECUTTER), 0, 0);
        poseStack.popPose();
    }
}
