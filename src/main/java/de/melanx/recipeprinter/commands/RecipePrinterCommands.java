package de.melanx.recipeprinter.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import de.melanx.recipeprinter.RecipePrinter;
import de.melanx.recipeprinter.RecipeRenderers;
import de.melanx.recipeprinter.util.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.ModList;

import java.util.Optional;

import static de.melanx.recipeprinter.commands.RecipeSelectorArgument.recipeSelector;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class RecipePrinterCommands {

    public static final SuggestionProvider<CommandSourceStack> TABS = ((context, builder) -> SharedSuggestionProvider
            .suggest(CreativeModeTabs.allTabs().stream().filter(Util::isNormalItemCategory).map(group -> ("\"" + group.getDisplayName().getString() + "\"")), builder));
    public static final SuggestionProvider<CommandSourceStack> RECIPE_TYPES = ((context, builder) -> {
        Optional<Registry<RecipeType<?>>> registry = context.getSource().getServer().registryAccess().registry(Registries.RECIPE_TYPE);
        return registry.map(recipeTypes -> SharedSuggestionProvider.suggest(recipeTypes.stream().filter(RecipeRenderers::isSupported).map(Object::toString), builder)).orElse(null);
    });

    public static void register(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> builder = literal(RecipePrinter.getInstance().modid)
                .then(literal("recipe")
                        .then(argument("recipes", recipeSelector())
                                .executes(new RecipeCommand())))
                .then(literal("recipeType")
                        .then(argument("type", StringArgumentType.word()).suggests(RECIPE_TYPES)
                                .executes(new RecipeTypeCommand())))
                .then(literal("itemgroup")
                        .then(argument("group", StringArgumentType.string()).suggests(TABS)
                                .executes(new ItemCategoryCommand()))
                );

        if (ModList.get().isLoaded("jei")) {
            builder = builder.then(literal("fromJei").then(argument("recipes", recipeSelector()).executes(new JeiCommand())));
        }

        event.getDispatcher().register(builder);
    }
}
