package gui.view;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class DualGradientPainter<T> implements IPainter<T> {

	private Color[] colors;
	
	private boolean rounded;
	private int arcSize;
	
	public DualGradientPainter(Color[] colors) {
		this.colors = colors;
	}
	
	public DualGradientPainter(Color[] colors, boolean rounded, int arcSize) {
		this(colors);
		this.rounded = rounded;
		this.arcSize = arcSize;
	}
	
	public DualGradientPainter(int[] hexColors, boolean rounded, int arcSize) {
		colors = new Color[hexColors.length];
		for (int i = 0; i < hexColors.length; ++i)
			colors[i] = new Color(hexColors[i]);
		this.rounded = rounded;
		this.arcSize = arcSize;
	}
	
	
	@Override
	public void paint(Graphics2D gContext, T objectToPaint, int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		GradientPaint paint1 = new GradientPaint(0f, 0f, colors[0], 0f, 0.499f * height, colors[1]);
		GradientPaint paint2 = new GradientPaint(0f, 0.5f * height, colors[2], 0f, height, colors[3]);
		
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLUE);
		
		if (rounded)
			g.fillRoundRect(0, 0, width, height, arcSize, arcSize);
		else
			gContext.fillRect(0, 0, width, height);
		
		g.setComposite(AlphaComposite.SrcIn);
		g.setPaint(paint1);
		g.fillRect(-10, 0, width + 20, height / 2);
		
		g.setPaint(paint2);
		g.fillRect(-10, height / 2, width + 20 , height);
		
		BufferedImage innerShadow = ShadowFactory.innerShadow(image, 1);
		g.setComposite(AlphaComposite.SrcAtop);
		g.drawImage(innerShadow, 0, 0, null);
		
//		g.setPaint(GradientFactory.createGradient(0xBBBBBB, 0x444444, height));
//		g.drawRoundRect(0, 0, width-1, height-1, arcSize, arcSize);
				
		gContext.drawImage(image, 0, 0, width, height, null);
		
		g.dispose();
	}

	public Color[] getColors() {
		return colors;
	}

	public void setColors(Color[] colors) {
		this.colors = colors;
	}

	public boolean getRounded() {
		return rounded;
	}
	
	public void setRounded(boolean rounded) {
		this.rounded = rounded;
	}
	
	public int getArcSize() {
		return arcSize;
	}
	
	public void setArcSize(int arcSize) {
		this.arcSize = arcSize;
	}
}
