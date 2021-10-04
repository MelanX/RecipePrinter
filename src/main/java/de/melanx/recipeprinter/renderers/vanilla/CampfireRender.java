package de.melanx.recipeprinter.renderers.vanilla;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelperMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;

public class CampfireRender implements IRecipeRender<CampfireCookingRecipe> {

    @Override
    public Class<CampfireCookingRecipe> getRecipeClass() {
        return CampfireCookingRecipe.class;
    }

    @Nullable
    @Override
    public RecipeType<? super CampfireCookingRecipe> getRecipeType() {
        return RecipeType.CAMPFIRE_COOKING;
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
    public void render(CampfireCookingRecipe recipe, PoseStack poseStack, MultiBufferSource buffer) {
        SmeltingRender.render(recipe, poseStack, buffer);
        RenderHelperMod.renderItem(poseStack, buffer, new ItemStack(Items.CAMPFIRE), 5, 41);
    }
}
