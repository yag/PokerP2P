package gui.controller;

import gui.view.BEGroupPanelBorder;
import gui.view.IPainter;
import gui.view.MenuPainter;
import gui.view.SpringUtilities;
import gui.view.components.BEButton;
import gui.view.components.BEMenuButton;
import gui.view.components.BEPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import core.controller.ProtocolController;
import core.model.Game;

public class CreateGameController implements BEController {
	
//=================================================================== Components
	private JPanel panel;
	
	private BEPanel menu;
	private JLabel menuTitle;
	
	private JPanel pnlServer;
	private JPanel pnlTable;
	private JPanel pnlBlinds;
	
	private BEButton btnBack;
	private BEButton btnCreate;
	
	//--------------------------------- Server configuration labels & textfields
	private JLabel lblTableName; private JTextField txtTableName;
	private JLabel lblIpAddress; private JTextField txtIpAddress;
	private JLabel lblPort;      private JTextField txtPort;
	
	//---------------------------------- Table configuration labels & textfields
	private JLabel lblMinPlayers; private JTextField txtMinPlayers;
	private JLabel lblMaxPlayers; private JTextField txtMaxPlayers;
	private JLabel lblStackInit;  private JTextField txtStackInit;
	private JLabel lblSpeakTime;  private JTextField txtSpeakTime;
	private JLabel lblAddTime;    private JTextField txtAddTime;
	
	//--------------------------------- Blinds configuration labels & textfields
	// FIXME: Implemented ???
	private JLabel lblRaiseBlinds;
	private JLabel lblRaiseScheme;
	
	//---------------------------------------------------------- Custom painters
	IPainter<BEPanel> menuPainter = new MenuPainter<BEPanel>();
//==============================================================================	
	
	public CreateGameController() {
		panel = new JPanel();
		
		menu = new BEPanel(menuPainter);
		menuTitle = new JLabel("Create Game");
		
		btnBack = new BEMenuButton("Back");
		btnCreate = new BEMenuButton("Create");
		
		pnlServer = new JPanel();
		pnlTable = new JPanel();
		pnlBlinds = new JPanel();
		
		lblTableName = new JLabel("Table name :", JLabel.TRAILING);
		txtTableName = new JTextField(20);
		lblIpAddress = new JLabel("IP Address :", JLabel.TRAILING);
		txtIpAddress = new JTextField(20);
		lblPort = new JLabel("Port :", JLabel.TRAILING);
		txtPort = new JTextField(20);
		
		lblMinPlayers = new JLabel("Minimum number of players :", JLabel.TRAILING);
		txtMinPlayers = new JTextField(20);
		lblMaxPlayers = new JLabel("Maximum number of players :", JLabel.TRAILING);
		txtMaxPlayers = new JTextField(20);
		lblStackInit = new JLabel("Initial Stack :", JLabel.TRAILING);
		txtStackInit = new JTextField(20);
		lblSpeakTime = new JLabel("Speak time (in seconds) :", JLabel.TRAILING);
		txtSpeakTime = new JTextField(20);
		lblAddTime = new JLabel("Additional speak time (in seconds) :", JLabel.TRAILING);
		txtAddTime = new JTextField(20);
		
		configureComponents();
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public void panelInstalled() {
		txtTableName.requestFocusInWindow();
	}
	
	private void configureComponents() {
		//------------------------------------------------- Window configuration
		panel.setOpaque(false);
		
		// Custom borders
		pnlServer.setBorder(new BEGroupPanelBorder("Server configuration"));
		pnlTable.setBorder(new BEGroupPanelBorder("Table configuration"));
		pnlServer.setOpaque(false);
		pnlTable.setOpaque(false);
		
		//-------------------------------------------- Menu button configuration
		btnBack.setForeground(Color.WHITE);
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AppController.app().showMain();
			}
		});
		
		btnCreate.setForeground(Color.WHITE);
		btnCreate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!validateData())
					return;
				
