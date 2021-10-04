package de.melanx.recipeprinter.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.melanx.recipeprinter.RecipePrinter;
import de.melanx.recipeprinter.util.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.ModList;
import staticnet.minecraft.commands.Commandss.literal;

import java.util.Arrays;
import java.util.stream.Collectors;

import static de.melanx.recipeprinter.commands.FilteredResourceLocationArgument.resourceLocation;
import static de.melanx.recipeprinter.commands.RecipeSelectorArgument.recipeSelector;
import static net.minecraft.command.Commands.argument;

public class RecipePrinterCommands {

    public static void register(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> builer = literal(RecipePrinter.getInstance().modid).then(
                literal("recipe").then(argument("recipes", recipeSelector()).executes(new RecipeCommand()))
        ).then(
                literal("itemgroup").then(argument("group",
                        resourceLocation(() -> Arrays.stream(CreativeModeTab.TABS).filter(Util::isNormalItemCategory).map(group -> new ResourceLocation(group.getRecipeFolderName())).collect(Collectors.toList())))
                        .executes(new ItemCategoryCommand()))
        );

        if (ModList.get().isLoaded("jei")) {
            builer = builer.then(literal("fromJei").then(argument("recipes", recipeSelector()).executes(new JeiCommand())));
        }

        event.getDispatcher().register(builer);
    }
}
