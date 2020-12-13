package de.melanx.recipeprinter.renderers.botania;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelperMod;
import net.minecraft.block.BlockState;
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
        RenderHelperMod.renderDefaultBackground(matrixStack, buffer, this.getRecipeWidth(), this.getRecipeHeight());
        matrixStack.push();
        matrixStack.translate(21, 5, 0);
        RenderHelperMod.renderBackground(OVERLAY_TEXTURE, matrixStack, buffer, 0, 0, 65, 44);
        matrixStack.pop();
        RenderHelperMod.renderItem(matrixStack, buffer, new ItemStack(ModSubtiles.pureDaisy), 44, 18);

        BlockState input = recipe.getInput().getDisplayed().get(0);
        RenderHelperMod.renderSlot(matrixStack, buffer, 14, 18);
        RenderHelperMod.renderBlockState(matrixStack, buffer, input, 14, 18);

        BlockState output = recipe.getOutputState();
        RenderHelperMod.renderSlot(matrixStack, buffer, 73, 18);
        RenderHelperMod.renderBlockState(matrixStack, buffer, output, 73, 18);
    }
}
