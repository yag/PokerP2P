package gui.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;

import javax.swing.border.Border;

public class BEGroupPanelBorder implements Border {
	
	private String title;
	private int fontSize = 11;
	private Font font;
	private int topInset = 15;
	
	public BEGroupPanelBorder(String title) {
		this.title = title;
		fontSize = 11;
		setFontSize(fontSize);
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		
		BufferedImage image = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		
		//Color t = Color.YELLOW.darker().darker();
		Color t = new Color(0, 128,255);
		
		//graphics.setColor(Color.WHITE);
		graphics.setColor(t);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setStroke(new BasicStroke(2.0f));
		graphics.drawRoundRect(0, 0, image.getWidth() - 1, image.getHeight() - 1, 15, 15);
		
		// Draw label inside border
		TextLayout textLayout = new TextLayout(title, font, graphics.getFontRenderContext());
		float ascent = textLayout.getAscent();
		
		float inset = ascent + textLayout.getDescent();
		topInset = (int)inset;
		
		//graphics.setColor(Color.WHITE);
		graphics.setColor(new Color(0,168, 255));
		textLayout.draw(graphics, 5, 4 + (int)ascent);
		
		graphics.dispose();
		
		BufferedImage image2 = image.getSubimage(x, y, width, height);
		
		g.drawImage(image2, x, y, null);
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(9 + topInset, 5, 5, 5);
	}

	@Override
	public boolean isBorderOpaque() {
		return false;
	}
	
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
		font = new Font("Verdana", Font.ITALIC, fontSize);
	}

}
