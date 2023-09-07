package de.melanx.recipeprinter.commands;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import de.melanx.recipeprinter.ModConfig;
import de.melanx.recipeprinter.RecipePrinter;
import de.melanx.recipeprinter.util.PrinterJob;
import de.melanx.recipeprinter.util.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import org.moddingx.libx.render.target.ImageHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ItemCategoryCommand implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String name = StringArgumentType.getString(context, "group");
        CreativeModeTab group = CreativeModeTabs.allTabs().stream().filter(g -> g.getDisplayName().getString().equals(name)).findFirst().orElse(null);
        if (group == null || !Util.isNormalItemCategory(group)) {
            throw new SimpleCommandExceptionType(Component.literal("This ItemGroup does not exist.")).create();
        }
        NonNullList<ItemStack> stacks = NonNullList.create();
        stacks.addAll(group.getDisplayItems());

        if (stacks.isEmpty()) {
            throw new SimpleCommandExceptionType(Component.literal("This ItemGroup is empty")).create();
        }

        int itemsPerRow = ModConfig.itemsPerRow;
        int rows = stacks.size() / itemsPerRow;
        if (rows * itemsPerRow < stacks.size()) {
            rows += 1;
        }
        int effectiveFinalRows = rows;

        Path path = context.getSource().getServer().getServerDirectory().toPath().resolve(RecipePrinter.getInstance().modid).resolve("item_groups").resolve(group.getDisplayName().getString().replace('/', '-') + ".png");
        if (!Files.exists(path.getParent())) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        CompletableFuture<NativeImage> img = ImageHelper.render(new PrinterJob(itemsPerRow * 18 + 8, rows * 18 + 24, ModConfig.scale, guiGraphics -> {
            Util.renderItemCategory(guiGraphics, stacks, effectiveFinalRows, itemsPerRow, group);
        }));

        try {
            img.get().writeToFile(path);
        } catch (IOException | InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        context.getSource().sendSuccess(() -> Component.literal("Started rendering ItemGroup " + group.getDisplayName().getString()), true);

        return 0;
    }
}
