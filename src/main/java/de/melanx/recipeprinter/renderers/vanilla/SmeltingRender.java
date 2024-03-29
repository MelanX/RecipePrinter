package de.melanx.recipeprinter.renderers.vanilla;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.RecipePrinter;
import de.melanx.recipeprinter.util.RenderHelperMod;
import de.melanx.recipeprinter.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import org.moddingx.libx.render.RenderHelper;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SmeltingRender implements IRecipeRender<SmeltingRecipe> {

    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/furnace.png");

    @Override
    public Class<SmeltingRecipe> getRecipeClass() {
        return SmeltingRecipe.class;
    }

    @Nullable
    @Override
    public RecipeType<? super SmeltingRecipe> getRecipeType() {
        return RecipeType.SMELTING;
    }

    @Override
    public int getRecipeWidth() {
        return 90;
    }

    @Override
    public int getRecipeHeight() {
        return 62;
    }

    @Override
    public void render(SmeltingRecipe recipe, PoseStack poseStack, MultiBufferSource buffer) {
        SmeltingRender.render((AbstractCookingRecipe) recipe, poseStack, buffer);
        RenderHelperMod.renderItem(poseStack, buffer, new ItemStack(Items.FURNACE), 5, 41);
    }

    public static void render(AbstractCookingRecipe recipe, PoseStack poseStack, MultiBufferSource buffer) {
        RenderHelperMod.renderBackground(BACKGROUND_TEXTURE, poseStack, buffer, 51, 12, 90, 62, true);
        poseStack.pushPose();
        poseStack.translate(6, 25, 10);
        RenderHelperMod.renderBackground(BACKGROUND_TEXTURE, poseStack, buffer, 176, 0, 14, 14, false);
        poseStack.popPose();
        RenderHelperMod.renderItem(poseStack, buffer, Util.getResultItem(recipe), 65, 23);
        RenderHelperMod.renderIngredient(poseStack, buffer, recipe.getIngredients().get(0), 5, 5);
        MutableComponent time = Component.translatable(RecipePrinter.getInstance().modid + ".time", BigDecimal.valueOf(recipe.getCookingTime() / 20d).setScale(2, RoundingMode.HALF_UP).toPlainString());
        Minecraft.getInstance().font.draw(poseStack, time.getString(), 26, 48, RenderHelperMod.TEXT_COLOR);
        RenderHelper.resetColor();
    }
}
