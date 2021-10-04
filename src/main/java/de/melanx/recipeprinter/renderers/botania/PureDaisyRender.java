package de.melanx.recipeprinter.renderers.botania;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelperMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.botania.api.recipe.IPureDaisyRecipe;
import vazkii.botania.common.block.ModSubtiles;
import vazkii.botania.common.crafting.ModRecipeTypes;

import javax.annotation.Nullable;

public class PureDaisyRender implements IRecipeRender<IPureDaisyRecipe> {

    public static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation("botania", "textures/gui/pure_daisy_overlay.png");

    @Override
    public Class<IPureDaisyRecipe> getRecipeClass() {
        return IPureDaisyRecipe.class;
    }

    @Nullable
    @Override
    public RecipeType<? super IPureDaisyRecipe> getRecipeType() {
        return ModRecipeTypes.PURE_DAISY_TYPE;
    }

    @Override
    public int getRecipeWidth() {
        return 104;
    }

    @Override
    public int getRecipeHeight() {
        return 52;
    }

    @Override
    public void render(IPureDaisyRecipe recipe, PoseStack poseStack, MultiBufferSource buffer) {
        RenderHelperMod.renderDefaultBackground(poseStack, buffer, this.getRecipeWidth(), this.getRecipeHeight());
        poseStack.pushPose();
        poseStack.translate(21, 5, 0);
        RenderHelperMod.renderBackground(OVERLAY_TEXTURE, poseStack, buffer, 0, 0, 65, 44);
        poseStack.popPose();
        RenderHelperMod.renderItem(poseStack, buffer, new ItemStack(ModSubtiles.pureDaisy), 44, 18);

        BlockState input = recipe.getInput().getDisplayed().get(0);
        RenderHelperMod.renderSlot(poseStack, buffer, 14, 18);
        RenderHelperMod.renderBlockState(poseStack, buffer, input, 14, 18);

        BlockState output = recipe.getOutputState();
        RenderHelperMod.renderSlot(poseStack, buffer, 73, 18);
        RenderHelperMod.renderBlockState(poseStack, buffer, output, 73, 18);
    }
}
