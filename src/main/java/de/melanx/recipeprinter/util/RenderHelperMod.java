package de.melanx.recipeprinter.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.recipeprinter.RecipePrinter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.moddingx.libx.render.RenderHelper;
import org.moddingx.libx.render.RenderHelperFluid;

import java.awt.Color;

public class RenderHelperMod {

    public static final int COLOR_GUI_BACKGROUND = 0xC6C6C6;
    public static final int COLOR_GUI_BACKGROUND_FRAME_INNER_BR = 0xB3B3B3;
    public static final int COLOR_GUI_BACKGROUND_FRAME_INNER_TL = 0xD8D8D8;
    public static final int COLOR_GUI_BACKGROUND_FRAME_OUTER = 0x999999;

    public static final int TEXT_COLOR = Color.DARK_GRAY.getRGB();
    public static final ResourceLocation TEXTURE_ICONS = new ResourceLocation(RecipePrinter.getInstance().modid, "textures/gui/icons.png");

    public static void renderBackground(ResourceLocation texture, PoseStack poseStack, MultiBufferSource buffer, int x, int y, int width, int height, int textureWidth, int textureHeight, boolean includeFrame) {
        RenderSystem.setShaderTexture(0, texture);
        RenderHelper.resetColor();
        if (includeFrame) {
            GuiComponent.blit(poseStack, 2, 2, x + 2, y + 2, width - 4, height - 4, textureWidth, textureHeight);
            applyFrame(poseStack, buffer, width, height);
        } else {
            GuiComponent.blit(poseStack, 0, 0, x, y, width, height, textureWidth, textureHeight);
        }
        poseStack.translate(0, 0, 100);
    }

    public static void renderBackground(ResourceLocation texture, PoseStack poseStack, MultiBufferSource buffer, int x, int y, int width, int height, boolean includeFrame) {
        RenderHelperMod.renderBackground(texture, poseStack, buffer, x, y, width, height, 256, 256, includeFrame);
    }

    public static void renderDefaultBackground(PoseStack poseStack, MultiBufferSource buffer, int width, int height) {
        RenderSystem.setShaderTexture(0, RenderHelper.TEXTURE_WHITE);

        RenderHelper.rgb(COLOR_GUI_BACKGROUND);
        GuiComponent.blit(poseStack, 2, 2, 0, 0, width - 4, height - 4, 256, 256);
        RenderHelper.resetColor();

        applyFrame(poseStack, buffer, width, height);

        poseStack.translate(0, 0, 100);
    }

    private static void applyFrame(PoseStack poseStack, MultiBufferSource buffer, int width, int height) {
        RenderSystem.setShaderTexture(0, RenderHelper.TEXTURE_WHITE);

        RenderHelper.rgb(COLOR_GUI_BACKGROUND_FRAME_INNER_TL);
        GuiComponent.blit(poseStack, 2, 1, 0, 0, width - 4, 1, 256, 256);
        GuiComponent.blit(poseStack, 1, 2, 0, 0, 1, height - 4, 256, 256);

        RenderHelper.rgb(COLOR_GUI_BACKGROUND_FRAME_INNER_BR);
        GuiComponent.blit(poseStack, 2, height - 2, 0, 0, width - 4, 1, 256, 256);
        GuiComponent.blit(poseStack, width - 2, 2, 0, 0, 1, height - 4, 256, 256);

        RenderHelper.rgb(COLOR_GUI_BACKGROUND_FRAME_OUTER);
        GuiComponent.blit(poseStack, 1, 1, 0, 0, 1, 1, 256, 256);
        GuiComponent.blit(poseStack, 1, height - 2, 0, 0, 1, 1, 256, 256);
        GuiComponent.blit(poseStack, width - 2, 1, 0, 0, 1, 1, 256, 256);
        GuiComponent.blit(poseStack, width - 2, height - 2, 0, 0, 1, 1, 256, 256);

        GuiComponent.blit(poseStack, 2, 0, 0, 0, width - 4, 1, 256, 256);
        GuiComponent.blit(poseStack, 0, 2, 0, 0, 1, height - 4, 256, 256);
        GuiComponent.blit(poseStack, 2, height - 1, 0, 0, width - 4, 1, 256, 256);
        GuiComponent.blit(poseStack, width - 1, 2, 0, 0, 1, height - 4, 256, 256);

        RenderHelper.resetColor();
    }

    public static void render(OverlayIcon icon, PoseStack poseStack, MultiBufferSource buffer, int x, int y) {
        RenderHelperMod.render(icon, poseStack, buffer, x, y, icon.width, icon.height);
    }

    public static void render(OverlayIcon icon, PoseStack poseStack, MultiBufferSource buffer, int x, int y, int width, int height) {
        RenderSystem.setShaderTexture(0, icon.texture);
        poseStack.pushPose();
        poseStack.translate(x, y, 10);
        poseStack.scale((float) width / icon.width, (float) height / icon.height, 1);
        GuiComponent.blit(poseStack, 0, 0, icon.u, icon.v, icon.width, icon.height, 256, 256);
        poseStack.popPose();
    }

    public static void renderSlot(PoseStack poseStack, MultiBufferSource buffer, int x, int y) {
        render(OverlayIcon.SLOT, poseStack, buffer, x - 1, y - 1);
    }

    public static void renderBigSlot(PoseStack poseStack, MultiBufferSource buffer, int x, int y) {
        render(OverlayIcon.BIG_SLOT, poseStack, buffer, x - 5, y - 5);
    }

    public static void renderItem(PoseStack poseStack, MultiBufferSource buffer, ItemStack stack, int x, int y) {
        poseStack.pushPose();
        poseStack.translate(0, 0, 100);
        RenderHelper.resetColor();

        // As the item render method does not accept a matrix stack to apply transformations,
        // we need to do it manually.
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        modelViewStack.mulPoseMatrix(poseStack.last().pose());
        RenderSystem.applyModelViewMatrix();
        Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(poseStack, stack, x, y);
        modelViewStack.popPose();
        RenderSystem.applyModelViewMatrix();

        poseStack.translate(0, 0, 100);

        // Same as above
        modelViewStack.pushPose();
        modelViewStack.mulPoseMatrix(poseStack.last().pose());
        RenderSystem.applyModelViewMatrix();
        Minecraft.getInstance().getItemRenderer().renderGuiItemDecorations(poseStack, Minecraft.getInstance().font, stack, x, y, null);
        modelViewStack.popPose();
        RenderSystem.applyModelViewMatrix();

        RenderHelper.resetColor();
        poseStack.popPose();
    }

    public static void renderIngredient(PoseStack poseStack, MultiBufferSource buffer, Ingredient ingredient, int x, int y) {
        if (!ingredient.isEmpty()) {
            ItemStack[] stacks = ingredient.getItems();
            if (stacks.length != 0) {
                RenderHelperMod.renderItem(poseStack, buffer, stacks[0], x, y);
            }
        }
    }

    public static void renderBlockState(PoseStack poseStack, MultiBufferSource buffer, BlockState state, int x, int y) {
        if (!state.getFluidState().isEmpty() && state.getMaterial().isLiquid()) {
            RenderHelperFluid.renderFluid(poseStack, buffer, new FluidStack(state.getFluidState().getType(), 1000), x, y, 16, 16);
        } else {
            // noinspection ConstantConditions,deprecation
            RenderHelperMod.renderItem(poseStack, buffer, state.getBlock().getCloneItemStack(Minecraft.getInstance().level, BlockPos.ZERO, state), x, y);
        }
    }
}
