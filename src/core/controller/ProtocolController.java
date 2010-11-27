package core.controller;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import core.model.Action;
import core.model.ActionType;
import core.model.Game;
import core.protocol.*;
import gui.GUIController;

public class ProtocolController {
	public void login(GUIController controller, String name, String serverhost, int serverport, int port) {
		selfport = port;
		self = new ClientImpl(controller, name, serverhost, serverport, port);
		try {
			//self = (Client)UnicastRemoteObject.exportObject(self, 0);
			LocateRegistry.getRegistry("localhost", port).bind("Client", UnicastRemoteObject.exportObject(self, 0));
			if (server == null) {
				server = (Server)LocateRegistry.getRegistry(serverhost, serverport).lookup("Server");
			}
			self.updateGame(server.login(java.net.InetAddress.getLocalHost().getHostAddress(), port));
			System.out.println("OK, you're logged in.");
		} catch (java.rmi.AlreadyBoundException e) {
			e.printStackTrace();System.exit(1);
		} catch (java.rmi.NotBoundException e) {
			e.printStackTrace();System.exit(1);
		} catch (java.rmi.AccessException e) {
			e.printStackTrace();System.exit(1);
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		} catch (java.net.UnknownHostException e) {
			e.printStackTrace();System.exit(1);
		}
	}
	public void logout() {
		try {
			server.logout(self);
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		}
	}
	public void realLogout() {
		try {
			LocateRegistry.getRegistry("localhost", selfport).unbind("Client");
			UnicastRemoteObject.unexportObject(self, false);
			// Am I the server too?
			String[] list = LocateRegistry.getRegistry("localhost", selfport).list();
			for (int i = 0 ; i < list.length ; ++i) {
				if (list[i].equals("Server")) {
					System.out.println("I'm the server, removing it.");
					server.disconnect(self);
					LocateRegistry.getRegistry("localhost", selfport).unbind("Server");
					UnicastRemoteObject.unexportObject(server, false);
					break;
				}
			}
			System.exit(0);
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		} catch (java.rmi.NotBoundException e) {
			e.printStackTrace();System.exit(1);
		}
	}
	public boolean becomePlayer() {
		try {
			Game game = server.becomePlayer(self);
			if (game != null) {
				self.updateGame(game);
				System.out.println("OK, I'm playing.");
				return true;
			}
			System.out.println("Server refused me to become a player.");
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		}
		return false;
	}
	public boolean becomeSpectator() {
		try {
			Game game = server.becomeSpectator(self);
			if (game != null) {
				self.updateGame(game);
				return true;
			}
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		}
		return false;
	}
	public void postChatMessage(String text) {
		try {
			server.postChatMessage(self, text);
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		}
	}
	public void act(Action action) {
		try {
			action.setPlayer(self);
			if (action.getType().equals(ActionType.SITOUT) || action.getType().equals(ActionType.FOLD)) {
				self.setCurrentBet(-1);
			}
			server.act(action);
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		}
	}
	public void createGame(Game game, int port) {
		server = new ServerImpl(game);
		try {
			//server = (Server)UnicastRemoteObject.exportObject(server, 0);
			LocateRegistry.getRegistry("localhost", port).bind("Server", UnicastRemoteObject.exportObject(server, 0));
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		} catch(AlreadyBoundException e) {
			e.printStackTrace();System.exit(1);
		}
	}
	public Client getClient() {
		return self;
	}
	private Client self;
	private Server server;
	private int selfport;
}
