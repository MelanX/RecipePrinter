package de.melanx.recipeprinter.renderers.vanilla;

import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelperMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;

public class CampfireRender implements IRecipeRender<CampfireCookingRecipe> {

    @Override
    public Class<CampfireCookingRecipe> getRecipeClass() {
        return CampfireCookingRecipe.class;
    }

    @Nullable
    @Override
    public RecipeType<? super CampfireCookingRecipe> getRecipeType() {
        return RecipeType.CAMPFIRE_COOKING;
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
    public void render(CampfireCookingRecipe recipe, GuiGraphics guiGraphics) {
        SmeltingRender.commonRender(recipe, guiGraphics);
        RenderHelperMod.renderItem(guiGraphics, new ItemStack(Items.CAMPFIRE), 5, 41);
    }
}
