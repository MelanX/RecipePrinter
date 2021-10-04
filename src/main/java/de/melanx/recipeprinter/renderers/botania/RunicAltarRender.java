//package de.melanx.recipeprinter.renderers.botania;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import de.melanx.recipeprinter.IRecipeRender;
//import de.melanx.recipeprinter.util.RenderHelperMod;
//import de.melanx.recipeprinter.util.Util;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.RecipeType;
//import net.minecraft.world.phys.Vec2;
//import vazkii.botania.api.recipe.IRuneAltarRecipe;
//import vazkii.botania.client.core.handler.HUDHandler;
//import vazkii.botania.common.block.ModBlocks;
//import vazkii.botania.common.block.tile.mana.TilePool;
//import vazkii.botania.common.crafting.ModRecipeTypes;
//
//import javax.annotation.Nullable;
//
//public class RunicAltarRender implements IRecipeRender<IRuneAltarRecipe> {
//
//    private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation("botania", "textures/gui/petal_overlay.png");
//
//    @Override
//    public Class<IRuneAltarRecipe> getRecipeClass() {
//        return IRuneAltarRecipe.class;
//    }
//
//    @Nullable
//    @Override
//    public RecipeType<? super IRuneAltarRecipe> getRecipeType() {
//        return ModRecipeTypes.RUNE_TYPE;
//    }
//
//    @Override
//    public int getRecipeWidth() {
//        return 122;
//    }
//
//    @Override
//    public int getRecipeHeight() {
//        return 112;
//    }
//
//    @Override
//    public void render(IRuneAltarRecipe recipe, PoseStack poseStack, MultiBufferSource buffer) {
//        RenderHelperMod.renderDefaultBackground(poseStack, buffer, this.getRecipeWidth(), this.getRecipeHeight());
//        RenderHelperMod.renderBackground(OVERLAY_TEXTURE, poseStack, buffer, 19, 0, 108, 93);
//        RenderHelperMod.renderItem(poseStack, buffer, new ItemStack(ModBlocks.runeAltar), 47, 53);
//        HUDHandler.renderManaBar(poseStack, 10, 102, 0x0000FF, 0.75F, recipe.getManaUsage(), TilePool.MAX_MANA / 10);
//
//        double angleBetweenEach = 360.0 / recipe.getIngredients().size();
//        Vec2 point = new Vec2(47, 21);
//        Vec2 center = new Vec2(47, 53);
//
//        for (int i = 0; i < recipe.getIngredients().size(); i++) {
//            RenderHelperMod.renderIngredient(poseStack, buffer, recipe.getIngredients().get(i), Math.round(point.x), Math.round(point.y));
//            point = Util.rotatePointAbout(point, center, angleBetweenEach);
//        }
//        RenderHelperMod.renderItem(poseStack, buffer, recipe.getResultItem(), 85, 18);
//    }
//}
