package de.melanx.recipedrawer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.awt.image.BufferedImage;

public class ImageUtilities {

    public static BufferedImage getTexture(ItemStack stack, ClientWorld world, PlayerEntity player) {
        Minecraft client = Minecraft.getInstance();
        IBakedModel model = client.getItemRenderer().getItemModelWithOverrides(stack, world, player);
        TextureAtlasSprite texture = model.getParticleTexture();
        BufferedImage icon = new BufferedImage(texture.getWidth(), texture.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < texture.getWidth(); x++) {
            for (int y = 0; y < texture.getHeight(); y++) {
                int rgba = texture.getPixelRGBA(0, x, y);
                int alpha = rgba & 0xFF;
                int rgb = rgba >>> 8;
                int argb = rgb | (alpha << 24);
                icon.setRGB(x, y, rgb);
            }
        }
        return icon;
    }
}
