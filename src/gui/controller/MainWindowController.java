package gui.controller;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import gui.view.BEButton;
import gui.view.BEPanel;
import gui.view.DualGradientPainter;
import gui.view.GradientButton;
import gui.view.GradientPainter;
import gui.view.IPainter;

import javax.swing.JFrame;
import javax.swing.text.StyledEditorKit.ForegroundAction;

public class MainWindowController {
	
//=================================================================== Components	
	private JFrame window;
	private BEPanel panel;
	
	private BEButton btnCreateGame;
	private BEButton btnJoinGame;
	private BEButton btnConfiguration;
	private BEButton btnQuit;
	
//======================================================================= Colors
	private final int cPanelBackground1 = 0x3a3a3a;
	private final int cPanelBackground2 = 0x4a4a4a;
	private final int[] cBtnBackgrounds =
		new int[] {
			0x999999, 0x444444,
			0x404040, 0x222222
		};
		
//==============================================================================	
	public MainWindowController() {
		window = new JFrame("Joker");
		panel = new BEPanel(new GradientPainter<BEPanel>(cPanelBackground1, cPanelBackground2));
		
		
		// Configure Button's Painters
		
		Color[] colors = new Color[cBtnBackgrounds.length];
		for (int i = 0; i < cBtnBackgrounds.length; ++i)
			colors[i] = new Color(cBtnBackgrounds[i]);
		IPainter<BEButton> backgroundPainter = new DualGradientPainter<BEButton>(colors, true, 15);
		
		Color[] rolloverColors = new Color[cBtnBackgrounds.length];
		for (int i = 0; i < rolloverColors.length; ++i)
			rolloverColors[i] = colors[i].darker();
		IPainter<BEButton> rolloverPainter = new DualGradientPainter<BEButton>(rolloverColors, true, 15);
		
		Color[] clickColors = new Color[cBtnBackgrounds.length];
		for (int i = 0; i < clickColors.length; ++i)
			clickColors[i] = colors[i].brighter();
		IPainter<BEButton> clickPainter = new DualGradientPainter<BEButton>(clickColors, true, 15);
		
		btnCreateGame = new BEButton("Create Game", backgroundPainter);
		btnCreateGame.setRolloverPainter(rolloverPainter);
		btnCreateGame.setClickPainter(clickPainter);
		
		btnJoinGame = new BEButton("Join Game", backgroundPainter);
		btnJoinGame.setRolloverPainter(rolloverPainter);
		btnJoinGame.setClickPainter(clickPainter);
		
		btnConfiguration = new BEButton("Configuration", backgroundPainter);
		btnConfiguration.setRolloverPainter(rolloverPainter);
		btnConfiguration.setClickPainter(clickPainter);
		
		btnQuit = new BEButton("Quit", backgroundPainter);
		btnQuit.setRolloverPainter(rolloverPainter);
		btnQuit.setClickPainter(clickPainter);
	}
	
	public void createAndShowUI() {
		configureComponents();
		window.pack();
		window.setVisible(true);
	}
	
	public void configureComponents() {
		window.setBounds(250, 250, 475, 350);
		window.setBackground(new Color(0x222222));
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		
		ConfigureButton(btnCreateGame, 25, 200);
		ConfigureButton(btnJoinGame, 250, 200);
		ConfigureButton(btnConfiguration, 25, 240);
		ConfigureButton(btnQuit, 250, 240);
		
		panel.setPreferredSize(new Dimension(475, 350));
		panel.setLayout(null); // absolute coordinates
		panel.add(btnCreateGame);
		panel.add(btnJoinGame);
		panel.add(btnConfiguration);
		panel.add(btnQuit);
		
		window.getContentPane().add(panel);
	}
	
	private static void ConfigureButton(BEButton b, int x, int y) {
		b.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		b.setForeground(Color.WHITE);
		b.setBounds(x, y, 200, 25);
	}
}
