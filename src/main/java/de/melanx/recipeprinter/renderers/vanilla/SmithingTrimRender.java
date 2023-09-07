package de.melanx.recipeprinter.renderers.vanilla;

import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelperMod;
import de.melanx.recipeprinter.util.Util;
import net.minecraft.client.gui.GuiGraphics;
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
    public void render(SmithingTrimRecipe recipe, GuiGraphics guiGraphics) {
        RenderHelperMod.renderBackground(SmithingTransformRender.BACKGROUND_TEXTURE, guiGraphics, 15, 5, 140, 63, true);
        guiGraphics.pose().translate(0, 0, 10);
        RenderHelperMod.renderSlot(guiGraphics, 61, 42);
        RenderHelperMod.renderIngredient(guiGraphics, recipe.base, 12, 42);
        RenderHelperMod.renderIngredient(guiGraphics, recipe.addition, 61, 42);
        RenderHelperMod.renderItem(guiGraphics, Util.getResultItem(recipe), 119, 42);
    }
}
