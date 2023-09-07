package de.melanx.recipeprinter.util;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.moddingx.libx.impl.render.JobRenderer;
import org.moddingx.libx.render.RenderHelper;
import org.moddingx.libx.render.target.RenderJob;
import org.moddingx.libx.render.target.RenderJobFailedException;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class Util {

    public static ItemStack getResultItem(Recipe<?> recipe) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            throw new NullPointerException("level must not be null.");
        }

        return recipe.getResultItem(level.registryAccess());
    }

    public static boolean isNormalItemCategory(CreativeModeTab category) {
        Optional<ResourceKey<CreativeModeTab>> resourceKey = BuiltInRegistries.CREATIVE_MODE_TAB.getResourceKey(category);
        return resourceKey.filter(creativeModeTabResourceKey -> !category.isAlignedRight() && creativeModeTabResourceKey != CreativeModeTabs.HOTBAR && creativeModeTabResourceKey != CreativeModeTabs.INVENTORY && creativeModeTabResourceKey != CreativeModeTabs.SEARCH).isPresent();
    }

    public static void renderItemCategory(GuiGraphics guiGraphics, NonNullList<ItemStack> stacks, int rows, int itemsPerRow, CreativeModeTab category) {
        RenderHelperMod.renderDefaultBackground(guiGraphics, itemsPerRow * 18 + 8, rows * 18 + 24);
        RenderHelperMod.renderItem(guiGraphics, category.getIconItem(), 5, 3);
        guiGraphics.drawString(Minecraft.getInstance().font, category.getDisplayName().getString(), 25, 8, Color.DARK_GRAY.getRGB() & 0xFFFFFF, false);
        RenderHelper.resetColor();
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < itemsPerRow; x++) {
                int idx = y * itemsPerRow + x;
                RenderHelperMod.renderSlot(guiGraphics, 5 + x * 18, 21 + y * 18);
                if (stacks.size() > idx) {
                    RenderHelperMod.renderItem(guiGraphics, stacks.get(idx), 5 + x * 18, 21 + y * 18);
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
