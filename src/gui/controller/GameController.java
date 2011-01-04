package gui.controller;

import static gui.view.BEBoxLayoutUtils.hg;
import static gui.view.BEBoxLayoutUtils.hs;
import static gui.view.BEBoxLayoutUtils.makeColumn;
import static gui.view.BEBoxLayoutUtils.makeLine;
import static gui.view.BEBoxLayoutUtils.vs;
import gui.view.BERoundBorder;
import gui.view.IPainter;
import gui.view.MenuPainter;
import gui.view.components.BEButton;
import gui.view.components.BEMenuButton;
import gui.view.components.BEPanel;
import gui.view.components.BERankingPanel;
import gui.view.components.BETablePanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameController implements BEController {

	
//=================================================================== Components
	private JPanel panel = new JPanel();
	
	private JPanel absoluteContainer = new JPanel();
	
	//-----------------------
	private IPainter<BEPanel> menuPainter = new MenuPainter<BEPanel>();
	
	private BEPanel menu = new BEPanel(menuPainter);
	private JLabel menuTitle = new JLabel("Table");
	private BEButton btnStat = new BEMenuButton("Stats");
	private BEButton btnQuit = new BEMenuButton("Quit");
	//-----------------------
	
	//---------------------------------------------------------------
	private JPanel pnlUpperContainer = new JPanel();
	
	private JPanel pnlNotification = new JPanel();
	private JLabel firstNotification = new JLabel("Player1 raised $ 5,000,000");
	private JLabel secondNotification = new JLabel("Player2 raised $ 4,000,000");
	
	private JPanel pnlRanking = new BERankingPanel();
	//---------------------------------------------------------------

	private BETablePanel pnlGame = new BETablePanel();
	
	//-----------------------------
	private JPanel pnlActionButton = new JPanel();
	private BEButton btnFold = new BEMenuButton("FOLD");
	private BEButton btnCallCheck = new BEMenuButton("Call");
	private BEButton btnRaise = new BEMenuButton("Raise");
	//-----------------------------
	
	//-----------------------------
	private JLabel playerLeftCard = new JLabel(ResourceManager.TEST_CARD_ICON);
	private JLabel playerRightCard = new JLabel(ResourceManager.TEST_CARD_ICON);
	//-----------------------------
	
	private JLabel playerStack = new JLabel("$ 5,000,000");
	
	//--------------------------------- Alignment Constraints & Size Constraints
	public static int PREFERRED_WIDTH = 1080;
	public static int PREFERRED_HEIGHT = 700;
	
	public static int MENU_HEIGHT = 40;
	
	public static int GAME_TABLE_PANEL_X = 140;
	public static int GAME_TABLE_PANEL_Y = 160;
	public static int GAME_TABLE_WIDTH = 800;
	public static int GAME_TABLE_HEIGHT = 400;
	
	public static int UPPER_CONTAINER_X = 0;
	public static int UPPER_CONTAINER_Y = 40;
	public static int UPPER_CONTAINER_HEIGHT = 110;
	
	public static int ACTION_BUTTON_CONTAINER_X = 0;
	public static int ACTION_BUTTON_CONTAINER_Y = 680;
	public static int ACTION_BUTTON_CONTAINER_HEIGHT = 20;
	
	public static int FIRST_PLAYER_CARD_X = 465;
	public static int SECOND_PLAYER_CARD_X = 560;
	public static int PLAYER_CARD_Y = 580;
	
	public static int ACTION_BUTTON_WIDTH = 170;
	public static int ACTION_BUTTON_HEIGHT = 20;
//==============================================================================	
	
	public GameController() { configureComponents(); }
	
//==============================================================================
	
	public void configureComponents() {

		//------------------------------------------------- Panels configuration
		panel.setOpaque(false);
		panel.setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT + MENU_HEIGHT));
		
		absoluteContainer.setLayout(null);
		absoluteContainer.setOpaque(false);
		absoluteContainer.setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));

		menu.setBounds(0, 0, PREFERRED_WIDTH, MENU_HEIGHT);

		pnlUpperContainer.setBounds(UPPER_CONTAINER_X, UPPER_CONTAINER_Y, 
				PREFERRED_WIDTH, UPPER_CONTAINER_HEIGHT);
		
		pnlActionButton.setBounds(ACTION_BUTTON_CONTAINER_X, ACTION_BUTTON_CONTAINER_Y,
				PREFERRED_WIDTH, ACTION_BUTTON_CONTAINER_HEIGHT);
		pnlActionButton.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		
		pnlGame.setBounds(GAME_TABLE_PANEL_X, GAME_TABLE_PANEL_Y, GAME_TABLE_WIDTH, GAME_TABLE_HEIGHT);
		
		pnlGame.setOpaque(false);
		pnlUpperContainer.setOpaque(false);
		pnlActionButton.setOpaque(false);
		
		
		//----------------------------------------------------------------------
		playerLeftCard.setBounds(FIRST_PLAYER_CARD_X, PLAYER_CARD_Y,
				                 playerLeftCard.getPreferredSize().width,
				                 playerLeftCard.getPreferredSize().height);
		playerRightCard.setBounds(SECOND_PLAYER_CARD_X, PLAYER_CARD_Y,
				playerRightCard.getPreferredSize().width,
				playerRightCard.getPreferredSize().height);
		
		playerStack.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		playerStack.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		playerStack.setFont(new Font("Verdana", Font.BOLD, 16));
		playerStack.setForeground(Color.WHITE);
		
		firstNotification.setForeground(Color.WHITE);
		secondNotification.setForeground(Color.WHITE);
		
		btnFold.setPreferredSize(new Dimension(ACTION_BUTTON_WIDTH, ACTION_BUTTON_HEIGHT));
		btnCallCheck.setPreferredSize(new Dimension(ACTION_BUTTON_WIDTH, ACTION_BUTTON_HEIGHT));
		btnRaise.setPreferredSize(new Dimension(ACTION_BUTTON_WIDTH, ACTION_BUTTON_HEIGHT));
		
		//---------------------------------------------------------- Menu layout
		menuTitle.setForeground(Color.WHITE);
		menuTitle.setFont(new Font("Verdana", Font.BOLD, 16));
		menuTitle.setHorizontalAlignment(JLabel.CENTER);
		
		Box box = Box.createHorizontalBox();
		box.setPreferredSize(new Dimension(450, 30));
		box.add(Box.createHorizontalStrut(5));
		box.add(btnStat);
		box.add(Box.createHorizontalGlue());
		box.add(menuTitle);
		box.add(Box.createHorizontalGlue());
		box.add(btnQuit);
		box.add(Box.createHorizontalStrut(5));

		menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
		menu.add(Box.createVerticalStrut(5));
		menu.add(box);
		menu.add(Box.createVerticalStrut(5));
		
		//------------------------------------------------------ Upper Container
		firstNotification.setBorder(new BERoundBorder());
		secondNotification.setBorder(new BERoundBorder());
		
		firstNotification.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
		secondNotification.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
		
		pnlUpperContainer.setLayout(new BoxLayout(pnlUpperContainer, BoxLayout.X_AXIS));
		pnlUpperContainer.add(
			makeColumn(
				hs(20),
				pnlRanking,
				hg(),
				makeLine(firstNotification, vs(20), secondNotification),
				hs(40)
			)	
		);
		
		//---------------------------------------------- Action Button Container
		pnlActionButton.setLayout(new BoxLayout(pnlActionButton, BoxLayout.X_AXIS));
		pnlActionButton.add(Box.createHorizontalGlue());
		pnlActionButton.add(btnFold);
		pnlActionButton.add(Box.createHorizontalStrut(30));
		pnlActionButton.add(btnCallCheck);
		pnlActionButton.add(Box.createHorizontalStrut(30));
		pnlActionButton.add(btnRaise);
		pnlActionButton.add(Box.createHorizontalGlue());
		
		//--------------------------------------------------------- panel layout
		absoluteContainer.add(menu);
		absoluteContainer.add(pnlUpperContainer);
		absoluteContainer.add(pnlGame);
		absoluteContainer.add(pnlActionButton);
		absoluteContainer.add(playerLeftCard);
		absoluteContainer.add(playerRightCard);
		
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(absoluteContainer);
		panel.add(Box.createVerticalGlue());
		panel.add(playerStack);
		panel.add(Box.createVerticalGlue());
	}
	
	
	public void setPnlGame(BETablePanel pnlGame) {
		if (this.pnlGame != null)
			absoluteContainer.remove(this.pnlGame);
		this.pnlGame = pnlGame;
		pnlGame.setBounds(new Rectangle(new Point(GAME_TABLE_PANEL_X, GAME_TABLE_PANEL_Y),
				          pnlGame.getPreferredSize()));
		absoluteContainer.add(pnlGame);
		panel.invalidate();
	}
	
	public BETablePanel getPnlGame() {
		return pnlGame;
	}
	
	@Override
	public JPanel getPanel() {
		return panel;
	}

	@Override
	public void panelInstalled() {
	}
	
}
