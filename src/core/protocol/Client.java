package core.protocol;

import core.model.ChatMessage;
import core.model.Action;
import core.model.Game;
import core.Pair;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Client extends Remote, java.io.Serializable {
	void clientLoggedIn(Client client, Game game) throws RemoteException;
	void clientLoggedOut(String name, Game game) throws RemoteException;
	void clientBanned(Client client, Game game) throws RemoteException;
	void clientBecamePlayer(Client client, Game game) throws RemoteException;
	void clientBecameSpectator(Client client, Game game) throws RemoteException;
	void addChatMessage(ChatMessage msg) throws RemoteException;
	void playerActed(Action action, Game game) throws RemoteException;
	void handBegan() throws RemoteException;
	void handEnded(List<Pair<Client, Integer>> winners) throws RemoteException;
	void becomeServer() throws RemoteException;
	void newServer(Game game) throws RemoteException;
	void play() throws RemoteException;
	void updateGame(Game game) throws RemoteException;
	String getName() throws RemoteException;
	int getMoney() throws RemoteException;
	void setCurrentBet(int currentBet) throws RemoteException;
	int getCurrentBet() throws RemoteException;
	Game getGame() throws RemoteException;
}
