package core.protocol;

import core.model.Action;
import core.model.ChatMessage;
import core.model.Game;
import core.Pair;
import gui.GUIController;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ClientImpl implements Client {
	public ClientImpl(GUIController controller, String name, String serverhost, int serverport, int port) {
		this.controller = controller;
		this.name = name;
		this.port = port;
		money = -1;
	}
	@Override
	public void clientLoggedIn(Client client, Game game) throws RemoteException {
		this.game = game;
		controller.clientLoggedIn(client);
	}
	@Override
	public void clientLoggedOut(String name, Game game) throws RemoteException {
		this.game = game;
		controller.clientLoggedOut(name);
	}
	@Override
	public void clientBanned(Client client, Game game) throws RemoteException {
		this.game = game;
		controller.clientBanned(client);
	}
	@Override
	public void clientBecamePlayer(Client client, Game game) throws RemoteException {
		this.game = game;
		controller.clientBecamePlayer(client);
	}
	@Override
	public void clientBecameSpectator(Client client, Game game) throws RemoteException {
		this.game = game;
		controller.clientBecameSpectator(client);
	}
	@Override
	public void addChatMessage(ChatMessage msg) throws RemoteException {
		controller.addChatMessage(msg);
	}
	@Override
	public void playerActed(Action action, Game game) throws RemoteException {
		this.game = game;
		controller.playerActed(action);
	}
	@Override
	public void handBegan() throws RemoteException {
		currentBet = 0;
		controller.handBegan();
	}
	@Override
	public void handEnded(List<Pair<Client, Integer>> winners) throws RemoteException {
		game.setCurrentRound(null);
		controller.handEnded(winners);
	}
	@Override
	public void becomeServer() throws RemoteException {
		System.out.println("OK, I'm the server.");
		Server server = new ServerImpl(game);
		try {
			LocateRegistry.getRegistry("localhost", port).bind("Server", UnicastRemoteObject.exportObject(server, 0));
			final List<Client> players = game.getPlayers();
			final List<Client> spectatorss = game.getSpectators();
			game.setServer(server);
			final Game g = game;
			new Thread() {
				@Override
				public void run() {
					for (Client c: players) {
						try {
							c.newServer(g);
						} catch (RemoteException e) {
							e.printStackTrace();System.exit(1);
						}
					}
					for (Client c: spectatorss) {
						try {
							c.newServer(g);
						} catch (RemoteException e) {
							e.printStackTrace();System.exit(1);
						}
					}
				}
			}.start();
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		} catch(java.rmi.AlreadyBoundException e) {
			e.printStackTrace();System.exit(1);
		}
	}
	@Override
	public void newServer(Game game) throws RemoteException {
		this.game = game;
	}
	@Override
	public void play() throws RemoteException {
		controller.play();
	}
	@Override
	public void updateGame(Game game) throws RemoteException {
		this.game = game;
		if (money == -1)
			money = game.getBuyIn();
	}
	@Override
	public String getName() throws RemoteException {
		return name;
	}
	@Override
	public int getMoney() throws RemoteException {
		return money;
	}
	@Override
	public void setCurrentBet(int currentBet) throws RemoteException {
		this.currentBet = currentBet;
	}
	@Override
	public int getCurrentBet() throws RemoteException {
		return currentBet;
	}
	@Override
	public Game getGame() throws RemoteException {
		return game;
	}
	private GUIController controller;
	private Game game;
	private String name;
	private int money;
	private int currentBet;
	private int port;
	private static final long serialVersionUID = 1L;
}
