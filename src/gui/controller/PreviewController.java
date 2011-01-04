package gui.controller;

import static gui.view.BEBoxLayoutUtils.hg;
import static gui.view.BEBoxLayoutUtils.hs;
import static gui.view.BEBoxLayoutUtils.makeColumn;
import static gui.view.BEBoxLayoutUtils.makeLine;
import static gui.view.BEBoxLayoutUtils.vs;
import gui.view.BEGroupPanelBorder;
import gui.view.BETextfieldBorder;
import gui.view.GradientPainter;
import gui.view.IPainter;
import gui.view.MenuPainter;
import gui.view.ShadowFactory;
import gui.view.SpringUtilities;
import gui.view.components.BEButton;
import gui.view.components.BEMenuButton;
import gui.view.components.BEPanel;
import gui.view.components.BETablePanel;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import org.jdesktop.animation.timing.Animator;
import org.jdesktop.animation.timing.TimingTarget;
import org.jdesktop.animation.timing.interpolation.PropertySetter;
import org.jdesktop.animation.timing.triggers.TimingTrigger;
import org.jdesktop.animation.timing.triggers.TimingTriggerEvent;


public class PreviewController implements BEController {
	
//=================================================================== Components
	private JPanel panel;
	
	private BEPanel menu;
	private JLabel menuTitle;
	
	private BEButton btnBack;
	private BEButton btnJoin;
	
	private BETablePanel pnlGamePreview;
	private JPanel pnlGameStat;
	private JPanel pnlSpecQueue;
	private JPanel pnlTableParam;
	private JPanel pnlTableStat;
	
	//---------------------------------------------------------------- Game stat
	private JLabel lblGameStat;
	private JLabel lblRanking;
	private JLabel lblFirstPlace;  private JLabel lblFirstPlaceCash;
	private JLabel lblSecondPlace; private JLabel lblSecondPlaceCash;
	private JLabel lblThirdPlace;  private JLabel lblThirdPlaceCash;
	
	//--------------------------------------------------------------- Table stat
	private JLabel lblTableStat;
	private JLabel lblAveragePot;    private JLabel lblAveragePotContent;
	private JLabel lblPlayersByGame; private JLabel lblPlayersByGameContent;
	private JLabel lblCurrentBlind;  private JLabel lblCurrentBlindContent;
	
	//--------------------------------------------------------- Spectators Queue
	// TODO
	
	//-------------------------------------------------------------- Table param
	private JLabel lblGameMaker;  private JLabel lblGameMakerContent;
	private JLabel lblMinPlayers; private JLabel lblMinPlayersContent;
	private JLabel lblMaxPlayers; private JLabel lblMaxPlayersContent;
	private JLabel lblStackInit;  private JLabel lblStackInitContent;
	private JLabel lblBlindup;    private JLabel lblBlindupContent;
	private JLabel lblSpeakTime;  private JLabel lblSpeakTimeContent;
	private JLabel lblAddTime;    private JLabel lblAddTimeContent;
	
	
	//---------------------------------------------------------- Custom painters
	private IPainter<BEPanel> menuPainter = new MenuPainter<BEPanel>();
//==============================================================================	

