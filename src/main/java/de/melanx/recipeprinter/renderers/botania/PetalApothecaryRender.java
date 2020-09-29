package de.melanx.recipeprinter.renderers.botania;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelper;
import de.melanx.recipeprinter.util.Util;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import vazkii.botania.api.recipe.IPetalRecipe;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.crafting.ModRecipeTypes;

import javax.annotation.Nullable;

public class PetalApothecaryRender implements IRecipeRender<IPetalRecipe> {

    private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation("botania", "textures/gui/petal_overlay.png");

    @Override
    public Class<IPetalRecipe> getRecipeClass() {
        return IPetalRecipe.class;
    }

    @Nullable
    @Override
    public IRecipeType<? super IPetalRecipe> getRecipeType() {
        return ModRecipeTypes.PETAL_TYPE;
    }

    @Override
    public int getRecipeWidth() {
        return 122;
    }

    @Override
    public int getRecipeHeight() {
        return 105;
    }

    @Override
    public void render(IPetalRecipe recipe, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        RenderHelper.renderDefaultBackground(matrixStack, buffer, this.getRecipeWidth(), this.getRecipeHeight());
        RenderHelper.renderBackground(OVERLAY_TEXTURE, matrixStack, buffer, 19, 0, 108, 93);
        RenderHelper.renderItem(matrixStack, buffer, new ItemStack(ModBlocks.defaultAltar), 47, 53);

        double angleBetweenEach = 360.0 / recipe.getIngredients().size();
        Vector2f point = new Vector2f(47, 21);
        Vector2f center = new Vector2f(47, 53);

        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            RenderHelper.renderIngredient(matrixStack, buffer, recipe.getIngredients().get(i), Math.round(point.x), Math.round(point.y));
            point = Util.rotatePointAbout(point, center, angleBetweenEach);
        }
        RenderHelper.renderItem(matrixStack, buffer, recipe.getRecipeOutput(), 85, 18);
    }
}
