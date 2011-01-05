package core.protocol;

import core.Pair;
import core.model.Game;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Client extends Remote, java.io.Serializable {
	void clientLoggedIn(Client client) throws RemoteException;
	void clientLoggedOut(String name) throws RemoteException;
	void clientBanned(String name) throws RemoteException;
	void clientBecamePlayer(Client client) throws RemoteException;
	void clientBecameSpectator(Client client) throws RemoteException;
	void addChatMessage(ChatMessage msg) throws RemoteException;
	void playerActed(Action action) throws RemoteException;
	void handBegan(Card[] flop, Card turn, Card river, List<Pair<Card, Card>> playersCards) throws RemoteException;
	void handEnded(List<Pair<List<Client>, Integer>> winners) throws RemoteException;
	Server becomeServer() throws RemoteException;
	void newServer(Server server) throws RemoteException;
	void play() throws RemoteException;
	String getName() throws RemoteException;
	int getMoney() throws RemoteException;
	void setCurrentBet(int currentBet) throws RemoteException;
	int getCurrentBet() throws RemoteException;
	// Implementation specific method (not part of the protocol)
	Game getGame() throws RemoteException;
	void setMoney(int money) throws RemoteException ;
}
