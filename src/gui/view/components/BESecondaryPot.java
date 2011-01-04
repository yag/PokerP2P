package gui.view.components;

import gui.controller.ResourceManager;
import gui.view.SpringUtilities;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class BESecondaryPot extends JPanel {
	
	private JLabel lblpot;
	
	private JLabel lblValue;
	private Integer value;
	
	private ImageIcon potIcon;
	
	public BESecondaryPot(int value){
		this.value = value;
		
		potIcon = ResourceManager.SECONDARY_POT_ICON;
		
		lblpot = new JLabel(potIcon);
		
		lblValue = new JLabel("$" + this.value.toString());
		
		setLayout(new SpringLayout());
		
		lblpot.setAlignmentX(CENTER_ALIGNMENT);
		lblValue.setAlignmentX(CENTER_ALIGNMENT);
		
		lblValue.setForeground(Color.WHITE);
		
		lblValue.setVisible(false);
		
		add(lblValue);
		add(lblpot);
		SpringUtilities.makeCompactGrid(this, 2, 1, 0, 0, 0, 0);
		
		setOpaque(false);
		
		
		lblpot.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				lblValue.setVisible(false);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				lblValue.setVisible(true);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
	}
}
