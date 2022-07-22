package de.melanx.recipeprinter.util;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import de.melanx.recipeprinter.RecipePrinter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.lwjgl.opengl.GL11;
import org.moddingx.libx.render.RenderHelper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.BiConsumer;

public class ImageHelper {

    private static final int TOO_LARGE_SIZE = 512;

    public static void addRenderJob(int width, int height, double scale, BiConsumer<PoseStack, MultiBufferSource> renderFunc, Path imagePath) {
        Minecraft.getInstance().progressTasks.add(() -> render(width, height, scale, renderFunc, imagePath));
    }

    public static void render(int width, int height, double scale, BiConsumer<PoseStack, MultiBufferSource> renderFunc, Path imagePath) {
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
        fb.setClearColor(0, 0, 0, 0);
        fb.clear(true);

        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        
        RenderSystem.enableBlend();
        RenderSystem.clear(0x4100, Minecraft.ON_OSX);
        fb.bindWrite(true);
        FogRenderer.setupNoFog();

        RenderSystem.enableTexture();
        RenderSystem.enableCull();

        RenderSystem.viewport(0, 0, realWidth, realHeight);

        modelViewStack.setIdentity();
        modelViewStack.translate(0, 0, -2000);
        RenderSystem.applyModelViewMatrix();
        if (tooLarge) {
            RenderSystem.setProjectionMatrix(Matrix4f.orthographic(0, TOO_LARGE_SIZE, 0, TOO_LARGE_SIZE, 1000, 3000));
        } else {
            RenderSystem.setProjectionMatrix(Matrix4f.orthographic(0, width, 0, height, 1000, 3000));
        }

        PoseStack poseStack = new PoseStack();
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
            try {
                renderFunc.accept(poseStack, buffer);
            } catch (RuntimeException e) {
                RecipePrinter.getInstance().logger.error("Could not print recipe: {}", e.getMessage());
            }
        }

        RenderSystem.disableBlend();
        modelViewStack.popPose();
        RenderSystem.applyModelViewMatrix();

        NativeImage img = takeNonOpaqueScreenshot(fb);

        try {
            img.writeToFile(imagePath);
        } catch (IOException e) {
            RecipePrinter.getInstance().logger.error("Could not print recipe: {}", e.getMessage());
        }
    }
    
    // See Screenshot.takeScreenshot
    private static NativeImage takeNonOpaqueScreenshot(RenderTarget fb) {
        NativeImage img = new NativeImage(fb.width, fb.height, false);
        RenderSystem.bindTexture(fb.getColorTextureId());
        img.downloadTexture(0, false);
        img.flipY();
        return img;
    }
}
