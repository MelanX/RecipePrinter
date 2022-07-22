package de.melanx.recipeprinter.commands;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public class RecipeSelectorArgument implements ArgumentType<RecipeSelector> {

    public static RecipeSelectorArgument recipeSelector() {
        return new RecipeSelectorArgument();
    }

    private RecipeSelectorArgument() {

    }

    public RecipeSelector parse(StringReader reader) {
        int start = reader.getCursor();
        while (reader.canRead() && (ResourceLocation.isAllowedInResourceLocation(reader.peek()) || reader.peek() == '*')) {
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
        for (ResourceLocation rl : Minecraft.getInstance().getSingleplayerServer().getRecipeManager().getRecipeIds().toList()) {
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

    public static class Serializer implements ArgumentTypeInfo<RecipeSelectorArgument, Serializer.Template> {

        @Override
        public void serializeToNetwork(@Nonnull Template argument, @Nonnull FriendlyByteBuf buffer) {

        }

        @Nonnull
        @Override
        public Template deserializeFromNetwork(@Nonnull FriendlyByteBuf buffer) {
            return new Template();
        }

        @Override
        public void serializeToJson(@Nonnull Template argument, @Nonnull JsonObject json) {

        }

        @Nonnull
        @Override
        public Template unpack(@Nonnull RecipeSelectorArgument argument) {
            return new Template();
        }

        public final class Template implements ArgumentTypeInfo.Template<RecipeSelectorArgument> {

            @Nonnull
            @Override
            public RecipeSelectorArgument instantiate(@Nonnull CommandBuildContext context) {
                return new RecipeSelectorArgument();
            }

            @Nonnull
            @Override
            public ArgumentTypeInfo<RecipeSelectorArgument, ?> type() {
                return Serializer.this;
            }
        }
    }
}
