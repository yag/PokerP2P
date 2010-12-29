package gui;

import gui.controller.MainWindowController;

import javax.swing.SwingUtilities;


public class MainApplication {

	public static void main(String...args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainWindowController controller = new MainWindowController();
				controller.createAndShowUI();
			}
		});
	}
}
