package de.melanx.recipeprinter.renderers.botania;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.OverlayIcon;
import de.melanx.recipeprinter.util.RenderHelperMod;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import vazkii.botania.api.recipe.IBrewRecipe;
import vazkii.botania.common.crafting.ModRecipeTypes;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nullable;

public class BrewRender implements IRecipeRender<IBrewRecipe> {

    private static final ItemStack VIAL = new ItemStack(ModItems.vial);

    @Override
    public Class<IBrewRecipe> getRecipeClass() {
        return IBrewRecipe.class;
    }

    @Nullable
    @Override
    public IRecipeType<? super IBrewRecipe> getRecipeType() {
        return ModRecipeTypes.BREW_TYPE;
    }

    @Override
    public int getRecipeWidth() {
        return 139;
    }

    @Override
    public int getRecipeHeight() {
        return 63;
    }

    @Override
    public void render(IBrewRecipe recipe, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        RenderHelperMod.renderDefaultBackground(matrixStack, buffer, this.getRecipeWidth(), this.getRecipeHeight());
        RenderHelperMod.renderSlot(matrixStack, buffer, 15, 40);
        RenderHelperMod.renderItem(matrixStack, buffer, VIAL, 15, 40);

        RenderHelperMod.renderSlot(matrixStack, buffer, 63, 40);
        RenderHelperMod.renderItem(matrixStack, buffer, recipe.getOutput(VIAL), 63, 40);

        int posX = 72 - (recipe.getIngredients().size() * 9);
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            RenderHelperMod.renderSlot(matrixStack, buffer, posX, 5);
            RenderHelperMod.renderIngredient(matrixStack, buffer, recipe.getIngredients().get(i), posX, 5);
            posX += 18;
        }

        RenderHelperMod.render(OverlayIcon.ARROW, matrixStack, buffer, 36, 40);
        RenderHelperMod.render(OverlayIcon.ARROW_DOWN, matrixStack, buffer, 63, 23);
    }
}
