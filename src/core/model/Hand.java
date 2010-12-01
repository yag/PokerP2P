package core.model;

import core.protocol.Card;

public class Hand implements java.io.Serializable {
	public Hand(Card c1, Card c2) {
		card[0] = c1;
		card[1] = c2;
	}
	public Card[] getCard() {
		return card;
	}
	private Card[] card = new Card[2];
	private static final long serialVersionUID = 1L;
}
