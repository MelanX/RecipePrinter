package de.melanx.recipeprinter.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import de.melanx.recipeprinter.Config;
import de.melanx.recipeprinter.RecipePrinter;
import de.melanx.recipeprinter.util.ImageHelper;
import de.melanx.recipeprinter.util.Util;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class ItemGroupCommand implements Command<CommandSource> {

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ResourceLocation rl = context.getArgument("group", ResourceLocation.class);
        ItemGroup group = Arrays.stream(ItemGroup.GROUPS).filter(g -> rl.getPath().equalsIgnoreCase(g.getPath())).findFirst().orElse(null);
        if (group == null || !Util.isNormalItemGroup(group)) {
            throw new SimpleCommandExceptionType(new StringTextComponent("This ItemGroup does not exist.")).create();
        }
        NonNullList<ItemStack> stacks = NonNullList.create();
        group.fill(stacks);

        if (stacks.isEmpty()) {
            throw new SimpleCommandExceptionType(new StringTextComponent("This ItemGroup is empty")).create();
        }

        int itemsPerRow = Config.itemsPerRow.get();
        int rows = stacks.size() / itemsPerRow;
        if (rows * itemsPerRow < stacks.size())
            rows += 1;
        int effectiveFinalRows = rows;

        Path path = context.getSource().getServer().getDataDirectory().toPath().resolve(RecipePrinter.getInstance().modid).resolve("item_groups").resolve(group.getPath().replace('/', '-') + ".png");
        if (!Files.exists(path.getParent())) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        ImageHelper.addRenderJob(itemsPerRow * 18 + 8, rows * 18 + 24, Config.scale.get(), (matrixStack, buffer) -> Util.renderItemGroup(matrixStack, buffer, stacks, effectiveFinalRows, itemsPerRow, group), path, true);

        context.getSource().sendFeedback(new StringTextComponent("Started rendering ItemGroup " + group.getPath()), true);

        return 0;
    }
}
