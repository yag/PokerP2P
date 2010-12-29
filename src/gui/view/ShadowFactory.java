package gui.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class ShadowFactory {
	private static ConvolveOp linearBlurOp(int size) {
		float[] data = new float[size * size];
		float value = 1.0f / (float) (size * size);
		for (int i = 0; i < data.length ; ++i) {
			data[i] = value;
		}
		return new ConvolveOp(new Kernel(size, size, data));
	}
	
	public static BufferedImage innerShadow(Image image, int offset) {
		int shadowSize = offset * 2;
		BufferedImage shadow = new BufferedImage(
				image.getWidth(null) + shadowSize,
				image.getHeight(null) + shadowSize,
				BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = shadow.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(new Color(0, 0, 0, 140));
		g.fillRect(0, 0, shadow.getWidth(), shadow.getHeight());
		g.setComposite(AlphaComposite.DstOut);
		g.drawImage(image, shadowSize, shadowSize, null);
		g.dispose();
		
		BufferedImage innerShadow = linearBlurOp(offset+2).filter(shadow, null);
		
		return innerShadow.getSubimage(offset, offset,
				shadow.getWidth() - shadowSize, shadow.getHeight() - shadowSize);
	}
}
