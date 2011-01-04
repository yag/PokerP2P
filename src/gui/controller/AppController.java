package gui.controller;

import gui.GUIController;
import gui.view.BEUtils;
import gui.view.components.BEPanel;
import gui.view.components.BETablePanel;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JFrame;

import core.controller.ProtocolController;

public class AppController {

	private static AppController instance;
	
	private Transition transition;
	private JFrame mainFrame;
	
	//-------------------------------------------------------------- Controllers
	private MainController mainController;
	private JoinGameController joinGameController;
	private CreateGameController createGameController;
	private PreviewController gamePreviewController;
	private GameController gameController;
	
	private ProtocolController protocolController;
	private GUIController guiController;
	
	BEController currentController;
	
	private AppController() {
		mainFrame = new JFrame();
		mainFrame.setContentPane(new BEPanel(null));
				
		transition = new Transition(mainFrame);
		
		mainController = new MainController();
		joinGameController = new JoinGameController();
		createGameController = new CreateGameController();
		gamePreviewController = new PreviewController();
		gameController = new GameController();
		
		protocolController = new ProtocolController();
		guiController = new GUIController(protocolController);
		
		currentController = mainController;
	}
	
	public void runApplication() {
		//-------------------------------------------------- Frame configuration
		mainFrame.setTitle("Joker");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);
		mainFrame.setBackground(new Color(38, 38, 38));
		mainFrame.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		// Attach main panel
		// Testing
		mainFrame.add(mainController.getPanel());
		//mainFrame.add(gameController.getPanel());
		mainController.panelInstalled();
		mainFrame.pack();
		BEUtils.centerOnScreen(mainFrame);
		mainFrame.setVisible(true);
	}
	
	private void showPanel(BEController controller) {
		BEController old = currentController;
		currentController = controller;
		transition.setTransition(old.getPanel(), controller.getPanel());
	}
	
	public void showMain() { showPanel(mainController); }
	
	public void showJoinGame() { showPanel(joinGameController); }
	
	public void showCreateGame() { showPanel(createGameController); }
	
	public void showGamePreview() { showPanel(gamePreviewController); }
	
	public void showGame(BETablePanel tablePanel) {
		if (gameController.getPnlGame() != null)
			gameController.setPnlGame(tablePanel);
		
		mainFrame.remove(currentController.getPanel());
		mainFrame.add(gameController.getPanel());
		mainFrame.pack();
		currentController = gameController;
		currentController.panelInstalled();
	}
	
	public ProtocolController getProtocolController() {
		return protocolController;
	}
	
	public GUIController getGuiController() {
		return guiController;
	}
	
	public static AppController app() {
		if (instance == null)
			instance = new AppController();
		
		return instance;
	}
	
	public JFrame getMainFrame() {
		return mainFrame;
	}
	
	public void transitionComplete() {
		currentController.panelInstalled();
	}
}
