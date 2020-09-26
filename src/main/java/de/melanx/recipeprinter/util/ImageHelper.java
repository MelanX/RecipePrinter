package de.melanx.recipeprinter.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ScreenShotHelper;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.BiConsumer;

public class ImageHelper {

    public static void addRenderJob(int width, int height, double scale, BiConsumer<MatrixStack, IRenderTypeBuffer> renderFunc, Path imagePath, boolean includeFrame) {
        Minecraft.getInstance().queueChunkTracking.add(() -> render(width, height, scale, renderFunc, imagePath, includeFrame));
    }

    public static void render(int width, int height, double scale, BiConsumer<MatrixStack, IRenderTypeBuffer> renderFunc, Path imagePath, boolean includeFrame) {
        int realWidth = (int) Math.round(scale * width);
        int realHeight = (int) Math.round(scale * height);

        boolean tooLarge = false;
        int maxTextureSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
        if (realWidth > maxTextureSize || realHeight > maxTextureSize) {
            tooLarge = true;
            realWidth = 512;
            realHeight = 512;
        }

        Framebuffer fb = new Framebuffer(realWidth, realHeight, true, Minecraft.IS_RUNNING_ON_MAC);

        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.clear(16640, Minecraft.IS_RUNNING_ON_MAC);
        fb.bindFramebuffer(true);
        FogRenderer.resetFog();

        RenderSystem.enableTexture();
        RenderSystem.enableCull();

        RenderSystem.viewport(0, 0, realWidth, realHeight);

        RenderSystem.matrixMode(GL11.GL_PROJECTION);
        RenderSystem.loadIdentity();
        if (tooLarge) {
            RenderSystem.ortho(0.0D, 512, 512, 0.0D, 1000.0D, 3000.0D);
        } else {
            RenderSystem.ortho(0.0D, width, height, 0.0D, 1000.0D, 3000.0D);
        }
        RenderSystem.matrixMode(GL11.GL_MODELVIEW);
        RenderSystem.loadIdentity();

        MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(0, 0, -2000);
        net.minecraft.client.renderer.RenderHelper.setupGui3DDiffuseLighting();

        IRenderTypeBuffer buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();

        RenderSystem.defaultAlphaFunc();

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
            matrixStack.translate(0, 0, 100);
            matrixStack.scale(2, 2, 2);
            for (int i = 0;i < msg.length; i++) {
                Minecraft.getInstance().fontRenderer.drawString(matrixStack, msg[i], 5, 5 + (i * (Minecraft.getInstance().fontRenderer.FONT_HEIGHT + 2)), RenderHelper.TEXT_COLOR);
            }
            RenderHelper.resetColor();
        } else {
            renderFunc.accept(matrixStack, buffer);
        }

        RenderSystem.disableBlend();
        RenderSystem.popMatrix();

        NativeImage img = ScreenShotHelper.createScreenshot(realWidth, realHeight, fb);

        if (includeFrame && !tooLarge) {
            applyFrame(img, width, height, scale);
        }

        try {
            img.write(imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void applyFrame(NativeImage img, int width, int height, double scale) {
        img.fillAreaRGBA(scale(0, scale), scale(0, scale), scale(2, scale), scale(1, scale), 0x00000000);
        img.fillAreaRGBA(scale(0, scale), scale(1, scale), scale(1, scale), scale(1, scale), 0x00000000);
        img.fillAreaRGBA(scale(width - 2, scale), scale(0, scale), scale(2, scale), scale(1, scale), 0x00000000);
        img.fillAreaRGBA(scale(width - 1, scale), scale(1, scale), scale(1, scale), scale(1, scale), 0x00000000);
        img.fillAreaRGBA(scale(0, scale), scale(height - 1, scale), scale(2, scale), scale(1, scale), 0x00000000);
        img.fillAreaRGBA(scale(0, scale), scale(height - 2, scale), scale(1, scale), scale(1, scale), 0x00000000);
        img.fillAreaRGBA(scale(width - 2, scale), scale(height - 1, scale), scale(2, scale), scale(1, scale), 0x00000000);
        img.fillAreaRGBA(scale(width - 1, scale), scale(height - 2, scale), scale(1, scale), scale(1, scale), 0x00000000);

        img.fillAreaRGBA(scale(1, scale), scale(1, scale), scale(1, scale), scale(1, scale), 0xFF999999);
        img.fillAreaRGBA(scale(width - 2, scale), scale(1, scale), scale(1, scale), scale(1, scale), 0xFF999999);
        img.fillAreaRGBA(scale(1, scale), scale(height - 2, scale), scale(1, scale), scale(1, scale), 0xFF999999);
        img.fillAreaRGBA(scale(width - 2, scale), scale(height - 2, scale), scale(1, scale), scale(1, scale), 0xFF999999);

        img.fillAreaRGBA(scale(2, scale), scale(0, scale), scale(width - 4, scale), scale(1, scale), 0xFF999999);
        img.fillAreaRGBA(scale(2, scale), scale(height - 1, scale), scale(width - 4, scale), scale(1, scale), 0xFF999999);
        img.fillAreaRGBA(scale(0, scale), scale(2, scale), scale(1, scale), scale(height - 4, scale), 0xFF999999);
        img.fillAreaRGBA(scale(width - 1, scale), scale(2, scale), scale(1, scale), scale(height - 4, scale), 0xFF999999);

        img.fillAreaRGBA(scale(2, scale), scale(1, scale), scale(width - 4, scale), scale(1, scale), 0xFFD8D8D8);
        img.fillAreaRGBA(scale(2, scale), scale(height - 2, scale), scale(width - 4, scale), scale(1, scale), 0xFFB3B3B3);
        img.fillAreaRGBA(scale(1, scale), scale(2, scale), scale(1, scale), scale(height - 4, scale), 0xFFD8D8D8);
        img.fillAreaRGBA(scale(width - 2, scale), scale(2, scale), scale(1, scale), scale(height - 4, scale), 0xFFB3B3B3);
    }

    private static int scale(int value, double scale) {
        return (int) Math.round(value * scale);
    }
}
