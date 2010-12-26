package core.controller;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import core.model.Game;
import core.protocol.*;
import gui.GUIController;

public class ProtocolController {
	public void login(GUIController controller, String name, String serverhost, int serverport, int port) {
		selfport = port;
		self = new ClientImpl(controller, name, serverhost, serverport, port);
		try {
			LocateRegistry.getRegistry("localhost", port).bind("Client", UnicastRemoteObject.exportObject(self, 0));
			if (server == null) {
				server = (Server)LocateRegistry.getRegistry(serverhost, serverport).lookup("Server");
			}
			((ClientImpl)self).setGameFrom(server.login(java.net.InetAddress.getLocalHost().getHostAddress(), port));
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
			self.getGame().removeClient(self.getName());
			realLogout();
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
					server.disconnect();
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
			if (server.becomePlayer(self)) {
				self.getGame().newPlayer(self);
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
			if (server.becomeSpectator(self)) {
				self.getGame().newSpectator(self);
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
			if (action.getType().equals(ActionType.RAISE) || action.getType().equals(ActionType.CALL) || action.getType().equals(ActionType.ALLIN) ) {
				self.setMoney(self.getMoney() - action.getBet() );
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
