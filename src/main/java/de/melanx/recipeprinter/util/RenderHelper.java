package de.melanx.recipeprinter.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.melanx.recipeprinter.RecipePrinter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;

public class RenderHelper {

    public static final int COLOR_GUI_BACKGROUND = 0xc6c6c6;
    public static final int TEXT_COLOR = Color.DARK_GRAY.getRGB();
    public static final ResourceLocation TEXTURE_WHITE = new ResourceLocation(RecipePrinter.MODID, "textures/white.png");
    public static final ResourceLocation TEXTURE_ICONS = new ResourceLocation(RecipePrinter.MODID, "textures/gui/icons.png");

    public static void renderBackground(ResourceLocation texture, MatrixStack matrixStack, IRenderTypeBuffer buffer, int x, int y, int width, int height) {
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        AbstractGui.blit(matrixStack, 0, 0, x, y, width, height, 256, 256);
        matrixStack.translate(0, 0, 100);
    }

    public static void renderDefaultBackground(MatrixStack matrixStack, IRenderTypeBuffer buffer, int width, int height) {
        Minecraft.getInstance().getTextureManager().bindTexture(TEXTURE_WHITE);
        color(COLOR_GUI_BACKGROUND);
        AbstractGui.blit(matrixStack, 0, 0, 0, 0, width, height, 256, 256);
        resetColor();
        matrixStack.translate(0, 0, 100);
    }

    public static void render(OverlayIcon icon, MatrixStack matrixStack, IRenderTypeBuffer buffer, int x, int y) {
        render(icon,  matrixStack, buffer, x, y, icon.width, icon.height);
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

        resetColor();
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

    public static void renderFluid(MatrixStack matrixStack, IRenderTypeBuffer buffer, FluidStack stack, int x, int y, int width, int height) {
        if (!stack.isEmpty()) {
            Fluid fluid = stack.getFluid();
            int color = fluid.getAttributes().getColor(stack);
            TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(fluid.getAttributes().getStillTexture(stack));
            renderFluid(matrixStack, buffer, sprite, color, x, y, width, height);
        }
    }

    public static void renderFluid(MatrixStack matrixStack, IRenderTypeBuffer buffer, int color, int x, int y, int width, int height) {
        Fluid fluid = Fluids.WATER;
        TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(fluid.getAttributes().getStillTexture());
        renderFluid(matrixStack, buffer, sprite, color, x, y, width, height);
    }

    private static void renderFluid(MatrixStack matrixStack, IRenderTypeBuffer buffer, TextureAtlasSprite sprite, int color, int x, int y, int width, int height) {
        matrixStack.push();
        matrixStack.translate(0, 0, 100);
        Minecraft.getInstance().getTextureManager().bindTexture(sprite.getAtlasTexture().getTextureLocation());
        color(color);
        repeatBlit(matrixStack, x, y, width, height, sprite);
        resetColor();
        matrixStack.pop();
    }

    public static void color(int color) {
        RenderSystem.color3f(((color >>> 16) & 0xFF) / 255f, ((color >>> 8) & 0xFF) / 255f, (color & 0xFF) / 255f);
    }

    public static void resetColor() {
        RenderSystem.color3f(1, 1, 1);
    }

    public static void repeatBlit(MatrixStack matrixStack, int x, int y, int displayWidth, int displayHeight, TextureAtlasSprite sprite) {
        repeatBlit(matrixStack, x, y, sprite.getWidth(), sprite.getHeight(), displayWidth, displayHeight, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
    }

    public static void repeatBlit(MatrixStack matrixStack, int x, int y, int texWidth, int texHeight, int displayWidth, int displayHeight, float minU, float maxU, float minV, float maxV) {
        int pixelsRenderedX = 0;
        while (pixelsRenderedX < displayWidth) {
            int pixelsNowX = Math.min(texWidth, displayWidth - pixelsRenderedX);
            float maxUnow = maxU;
            if (pixelsNowX < texWidth) {
                maxUnow = minU + ((maxU - minU) * (pixelsNowX / (float) texWidth));
            }

            int pixelsRenderedY = 0;
            while (pixelsRenderedY < displayHeight) {
                int pixelsNowY = Math.min(texHeight, displayHeight - pixelsRenderedY);
                float maxVnow = maxV;
                if (pixelsNowY < texHeight) {
                    maxVnow = minV + ((maxV - minV) * (pixelsNowY / (float) texHeight));
                }

                AbstractGui.innerBlit(matrixStack.getLast().getMatrix(), x + pixelsRenderedX, x + pixelsRenderedX + pixelsNowX,
                        y + pixelsRenderedY, y + pixelsRenderedY + pixelsNowY,
                        0, minU, maxUnow, minV, maxVnow);

                pixelsRenderedY += pixelsNowY;
            }
            pixelsRenderedX += pixelsNowX;
        }
    }
}