	public PreviewController() {
		panel = new JPanel();
		
		menu = new BEPanel(menuPainter);
		menuTitle = new JLabel("Preview");

		btnBack = new BEMenuButton("Back");
		btnJoin = new BEMenuButton("Join");

		//pnlGamePreview = new JPanel();
		pnlGamePreview = new BETablePanel();
		
		IPainter<BEPanel> painter = new IPainter<BEPanel>() {
			@Override
			public void paint(Graphics2D gContext, BEPanel objectToPaint,
					int width, int height) {
				BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = image.createGraphics();
				GradientPaint paint1 = new GradientPaint(0f, 0f, new Color(0x2e2e2e), 0f, 0.499f * height, new Color(0x2e2e2e));
				GradientPaint paint2 = new GradientPaint(0f, 0.5f * height, new Color(0x2e2e2e), 0f, height, new Color(0x242424));
				
				
				
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setColor(Color.BLACK);
				
				g.fillRoundRect(0, 0, width, height, 10, 10);
				
				g.setComposite(AlphaComposite.SrcIn);
				g.setPaint(paint1);
				g.fillRect(-10, 0, width + 20, 15);
				
				g.setPaint(paint2);
				g.fillRect(-10, 15, width + 20 , height - 15);
				
				BufferedImage innerShadow = ShadowFactory.innerShadow(image, 1);
				g.setComposite(AlphaComposite.SrcAtop);
				g.drawImage(innerShadow, 0, 0, null);
						
				gContext.drawImage(image, 0, 0, width, height, null);
				
				g.dispose();
			}
		};
		
		pnlGameStat = new BEPanel(painter);
		pnlSpecQueue = new BEPanel(new GradientPainter<BEPanel>(0x202020, 0x303030));
		pnlTableParam = new BEPanel(painter);
		pnlTableStat = new BEPanel(painter);

		lblGameStat = new JLabel();
		lblRanking = new JLabel("Ranking");
		lblFirstPlace = new JLabel("1. Player1");
		lblFirstPlaceCash = new JLabel("$5,450,000");
		lblSecondPlace = new JLabel("2. Player2");
		lblSecondPlaceCash = new JLabel("$4,000,000");		
		lblThirdPlace = new JLabel("3. Player3");
		lblThirdPlaceCash = new JLabel("$3,000,000");

		lblTableStat = new JLabel();
		lblAveragePot = new JLabel("Average pot :"); 
		lblAveragePotContent = new JLabel("$ 5,000,000");
		lblPlayersByGame = new JLabel("Average number of players by game :"); 
		lblPlayersByGameContent = new JLabel("3");
		lblCurrentBlind = new JLabel("Current blind :"); 
		lblCurrentBlindContent = new JLabel("12");

		lblGameMaker = new JLabel("Table creator :");
		lblGameMakerContent = new JLabel("Noob");
		lblMinPlayers = new JLabel("Minimum number of players :");
		lblMinPlayersContent = new JLabel("12");
		lblMaxPlayers = new JLabel("Maximum number of players :");
		lblMaxPlayersContent = new JLabel("15");
		lblStackInit = new JLabel("Initial Stack :");
		lblStackInitContent = new JLabel("$ 150000");
		lblBlindup = new JLabel("Blind-up :");
		lblBlindupContent = new JLabel("1500");
		lblSpeakTime = new JLabel("Speak time (in seconds) :");
		lblSpeakTimeContent = new JLabel("15");
		lblAddTime = new JLabel("Additional Time :");
		lblAddTimeContent = new JLabel("15");
		
		configureComponents();
	}
	
