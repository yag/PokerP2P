package gui.view.components;

import gui.view.BERoundBorder;
import gui.view.DualGradientPainter;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

public class BEPlayerPanel extends BEPanel {

	private JLabel lblPlayerName;
	private JLabel lblPlayerStack;
	
	public BEPlayerPanel(String playerName, int currentStack) {
		// TODO: playerPanelModel
		
		super(new DualGradientPainter<BEPanel>(
				new int[] { 0x535353, 0x434343, 0x3e3e3e, 0x323232} , true, 10));
		
		lblPlayerName = new JLabel("Noob");
		lblPlayerStack = new JLabel("$5,000,000");
		
		setBorder(new BERoundBorder());
		
		configureComponents();
	}
	
	private void configureComponents() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		lblPlayerName.setAlignmentX(CENTER_ALIGNMENT);
		lblPlayerName.setAlignmentY(TOP_ALIGNMENT);
		lblPlayerStack.setAlignmentX(CENTER_ALIGNMENT);
		lblPlayerStack.setAlignmentY(BOTTOM_ALIGNMENT);
		
		lblPlayerName.setForeground(Color.WHITE);
		lblPlayerStack.setForeground(Color.WHITE);
		
		add(lblPlayerName);
		add(lblPlayerStack);
	}
	
	
}
