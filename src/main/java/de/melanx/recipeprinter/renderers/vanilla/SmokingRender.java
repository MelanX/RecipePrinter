package de.melanx.recipeprinter.renderers.vanilla;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelperMod;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.SmokingRecipe;

import javax.annotation.Nullable;

public class SmokingRender implements IRecipeRender<SmokingRecipe> {

    @Override
    public Class<SmokingRecipe> getRecipeClass() {
        return SmokingRecipe.class;
    }

    @Nullable
    @Override
    public IRecipeType<? super SmokingRecipe> getRecipeType() {
        return IRecipeType.SMOKING;
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
    public void render(SmokingRecipe recipe, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        SmeltingRender.render(recipe, matrixStack, buffer);
        RenderHelperMod.renderItem(matrixStack, buffer, new ItemStack(Items.SMOKER), 5, 41);
    }
}
