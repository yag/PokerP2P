package core.protocol;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote, java.io.Serializable {
	GameStatus login(String host, int port) throws RemoteException;
	void logout(Client client) throws RemoteException;
	boolean becomePlayer(Client client) throws RemoteException;
	boolean becomeSpectator(Client client) throws RemoteException;
	void postChatMessage(Client client, String text) throws RemoteException;
	void act(Action action) throws RemoteException;
	void disconnect(Client self) throws RemoteException;
}
