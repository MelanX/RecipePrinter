package de.melanx.recipedrawer.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import de.melanx.recipedrawer.IRecipeRender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.ScreenShotHelper;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.function.BiConsumer;

public class ImageHelper {

    public static void addRenderJob(int width, int height, double scale, BiConsumer<MatrixStack, IRenderTypeBuffer> renderFunc, Path imagePath) {
        Minecraft.getInstance().queueChunkTracking.add(() -> render(width, height, scale, renderFunc, imagePath));
    }

    public static void render(int width, int height, double scale, BiConsumer<MatrixStack, IRenderTypeBuffer> renderFunc, Path imagePath) {
        int realWidth = (int) Math.round(scale * width);
        int realHeight = (int) Math.round(scale * height);

        Framebuffer fb = new Framebuffer(realWidth, realHeight, true, Minecraft.IS_RUNNING_ON_MAC);

        RenderSystem.pushMatrix();
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

        RenderSystem.popMatrix();

        NativeImage img = ScreenShotHelper.createScreenshot(realWidth, realHeight, fb);
        try {
            img.write(imagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void replaceBackground(IRecipeRender<?> render, Path path) {
        Minecraft.getInstance().queueChunkTracking.add(() -> {
            try {
                BufferedImage image = ImageIO.read(path.toFile());
                for (int x = 0; x < image.getWidth(); x++) {
                    for (int y = 0; y < image.getHeight(); y++) {
                        if (!render.isProtected(x, y) && image.getRGB(x, y) == Color.GREEN.getRGB()) {
                            image.setRGB(x, y, new Color(0, 0, 0, 1).getRGB());
                        }
                    }
                }
                ImageIO.write(image, "png", path.toFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
