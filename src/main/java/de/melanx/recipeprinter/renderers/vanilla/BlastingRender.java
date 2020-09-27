package de.melanx.recipeprinter.renderers.vanilla;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.BlastingRecipe;
import net.minecraft.item.crafting.IRecipeType;

import javax.annotation.Nullable;

public class BlastingRender implements IRecipeRender<BlastingRecipe> {

    @Override
    public Class<BlastingRecipe> getRecipeClass() {
        return BlastingRecipe.class;
    }

    @Nullable
    @Override
    public IRecipeType<? super BlastingRecipe> getRecipeType() {
        return IRecipeType.BLASTING;
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
    public void render(BlastingRecipe recipe, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        SmeltingRender.render(recipe, matrixStack, buffer);
        RenderHelper.renderItem(matrixStack, buffer, new ItemStack(Items.BLAST_FURNACE), 5, 41);
    }
}
