package de.melanx.recipedrawer.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.melanx.recipedrawer.ImageUtilities;
import de.melanx.recipedrawer.RecipeDrawer;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.BuiltInModel;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.SimpleBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PrintCommand {

    private static final int width = 124;
    private static final int height = 62;

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("print").executes(command -> {
            return runAll(command, null, "print");
        }).then(Commands.argument("modid", StringArgumentType.word()).suggests((context, provider) -> {
            return ISuggestionProvider.suggest(RecipeDrawer.ALL_MODIDS, provider);
        })
                .executes(command -> {
                    return run(command, StringArgumentType.getString(command, "modid"), "print");
                }));
    }

    private static int run(CommandContext<CommandSource> command, @Nullable String modid, String title) {
//        List<String> recipes = new ArrayList<>();
//        AdvancementManager manager = command.getSource().getWorld().getServer().getAdvancementManager();
//        manager.getAllAdvancements().forEach(advancement -> {
//            if (!advancement.getId().getPath().startsWith("recipes")) {
//                if (modid == null || advancement.getId().getNamespace().equals(modid))
//                    recipes.add(advancement.getId().toString());
//            }
//        });
//        command.getSource().sendFeedback(new TranslationTextComponent("Printed %s recipes", recipes.size()), true);
        return 1;
    }

    private static int runAll(CommandContext<CommandSource> command, @Nullable String modid, String title) throws CommandSyntaxException {
        try {
            MinecraftServer server = command.getSource().getServer();
            Minecraft client = Minecraft.getInstance();
            Path base = server.getDataDirectory().toPath().resolve(RecipeDrawer.MODID);
            if (!Files.exists(base)) Files.createDirectories(base);
            List<IRecipe<?>> recipes = new ArrayList<>();

            IResourceManager resourceManager = client.getResourceManager();
            ResourceLocation location = new ResourceLocation(RecipeDrawer.MODID, "textures/gui/crafting_gui.png");
            IResource resource = resourceManager.getResource(location);
            BufferedImage template = ImageIO.read(resource.getInputStream());
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            for (IRecipe<?> recipe : command.getSource().getServer().getRecipeManager().getRecipes()) {
                if (recipe.getType() == IRecipeType.CRAFTING) {
                    if (recipe.canFit(2, 2)) System.out.println("Recipe is 2x2");
                    else System.out.println("Recipe is 3x3");

                    Graphics2D g2d = bufferedImage.createGraphics();
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
                    g2d.drawImage(template, 0, 0, null);


                    NonNullList<ItemStack> ingredients = NonNullList.withSize(9, ItemStack.EMPTY);
                    int i = 0;
                    for (Ingredient ingredient : recipe.getIngredients()) {
                        if (recipe.canFit(2, 2)) {
                            if (i == 2) {
                                ingredients.set(i, ItemStack.EMPTY);
                                i++;
                            }
                            if (i > 4) {
                                continue;
                            }
                        }
                        if (!ingredient.hasNoMatchingItems()) {
                            ingredients.set(i, ingredient.getMatchingStacks()[0]);
                        }
                        i++;
                    }
                    System.out.println(recipe.getId());
                    i = 0;
                    if (recipe.getId().equals(ResourceLocation.tryCreate("minecraft:map")))
                        System.out.println("break");
                    for (ItemStack stack : ingredients) {
                        if (!stack.isEmpty()) {
                            BufferedImage icon = ImageUtilities.getTexture(stack, client.world, command.getSource().asPlayer());
                            g2d.drawImage(icon, 5, 5, null);
                        }
                    }
                    BufferedImage output = ImageUtilities.getTexture(recipe.getRecipeOutput(), client.world, command.getSource().asPlayer());
                    g2d.drawImage(output, 99, 23, null);
                    System.out.println("\n\n\n");
                    g2d.dispose();
                    File file = new File(base + "\\" + recipe.getId().getPath() + ".png");
                    ImageIO.write(bufferedImage, "png", file);
                    recipes.add(recipe);
                }
            }
            command.getSource().sendFeedback(new TranslationTextComponent("Printed %s recipes", recipes.size()), true);
        } catch (UnsupportedOperationException | IOException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }
}
