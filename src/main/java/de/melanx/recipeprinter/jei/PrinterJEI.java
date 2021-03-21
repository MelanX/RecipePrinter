package de.melanx.recipeprinter.jei;

import de.melanx.recipeprinter.RecipePrinter;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.MethodsReturnNonnullByDefault;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.load.registration.RecipeCategoryRegistration;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

@JeiPlugin
@MethodsReturnNonnullByDefault
public class PrinterJEI implements IModPlugin {
	@Nullable
	public static RecipeCategoryRegistration REG = null;

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(RecipePrinter.getInstance().modid, "fake_plugin");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		if (registration instanceof RecipeCategoryRegistration)
			REG = ((RecipeCategoryRegistration) registration);
	}
}
