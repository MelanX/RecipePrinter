package de.melanx.recipeprinter.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.phys.Vec2;
import org.moddingx.libx.render.RenderHelper;

import java.awt.Color;

public class Util {

    public static ItemStack getResultItem(Recipe<?> recipe) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            throw new NullPointerException("level must not be null.");
        }

        return recipe.getResultItem(level.registryAccess());
    }

    public static boolean isNormalItemCategory(CreativeModeTab category) {
        return !category.isAlignedRight() && category != CreativeModeTabs.HOTBAR && category != CreativeModeTabs.INVENTORY && category != CreativeModeTabs.SEARCH;
    }

    public static void renderItemCategory(PoseStack poseStack, MultiBufferSource buffer, NonNullList<ItemStack> stacks, int rows, int itemsPerRow, CreativeModeTab category) {
        RenderHelperMod.renderDefaultBackground(poseStack, buffer, itemsPerRow * 18 + 8, rows * 18 + 24);
        RenderHelperMod.renderItem(poseStack, buffer, category.getIconItem(), 5, 3);
        Minecraft.getInstance().font.draw(poseStack, category.getDisplayName().getString(), 25, 8, Color.DARK_GRAY.getRGB() & 0xFFFFFF);
        RenderHelper.resetColor();
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < itemsPerRow; x++) {
                int idx = y * itemsPerRow + x;
                RenderHelperMod.renderSlot(poseStack, buffer, 5 + x * 18, 21 + y * 18);
                if (stacks.size() > idx) {
                    RenderHelperMod.renderItem(poseStack, buffer, stacks.get(idx), 5 + x * 18, 21 + y * 18);
                }
            }
        }
    }

    public static Vec2 rotatePointAbout(Vec2 in, Vec2 about, double degrees) {
        double rad = degrees * Math.PI / 180.0;
        double newX = Math.cos(rad) * (in.x - about.x) - Math.sin(rad) * (in.y - about.y) + about.x;
        double newY = Math.sin(rad) * (in.x - about.x) + Math.cos(rad) * (in.y - about.y) + about.y;
        return new Vec2((float) newX, (float) newY);
    }
}
