//package de.melanx.recipeprinter.renderers.botania;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.melanx.recipeprinter.IRecipeRender;
//import de.melanx.recipeprinter.util.OverlayIcon;
//import de.melanx.recipeprinter.util.RenderHelperMod;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.RecipeType;
//import vazkii.botania.api.recipe.IBrewRecipe;
//import vazkii.botania.common.crafting.ModRecipeTypes;
//import vazkii.botania.common.item.ModItems;
//
//import javax.annotation.Nullable;
//
//public class BrewRender implements IRecipeRender<IBrewRecipe> {
//
//    private static final ItemStack VIAL = new ItemStack(ModItems.vial);
//
//    @Override
//    public Class<IBrewRecipe> getRecipeClass() {
//        return IBrewRecipe.class;
//    }
//
//    @Nullable
//    @Override
//    public RecipeType<? super IBrewRecipe> getRecipeType() {
//        return ModRecipeTypes.BREW_TYPE;
//    }
//
//    @Override
//    public int getRecipeWidth() {
//        return 139;
//    }
//
//    @Override
//    public int getRecipeHeight() {
//        return 63;
//    }
//
//    @Override
//    public void render(IBrewRecipe recipe, PoseStack poseStack, MultiBufferSource buffer) {
//        RenderHelperMod.renderDefaultBackground(poseStack, buffer, this.getRecipeWidth(), this.getRecipeHeight());
//        RenderHelperMod.renderSlot(poseStack, buffer, 15, 40);
//        RenderHelperMod.renderItem(poseStack, buffer, VIAL, 15, 40);
//
//        RenderHelperMod.renderSlot(poseStack, buffer, 63, 40);
//        RenderHelperMod.renderItem(poseStack, buffer, recipe.getOutput(VIAL), 63, 40);
//
//        int posX = 72 - (recipe.getIngredients().size() * 9);
//        for (int i = 0; i < recipe.getIngredients().size(); i++) {
//            RenderHelperMod.renderSlot(poseStack, buffer, posX, 5);
//            RenderHelperMod.renderIngredient(poseStack, buffer, recipe.getIngredients().get(i), posX, 5);
//            posX += 18;
//        }
//
//        RenderHelperMod.render(OverlayIcon.ARROW, poseStack, buffer, 36, 40);
//        RenderHelperMod.render(OverlayIcon.ARROW_DOWN, poseStack, buffer, 63, 23);
//    }
//}
