package gui.controller;

import gui.view.SpringUtilities;
import gui.view.components.BEButton;
import gui.view.components.BEMenuButton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class MainController implements BEController {
	
//=================================================================== Components
	private JPanel panel;
	private JPanel panelButtons;
	
	private BEButton btnCreateGame;
	private BEButton btnJoinGame;
	private BEButton btnConfiguration;
	private BEButton btnQuit;
	
	private JLabel jokerImage;
	
//==============================================================================	
	public MainController() {
		
		panel = new JPanel();
		panelButtons = new JPanel();
		
		jokerImage = new JLabel();
		ImageIcon icon = new ImageIcon(this.getClass().getResource("/assets/main_menu.png"));
		jokerImage.setIcon(icon);
		jokerImage.setHorizontalAlignment(JLabel.CENTER);
		
		btnCreateGame = new BEMenuButton("Create Game");
		btnJoinGame = new BEMenuButton("Join Game");
		btnConfiguration = new BEMenuButton("Configuration");
		btnQuit = new BEMenuButton("Quit");
		
		configureComponents();
	}
	
	private void configureComponents() {
		//--------------------------------------------------------- Panel layout
		panelButtons.setOpaque(false);
		panelButtons.setLayout(new SpringLayout());
		panelButtons.add(btnCreateGame);
		panelButtons.add(btnJoinGame);
		panelButtons.add(btnConfiguration);
		panelButtons.add(btnQuit);
		SpringUtilities.makeCompactGrid(panelButtons, 2, 2, 10, 10, 10, 10);
		
		//------------------------------------------------- Button configuration
		ConfigureButton(btnCreateGame);
		btnCreateGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AppController.app().showCreateGame();
			}
		});
		
		ConfigureButton(btnJoinGame);
		btnJoinGame.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AppController.app().showJoinGame();
			}
		});
		
		ConfigureButton(btnConfiguration);
		
		ConfigureButton(btnQuit);
		btnQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		//-------------------------------------------------------- Window layout
		panel.setOpaque(false);
		panel.setLayout(new SpringLayout());
		panel.add(jokerImage);
		panel.add(panelButtons);
		SpringUtilities.makeCompactGrid(panel, 2, 1, 10, 10, 10, 10);
	}
	
	private static void ConfigureButton(BEButton b) {
		b.setFont(new Font("Lucida Grande", Font.BOLD, 14));
		b.setForeground(Color.WHITE);
		b.setPreferredSize(new Dimension(200, 30));
	}
	
	public JPanel getPanel() {
		return panel;
	}	
	
	public void panelInstalled() {}
}
