package de.melanx.recipedrawer.util;

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
        RenderSystem.ortho(0.0D, width, height, 0.0D, 1000.0D, 3000.0D);
        RenderSystem.matrixMode(GL11.GL_MODELVIEW);
        RenderSystem.loadIdentity();
        RenderSystem.translatef(0.0F, 0.0F, -2000.0F);
        net.minecraft.client.renderer.RenderHelper.setupGui3DDiffuseLighting();

        MatrixStack matrixStack = new MatrixStack();
        IRenderTypeBuffer buffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();

        RenderSystem.defaultAlphaFunc();

        renderFunc.accept(matrixStack, buffer);

        RenderSystem.disableBlend();
        RenderSystem.popMatrix();

        NativeImage img = ScreenShotHelper.createScreenshot(realWidth, realHeight, fb);

        if (includeFrame) {
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
