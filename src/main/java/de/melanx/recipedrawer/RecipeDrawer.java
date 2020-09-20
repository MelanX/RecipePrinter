package de.melanx.recipedrawer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(RecipeDrawer.MODID)
public class RecipeDrawer {

    public static final String MODID = "recipedrawer";
    private static final Logger LOGGER = LogManager.getLogger(MODID);
    public RecipeDrawer instance;

    public RecipeDrawer() {
        instance = this;

        MinecraftForge.EVENT_BUS.register(this);
    }
}
