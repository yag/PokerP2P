package core.protocol;

public class Card implements java.io.Serializable {
	public Card(Value value, Suit suit) {
		this.value = value;
		this.suit = suit;
	}
	public Value getValue() {
		return value;
	}

	public Suit getSuit() {
		return suit;
	}
	private Value value;
	private Suit suit;
	private static final long serialVersionUID = 1L;
}
