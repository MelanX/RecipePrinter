package de.melanx.recipeprinter;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec CONFIG;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    static {
        init(BUILDER);
        CONFIG = BUILDER.build();
    }

    public static ForgeConfigSpec.IntValue scale;
    public static ForgeConfigSpec.IntValue itemsPerRow;

    public static void init(ForgeConfigSpec.Builder builder) {
        scale = builder.comment("Scale for the images.")
                .defineInRange("scale", 5, 1, Integer.MAX_VALUE);
        itemsPerRow = builder.comment("The amount of items per row when rendering creative tabs")
                .defineInRange("items_per_row", 9, 1, Integer.MAX_VALUE);
    }
}
