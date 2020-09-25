package de.melanx.recipeprinter.commands;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.command.arguments.IArgumentSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class RecipeSelectorArgument implements ArgumentType<RecipeSelector> {

    public static RecipeSelectorArgument recipeSelector() {
        return new RecipeSelectorArgument();
    }

    private RecipeSelectorArgument() {

    }

    public RecipeSelector parse(StringReader reader) {
        int start = reader.getCursor();
        while(reader.canRead() && (ResourceLocation.isValidPathCharacter(reader.peek()) || reader.peek() == '*')) {
            reader.skip();
        }
        String str = reader.getString().substring(start, reader.getCursor());
        return new RecipeSelector(str);
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        StringReader reader = new StringReader(context.getInput());
        reader.setCursor(builder.getStart());
        String theString = reader.getRemaining().toLowerCase();
        if (theString.endsWith(":") && theString.indexOf(':') == theString.lastIndexOf(':')) {
            builder.suggest(theString + "*");
        }
        //noinspection ConstantConditions
        for (ResourceLocation rl : Minecraft.getInstance().getIntegratedServer().getRecipeManager().getKeys().collect(Collectors.toList())) {
            if (rl.toString().toLowerCase().startsWith(theString))
                builder.suggest(rl.toString());
        }
        return CompletableFuture.completedFuture(builder.build());
    }

    public Collection<String> getExamples() {
        return ImmutableList.of(
                "*",
                "minecraft:*"
        );
    }

    public static class Serializer implements IArgumentSerializer<RecipeSelectorArgument> {

        @Override
        public void write(@Nonnull RecipeSelectorArgument argument, @Nonnull PacketBuffer buffer) {

        }

        @Nonnull
        @Override
        public RecipeSelectorArgument read(@Nonnull PacketBuffer buffer) {
            return new RecipeSelectorArgument();
        }

        @Override
        public void write(@Nonnull RecipeSelectorArgument argument, @Nonnull JsonObject json) {

        }
    }
}
