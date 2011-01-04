package gui.view;

import java.awt.Color;
import java.awt.Graphics2D;

public class MenuPainter<T> implements IPainter<T> {

	@Override
	public void paint(Graphics2D g, T objectToPaint, int width,
			int height) {
		
		int upmid = height / 2;
		int downmid = height / 2 + height % 2;
		
		//--------------------------------------------------------------- Colors
		// Gradients
		int cBackground_1 = 0x393939;
		int cBackground_2 = 0x2e2e2e;
		int cBackground_3 = 0x232323;
		int cBackground_4 = 0x282828;
		
		// Top inner shadow
		Color cInner_1 = new Color(0x292929);
		Color cInner_2 = new Color(0x353535);
		Color cInner_3 = new Color(0x383838);
		
		// Bottom inner shadow
		Color cInner_4 = new Color(0x2c2c2c);
		Color cInner_5 = new Color(0x363636);
		
		// Borders
		Color cBorders = new Color(0x171717);
		
		// Paint top gradient
		g.setPaint(GradientFactory.createGradient(cBackground_1, cBackground_2, upmid));
		g.fillRect(0, 0, width, upmid);
		
		g.setPaint(GradientFactory.createGradient(cBackground_3, cBackground_4, downmid));
		g.fillRect(0, upmid, width, downmid);
		
		// Paint top inner shadow
		g.setColor(cInner_1);
		g.drawLine(0, 1, width, 1);
		g.setColor(cInner_2);
		g.drawLine(0, 2, width, 2);
		g.setColor(cInner_3);
		g.drawLine(0, 3, width, 3);
		
		// Paint bottom inner shadow
		g.setColor(cInner_4);
		g.drawLine(0, height - 3, width, height - 3);
		g.setColor(cInner_5);
		g.drawLine(0, height - 3, width, height - 3);
		
		// Draw borders
		g.setColor(cBorders);
		g.drawLine(0, 0, width, 0);
		g.drawLine(0, height - 1, width, height - 1);
	}

}
