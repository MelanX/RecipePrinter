package de.melanx.recipeprinter.renderers.botania;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.recipe.IPureDaisyRecipe;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.crafting.ModRecipeTypes;

import javax.annotation.Nullable;

public class PureDaisyRender implements IRecipeRender<IPureDaisyRecipe> {

    public static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation("botania", "textures/gui/pure_daisy_overlay.png");

    @Override
    public Class<IPureDaisyRecipe> getRecipeClass() {
        return IPureDaisyRecipe.class;
    }

    @Nullable
    @Override
    public IRecipeType<? super IPureDaisyRecipe> getRecipeType() {
        return ModRecipeTypes.PURE_DAISY_TYPE;
    }

    @Override
    public int getRecipeWidth() {
        return 104;
    }

    @Override
    public int getRecipeHeight() {
        return 52;
    }

    @Override
    public void render(IPureDaisyRecipe recipe, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        RenderHelper.renderDefaultBackground(matrixStack, buffer, this.getRecipeWidth(), this.getRecipeHeight());
        matrixStack.push();
        matrixStack.translate(21, 5, 0);
        RenderHelper.renderBackground(OVERLAY_TEXTURE, matrixStack, buffer, 0, 0, 65, 44);
        matrixStack.pop();
        RenderHelper.renderItem(matrixStack, buffer, new ItemStack(ModSubtiles.pureDaisy), 44, 18);
        Block input = recipe.getInput().getDisplayed().get(0).getBlock();
        RenderHelper.renderFluidOrItem(matrixStack, buffer, input, 14, 18);
        Block output = recipe.getOutputState().getBlock();
        RenderHelper.renderFluidOrItem(matrixStack, buffer, output, 73, 18);
    }
}
