package core.protocol;

import core.Pair;
import java.util.List;

public class GameStatus implements java.io.Serializable {
	public List<Client> players;
	public List<Client> spectators;
	public int minNbPlayers;
	public int thinkTime;
	public int buyIn;
	public int blindUpTime;
	public float blindUpInc;
	// Everything next is null if there is no round currently
	public Card[] flop;
	public Card turn;
	public Card river;
	public List<Pair<Card, Card>> playersCards;
	public Client currentPlayer;
	public Client dealer;
	public RoundState state = RoundState.PREFLOP;
	//public List<Integer> pots;
	public List<Pair<List<Client>,Integer>> pots ;
	
	private static final long serialVersionUID = 1L;
}
