package de.melanx.recipeprinter.jei;

import de.melanx.recipeprinter.RecipePrinter;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.library.load.registration.RecipeCategoryRegistration;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@JeiPlugin
@MethodsReturnNonnullByDefault
public class PrinterJEI implements IModPlugin {

    @Nullable
    public static RecipeCategoryRegistration REG = null;

    public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
//        event.registerReloadListener(Internal.getTextures());
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
