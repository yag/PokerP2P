package core.protocol;

import core.model.Game;
import core.model.Action;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote, java.io.Serializable {
	Game login(String host, int port) throws RemoteException;
	void logout(Client client) throws RemoteException;
	Game becomePlayer(Client client) throws RemoteException;
	Game becomeSpectator(Client client) throws RemoteException;
	void postChatMessage(Client client, String text) throws RemoteException;
	void act(Action action) throws RemoteException;
	void disconnect(Client self) throws RemoteException;
}
