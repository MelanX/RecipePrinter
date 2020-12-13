package de.melanx.recipeprinter.renderers.botania;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelperMod;
import de.melanx.recipeprinter.util.Util;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import vazkii.botania.api.recipe.IRuneAltarRecipe;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.crafting.ModRecipeTypes;

import javax.annotation.Nullable;

public class RunicAltarRender implements IRecipeRender<IRuneAltarRecipe> {

    private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation("botania", "textures/gui/petal_overlay.png");

    @Override
    public Class<IRuneAltarRecipe> getRecipeClass() {
        return IRuneAltarRecipe.class;
    }

    @Nullable
    @Override
    public IRecipeType<? super IRuneAltarRecipe> getRecipeType() {
        return ModRecipeTypes.RUNE_TYPE;
    }

    @Override
    public int getRecipeWidth() {
        return 122;
    }

    @Override
    public int getRecipeHeight() {
        return 112;
    }

    @Override
    public void render(IRuneAltarRecipe recipe, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        RenderHelperMod.renderDefaultBackground(matrixStack, buffer, this.getRecipeWidth(), this.getRecipeHeight());
        RenderHelperMod.renderBackground(OVERLAY_TEXTURE, matrixStack, buffer, 19, 0, 108, 93);
        RenderHelperMod.renderItem(matrixStack, buffer, new ItemStack(ModBlocks.runeAltar), 47, 53);
        HUDHandler.renderManaBar(matrixStack, 10, 102, 0x0000FF, 0.75F, recipe.getManaUsage(), TilePool.MAX_MANA / 10);

        double angleBetweenEach = 360.0 / recipe.getIngredients().size();
        Vector2f point = new Vector2f(47, 21);
        Vector2f center = new Vector2f(47, 53);

        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            RenderHelperMod.renderIngredient(matrixStack, buffer, recipe.getIngredients().get(i), Math.round(point.x), Math.round(point.y));
            point = Util.rotatePointAbout(point, center, angleBetweenEach);
        }
        RenderHelperMod.renderItem(matrixStack, buffer, recipe.getRecipeOutput(), 85, 18);
    }
}
