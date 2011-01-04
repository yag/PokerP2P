package gui.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ButtonMenuPainter<T> implements IPainter<T> {

	//Gradients
	int cBackground_1 = 0x535353;
	int cBackground_2 = 0x4e4e4e;
	int cBackground_3 = 0x333333;
	int cBackground_4 = 0x383838;
	
	public ButtonMenuPainter(int background1, int background2, int background3, int background4) {
		cBackground_1 = background1;
		cBackground_2 = background2;
		cBackground_3 = background3;
		cBackground_4 = background4;
	}
	
	public ButtonMenuPainter() {}
	
	@Override
	public void paint(Graphics2D g, T objectToPaint, int width,
			int height) {
		
		//--------------------------------------------------------------- Colors
		// Outer Shadow
		Color cOuter_1 = new Color(0x363636);
		
		// Borders
		Color cBorders = new Color(0x232323);
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int h = height - 1;
		int upmid = h / 2;
		int downmid = h / 2 + h % 2;
		
		// Paint outer glow
		graphics.setColor(cOuter_1);
		graphics.fillRoundRect(0, 1, width, h, 10, 10);
		
		// Composite
		graphics.setColor(Color.BLACK);
		graphics.fillRoundRect(0, 0, width, h, 10, 10);
		Composite old = graphics.getComposite();
		graphics.setComposite(AlphaComposite.SrcIn);
		
		
		// Paint top gradient
		graphics.setPaint(GradientFactory.createGradient(cBackground_1, cBackground_2, upmid));
		graphics.fillRect(0, 0, width, upmid);
		
		// Paint bottom gradient
		graphics.setPaint(GradientFactory.createGradient(cBackground_3, cBackground_4, downmid));
		graphics.fillRect(0, upmid, width, downmid);
		
		// Draw borders
		graphics.setComposite(old);
		graphics.setColor(cBorders);
		graphics.drawRoundRect(0, 0, width-1, h-1, 10, 10);
		
		graphics.dispose();
		
		g.drawImage(image, 0, 0, null);
	}

}