	private void configureComponents() {
		//------------------------------------------------- Panels Configuration
		panel.setOpaque(false);
		
		pnlGamePreview.setOpaque(false);
		
		pnlGameStat.setOpaque(false);
		pnlGameStat.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		pnlGameStat.setPreferredSize(new Dimension(250, 0));
		
		pnlSpecQueue.setBackground(Color.GREEN);
		pnlSpecQueue.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		pnlSpecQueue.setPreferredSize(new Dimension(0, 50));
		pnlSpecQueue.setBorder(new BETextfieldBorder());
		
		pnlTableParam.setOpaque(false);
		pnlTableStat.setOpaque(false);
		
		//-------------------------------------------- Menu button configuration
		btnBack.setForeground(Color.WHITE);
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AppController.app().showMain();
			}
		});
		
		btnJoin.setForeground(Color.WHITE);
		btnJoin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				final Rectangle currentBounds = pnlGamePreview.getBounds();
				
				// Fade effect
				Animator a = new Animator(500, new TimingTarget() {
					
					@Override
					public void timingEvent(float arg0) {}
					@Override
					public void repeat() {}
					@Override
					public void begin() {}
					@Override
					public void end() {
						Point loc = SwingUtilities.convertPoint(pnlGamePreview, currentBounds.getLocation(), panel);
						panel.removeAll();
						panel.setLayout(null);
						panel.repaint();
						Rectangle b = currentBounds;
						b.setLocation(loc);
						pnlGamePreview.setBounds(currentBounds);
						panel.add(pnlGamePreview);
					}
				});
				a.addTarget(new PropertySetter(pnlTableParam, "alpha", 0.0f));
				a.addTarget(new PropertySetter(pnlTableStat, "alpha", 0.0f));
				a.addTarget(new PropertySetter(pnlGameStat, "alpha", 0.0f));
				a.addTarget(new PropertySetter(pnlSpecQueue, "alpha", 0.0f));
				a.addTarget(new PropertySetter(menu, "alpha", 0.0f));
				a.setAcceleration(.2f);
				a.setDeceleration(.4f);
				
				//Move effect
				
				int w = panel.getPreferredSize().width;
				int h = panel.getPreferredSize().height;
				int newX = GameController.GAME_TABLE_PANEL_X;
				int newY = GameController.GAME_TABLE_PANEL_Y;
				
				Animator b = new Animator(500, new TimingTarget() {
					
					@Override
					public void timingEvent(float arg0) {
					}
					
					@Override
					public void repeat() {
					}
					
					@Override
					public void end() {
						AppController.app().showGame(pnlGamePreview);
					}
					
					@Override
					public void begin() {
					}
				});
				b.setAcceleration(.2f);
				b.setDeceleration(.4f);
				b.addTarget(new PropertySetter(pnlGamePreview, "location", new Point(newX, newY)));
				
				TimingTrigger.addTrigger(a, b, TimingTriggerEvent.STOP);
				
				a.start();
				
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
		box.add(btnJoin);
		box.add(Box.createHorizontalStrut(5));
		
		menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
		menu.add(Box.createVerticalStrut(5));
		menu.add(box);
		menu.add(Box.createVerticalStrut(5));
		
		
		//--------------------------------------------- Table Param configuration
		lblGameMaker.setForeground(Color.WHITE);
		lblGameMakerContent.setForeground(Color.WHITE);
		lblMinPlayers.setForeground(Color.WHITE);
		lblMinPlayersContent.setForeground(Color.WHITE);
		lblMaxPlayers.setForeground(Color.WHITE);
		lblMaxPlayersContent.setForeground(Color.WHITE);
		lblStackInit.setForeground(Color.WHITE);
		lblStackInitContent.setForeground(Color.WHITE);
		lblBlindup.setForeground(Color.WHITE);
		lblBlindupContent.setForeground(Color.WHITE);
		lblSpeakTime.setForeground(Color.WHITE);
		lblSpeakTimeContent.setForeground(Color.WHITE);
		lblAddTime.setForeground(Color.WHITE);
		lblAddTimeContent.setForeground(Color.WHITE);
		
		lblGameMaker.setHorizontalAlignment(JLabel.RIGHT);
		lblMinPlayers.setHorizontalAlignment(JLabel.RIGHT);
		lblMaxPlayers.setHorizontalAlignment(JLabel.RIGHT);
		lblStackInit.setHorizontalAlignment(JLabel.RIGHT);
		lblBlindup.setHorizontalAlignment(JLabel.RIGHT);
		lblSpeakTime.setHorizontalAlignment(JLabel.RIGHT);
		lblAddTime.setHorizontalAlignment(JLabel.RIGHT);
	
		
		JPanel tableParamContent = new JPanel(new SpringLayout());
		tableParamContent.setOpaque(false);
		tableParamContent.add(lblGameMaker);  tableParamContent.add(lblGameMakerContent);
		tableParamContent.add(lblMinPlayers); tableParamContent.add(lblMinPlayersContent);
		tableParamContent.add(lblMaxPlayers); tableParamContent.add(lblMaxPlayersContent);
		tableParamContent.add(lblStackInit);  tableParamContent.add(lblStackInitContent);
		tableParamContent.add(lblBlindup);    tableParamContent.add(lblBlindupContent);
		tableParamContent.add(lblSpeakTime);  tableParamContent.add(lblSpeakTimeContent);
		tableParamContent.add(lblAddTime);    tableParamContent.add(lblAddTimeContent);
		SpringUtilities.makeCompactGrid(tableParamContent, 7, 2, 20, 10, 10, 5);
		tableParamContent.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		
		
		BEGroupPanelBorder border = new BEGroupPanelBorder("Table Settings");
		border.setFontSize(14);
		pnlTableParam.setBorder(border);
		pnlTableParam.setLayout(new BorderLayout(10, 0));
		
		pnlTableParam.add(tableParamContent);
		
		
		//--------------------------------------------- Table Stat configuration
		lblTableStat.setForeground(Color.WHITE);
		lblAveragePot.setForeground(Color.WHITE);
		lblAveragePotContent.setForeground(Color.WHITE);
		lblPlayersByGame.setForeground(Color.WHITE);
		lblPlayersByGameContent.setForeground(Color.WHITE);
		lblCurrentBlind.setForeground(Color.WHITE);
		lblCurrentBlindContent.setForeground(Color.WHITE);
		
		lblAveragePot.setHorizontalAlignment(JLabel.RIGHT);
		lblPlayersByGame.setHorizontalAlignment(JLabel.RIGHT);
		lblCurrentBlind.setHorizontalAlignment(JLabel.RIGHT);
		
		JPanel tableStatContent = new JPanel(new SpringLayout());
		tableStatContent.setOpaque(false);
		tableStatContent.add(lblAveragePot); tableStatContent.add(lblAveragePotContent);
		tableStatContent.add(lblPlayersByGame); tableStatContent.add(lblPlayersByGameContent);
		tableStatContent.add(lblCurrentBlind); tableStatContent.add(Box.createHorizontalGlue());
		SpringUtilities.makeCompactGrid(tableStatContent, 3, 2, 20, 10, 10, 5);
		tableStatContent.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		
		BEGroupPanelBorder border2 = new BEGroupPanelBorder("Table Statistics");
		border2.setFontSize(14);
		pnlTableStat.setBorder(border2);
		pnlTableStat.setLayout(new BorderLayout(10, 0));
		pnlTableStat.add(tableStatContent);
		
		//------------------------------------------------------ Game statistics
		lblGameStat.setForeground(Color.WHITE);
		lblRanking.setForeground(Color.WHITE);
		lblFirstPlace.setForeground(Color.WHITE);
		lblFirstPlaceCash.setForeground(Color.WHITE);
		lblSecondPlace.setForeground(Color.WHITE);
		lblSecondPlaceCash.setForeground(Color.WHITE);
		lblThirdPlace.setForeground(Color.WHITE);
		lblThirdPlaceCash.setForeground(Color.WHITE);
		
		JSeparator gameStatSep = new JSeparator(JSeparator.HORIZONTAL);
		gameStatSep.setForeground(new Color(0,128,255));
		gameStatSep.setMaximumSize(new Dimension(500, 10));
		
		Box gameStatBox = makeLine(
				vs(20),
				makeColumn(lblRanking, hg()),
				vs(2),
				gameStatSep,
				makeColumn(lblFirstPlace, hg(), lblFirstPlaceCash),
				makeColumn(lblSecondPlace, hg(), lblSecondPlaceCash),
				makeColumn(lblThirdPlace, hg(), lblThirdPlaceCash),
				hg()
		);
		
		gameStatBox.setAlignmentY(Box.TOP_ALIGNMENT);
		
		BEGroupPanelBorder border3 = new BEGroupPanelBorder("Game Statistics");
		border3.setFontSize(14);
		pnlGameStat.setLayout(new BorderLayout());
		pnlGameStat.setBorder(border3);
		
		pnlGameStat.add(makeColumn(hs(10), gameStatBox, hs(10)));
		
		//------------------------------------------------------- General Layout
		Box previewQueueBox = Box.createVerticalBox();
		previewQueueBox.add(pnlGamePreview);
		previewQueueBox.add(vs(10));
		previewQueueBox.add(pnlSpecQueue);
		
		Box upperBox = makeColumn(
			hs(10), previewQueueBox, hs(10), pnlGameStat, hs(10)	
		);
		
		Box lowerVBox = Box.createVerticalBox();
		
		Box lowerBox = Box.createHorizontalBox();
		lowerBox.add(Box.createHorizontalStrut(10));
		lowerBox.add(pnlTableParam);
		lowerBox.add(Box.createHorizontalStrut(10));
		lowerBox.add(pnlTableStat);
		lowerBox.add(Box.createHorizontalStrut(10));
		
		lowerVBox.add(lowerBox);
		lowerVBox.add(Box.createVerticalStrut(10));
		
		//--------------------------------------------------------  panel layout
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(menu);
		panel.add(Box.createVerticalStrut(10));
		panel.add(upperBox);
		panel.add(Box.createVerticalStrut(10));
		panel.add(lowerVBox);
		panel.add(Box.createVerticalStrut(10));
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}
	
	public void panelInstalled() {
	}

}
