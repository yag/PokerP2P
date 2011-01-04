package gui;

import gui.controller.AppController;

import javax.swing.SwingUtilities;


public class MainApplication {

	public static void main(String...args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				AppController.app().runApplication();
			}
		});
	}
}
