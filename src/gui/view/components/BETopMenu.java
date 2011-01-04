package gui.view.components;

import gui.view.GradientFactory;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;

public class BETopMenu extends JButton {

//	public BETopMenu() {
//		super(new IPainter<BEPanel>() {
//
//			@Override
//			public void paint(Graphics2D g, BEPanel objectToPaint,
//					int width, int height) {
//				
//				//Paint background (borders)
//				g.setColor(Color.BLACK);
//				g.fillRect(0, 0, width, height);
//				
//				// Paint a inner shadow
//				g.setColor(new Color(0x292929));
//				g.fillRect(0, 1, width, height-1);
//				
//				//Paint inner glow 1
//				g.setColor(new Color(0x353535));
//				g.fillRect(0, 2, width, height-2);
//				
//				//Paint inner glow 2
//				g.setColor(new Color(0x383838));
//				g.fillRect(0, 3, width, height-3);
//				
//				// First gradient
//				g.setPaint(GradientFactory.createGradient(0x393939, 
//						0x2e2e2e, (height - 4) / 2));
//				g.fillRect(0, 4, width, (height - 4)/2);
//				
//				// Second gradient
//				g.setPaint(GradientFactory.createGradient(0x232323, 
//						0x282828, (height - 4) / 2));
//				g.fillRect(0, (height - 4) / 2 + 1, width, height);
//			}
//		});
//	}
	
	@Override
	protected void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D)graphics.create();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		// Paint a inner shadow
		g.setColor(new Color(0x292929));
		g.fillRect(0, 1, getWidth(), getHeight()-1);
		
		//Paint inner glow 1
		g.setColor(new Color(0x353535));
		g.fillRect(0, 2, getWidth(), getHeight()-2);
		
		//Paint inner glow 2
		g.setColor(new Color(0x383838));
		g.fillRect(0, 3, getWidth(), getHeight()-3);
		
		// First gradient
		g.setPaint(GradientFactory.createGradient(0x393939, 
				0x2e2e2e, (getHeight() - 4) / 2));
		g.fillRect(0, 4, getWidth(), (getHeight() - 4)/2);
		
		// Second gradient
		g.setPaint(GradientFactory.createGradient(0x232323, 
				0x282828, (getHeight() - 4) / 2));
		g.fillRect(0, (getHeight() - 4) / 2 + 1, getWidth(), getHeight());
		
		g.dispose();
		
		super.paintComponent(graphics);
	}
	
}
