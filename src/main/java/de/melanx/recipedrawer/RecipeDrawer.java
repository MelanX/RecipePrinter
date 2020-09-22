package de.melanx.recipedrawer;

import com.mojang.brigadier.CommandDispatcher;
import de.melanx.recipedrawer.commands.PrintCommand;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL12;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Mod(RecipeDrawer.MODID)
public class RecipeDrawer {

    public static final String MODID = "recipedrawer";
    public static final List<String> ALL_MODIDS = new ArrayList<>();
    private static final Logger LOGGER = LogManager.getLogger(MODID);
    public RecipeDrawer instance;

    public RecipeDrawer() {
        instance = this;

        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onCommonSetup);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal(MODID)
                .then(PrintCommand.register()));
    }

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
            if (event.getState().getBlock() == Blocks.GRASS_BLOCK) {
                try {
                    Path base = FMLPaths.GAMEDIR.get().resolve(RecipeDrawer.MODID);
                    IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
                    ResourceLocation location = new ResourceLocation(RecipeDrawer.MODID, "gui/crafting_gui");
                    IResource resource = resourceManager.getResource(location);
                    BufferedImage template = ImageIO.read(resource.getInputStream());
                    BufferedImage bufferedImage = new BufferedImage(62, 124, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2d = bufferedImage.createGraphics();
                    g2d.drawImage(template, 0, 0, null);
                    g2d.setBackground(Color.BLUE);
                    g2d.dispose();
                    File file = new File(base + "\\image.png");
                    ImageIO.write(bufferedImage, "png", file);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        ModList.get().getModFiles().forEach(file -> file.getMods().forEach(mod -> ALL_MODIDS.add(mod.getModId())));
    }
}
