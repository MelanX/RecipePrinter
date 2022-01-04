package de.melanx.recipeprinter.util;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import de.melanx.recipeprinter.RecipePrinter;
import io.github.noeppi_noeppi.libx.render.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.BiConsumer;

public class ImageHelper {

    private static final int TOO_LARGE_SIZE = 512;

    public static void addRenderJob(int width, int height, double scale, BiConsumer<PoseStack, MultiBufferSource> renderFunc, Path imagePath, boolean includeFrame) {
        Minecraft.getInstance().progressTasks.add(() -> render(width, height, scale, renderFunc, imagePath, includeFrame));
    }

    public static void render(int width, int height, double scale, BiConsumer<PoseStack, MultiBufferSource> renderFunc, Path imagePath, boolean includeFrame) {
        int realWidth = (int) Math.round(scale * width);
        int realHeight = (int) Math.round(scale * height);

        boolean tooLarge = false;
        int maxTextureSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
        if (realWidth > maxTextureSize || realHeight > maxTextureSize) {
            tooLarge = true;
            realWidth = TOO_LARGE_SIZE;
            realHeight = TOO_LARGE_SIZE;
        }

        RenderTarget fb = new TextureTarget(realWidth, realHeight, true, Minecraft.ON_OSX);

        PoseStack ps2 = RenderSystem.getModelViewStack();
        ps2.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.clear(16640, Minecraft.ON_OSX);
        fb.bindWrite(true);
        FogRenderer.setupNoFog();

        RenderSystem.enableTexture();
        RenderSystem.enableCull();

        RenderSystem.viewport(0, 0, realWidth, realHeight);

        ps2.setIdentity();
        if (tooLarge) {
            Matrix4f matrix4f = Matrix4f.orthographic(0, TOO_LARGE_SIZE, TOO_LARGE_SIZE, 0, 1000.0F, 3000.0F);
            RenderSystem.setProjectionMatrix(matrix4f);
        } else {
            Matrix4f matrix4f = Matrix4f.orthographic(0, width, height, 0, 1000.0F, 3000.0F);
            RenderSystem.setProjectionMatrix(matrix4f);
        }

        PoseStack poseStack = new PoseStack();
        poseStack.translate(0, 0, -2000);
        Lighting.setupFor3DItems();

        MultiBufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();

        RenderSystem.defaultBlendFunc();

        if (tooLarge) {
            String[] msg = new String[]{
                    "Too large",
                    "Your OpenGL implementation has a",
                    "maximum texture size",
                    "of " + maxTextureSize,
                    "this image would have had a width",
                    "of " + (int) Math.round(scale * width),
                    "and a height",
                    "of " + (int) Math.round(scale * height),
                    "which is too large.",
                    "To fix this lower the scale in",
                    "the config."
            };
            poseStack.translate(0, 0, 100);
            poseStack.scale(2, 2, 2);
            for (int i = 0; i < msg.length; i++) {
                Minecraft.getInstance().font.draw(poseStack, msg[i], 5, 5 + (i * (Minecraft.getInstance().font.lineHeight + 2)), RenderHelperMod.TEXT_COLOR);
            }
            RenderHelper.resetColor();
        } else {
            renderFunc.accept(poseStack, buffer);
        }

        RenderSystem.disableBlend();
        RenderSystem.applyModelViewMatrix();
        ps2.popPose();

        NativeImage img = Screenshot.takeScreenshot(fb);

        if (includeFrame && !tooLarge) {
            applyFrame(img, width, height, scale);
        }

        try {
            img.writeToFile(imagePath);
        } catch (IOException e) {
            RecipePrinter.getInstance().logger.error("Could not print recipe: {}", e.getMessage());
        }
    }

    private static void applyFrame(NativeImage img, int width, int height, double scale) {
        img.fillRect(scale(0, scale), scale(0, scale), scale(2, scale), scale(1, scale), 0x00000000);
        img.fillRect(scale(0, scale), scale(1, scale), scale(1, scale), scale(1, scale), 0x00000000);
        img.fillRect(scale(width - 2, scale), scale(0, scale), scale(2, scale), scale(1, scale), 0x00000000);
        img.fillRect(scale(width - 1, scale), scale(1, scale), scale(1, scale), scale(1, scale), 0x00000000);
        img.fillRect(scale(0, scale), scale(height - 1, scale), scale(2, scale), scale(1, scale), 0x00000000);
        img.fillRect(scale(0, scale), scale(height - 2, scale), scale(1, scale), scale(1, scale), 0x00000000);
        img.fillRect(scale(width - 2, scale), scale(height - 1, scale), scale(2, scale), scale(1, scale), 0x00000000);
        img.fillRect(scale(width - 1, scale), scale(height - 2, scale), scale(1, scale), scale(1, scale), 0x00000000);

        img.fillRect(scale(1, scale), scale(1, scale), scale(1, scale), scale(1, scale), 0xFF999999);
        img.fillRect(scale(width - 2, scale), scale(1, scale), scale(1, scale), scale(1, scale), 0xFF999999);
        img.fillRect(scale(1, scale), scale(height - 2, scale), scale(1, scale), scale(1, scale), 0xFF999999);
        img.fillRect(scale(width - 2, scale), scale(height - 2, scale), scale(1, scale), scale(1, scale), 0xFF999999);

        img.fillRect(scale(2, scale), scale(0, scale), scale(width - 4, scale), scale(1, scale), 0xFF999999);
        img.fillRect(scale(2, scale), scale(height - 1, scale), scale(width - 4, scale), scale(1, scale), 0xFF999999);
        img.fillRect(scale(0, scale), scale(2, scale), scale(1, scale), scale(height - 4, scale), 0xFF999999);
        img.fillRect(scale(width - 1, scale), scale(2, scale), scale(1, scale), scale(height - 4, scale), 0xFF999999);

        img.fillRect(scale(2, scale), scale(1, scale), scale(width - 4, scale), scale(1, scale), 0xFFD8D8D8);
        img.fillRect(scale(2, scale), scale(height - 2, scale), scale(width - 4, scale), scale(1, scale), 0xFFB3B3B3);
        img.fillRect(scale(1, scale), scale(2, scale), scale(1, scale), scale(height - 4, scale), 0xFFD8D8D8);
        img.fillRect(scale(width - 2, scale), scale(2, scale), scale(1, scale), scale(height - 4, scale), 0xFFB3B3B3);
    }

    private static int scale(int value, double scale) {
        return (int) Math.round(value * scale);
    }
}
