package gui.view;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class BEPanel extends JPanel {

	private IPainter<BEPanel> backgroundPainter;
	
	public BEPanel(IPainter<BEPanel> backgroundPainter) {
		this.backgroundPainter = backgroundPainter;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		if (backgroundPainter != null) {
			Graphics2D graphics2d = (Graphics2D)g.create();
			backgroundPainter.paint(graphics2d, this, getWidth(), getHeight());
			graphics2d.dispose();
		}
		
		super.paintComponent(g);
	}
}
