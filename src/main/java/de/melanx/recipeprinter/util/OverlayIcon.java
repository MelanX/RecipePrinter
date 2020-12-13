package de.melanx.recipeprinter.util;

import net.minecraft.util.ResourceLocation;

public enum OverlayIcon {
    SLOT(RenderHelperMod.TEXTURE_ICONS, 0, 0, 18, 18),
    BIG_SLOT(RenderHelperMod.TEXTURE_ICONS, 0, 18, 26, 26),
    SHAPELESS(RenderHelperMod.TEXTURE_ICONS, 26, 0, 36, 36),
    ARROW(RenderHelperMod.TEXTURE_ICONS, 0, 44, 22, 16),
    ARROW_DOWN(RenderHelperMod.TEXTURE_ICONS, 26, 36, 15, 14);

    public final ResourceLocation texture;
    public final int u;
    public final int v;
    public final int width;
    public final int height;

    OverlayIcon(ResourceLocation texture, int u, int v, int width, int height) {
        this.texture = texture;
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
    }
}
