package de.melanx.recipeprinter.renderers.vanilla;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.OverlayIcon;
import de.melanx.recipeprinter.util.RenderHelperMod;
import de.melanx.recipeprinter.util.Util;
import net.minecraft.client.gui.GuiGraphics;
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
    public void render(StonecutterRecipe recipe, GuiGraphics guiGraphics) {
        PoseStack poseStack = guiGraphics.pose();
        RenderHelperMod.renderDefaultBackground(guiGraphics, 70, 26);
        RenderHelperMod.renderSlot(guiGraphics, 5, 5);
        RenderHelperMod.renderIngredient(guiGraphics, recipe.getIngredients().get(0), 5, 5);
        RenderHelperMod.renderSlot(guiGraphics, 49, 5);
        RenderHelperMod.renderItem(guiGraphics, Util.getResultItem(recipe), 49, 5);
        RenderHelperMod.render(OverlayIcon.ARROW, guiGraphics, 24, 5);
        poseStack.pushPose();
        poseStack.translate(30, 8, 0);
        poseStack.scale(10f / 16f, 10f / 16f, 10f / 16f);
        RenderHelperMod.renderItem(guiGraphics, new ItemStack(Items.STONECUTTER), 0, 0);
        poseStack.popPose();
    }
}
