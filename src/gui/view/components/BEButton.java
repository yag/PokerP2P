package gui.view.components;

import gui.view.IPainter;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

public class BEButton extends JButton implements MouseListener {

	private IPainter<BEButton> currentPainter;
	private IPainter<BEButton> backgroundPainter;
	private IPainter<BEButton> rolloverPainter;
	private IPainter<BEButton> clickPainter;
	
	
	public BEButton(String label, IPainter<BEButton> backgroundPainter) {
		super(label);
		this.backgroundPainter = currentPainter = backgroundPainter;
		
		// We draw our custom background through backgroundPainter
		setContentAreaFilled(false);
		setFocusPainted(false);
		setBorderPainted(false);
		
		addMouseListener(this);
	}
	
	public void setBackgroundPainter(IPainter<BEButton> backgroundPainter) {
		this.backgroundPainter = backgroundPainter;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
		if (currentPainter != null) {
			Graphics2D graphics2d = (Graphics2D)g.create();
			currentPainter.paint(graphics2d, this, getWidth(), getHeight());
			graphics2d.dispose();
		}
		
		super.paintComponent(g);
	}

//============================================================== Mouse Listener
	@Override
	public void mouseClicked(MouseEvent e) {
		currentPainter = backgroundPainter;
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (clickPainter != null) {
			currentPainter = clickPainter;
			repaint();
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (clickPainter != null) {
			// Check to see whether the mouse is release inside the button
			if (e.getX() >= 0 && e.getX() <= getWidth()
					&& e.getY() >= 0 && e.getY() <= getHeight())
				currentPainter = rolloverPainter;
			else
				currentPainter = backgroundPainter;
			repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (rolloverPainter != null) {
			currentPainter = rolloverPainter;
			repaint();
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (rolloverPainter != null) {
			currentPainter = backgroundPainter;
			repaint();
		}
	}

//============================================================ Getters & Setters

	public IPainter<BEButton> getCurrentPainter() {
		return currentPainter;
	}

	public void setCurrentPainter(IPainter<BEButton> currentPainter) {
		this.currentPainter = currentPainter;
	}

	public IPainter<BEButton> getRolloverPainter() {
		return rolloverPainter;
	}

	public void setRolloverPainter(IPainter<BEButton> rolloverPainter) {
		this.rolloverPainter = rolloverPainter;
	}

	public IPainter<BEButton> getClickPainter() {
		return clickPainter;
	}

	public void setClickPainter(IPainter<BEButton> clickPainter) {
		this.clickPainter = clickPainter;
	}

	public IPainter<BEButton> getBackgroundPainter() {
		return backgroundPainter;
	}	
}