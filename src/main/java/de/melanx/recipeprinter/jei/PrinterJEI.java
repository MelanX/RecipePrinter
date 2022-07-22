package de.melanx.recipeprinter.jei;

import de.melanx.recipeprinter.RecipePrinter;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.common.Internal;
import mezz.jei.common.gui.textures.JeiSpriteUploader;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.common.load.registration.RecipeCategoryRegistration;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@JeiPlugin
@MethodsReturnNonnullByDefault
public class PrinterJEI implements IModPlugin {
    @Nullable
    public static RecipeCategoryRegistration REG = null;

    private static final JeiSpriteUploader JEI_SPRITE_UPLOADER = new JeiSpriteUploader(Minecraft.getInstance().textureManager);
    public static final Textures TEXTURES;

    static {
        TEXTURES = new Textures(JEI_SPRITE_UPLOADER);
        Internal.setTextures(TEXTURES);
    }

    public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(JEI_SPRITE_UPLOADER);
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(RecipePrinter.getInstance().modid, "fake_plugin");
    }

    @Override
    public void registerCategories(@Nonnull IRecipeCategoryRegistration registration) {
        if (registration instanceof RecipeCategoryRegistration reg) {
            REG = reg;
        }
    }
}
