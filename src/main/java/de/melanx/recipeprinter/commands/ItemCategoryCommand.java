package de.melanx.recipeprinter.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import de.melanx.recipeprinter.Config;
import de.melanx.recipeprinter.RecipePrinter;
import de.melanx.recipeprinter.util.ImageHelper;
import de.melanx.recipeprinter.util.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class ItemCategoryCommand implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ResourceLocation rl = context.getArgument("group", ResourceLocation.class);
        CreativeModeTab group = Arrays.stream(CreativeModeTab.TABS).filter(g -> rl.getPath().equalsIgnoreCase(g.getRecipeFolderName())).findFirst().orElse(null);
        if (group == null || !Util.isNormalItemCategory(group)) {
            throw new SimpleCommandExceptionType(new TextComponent("This ItemGroup does not exist.")).create();
        }
        NonNullList<ItemStack> stacks = NonNullList.create();
        group.fillItemList(stacks);

        if (stacks.isEmpty()) {
            throw new SimpleCommandExceptionType(new TextComponent("This ItemGroup is empty")).create();
        }

        int itemsPerRow = Config.itemsPerRow.get();
        int rows = stacks.size() / itemsPerRow;
        if (rows * itemsPerRow < stacks.size())
            rows += 1;
        int effectiveFinalRows = rows;

        Path path = context.getSource().getServer().getServerDirectory().toPath().resolve(RecipePrinter.getInstance().modid).resolve("item_groups").resolve(group.getRecipeFolderName().replace('/', '-') + ".png");
        if (!Files.exists(path.getParent())) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        ImageHelper.addRenderJob(itemsPerRow * 18 + 8, rows * 18 + 24, Config.scale.get(), (matrixStack, buffer) -> Util.renderItemCategory(matrixStack, buffer, stacks, effectiveFinalRows, itemsPerRow, group), path, true);

        context.getSource().sendSuccess(new TextComponent("Started rendering ItemGroup " + group.getRecipeFolderName()), true);

        return 0;
    }
}
