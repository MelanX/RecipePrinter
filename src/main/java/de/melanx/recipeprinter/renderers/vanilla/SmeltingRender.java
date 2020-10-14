package de.melanx.recipeprinter.renderers.vanilla;

import com.mojang.blaze3d.matrix.MatrixStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.RecipePrinter;
import de.melanx.recipeprinter.util.RenderHelperMod;
import io.github.noeppi_noeppi.libx.render.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SmeltingRender implements IRecipeRender<FurnaceRecipe> {

    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/furnace.png");

    @Override
    public Class<FurnaceRecipe> getRecipeClass() {
        return FurnaceRecipe.class;
    }

    @Nullable
    @Override
    public IRecipeType<? super FurnaceRecipe> getRecipeType() {
        return IRecipeType.SMELTING;
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
    public void render(FurnaceRecipe recipe, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        SmeltingRender.render((AbstractCookingRecipe) recipe, matrixStack, buffer);
        RenderHelperMod.renderItem(matrixStack, buffer, new ItemStack(Items.FURNACE), 5, 41);
    }

    public static void render(AbstractCookingRecipe recipe, MatrixStack matrixStack, IRenderTypeBuffer buffer) {
        RenderHelperMod.renderBackground(BACKGROUND_TEXTURE, matrixStack, buffer, 51, 12, 90, 62);
        matrixStack.push();
        matrixStack.translate(6, 25, 10);
        RenderHelperMod.renderBackground(BACKGROUND_TEXTURE, matrixStack, buffer, 176, 0, 14, 14);
        matrixStack.pop();
        RenderHelperMod.renderItem(matrixStack, buffer, recipe.getRecipeOutput(), 65, 23);
        RenderHelperMod.renderIngredient(matrixStack, buffer, recipe.getIngredients().get(0), 5, 5);
        TranslationTextComponent time = new TranslationTextComponent(RecipePrinter.getInstance().modid + ".time", BigDecimal.valueOf(recipe.getCookTime() / 20d).setScale(2, RoundingMode.HALF_UP).toPlainString());
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, time.getString(), 26, 48, RenderHelperMod.TEXT_COLOR);
        RenderHelper.resetColor();
    }
}
