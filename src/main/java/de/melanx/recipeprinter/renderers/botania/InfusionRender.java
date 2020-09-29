package de.melanx.recipeprinter.renderers.botania;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelper;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.recipe.IManaInfusionRecipe;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.ModRecipeTypes;

import javax.annotation.Nullable;

public class InfusionRender implements IRecipeRender<IManaInfusionRecipe> {

    public static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation("botania", "textures/gui/pure_daisy_overlay.png");
    private final ItemStack pool = new ItemStack(ModBlocks.manaPool);

    public InfusionRender() {
        ItemNBTHelper.setBoolean(pool, "RenderFull", true);
    }

    @Override
    public Class<IManaInfusionRecipe> getRecipeClass() {
        return IManaInfusionRecipe.class;
    }

    @Nullable
    @Override
    public IRecipeType<? super IManaInfusionRecipe> getRecipeType() {
        return ModRecipeTypes.MANA_INFUSION_TYPE;
    }

    @Override
    public int getRecipeWidth() {
        return 150;
    }

    @Override
    public int getRecipeHeight() {
        return 63;
    }

    @Override
    public void render(IManaInfusionRecipe recipe, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        RenderHelper.renderDefaultBackground(matrixStack, buffer, this.getRecipeWidth(), this.getRecipeHeight());
        matrixStack.push();
        matrixStack.translate(44, 4, 0);
        RenderHelper.renderBackground(OVERLAY_TEXTURE, matrixStack, buffer, 0, 0, 65, 44);
        matrixStack.pop();
        HUDHandler.renderManaBar(matrixStack, 24, 54, 0x0000FF, 0.75F, recipe.getManaToConsume(), TilePool.MAX_MANA / 10);
        RenderHelper.renderItem(matrixStack, buffer, pool, 67, 17);

        RenderHelper.renderSlot(matrixStack, buffer, 37, 17);
        RenderHelper.renderIngredient(matrixStack, buffer, recipe.getIngredients().get(0), 37, 17);

        RenderHelper.renderSlot(matrixStack, buffer, 96, 17);
        RenderHelper.renderItem(matrixStack, buffer, recipe.getRecipeOutput(), 96, 17);
    }
}
