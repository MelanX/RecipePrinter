package de.melanx.recipedrawer.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class Util {

    public static boolean isNormalItemGroup(ItemGroup group) {
        return !group.isAlignedRight() && group != ItemGroup.HOTBAR && group != ItemGroup.INVENTORY && group != ItemGroup.SEARCH;
    }

    public static void renderItemGroup(MatrixStack matrixStack, IRenderTypeBuffer buffer, NonNullList<ItemStack> stacks, int rows) {
        RenderHelper.renderDefaultBackground(matrixStack, buffer, 170, rows * 18 + 8);
        for (int y = 0; y < rows; y ++) {
            for (int x = 0; x < 9; x++) {
                int idx = y * 9 + x;
                RenderHelper.renderSlot(matrixStack, buffer, 5 + x * 18, 5 + y * 18);
                if (stacks.size() > idx) {
                    RenderHelper.renderItem(matrixStack, buffer, stacks.get(idx), 5 + x * 18, 5 + y * 18);
                }
            }
        }
    }
}
