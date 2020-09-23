package de.melanx.recipedrawer.commands;

import de.melanx.recipedrawer.RecipeDrawer;
import net.minecraftforge.event.RegisterCommandsEvent;

import static net.minecraft.command.Commands.*;
import static de.melanx.recipedrawer.commands.RecipeSelectorArgument.*;

public class RecipeDrawerCommands {

    public static void register(RegisterCommandsEvent event) {
        event.getDispatcher().register(literal(RecipeDrawer.MODID)
                .then(argument("recipes", recipeSelector()).executes(new RecipeDrawerCommand()))
        );
    }
}
