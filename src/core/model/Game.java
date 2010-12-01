package core.model;

import core.protocol.Client;
import core.protocol.Server;
import java.util.List;
import java.util.LinkedList;

public class Game implements java.io.Serializable {
	public Game(int minNbPlayers, int thinkTime, int buyIn, int blindUpTime, float blindUpInc) {
		this.minNbPlayers = minNbPlayers;
		this.thinkTime = thinkTime;
		this.buyIn = buyIn;
		this.blindUpTime = blindUpTime;
		this.blindUpInc = blindUpInc;
	}
	public Round getCurrentRound() {
		return currentRound;
	}
	public void setCurrentRound(Round currentRound) {
		this.currentRound = currentRound;
	}
	public int getMinNbPlayers() {
		return minNbPlayers;
	}
	public int getThinkTime() {
		return thinkTime;
	}
	public int getBuyIn() {
		return buyIn;
	}
	public int getBlindUpTime() {
		return blindUpTime;
	}
	public float getBlindUpInc() {
		return blindUpInc;
	}
	public List<Client> getPlayers() {
		return players;
	}
	public void setPlayers(List<Client> players) {
		this.players = players;
	}
	public List<Client> getSpectators() {
		return spectators;
	}
	public void setSpectators(List<Client> spectators) {
		this.spectators = spectators;
	}
	public Server getServer() {
		return server;
	}
	public void setServer(Server server) {
		this.server = server;
	}
	private Round currentRound;
	private int minNbPlayers;
	private int thinkTime;
	private int buyIn;
	private int blindUpTime;
	private float blindUpInc;
	// FIXME: use java.util.Collections.synchronizedList?
	private List<Client> players = new LinkedList<Client>();
	private List<Client> spectators = new LinkedList<Client>();
	private Server server;
	public static final int MAX_NB_PLAYERS = 10;
	private static final long serialVersionUID = 1L;
}
