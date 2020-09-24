package de.melanx.recipedrawer.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import de.melanx.recipedrawer.RecipeDrawer;
import de.melanx.recipedrawer.util.ImageHelper;
import de.melanx.recipedrawer.util.Util;
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

        int rows = stacks.size() / 9;
        if (rows * 9 < stacks.size())
            rows += 1;
        int effectiveFinalRows = rows;

        Path path = context.getSource().getServer().getDataDirectory().toPath().resolve(RecipeDrawer.MODID).resolve("item_groups").resolve(group.getPath().replace('/', '-') + ".png");
        if (!Files.exists(path.getParent())) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        ImageHelper.addRenderJob(170, rows * 18 + 8, 5, (matrixStack, buffer) -> Util.renderItemGroup(matrixStack, buffer, stacks, effectiveFinalRows), path);

        context.getSource().sendFeedback(new StringTextComponent("Started rendering ItemGroup " + group.getPath()), true);

        return 0;
    }
}
