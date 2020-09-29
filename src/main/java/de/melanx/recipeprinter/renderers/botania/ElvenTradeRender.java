package de.melanx.recipeprinter.renderers.botania;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.recipe.IElvenTradeRecipe;
import vazkii.botania.common.crafting.ModRecipeTypes;

import javax.annotation.Nullable;

public class ElvenTradeRender implements IRecipeRender<IElvenTradeRecipe> {

    private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation("botania", "textures/gui/elven_trade_overlay.png");
    private static final ResourceLocation PORTAL_TEXTURE = new ResourceLocation("botania", "textures/block/alfheim_portal_swirl.png");

    @Override
    public Class<IElvenTradeRecipe> getRecipeClass() {
        return IElvenTradeRecipe.class;
    }

    @Nullable
    @Override
    public IRecipeType<? super IElvenTradeRecipe> getRecipeType() {
        return ModRecipeTypes.ELVEN_TRADE_TYPE;
    }

    @Override
    public int getRecipeWidth() {
        return 153;
    }

    @Override
    public int getRecipeHeight() {
        return 103;
    }

    @Override
    public void render(IElvenTradeRecipe recipe, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        RenderHelper.renderDefaultBackground(matrixStack, buffer, this.getRecipeWidth(), this.getRecipeHeight());
        RenderHelper.renderBackground(OVERLAY_TEXTURE, matrixStack, buffer, 0, 7, 91, 87);

        matrixStack.push();
        matrixStack.translate(22, 29, 0);
        RenderHelper.renderBackground(PORTAL_TEXTURE, matrixStack, buffer, 0, 0, 48, 48, 48, 768);
        matrixStack.pop();

        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            Ingredient ingredient = recipe.getIngredients().get(i);
            RenderHelper.renderSlot(matrixStack, buffer, 43 + (18 * i), 5);
            RenderHelper.renderIngredient(matrixStack, buffer, ingredient, 43 + (18 * i), 5);
        }
        for (int i = 0; i < recipe.getOutputs().size(); i++) {
            RenderHelper.renderSlot(matrixStack, buffer, 93 + (18 * i), 46);
            RenderHelper.renderItem(matrixStack, buffer, recipe.getOutputs().get(i), 93 + (18 * i), 46);
        }
    }
}
