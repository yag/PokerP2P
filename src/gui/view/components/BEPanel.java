package gui.view.components;

import gui.view.IPainter;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class BEPanel extends JPanel {

	private IPainter<BEPanel> backgroundPainter;
	
	public BEPanel(IPainter<BEPanel> backgroundPainter) {
		this.backgroundPainter = backgroundPainter;
		setOpaque(false);
		alpha = 1.0f;
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
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		super.paint(g);
	}
	
	private float alpha;
	
	public void setAlpha(float alpha) {
		this.alpha = alpha;
		repaint();
	}
	
	public float getAlpha() {
		return alpha;
	}
}
