package de.melanx.recipeprinter.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.recipeprinter.RecipePrinter;
import io.github.noeppi_noeppi.libx.render.RenderHelper;
import io.github.noeppi_noeppi.libx.render.RenderHelperFluid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

import java.awt.Color;

public class RenderHelperMod {

    public static final int COLOR_GUI_BACKGROUND = 0xc6c6c6;
    public static final int TEXT_COLOR = Color.DARK_GRAY.getRGB();
    public static final ResourceLocation TEXTURE_ICONS = new ResourceLocation(RecipePrinter.getInstance().modid, "textures/gui/icons.png");

    public static void renderBackground(ResourceLocation texture, PoseStack poseStack, MultiBufferSource buffer, int x, int y, int width, int height, int textureWidth, int textureHeight) {
        Minecraft.getInstance().getTextureManager().bind(texture);
        GuiComponent.blit(poseStack, 0, 0, x, y, width, height, textureWidth, textureHeight);
        poseStack.translate(0, 0, 100);
    }

    public static void renderBackground(ResourceLocation texture, PoseStack poseStack, MultiBufferSource buffer, int x, int y, int width, int height) {
        renderBackground(texture, poseStack, buffer, x, y, width, height, 256, 256);
    }

    public static void renderDefaultBackground(PoseStack poseStack, MultiBufferSource buffer, int width, int height) {
        Minecraft.getInstance().getTextureManager().bind(RenderHelper.TEXTURE_WHITE);
        RenderHelper.rgb(COLOR_GUI_BACKGROUND);
        GuiComponent.blit(poseStack, 0, 0, 0, 0, width, height, 256, 256);
        RenderHelper.resetColor();
        poseStack.translate(0, 0, 100);
    }

    public static void render(OverlayIcon icon, PoseStack poseStack, MultiBufferSource buffer, int x, int y) {
        render(icon, poseStack, buffer, x, y, icon.width, icon.height);
    }

    public static void render(OverlayIcon icon, PoseStack poseStack, MultiBufferSource buffer, int x, int y, int width, int height) {
        Minecraft.getInstance().getTextureManager().bind(icon.texture);
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

        // As the item render method does not accept a matrix stack to apply transformations,
        // we need to do it manually.
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        RenderSystem.multMatrix(poseStack.last().pose());
        Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(stack, x, y);
        RenderSystem.popMatrix();

        poseStack.translate(0, 0, 100);

        // Same as above
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        RenderSystem.multMatrix(poseStack.last().pose());
        Minecraft.getInstance().getItemRenderer().renderGuiItemDecorations(Minecraft.getInstance().font, stack, x, y, null);
        RenderSystem.popMatrix();

        RenderHelper.resetColor();
        poseStack.popPose();
    }

    public static void renderIngredient(PoseStack poseStack, MultiBufferSource buffer, Ingredient ingredient, int x, int y) {
        if (!ingredient.isEmpty()) {
            ItemStack[] stacks = ingredient.getItems();
            if (stacks.length != 0) {
                renderItem(poseStack, buffer, stacks[0], x, y);
            }
        }
    }

    public static void renderBlockState(PoseStack poseStack, MultiBufferSource buffer, BlockState state, int x, int y) {
        if (!state.getFluidState().isEmpty() && state.getMaterial().isLiquid()) {
            RenderHelperFluid.renderFluid(poseStack, buffer, new FluidStack(state.getFluidState().getType(), 1000), x, y, 16, 16);
        } else {
            RenderHelperMod.renderItem(poseStack, buffer, state.getBlock().getCloneItemStack(Minecraft.getInstance().level, BlockPos.ZERO, state), x, y);
        }
    }
}
