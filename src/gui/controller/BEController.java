package gui.controller;

import javax.swing.JPanel;

public interface BEController {

	// main shared panel
	public JPanel getPanel();
	
	// callback when a panel is attached
	public void panelInstalled();
}
