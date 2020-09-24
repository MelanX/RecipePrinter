package de.melanx.recipedrawer.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.melanx.recipedrawer.RecipeDrawer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public class RenderHelper {

    public static final int COLOR_GUI_BACKGROUND = 0xc6c6c6;
    public static final ResourceLocation TEXTURE_WHITE = new ResourceLocation(RecipeDrawer.MODID, "textures/white.png");
    public static final ResourceLocation TEXTURE_ICONS = new ResourceLocation(RecipeDrawer.MODID, "textures/gui/icons.png");

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
        Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(stack, x, y);
        matrixStack.translate(0, 0, 100);
        Minecraft.getInstance().getItemRenderer().renderItemOverlayIntoGUI(Minecraft.getInstance().fontRenderer, stack, x, y, null);
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

    public static void color(int color) {
        RenderSystem.color3f(((color >>> 16) & 0xFF) / 255f, ((color >>> 8) & 0xFF) / 255f, (color & 0xFF) / 255f);
    }

    public static void resetColor() {
        RenderSystem.color3f(1, 1, 1);
    }
}
