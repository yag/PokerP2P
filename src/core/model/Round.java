package core.model;

import core.protocol.Card;
import core.protocol.RoundState;
import core.protocol.Client;
import java.util.List;
import java.util.LinkedList;

public class Round implements java.io.Serializable {
	public Round(Card f1, Card f2, Card f3, Card t, Card r, List<Hand> p) {
		flop[0] = f1;
		flop[1] = f2;
		flop[2] = f3;
		turn = t;
		river = r;
		playersCards = p;
		pots.add(0);
	}
	public Card[] getFlop() {
		return flop;
	}
	public Card getTurn() {
		return turn;
	}
	public Card getRiver() {
		return river;
	}
	public List<Hand> getPlayersCards() {
		return playersCards;
	}
	public Client getCurrentPlayer() {
		return currentPlayer;
	}
	public void setCurrentPlayer(Client currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	public Client getDealer() {
		return dealer;
	}
	public void setDealer(Client dealer) {
		this.dealer = dealer;
	}
	public RoundState getState() {
		return state;
	}
	public void setState(RoundState state) {
		this.state = state;
	}
	public List<Integer> getPots() {
		return pots;
	}
	public void setPots(List<Integer> pots) {
		this.pots = pots;
	}
	public void addToPot(int bet) {
		pots.set(pots.size() - 1, pots.get(pots.size() - 1) + bet);
	}
	private Card[] flop = new Card[3];
	private Card turn;
	private Card river;
	private List<Hand> playersCards;
	private Client currentPlayer;
	private Client dealer;
	private RoundState state = RoundState.PREFLOP;
	private List<Integer> pots = new LinkedList<Integer>();
	private static final long serialVersionUID = 1L;
}
