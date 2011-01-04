package gui.controller;

import javax.swing.ImageIcon;

import core.protocol.Suit;
import core.protocol.Value;

public class ResourceManager {

	private static ResourceManager instance = new ResourceManager();
	
	private ResourceManager() {}
	
	
	public static ImageIcon SECONDARY_POT_ICON = new ImageIcon(instance.getClass().getResource("/assets/secondary_pot.png"));
	public static ImageIcon TEST_CARD_ICON = new ImageIcon(instance.getClass().getResource("/assets/roi_p.png"));
	public static ImageIcon PLAYERS_CARDS = new ImageIcon(instance.getClass().getResource("/assets/players_card.png"));
	public static ImageIcon DEALER_ICON = new ImageIcon(instance.getClass().getResource("/assets/dealer_button.png"));
	
	public static ImageIcon getCard(Value value, Suit suit) {
		String prefix = "";
		switch (value) {
		case ACE: prefix = "as_";
			break;
		case KING: prefix = "valet_";
			break;
		case QUEEN: prefix = "queen_";
			break;
		case JACK: prefix = "valet_";
			break;
		case TEN: prefix = "dix_";
			break;
		case NINE: prefix = "neuf_";
			break;
		case HEIGHT: prefix = "huit_";
			break;
		case SEVEN: prefix = "sept_";
			break;
		case SIX: prefix = "six_";
			break;
		case FIVE: prefix = "cinq_";
			break;
		case FOUR: prefix = "quatre_";
			break;
		case THREE: prefix = "trois_";
			break;
		case TWO: prefix = "two_";
			break;
		}
		
		String name = "";
		switch (suit) {
		case HEARTS: name = prefix + "co.png";
			break;
		case DIAMONDS: name = prefix + "ca.png";
			break;
		case SPADES: name = prefix + "p.png";
			break;
		case CLUBS: name = prefix + "t.png";
			break;
		}
		return new ImageIcon(instance.getClass().getResource("/assets/" + name));
	}
	
}
