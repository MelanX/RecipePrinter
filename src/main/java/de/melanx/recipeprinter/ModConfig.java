package de.melanx.recipeprinter;

import io.github.noeppi_noeppi.libx.annotation.config.RegisterConfig;
import io.github.noeppi_noeppi.libx.config.Config;
import io.github.noeppi_noeppi.libx.config.validator.IntRange;

@RegisterConfig(client = true)
public class ModConfig {

    @Config("Scale for the images.")
    @IntRange(min = 1)
    public static int scale = 5;

    @Config("The amount of items per row when rendering creative tabs")
    @IntRange(min = 1)
    public static int itemsPerRow;
}
