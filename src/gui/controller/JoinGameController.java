package gui.controller;

import gui.view.IPainter;
import gui.view.MenuPainter;
import gui.view.components.BEButton;
import gui.view.components.BEMenuButton;
import gui.view.components.BEPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JoinGameController implements BEController {
	
//=================================================================== Components
	private JPanel panel;
	
	private BEPanel menu;
	private JLabel menuTitle;

	private BEButton btnBack;
	private BEButton btnConnect;
	
	private JLabel lblInput;
	private JLabel lblColon;
	
	private JTextField txtIpAddress;
	private JTextField txtPort;
	
	//---------------------------------------------------------- Custom painters
	IPainter<BEPanel> menuPainter = new MenuPainter<BEPanel>();
//==============================================================================	
	
	public JoinGameController() {
		panel = new JPanel();
		
		menu = new BEPanel(menuPainter);
		menuTitle = new JLabel("Join Game");
		
		btnBack = new BEMenuButton("Back");
		btnConnect = new BEMenuButton("Connect");
		
		btnBack.setFocusable(false);
		btnConnect.setFocusable(false);
		
		lblInput = new JLabel("Input game server adress :");
		lblColon = new JLabel(":");
		
		txtIpAddress = new JTextField(10);
		txtIpAddress.setText("0.0.0.0");
		txtPort = new JTextField(4);
		txtPort.setText("2000");
		
		configureComponents();
		//txtIpAddress.requestFocus();
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	public void panelInstalled() {
		txtIpAddress.requestFocusInWindow();
	}
	
	private void configureComponents() {
		//-------------------------------------------------- Label configuration
		lblInput.setForeground(Color.WHITE);
		lblColon.setForeground(Color.WHITE);
		
		//---------------------------------------------- Textfield configuration
		txtIpAddress.setHorizontalAlignment(JTextField.CENTER);
		txtPort.setHorizontalAlignment(JTextField.CENTER);

		Font font = new Font("Verdana", Font.PLAIN, 16);
		txtIpAddress.setFont(font);
		txtPort.setFont(font);
		
		//-------------------------------------------- Menu button configuration
		btnBack.setForeground(Color.WHITE);
		btnConnect.setForeground(Color.WHITE);
		
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AppController.app().showMain();
			}
		});
		btnConnect.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if (!validateData())
					return;
				
//				AppController main = AppController.app();
//				ProtocolController p = main.getProtocolController();
//				
//				String host = txtIpAddress.getText();
//				int port = Integer.parseInt(txtPort.getText());
//				
//				p.login(main.getGuiController(), "noob", host, port, 2001);
				
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
		box.add(btnConnect);
		box.add(Box.createHorizontalStrut(5));
		
		menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
		menu.add(Box.createVerticalStrut(5));
		menu.add(box);
		menu.add(Box.createVerticalStrut(5));
		
		//-------------------------------------------------------- Window layout
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		panel.add(menu);
		panel.add(Box.createVerticalStrut(10));
		
		Box box1 = Box.createHorizontalBox();
		box1.add(Box.createHorizontalStrut(20));
		box1.add(lblInput);
		box1.add(Box.createHorizontalGlue());
		
		JPanel content = new JPanel();
		content.setOpaque(false);
		content.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
		
		content.add(txtIpAddress);
		content.add(lblColon);
		content.add(txtPort);
		
		panel.add(box1);
		panel.add(Box.createVerticalStrut(10));	
		panel.add(content);
		panel.add(Box.createVerticalStrut(20));
	}
	
	//---------------------------------------------------------- Data validation
	// TODO: validate data
	private boolean validateData() {return true;}
	
	
}
