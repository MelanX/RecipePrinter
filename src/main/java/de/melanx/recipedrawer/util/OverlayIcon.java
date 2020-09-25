package de.melanx.recipedrawer.util;

import net.minecraft.util.ResourceLocation;

public enum OverlayIcon {

    SLOT(RenderHelper.TEXTURE_ICONS, 0, 0, 18, 18),
    BIG_SLOT(RenderHelper.TEXTURE_ICONS, 0, 18, 26, 26),
    SHAPELESS(RenderHelper.TEXTURE_ICONS, 26, 0, 36, 36),
    ARROW(RenderHelper.TEXTURE_ICONS, 0, 44, 22, 16);

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
