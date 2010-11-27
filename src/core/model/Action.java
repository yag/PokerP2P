package core.model;

import core.protocol.Client;

public class Action implements java.io.Serializable {
	public Action(Client player, ActionType type, int bet) {
		this.player = player;
		this.type = type;
		this.bet = bet;
	}
	public Action(ActionType type, int bet) {
		this.type = type;
		this.bet = bet;
	}
	public Client getPlayer() {
		return player;
	}
	public void setPlayer(Client player) {
		this.player = player;
	}
	public ActionType getType() {
		return type;
	}
	public int getBet() {
		return bet;
	}
	private Client player;
	private ActionType type;
	private int bet;
	private static final long serialVersionUID = 1L;
}
