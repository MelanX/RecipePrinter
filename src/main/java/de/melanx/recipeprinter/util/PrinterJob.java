package de.melanx.recipeprinter.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;
import org.moddingx.libx.render.target.RenderJob;

import java.util.function.BiConsumer;

public class PrinterJob implements RenderJob {

    private final int width;
    private final int height;
    private final int scale;
    private final BiConsumer<PoseStack, MultiBufferSource> renderFunc;

    public PrinterJob(int width, int height, int scale, BiConsumer<PoseStack, MultiBufferSource> renderFunc) {
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.renderFunc = renderFunc;
    }

    @Override
    public Matrix4f setupProjectionMatrix() {
//        return new Matrix4f().ortho(0, this.width(), this.height(), 0, 500, 6000);
        return new Matrix4f().setOrtho(0, this.width, this.height, 0, 500, 6000);
    }

    @Override
    public int width() {
        return this.width * this.scale;
    }

    @Override
    public int height() {
        return this.height * this.scale;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer) {
        this.renderFunc.accept(poseStack, buffer);
    }
}
