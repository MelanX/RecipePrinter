package de.melanx.recipeprinter.renderers.botania;

import com.mojang.blaze3d.vertex.PoseStack;
import de.melanx.recipeprinter.IRecipeRender;
import de.melanx.recipeprinter.util.RenderHelperMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import vazkii.botania.api.recipe.IManaInfusionRecipe;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.crafting.ModRecipeTypes;

import javax.annotation.Nullable;

public class InfusionRender implements IRecipeRender<IManaInfusionRecipe> {

    public static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation("botania", "textures/gui/pure_daisy_overlay.png");
    private final ItemStack pool = new ItemStack(ModBlocks.manaPool);

    public InfusionRender() {
        ItemNBTHelper.setBoolean(this.pool, "RenderFull", true);
    }

    @Override
    public Class<IManaInfusionRecipe> getRecipeClass() {
        return IManaInfusionRecipe.class;
    }

    @Nullable
    @Override
    public RecipeType<? super IManaInfusionRecipe> getRecipeType() {
        return ModRecipeTypes.MANA_INFUSION_TYPE;
    }

    @Override
    public int getRecipeWidth() {
        return 150;
    }

    @Override
    public int getRecipeHeight() {
        return 63;
    }

    @Override
    public void render(IManaInfusionRecipe recipe, PoseStack poseStack, MultiBufferSource buffer) {
        RenderHelperMod.renderDefaultBackground(poseStack, buffer, this.getRecipeWidth(), this.getRecipeHeight());
        poseStack.pushPose();
        poseStack.translate(44, 4, 0);
        RenderHelperMod.renderBackground(OVERLAY_TEXTURE, poseStack, buffer, 0, 0, 65, 44);
        poseStack.popPose();
        HUDHandler.renderManaBar(poseStack, 24, 54, 0x0000FF, 0.75F, recipe.getManaToConsume(), TilePool.MAX_MANA / 10);
        RenderHelperMod.renderItem(poseStack, buffer, this.pool, 67, 17);

        RenderHelperMod.renderSlot(poseStack, buffer, 37, 17);
        RenderHelperMod.renderIngredient(poseStack, buffer, recipe.getIngredients().get(0), 37, 17);

        RenderHelperMod.renderSlot(poseStack, buffer, 96, 17);
        RenderHelperMod.renderItem(poseStack, buffer, recipe.getResultItem(), 96, 17);
    }
}
