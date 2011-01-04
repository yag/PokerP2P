package gui.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.border.Border;

public class BETestTextBorder implements Border {

	Color borderColor = new Color(255,128,0);
	
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//graphics.setColor(Color.WHITE);
		//graphics.fillRoundRect(1, 1, width - 2, width - 2, 0, 0);
		
		graphics.setColor(borderColor);
		graphics.drawRoundRect(0, 0, width - 1, height - 1, 10, 10);
		
		BufferedImage shadowImage = ShadowFactory.innerShadow(image, 1);
		graphics.setComposite(AlphaComposite.SrcAtop);
		graphics.drawImage(shadowImage, 0, 0, null);
		graphics.dispose();
		
		Image subImage = image.getSubimage(x, y, width, height);
		
		g.drawImage(subImage, x, y, null);
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(3, 3, 3, 3);
	}

	@Override
	public boolean isBorderOpaque() {
		return false;
	}

}
