package de.melanx.recipeprinter.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;

public class Util {

    public static boolean isNormalItemGroup(ItemGroup group) {
        return !group.isAlignedRight() && group != ItemGroup.HOTBAR && group != ItemGroup.INVENTORY && group != ItemGroup.SEARCH;
    }

    public static void renderItemGroup(MatrixStack matrixStack, IRenderTypeBuffer buffer, NonNullList<ItemStack> stacks, int rows, ItemGroup group) {
        RenderHelper.renderDefaultBackground(matrixStack, buffer, 170, rows * 18 + 24);
        RenderHelper.renderItem(matrixStack, buffer, group.getIcon(), 5, 3);
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, Util.getGroupName(group), 23, 8, Color.DARK_GRAY.getRGB());
        for (int y = 0; y < rows; y ++) {
            for (int x = 0; x < 9; x++) {
                int idx = y * 9 + x;
                RenderHelper.renderSlot(matrixStack, buffer, 5 + x * 18, 21 + y * 18);
                if (stacks.size() > idx) {
                    RenderHelper.renderItem(matrixStack, buffer, stacks.get(idx), 5 + x * 18, 21 + y * 18);
                }
            }
        }
    }

    public static String getGroupName(ItemGroup group) {
        return group.getGroupName().getString();
    }
}
