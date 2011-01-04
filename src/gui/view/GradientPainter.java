package gui.view;

import java.awt.Graphics2D;

public class GradientPainter<T> implements IPainter<T> {
	
	private int hexColor1;
	private int hexColor2;
	
	public GradientPainter(int hexColor1, int hexColor2) {
		this.hexColor1 = hexColor1;
		this.hexColor2 = hexColor2;
	}

	@Override
	public void paint(Graphics2D gContext, T objectToPaint, int width,
			int height) {
		
		gContext.setPaint(GradientFactory.createGradient(hexColor1, hexColor2, height));
		gContext.fillRect(0, 0, width, height);
	}

}
