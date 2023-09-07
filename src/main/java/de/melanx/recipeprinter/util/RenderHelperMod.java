package de.melanx.recipeprinter.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.recipeprinter.RecipePrinter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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

    public static void renderBackground(ResourceLocation texture, GuiGraphics guiGraphics, int x, int y, int width, int height, int textureWidth, int textureHeight, boolean includeFrame) {
RenderHelper.renderGuiBackground(guiGraphics, x, y, width, height);
        //        RenderHelper.resetColor();
//        if (includeFrame) {
//            guiGraphics.blit(texture, 2, 2, x + 2, y + 2, width - 4, height - 4, textureWidth, textureHeight);
//            applyFrame(guiGraphics, width, height);
//        } else {
//            guiGraphics.blit(texture, 0, 0, x, y, width, height, textureWidth, textureHeight);
//        }
//        guiGraphics.pose().translate(0, 0, 100);
    }

    public static void renderBackground(ResourceLocation texture, GuiGraphics guiGraphics, int x, int y, int width, int height, boolean includeFrame) {
        RenderHelperMod.renderBackground(texture, guiGraphics, x, y, width, height, 256, 256, includeFrame);
    }

    public static void renderDefaultBackground(GuiGraphics guiGraphics, int width, int height) {
        RenderHelper.rgb(COLOR_GUI_BACKGROUND);
        guiGraphics.blit(RenderHelper.TEXTURE_WHITE, 2, 2, 0, 0, width - 4, height - 4, 256, 256);
        RenderHelper.resetColor();

        applyFrame(guiGraphics, width, height);

        guiGraphics.pose().translate(0, 0, 100);
    }

    private static void applyFrame(GuiGraphics guiGraphics, int width, int height) {
        RenderHelper.rgb(COLOR_GUI_BACKGROUND_FRAME_INNER_TL);
        guiGraphics.blit(RenderHelper.TEXTURE_WHITE, 2, 1, 0, 0, width - 4, 1, 256, 256);
        guiGraphics.blit(RenderHelper.TEXTURE_WHITE, 1, 2, 0, 0, 1, height - 4, 256, 256);

        RenderHelper.rgb(COLOR_GUI_BACKGROUND_FRAME_INNER_BR);
        guiGraphics.blit(RenderHelper.TEXTURE_WHITE, 2, height - 2, 0, 0, width - 4, 1, 256, 256);
        guiGraphics.blit(RenderHelper.TEXTURE_WHITE, width - 2, 2, 0, 0, 1, height - 4, 256, 256);

        RenderHelper.rgb(COLOR_GUI_BACKGROUND_FRAME_OUTER);
        guiGraphics.blit(RenderHelper.TEXTURE_WHITE, 1, 1, 0, 0, 1, 1, 256, 256);
        guiGraphics.blit(RenderHelper.TEXTURE_WHITE, 1, height - 2, 0, 0, 1, 1, 256, 256);
        guiGraphics.blit(RenderHelper.TEXTURE_WHITE, width - 2, 1, 0, 0, 1, 1, 256, 256);
        guiGraphics.blit(RenderHelper.TEXTURE_WHITE, width - 2, height - 2, 0, 0, 1, 1, 256, 256);

        guiGraphics.blit(RenderHelper.TEXTURE_WHITE, 2, 0, 0, 0, width - 4, 1, 256, 256);
        guiGraphics.blit(RenderHelper.TEXTURE_WHITE, 0, 2, 0, 0, 1, height - 4, 256, 256);
        guiGraphics.blit(RenderHelper.TEXTURE_WHITE, 2, height - 1, 0, 0, width - 4, 1, 256, 256);
        guiGraphics.blit(RenderHelper.TEXTURE_WHITE, width - 1, 2, 0, 0, 1, height - 4, 256, 256);

        RenderHelper.resetColor();
    }

    public static void render(OverlayIcon icon, GuiGraphics guiGraphics, int x, int y) {
        RenderHelperMod.render(icon, guiGraphics, x, y, icon.width, icon.height);
    }

    public static void render(OverlayIcon icon, GuiGraphics guiGraphics, int x, int y, int width, int height) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, 10);
        guiGraphics.pose().scale((float) width / icon.width, (float) height / icon.height, 1);
        guiGraphics.blit(icon.texture, 0, 0, icon.u, icon.v, icon.width, icon.height, 256, 256);
        guiGraphics.pose().popPose();
    }

    public static void renderSlot(GuiGraphics guiGraphics, int x, int y) {
        render(OverlayIcon.SLOT, guiGraphics, x - 1, y - 1);
    }

    public static void renderBigSlot(GuiGraphics guiGraphics, int x, int y) {
        render(OverlayIcon.BIG_SLOT, guiGraphics, x - 5, y - 5);
    }

    public static void renderItem(GuiGraphics guiGraphics, ItemStack stack, int x, int y) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(0, 0, 100);
        RenderHelper.resetColor();

        guiGraphics.renderFakeItem(stack, x, y);
        poseStack.translate(0, 0, 100);
        guiGraphics.renderItemDecorations(Minecraft.getInstance().font, stack, x, y);

        RenderHelper.resetColor();
        poseStack.popPose();
    }

    public static void renderIngredient(GuiGraphics guiGraphics, Ingredient ingredient, int x, int y) {
        if (!ingredient.isEmpty()) {
            ItemStack[] stacks = ingredient.getItems();
            if (stacks.length != 0) {
                RenderHelperMod.renderItem(guiGraphics, stacks[0], x, y);
            }
        }
    }

    public static void renderBlockState(GuiGraphics guiGraphics, BlockState state, int x, int y) {
        //noinspection deprecation
        if (!state.getFluidState().isEmpty() && state.liquid()) {
            RenderHelperFluid.renderFluid(guiGraphics, new FluidStack(state.getFluidState().getType(), 1000), x, y, 16, 16);
        } else {
            // noinspection ConstantConditions,deprecation
            RenderHelperMod.renderItem(guiGraphics, state.getBlock().getCloneItemStack(Minecraft.getInstance().level, BlockPos.ZERO, state), x, y);
        }
    }
}
