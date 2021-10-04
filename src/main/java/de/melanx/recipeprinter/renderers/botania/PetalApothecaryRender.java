package de.melanx.recipeprinter.renderers.botania;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelperMod;
import de.melanx.recipeprinter.util.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.phys.Vec2;
import vazkii.botania.api.recipe.IPetalRecipe;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.crafting.ModRecipeTypes;

import javax.annotation.Nullable;

public class PetalApothecaryRender implements IRecipeRender<IPetalRecipe> {

    private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation("botania", "textures/gui/petal_overlay.png");

    @Override
    public Class<IPetalRecipe> getRecipeClass() {
        return IPetalRecipe.class;
    }

    @Nullable
    @Override
    public RecipeType<? super IPetalRecipe> getRecipeType() {
        return ModRecipeTypes.PETAL_TYPE;
    }

    @Override
    public int getRecipeWidth() {
        return 122;
    }

    @Override
    public int getRecipeHeight() {
        return 105;
    }

    @Override
    public void render(IPetalRecipe recipe, PoseStack poseStack, MultiBufferSource buffer) {
        RenderHelperMod.renderDefaultBackground(poseStack, buffer, this.getRecipeWidth(), this.getRecipeHeight());
        RenderHelperMod.renderBackground(OVERLAY_TEXTURE, poseStack, buffer, 19, 0, 108, 93);
        RenderHelperMod.renderItem(poseStack, buffer, new ItemStack(ModBlocks.defaultAltar), 47, 53);

        double angleBetweenEach = 360.0 / recipe.getIngredients().size();
        Vec2 point = new Vec2(47, 21);
        Vec2 center = new Vec2(47, 53);

        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            RenderHelperMod.renderIngredient(poseStack, buffer, recipe.getIngredients().get(i), Math.round(point.x), Math.round(point.y));
            point = Util.rotatePointAbout(point, center, angleBetweenEach);
        }
        RenderHelperMod.renderItem(poseStack, buffer, recipe.getResultItem(), 85, 18);
    }
}