//				// Create game
//				int minNbPlayers = Integer.parseInt(txtMinPlayers.getText());
//				int thinkTime = Integer.parseInt(txtSpeakTime.getText());
//				int buyIn = Integer.parseInt(txtStackInit.getText());
//				int blindUpTime = Integer.parseInt(txtSpeakTime.getText());
//				float blindUpInc = .5f;
//				Game game = new Game(minNbPlayers, thinkTime, buyIn, blindUpTime, blindUpInc);
//				
//				// Table configuration
//				String tableName = txtTableName.getText();
//				String host = txtIpAddress.getText();
//				int port = Integer.parseInt(txtPort.getText());
//				
//				AppController app = AppController.app();
//				ProtocolController p = app.getProtocolController();
//				
//				try {
//					LocateRegistry.createRegistry(port);
//				} catch (Exception e1) {
//					
//				}
//				
//				p.createGame(game, 2000);
//				p.login(app.getGuiController(), tableName, host, port, port);
				
				AppController.app().showGamePreview();
			}
		});
		
		menuTitle.setForeground(Color.WHITE);
		menuTitle.setFont(new Font("Verdana", Font.BOLD, 16));
		menuTitle.setHorizontalAlignment(JLabel.CENTER);
		
		//---------------------------------------------------------- Menu layout
		Box box = Box.createHorizontalBox();
		box.setPreferredSize(new Dimension(450, 30));
		box.add(Box.createHorizontalStrut(5));
		box.add(btnBack);
		box.add(Box.createHorizontalGlue());
		box.add(menuTitle);
		box.add(Box.createHorizontalGlue());
		box.add(btnCreate);
		box.add(Box.createHorizontalStrut(5));
		
		menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
		menu.add(Box.createVerticalStrut(5));
		menu.add(box);
		menu.add(Box.createVerticalStrut(5));
		
		//-------------------------------------------------- Server panel layout	
		lblTableName.setForeground(Color.WHITE);
		lblIpAddress.setForeground(Color.WHITE);
		lblPort.setForeground(Color.WHITE);

		Box boxServer = Box.createHorizontalBox();
		boxServer.add(Box.createHorizontalStrut(10));
		boxServer.add(pnlServer);
		boxServer.add(Box.createHorizontalStrut(10));
		
		pnlServer.setLayout(new SpringLayout());
		pnlServer.add(lblTableName); pnlServer.add(txtTableName);
		pnlServer.add(lblIpAddress); pnlServer.add(txtIpAddress);
		pnlServer.add(lblPort);      pnlServer.add(txtPort);
		SpringUtilities.makeCompactGrid(pnlServer, 3, 2, 10, 10, 5, 10);
		
		//------------------------------------------- Table configuration layout
		lblMinPlayers.setForeground(Color.WHITE);
		lblMaxPlayers.setForeground(Color.WHITE);
		lblStackInit.setForeground(Color.WHITE);
		lblSpeakTime.setForeground(Color.WHITE);
		lblAddTime.setForeground(Color.WHITE);
		
		Box boxTable = Box.createHorizontalBox();
		boxTable.add(Box.createHorizontalStrut(10));
		boxTable.add(pnlTable);
		boxTable.add(Box.createHorizontalStrut(10));
		
		pnlTable.setLayout(new SpringLayout());
		pnlTable.add(lblMinPlayers); pnlTable.add(txtMinPlayers);
		pnlTable.add(lblMaxPlayers); pnlTable.add(txtMaxPlayers);
		pnlTable.add(lblStackInit); pnlTable.add(txtStackInit);
		pnlTable.add(lblSpeakTime); pnlTable.add(txtSpeakTime);
		pnlTable.add(lblAddTime); pnlTable.add(txtAddTime);
		SpringUtilities.makeCompactGrid(pnlTable, 5, 2, 10, 10, 5, 10);
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(menu);
		panel.add(Box.createVerticalStrut(10));
		panel.add(boxServer);
		panel.add(Box.createVerticalStrut(10));
		panel.add(boxTable);
		panel.add(Box.createVerticalStrut(10));
	}
	
	// TODO: data validation
	private boolean validateData() {
		return true;
	}
	
}
