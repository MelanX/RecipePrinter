package de.melanx.recipeprinter;

import org.checkerframework.common.value.qual.IntRange;
import org.moddingx.libx.annotation.config.RegisterConfig;
import org.moddingx.libx.config.Config;

@RegisterConfig(client = true)
public class ModConfig {

    @Config("Scale for the images.")
    @IntRange(from = 1)
    public static int scale = 5;

    @Config("The amount of items per row when rendering creative tabs")
    @IntRange(from = 1)
    public static int itemsPerRow = 9;
}
