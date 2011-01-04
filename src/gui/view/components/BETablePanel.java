package gui.view.components;

import gui.controller.ResourceManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import core.protocol.Suit;
import core.protocol.Value;

public class BETablePanel extends JPanel {

	public static final int MAX_PLAYERS_NUM = 9;
	
//=================================================================== Components	
	private ImageIcon tableIcon;
	private ImageIcon stackIcon;
	
	private JPanel[] playersPanel;
	
	private JLabel table;
	private JLabel[] publicCards;
	
	private JLabel lblPot;
	private JLabel lblPotContent;
	
	private int[] playersX = new int[] { 44, 14, 44, 190, 351, 512, 653, 689, 653 };
	private int[] playersY = new int[] { 41, 154, 267, 326, 326, 326, 267, 154, 41};
	
	private int[] cardsX = new int[] { 235, 303, 371, 439, 507 };
	
	private BESecondaryPot spot = new BESecondaryPot(10000);
	
	private JLabel mainPot = new JLabel("$5000000", ResourceManager.SECONDARY_POT_ICON, JLabel.LEFT);
	private int mainPotValue;
	
	public BETablePanel() {
		//TODO: Model
		setLayout(null); // We use absolute positioning
		
		tableIcon = new ImageIcon(this.getClass().getResource("/assets/table.png"));
		stackIcon = null; //TODO
		
		table = new JLabel(tableIcon);
		
		playersPanel = new BEPlayerPanel[] {
			new BEPlayerPanel("", 0),
			new BEPlayerPanel("", 0),
			new BEPlayerPanel("", 0),
			new BEPlayerPanel("", 0),
			new BEPlayerPanel("", 0),
			new BEPlayerPanel("", 0),
			new BEPlayerPanel("", 0),
			new BEPlayerPanel("", 0),
			new BEPlayerPanel("", 0)
		};
		
		ImageIcon card = new ImageIcon(this.getClass().getResource("/assets/roi_p.png"));
		ImageIcon scard = scaleImage(card.getImage(), 59, 90);
		
		publicCards = new JLabel[] {
			new JLabel(scard),
			new JLabel(scard),
			new JLabel(scard),
			new JLabel(scard),
			new JLabel(scard)
		};
		
		lblPot = new JLabel("POT");
		lblPotContent = new JLabel("$5,750,000");
		
		setPreferredSize(new Dimension(800, 400));
		
		configureComponents();
	}
	
	private void configureComponents() {
		configureCardsBounds();
		configurePlayersBounds();
		
		spot.setBounds(371, 70, spot.getPreferredSize().width, spot.getPreferredSize().height);
		mainPot.setBounds(371, 230, mainPot.getPreferredSize().width, mainPot.getPreferredSize().height);
		
		add(spot);
		add(mainPot);
		
		table.setBounds(122, 42, 556, 263);
		add(table);
		
		lblPot.setBounds(336, 7, 100, 16);
		lblPotContent.setBounds(336, 24, 100, 16);
		
		lblPot.setHorizontalAlignment(JLabel.CENTER);
		lblPotContent.setHorizontalAlignment(JLabel.CENTER);
		
		lblPot.setForeground(Color.WHITE);
		lblPotContent.setForeground(Color.WHITE);
		
		add(lblPot);
		add(lblPotContent);
	}
	
	private void configurePlayersBounds() {
		for (int i = 0; i < playersPanel.length; ++i) {
			if (playersPanel[i] == null)
				continue;
			
			playersPanel[i].setBounds(playersX[i], playersY[i], 99, 38);
			
			JLabel l =new JLabel(ResourceManager.PLAYERS_CARDS);
			JLabel d = new JLabel(ResourceManager.DEALER_ICON);
			JLabel c = new JLabel(scaleImage(ResourceManager.getCard(Value.ACE, Suit.HEARTS).getImage(), 50, 70));
			JLabel u = new JLabel(scaleImage(ResourceManager.TEST_CARD_ICON.getImage(), 50, 70));
			
			switch (i) {
			case 0:
			case 1:
			case 2:
				c.setBounds(playersX[i]+100, playersY[i]-25, c.getPreferredSize().width, c.getPreferredSize().height);
				u.setBounds(playersX[i]+110, playersY[i]-25, c.getPreferredSize().width, c.getPreferredSize().height);
				break;

			case 3:
			case 4:
			case 5:
				c.setBounds(playersX[i]+20, playersY[i]-75, c.getPreferredSize().width, c.getPreferredSize().height);
				u.setBounds(playersX[i]+30, playersY[i]-75, c.getPreferredSize().width, c.getPreferredSize().height);
				break;
				
			default:
				u.setBounds(playersX[i]-48, playersY[i]-25, c.getPreferredSize().width, c.getPreferredSize().height);
				c.setBounds(playersX[i]-58, playersY[i]-25, c.getPreferredSize().width, c.getPreferredSize().height);
				break;
			}
			
			d.setBounds(playersX[i]+20, playersY[i]+40, d.getPreferredSize().width, d.getPreferredSize().width);
			l.setBounds(playersX[i]+60, playersY[i]+45, l.getPreferredSize().width, l.getPreferredSize().height);
			
//			if (i < 3 )
//				l.setBounds(playersX[i]+100, playersY[i] - 5, l.getPreferredSize().width, l.getPreferredSize().height);
//			else
//				l.setBounds(playersX[i]-40, playersY[i] - 5, l.getPreferredSize().width, l.getPreferredSize().height);
			this.add(c);
			this.add(u);
			this.add(d);
			this.add(l);
			this.add(playersPanel[i]);
		}
	}
	
	private void configureCardsBounds() {
		for (int i = 0; i < publicCards.length; ++i) {
			if (publicCards[i] == null) 
				continue;
			
			publicCards[i].setBounds(cardsX[i], 124, 59, 90);
			//this.add(publicCards[i]);
		}
	}
	
	private static ImageIcon scaleImage(Image image, int w, int h) {
		BufferedImage i = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = i.createGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		
		g.drawImage(image, 0, 0, w, h, null);
		g.dispose();
		
		return new ImageIcon(i);
	}
	
	

}
