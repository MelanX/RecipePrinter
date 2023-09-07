package de.melanx.recipeprinter.renderers.vanilla;

import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelperMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.BlastingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;

public class BlastingRender implements IRecipeRender<BlastingRecipe> {

    @Override
    public Class<BlastingRecipe> getRecipeClass() {
        return BlastingRecipe.class;
    }

    @Nullable
    @Override
    public RecipeType<? super BlastingRecipe> getRecipeType() {
        return RecipeType.BLASTING;
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
    public void render(BlastingRecipe recipe, GuiGraphics guiGraphics) {
        SmeltingRender.commonRender(recipe, guiGraphics);
        RenderHelperMod.renderItem(guiGraphics, new ItemStack(Items.BLAST_FURNACE), 5, 41);
    }
}
