package de.melanx.recipeprinter.renderers.vanilla;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelperMod;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.CampfireCookingRecipe;
import net.minecraft.item.crafting.IRecipeType;

import javax.annotation.Nullable;

public class CampfireRender implements IRecipeRender<CampfireCookingRecipe> {

    @Override
    public Class<CampfireCookingRecipe> getRecipeClass() {
        return CampfireCookingRecipe.class;
    }

    @Nullable
    @Override
    public IRecipeType<? super CampfireCookingRecipe> getRecipeType() {
        return IRecipeType.CAMPFIRE_COOKING;
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
    public void render(CampfireCookingRecipe recipe, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        SmeltingRender.render(recipe, matrixStack, buffer);
        RenderHelperMod.renderItem(matrixStack, buffer, new ItemStack(Items.CAMPFIRE), 5, 41);
    }
}
