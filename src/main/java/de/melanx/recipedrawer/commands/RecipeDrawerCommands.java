package de.melanx.recipedrawer.commands;

import de.melanx.recipedrawer.RecipeDrawer;
import de.melanx.recipedrawer.util.Util;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegisterCommandsEvent;

import java.util.Arrays;
import java.util.stream.Collectors;

import static de.melanx.recipedrawer.commands.FilteredResourceLocationArgument.resourceLocation;
import static de.melanx.recipedrawer.commands.RecipeSelectorArgument.recipeSelector;
import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

public class RecipeDrawerCommands {

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(literal(RecipeDrawer.MODID).then(
                literal("recipe").then(argument("recipes", recipeSelector()).executes(new RecipeCommand()))
        ).then(
                literal("itemgroup").then(argument("group",
                        resourceLocation(() -> Arrays.stream(ItemGroup.GROUPS).filter(Util::isNormalItemGroup).map(group -> new ResourceLocation(group.getPath())).collect(Collectors.toList())))
                .executes(new ItemGroupCommand()))
        ));
    }
}
