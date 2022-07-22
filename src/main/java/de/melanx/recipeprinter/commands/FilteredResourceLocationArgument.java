package de.melanx.recipeprinter.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class FilteredResourceLocationArgument implements ArgumentType<ResourceLocation> {

    public static FilteredResourceLocationArgument resourceLocation(Supplier<List<ResourceLocation>> args) {
        return new FilteredResourceLocationArgument(args);
    }

    private final Supplier<List<ResourceLocation>> args;

    private final ResourceLocationArgument rla = new ResourceLocationArgument();

    public FilteredResourceLocationArgument(Supplier<List<ResourceLocation>> args) {
        this.args = args;
    }

    public ResourceLocation parse(StringReader reader) throws CommandSyntaxException {
        ResourceLocation rl = this.rla.parse(reader);
        if (!this.args.get().contains(rl)) {
            throw new SimpleCommandExceptionType(Component.literal("This resource can not be found.")).create();
        }
        return rl;
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        StringReader reader = new StringReader(context.getInput());
        reader.setCursor(builder.getStart());
        String theString = reader.getRemaining().toLowerCase();
        for (ResourceLocation rl : this.args.get()) {
            if (rl.toString().toLowerCase().startsWith(theString))
                builder.suggest(rl.toString());
        }
        return CompletableFuture.completedFuture(builder.build());
    }

    public Collection<String> getExamples() {
        List<String> examples = new ArrayList<>();
        for (ResourceLocation rl : this.args.get())
            examples.add(rl.toString());
        return examples;
    }

    public static class Serializer implements ArgumentTypeInfo<FilteredResourceLocationArgument, Serializer.Template> {

        @Override
        public void serializeToNetwork(Serializer.Template template, FriendlyByteBuf buffer) {
            List<ResourceLocation> rls = template.args.get();
            buffer.writeInt(rls.size());
            for (ResourceLocation rl : rls) {
                buffer.writeResourceLocation(rl);
            }
        }

        @Nonnull
        @Override
        public Serializer.Template deserializeFromNetwork(@Nonnull FriendlyByteBuf buffer) {
            int amount = buffer.readInt();
            List<ResourceLocation> rls = new ArrayList<>();
            for (int i = 0; i < amount; i++) {
                rls.add(buffer.readResourceLocation());
            }

            return new Template(() -> rls);
        }

        @Override
        public void serializeToJson(Serializer.Template template, @Nonnull JsonObject json) {
            JsonArray list = new JsonArray();
            for (ResourceLocation rl : template.args.get()) {
                list.add(rl.toString());
            }

            json.add("resourceLocations", list);
        }

        @Nonnull
        @Override
        public Template unpack(@Nonnull FilteredResourceLocationArgument argument) {
            return new Template(argument.args);
        }

        public final class Template implements ArgumentTypeInfo.Template<FilteredResourceLocationArgument> {

            private final Supplier<List<ResourceLocation>> args;

            Template(Supplier<List<ResourceLocation>> args) {
                this.args = args;
            }

            @Nonnull
            @Override
            public FilteredResourceLocationArgument instantiate(@Nonnull CommandBuildContext context) {
                return new FilteredResourceLocationArgument(this.args);
            }

            @Nonnull
            @Override
            public ArgumentTypeInfo<FilteredResourceLocationArgument, ?> type() {
                return Serializer.this;
            }
        }
    }
}
