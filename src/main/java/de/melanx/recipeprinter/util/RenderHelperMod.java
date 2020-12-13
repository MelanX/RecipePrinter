package de.melanx.recipeprinter.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.melanx.recipeprinter.RecipePrinter;
import io.github.noeppi_noeppi.libx.render.RenderHelper;
import io.github.noeppi_noeppi.libx.render.RenderHelperFluid;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;

public class RenderHelperMod {

    public static final int COLOR_GUI_BACKGROUND = 0xc6c6c6;
    public static final int TEXT_COLOR = Color.DARK_GRAY.getRGB();
    public static final ResourceLocation TEXTURE_ICONS = new ResourceLocation(RecipePrinter.getInstance().modid, "textures/gui/icons.png");

    public static void renderBackground(ResourceLocation texture, MatrixStack matrixStack, IRenderTypeBuffer buffer, int x, int y, int width, int height, int textureWidth, int textureHeight) {
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        AbstractGui.blit(matrixStack, 0, 0, x, y, width, height, textureWidth, textureHeight);
        matrixStack.translate(0, 0, 100);
    }

    public static void renderBackground(ResourceLocation texture, MatrixStack matrixStack, IRenderTypeBuffer buffer, int x, int y, int width, int height) {
        renderBackground(texture, matrixStack, buffer, x, y, width, height, 256, 256);
    }

    public static void renderDefaultBackground(MatrixStack matrixStack, IRenderTypeBuffer buffer, int width, int height) {
        Minecraft.getInstance().getTextureManager().bindTexture(RenderHelper.TEXTURE_WHITE);
        RenderHelper.color(COLOR_GUI_BACKGROUND);
        AbstractGui.blit(matrixStack, 0, 0, 0, 0, width, height, 256, 256);
        RenderHelper.resetColor();
        matrixStack.translate(0, 0, 100);
    }

    public static void render(OverlayIcon icon, MatrixStack matrixStack, IRenderTypeBuffer buffer, int x, int y) {
        render(icon, matrixStack, buffer, x, y, icon.width, icon.height);
    }

    public static void render(OverlayIcon icon, MatrixStack matrixStack, IRenderTypeBuffer buffer, int x, int y, int width, int height) {
        Minecraft.getInstance().getTextureManager().bindTexture(icon.texture);
        matrixStack.push();
        matrixStack.translate(x, y, 10);
        matrixStack.scale((float) width / icon.width, (float) height / icon.height, 1);
        AbstractGui.blit(matrixStack, 0, 0, icon.u, icon.v, icon.width, icon.height, 256, 256);
        matrixStack.pop();
    }

    public static void renderSlot(MatrixStack matrixStack, IRenderTypeBuffer buffer, int x, int y) {
        render(OverlayIcon.SLOT, matrixStack, buffer, x - 1, y - 1);
    }

    public static void renderBigSlot(MatrixStack matrixStack, IRenderTypeBuffer buffer, int x, int y) {
        render(OverlayIcon.BIG_SLOT, matrixStack, buffer, x - 5, y - 5);
    }

    public static void renderItem(MatrixStack matrixStack, IRenderTypeBuffer buffer, ItemStack stack, int x, int y) {
        matrixStack.push();
        matrixStack.translate(0, 0, 100);

        // As the item render method does not accept a matrix stack to apply transformations,
        // we need to do it manually.
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        RenderSystem.multMatrix(matrixStack.getLast().getMatrix());
        Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(stack, x, y);
        RenderSystem.popMatrix();

        matrixStack.translate(0, 0, 100);

        // Same as above
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        RenderSystem.multMatrix(matrixStack.getLast().getMatrix());
        Minecraft.getInstance().getItemRenderer().renderItemOverlayIntoGUI(Minecraft.getInstance().fontRenderer, stack, x, y, null);
        RenderSystem.popMatrix();

        RenderHelper.resetColor();
        matrixStack.pop();
    }

    public static void renderIngredient(MatrixStack matrixStack, IRenderTypeBuffer buffer, Ingredient ingredient, int x, int y) {
        if (!ingredient.hasNoMatchingItems()) {
            ItemStack[] stacks = ingredient.getMatchingStacks();
            if (stacks.length != 0) {
                renderItem(matrixStack, buffer, stacks[0], x, y);
            }
        }
    }

    public static void renderBlockState(MatrixStack matrixStack, IRenderTypeBuffer buffer, BlockState state, int x, int y) {
        if (!state.getFluidState().isEmpty() && state.getMaterial().isLiquid()) {
            RenderHelperFluid.renderFluid(matrixStack, buffer, new FluidStack(state.getFluidState().getFluid(), 1000), x, y, 16, 16);
        } else {
            RenderHelperMod.renderItem(matrixStack, buffer, state.getBlock().getItem(Minecraft.getInstance().world, BlockPos.ZERO, state), x, y);
        }
    }
}
