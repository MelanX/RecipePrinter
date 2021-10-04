//package de.melanx.recipeprinter.renderers.botania;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.melanx.recipeprinter.IRecipeRender;
//import de.melanx.recipeprinter.util.RenderHelperMod;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.world.item.crafting.RecipeType;
//import vazkii.botania.api.recipe.IElvenTradeRecipe;
//import vazkii.botania.common.crafting.ModRecipeTypes;
//
//import javax.annotation.Nullable;
//
//public class ElvenTradeRender implements IRecipeRender<IElvenTradeRecipe> {
//
//    private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation("botania", "textures/gui/elven_trade_overlay.png");
//    private static final ResourceLocation PORTAL_TEXTURE = new ResourceLocation("botania", "textures/block/alfheim_portal_swirl.png");
//
//    @Override
//    public Class<IElvenTradeRecipe> getRecipeClass() {
//        return IElvenTradeRecipe.class;
//    }
//
//    @Nullable
//    @Override
//    public RecipeType<? super IElvenTradeRecipe> getRecipeType() {
//        return ModRecipeTypes.ELVEN_TRADE_TYPE;
//    }
//
//    @Override
//    public int getRecipeWidth() {
//        return 153;
//    }
//
//    @Override
//    public int getRecipeHeight() {
//        return 103;
//    }
//
//    @Override
//    public void render(IElvenTradeRecipe recipe, PoseStack poseStack, MultiBufferSource buffer) {
//        RenderHelperMod.renderDefaultBackground(poseStack, buffer, this.getRecipeWidth(), this.getRecipeHeight());
//        RenderHelperMod.renderBackground(OVERLAY_TEXTURE, poseStack, buffer, 0, 7, 91, 87);
//
//        poseStack.pushPose();
//        poseStack.translate(22, 29, 0);
//        RenderHelperMod.renderBackground(PORTAL_TEXTURE, poseStack, buffer, 0, 0, 48, 48, 48, 768);
//        poseStack.popPose();
//
//        for (int i = 0; i < recipe.getIngredients().size(); i++) {
//            Ingredient ingredient = recipe.getIngredients().get(i);
//            RenderHelperMod.renderSlot(poseStack, buffer, 43 + (18 * i), 5);
//            RenderHelperMod.renderIngredient(poseStack, buffer, ingredient, 43 + (18 * i), 5);
//        }
//        for (int i = 0; i < recipe.getOutputs().size(); i++) {
//            RenderHelperMod.renderSlot(poseStack, buffer, 93 + (18 * i), 46);
//            RenderHelperMod.renderItem(poseStack, buffer, recipe.getOutputs().get(i), 93 + (18 * i), 46);
//        }
//    }
//}
