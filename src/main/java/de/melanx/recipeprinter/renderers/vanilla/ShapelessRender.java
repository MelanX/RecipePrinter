package de.melanx.recipeprinter.renderers.vanilla;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.OverlayIcon;
import de.melanx.recipeprinter.util.RenderHelperMod;
import de.melanx.recipeprinter.util.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapelessRecipe;

import javax.annotation.Nullable;
import java.util.List;

public class ShapelessRender implements IRecipeRender<ShapelessRecipe> {

    public static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("minecraft", "textures/gui/container/crafting_table.png");

    @Override
    public Class<ShapelessRecipe> getRecipeClass() {
        return ShapelessRecipe.class;
    }

    @Nullable
    @Override
    public RecipeType<? super ShapelessRecipe> getRecipeType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public int getRecipeWidth() {
        return 124;
    }

    @Override
    public int getRecipeHeight() {
        return 62;
    }

    @Override
    public void render(ShapelessRecipe recipe, PoseStack poseStack, MultiBufferSource buffer) {
        RenderHelperMod.renderBackground(BACKGROUND_TEXTURE, poseStack, buffer, 25, 12, 124, 62, true);
        poseStack.pushPose();
        poseStack.translate(108, 4, 0);
        poseStack.scale(1f / 3, 1f / 3, 1);
        RenderHelperMod.render(OverlayIcon.SHAPELESS, poseStack, buffer, 0, 0);
        poseStack.popPose();
        RenderHelperMod.renderItem(poseStack, buffer, Util.getResultItem(recipe), 99, 23);
        List<Ingredient> ingredients = recipe.getIngredients();
        int max = ingredients.size() > 4 ? 3 : 2;
        for (int x = 0; x < max; x++) {
            for (int y = 0; y < max; y++) {
                int idx = x + (max * y);
                if (ingredients.size() == 1) {
                    RenderHelperMod.renderIngredient(poseStack, buffer, ingredients.get(idx), 23, 23);
                    return;
                } else if (idx < ingredients.size()) {
                    RenderHelperMod.renderIngredient(poseStack, buffer, ingredients.get(idx), 5 + (x * 18), 5 + (y * 18));
                }
            }
        }
    }
}
