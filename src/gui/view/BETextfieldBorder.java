package gui.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.border.Border;

public class BETextfieldBorder implements Border {

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		
		Graphics2D gg = (Graphics2D)g;
		
		BufferedImage image = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		
		//graphics.setColor(Color.BLACK);
		//graphics.fillRoundRect(0, 0, image.getWidth(), image.getHeight(), 10, 10);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(new Color(0,128,255));
		graphics.setStroke(new BasicStroke(2.0f));
		graphics.drawRoundRect(0, 0, image.getWidth() - 1, image.getHeight() - 1, 10, 10);
		
		graphics.dispose();
		
		BufferedImage image2 = image.getSubimage(x, y, width, height);
		
		g.drawImage(image2, x, y, null);
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(5, 5, 5, 5);
	}

	@Override
	public boolean isBorderOpaque() {
		return false;
	}

}
