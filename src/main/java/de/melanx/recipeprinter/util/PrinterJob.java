package de.melanx.recipeprinter.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;
import org.moddingx.libx.render.target.RenderJob;

import java.util.function.Consumer;

public class PrinterJob implements RenderJob {

    private final int width;
    private final int height;
    private final int scale;
    private final Consumer<GuiGraphics> renderFunc;

    public PrinterJob(int width, int height, int scale, Consumer<GuiGraphics> renderFunc) {
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.renderFunc = renderFunc;
    }

    @Override
    public Matrix4f setupProjectionMatrix() {
        return new Matrix4f().setOrtho(0, this.width, this.height, 0, 1000, 1000 + GuiGraphics.MAX_GUI_Z - GuiGraphics.MIN_GUI_Z);
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
        GuiGraphics guiGraphics = new GuiGraphics(Minecraft.getInstance(), (MultiBufferSource.BufferSource) buffer);
        guiGraphics.pose().mulPoseMatrix(poseStack.last().pose());
        this.renderFunc.accept(guiGraphics);
    }
}
