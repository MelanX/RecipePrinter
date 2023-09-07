package de.melanx.recipeprinter.renderers.vanilla;

import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelperMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmokingRecipe;

import javax.annotation.Nullable;

public class SmokingRender implements IRecipeRender<SmokingRecipe> {

    @Override
    public Class<SmokingRecipe> getRecipeClass() {
        return SmokingRecipe.class;
    }

    @Nullable
    @Override
    public RecipeType<? super SmokingRecipe> getRecipeType() {
        return RecipeType.SMOKING;
    }

    @Override
    public int getRecipeWidth() {
        return 90;
    }

    @Override
    public int getRecipeHeight() {
        return 62;
    }

    @Override
    public void render(SmokingRecipe recipe, GuiGraphics guiGraphics) {
        SmeltingRender.commonRender(recipe, guiGraphics);
        RenderHelperMod.renderItem(guiGraphics, new ItemStack(Items.SMOKER), 5, 41);
    }
}
