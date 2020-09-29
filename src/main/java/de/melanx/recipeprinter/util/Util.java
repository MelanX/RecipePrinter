package de.melanx.recipeprinter.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.vector.Vector2f;

import java.awt.*;

public class Util {

    public static boolean isNormalItemGroup(ItemGroup group) {
        return !group.isAlignedRight() && group != ItemGroup.HOTBAR && group != ItemGroup.INVENTORY && group != ItemGroup.SEARCH;
    }

    public static void renderItemGroup(MatrixStack matrixStack, IRenderTypeBuffer buffer, NonNullList<ItemStack> stacks, int rows, int itemsPerRow, ItemGroup group) {
        RenderHelper.renderDefaultBackground(matrixStack, buffer, itemsPerRow * 18 + 8, rows * 18 + 24);
        RenderHelper.renderItem(matrixStack, buffer, group.getIcon(), 5, 3);
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, group.getGroupName().getString(), 25, 8, Color.DARK_GRAY.getRGB());
        RenderHelper.resetColor();
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < itemsPerRow; x++) {
                int idx = y * itemsPerRow + x;
                RenderHelper.renderSlot(matrixStack, buffer, 5 + x * 18, 21 + y * 18);
                if (stacks.size() > idx) {
                    RenderHelper.renderItem(matrixStack, buffer, stacks.get(idx), 5 + x * 18, 21 + y * 18);
                }
            }
        }
    }

    public static Vector2f rotatePointAbout(Vector2f in, Vector2f about, double degrees) {
        double rad = degrees * Math.PI / 180.0;
        double newX = Math.cos(rad) * (in.x - about.x) - Math.sin(rad) * (in.y - about.y) + about.x;
        double newY = Math.sin(rad) * (in.x - about.x) + Math.cos(rad) * (in.y - about.y) + about.y;
        return new Vector2f((float) newX, (float) newY);
    }
}
