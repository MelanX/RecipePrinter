package de.melanx.recipeprinter.renderers.vanilla;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelperMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmokingRecipe;

import javax.annotation.Nullable;

public class SmokingRender implements IRecipeRender<SmokingRecipe> {

    @Override
    public Class<SmokingRecipe> getRecipeClass() {
        return SmokingRecipe.class;
    }

    @Nullable
    @Override
    public RecipeType<? super SmokingRecipe> getRecipeType() {
        return RecipeType.SMOKING;
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
    public void render(SmokingRecipe recipe, PoseStack poseStack, MultiBufferSource buffer) {
        SmeltingRender.render(recipe, poseStack, buffer);
        RenderHelperMod.renderItem(poseStack, buffer, new ItemStack(Items.SMOKER), 5, 41);
    }
}
