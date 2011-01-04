package gui.view.components;

import static gui.view.BEBoxLayoutUtils.hg;
import static gui.view.BEBoxLayoutUtils.hs;
import static gui.view.BEBoxLayoutUtils.makeColumn;
import static gui.view.BEBoxLayoutUtils.makeLine;
import static gui.view.BEBoxLayoutUtils.vs;

import gui.view.BERoundBorder;
import gui.view.DualGradientPainter;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JLabel;

public class BERankingPanel extends BEPanel {
	
	public static int RANKING_WIDTH = 200;
	public static int RANKING_HEIGHT = 90;
	
	//=============================================================== Components
	private JLabel lblRanking = new JLabel("Ranking");
	private JLabel lblFirstPlace = new JLabel("1. Player");  
	private JLabel lblFirstPlaceCash = new JLabel("$ 5,000,000");
	
	private JLabel lblSecondPlace = new JLabel("2. Player"); 
	private JLabel lblSecondPlaceCash = new JLabel("$ 5,000,000");
	
	private JLabel lblThirdPlace = new JLabel("2. Player"); 
	private JLabel lblThirdPlaceCash = new JLabel("$ 5,000,000");
		
	public BERankingPanel() {
		super(new DualGradientPainter<BEPanel>(
				new int[] { 0x535353, 0x434343, 0x3e3e3e, 0x323232} , true, 10));
		
		setLayout(new BorderLayout());
		
		setPreferredSize(new Dimension(RANKING_WIDTH, RANKING_HEIGHT));
		setMaximumSize(new Dimension(RANKING_WIDTH+100, RANKING_HEIGHT));
		
		lblRanking.setAlignmentX(CENTER_ALIGNMENT);
		
		lblRanking.setForeground(Color.WHITE);
		lblFirstPlace.setForeground(Color.WHITE);
		lblFirstPlaceCash.setForeground(Color.WHITE);
		lblSecondPlace.setForeground(Color.WHITE);
		lblSecondPlaceCash.setForeground(Color.WHITE);
		lblThirdPlace.setForeground(Color.WHITE);
		lblThirdPlaceCash.setForeground(Color.WHITE);
		
		setOpaque(false);

		setBorder(new BERoundBorder());
		
		Box gameStatBox = makeLine(
				lblRanking,
				vs(10),
				makeColumn(hs(5), lblFirstPlace, hg(), lblFirstPlaceCash, hs(5)),
				makeColumn(hs(5),lblSecondPlace, hg(), lblSecondPlaceCash, hs(5)),
				makeColumn(hs(5),lblThirdPlace, hg(), lblThirdPlaceCash, hs(5))
		);
		
		add(gameStatBox);
	}
}
