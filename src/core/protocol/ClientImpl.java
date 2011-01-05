package core.protocol;

import core.model.*;
import core.Pair;
import gui.GUIController;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

public class ClientImpl implements Client {
	public ClientImpl(GUIController controller, String name, String serverhost, int serverport, int port) {
		this.controller = controller;
		this.name = name;
		this.port = port;
		money = -1;
	}
	@Override
	public void clientLoggedIn(Client client) throws RemoteException {
		game.newSpectator(client);
		controller.clientLoggedIn(client);
	}
	@Override
	public void clientLoggedOut(String name) throws RemoteException {
		List<Client> players = game.getPlayers();
		Client nclient = null;
		if (game.getCurrentRound() != null) {
			// Just have to replace the client with a fake
			for (int i = 0 ; i < players.size() ; ++i) {
				if (players.get(i).getName().equals(name)) {
					nclient = new FakeClient(name, players.get(i).getMoney(), players.get(i).getCurrentBet(), game);
					players.set(i, nclient);
				}
			}
			game.getCurrentRound().updatePlayer(name, nclient);
		} else {
			game.removeClient(name);
			controller.clientLoggedOut(name);
		}
	}
	@Override
	public void clientBanned(String name) throws RemoteException {
		game.removeClient(name);
		controller.clientBanned(name);
	}
	@Override
	public void clientBecamePlayer(Client client) throws RemoteException {
		game.newPlayer(client);
		controller.clientBecamePlayer(client);
	}
	@Override
	public void clientBecameSpectator(Client client) throws RemoteException {
		game.newSpectator(client);
		controller.clientBecameSpectator(client);
	}
	@Override
	public void addChatMessage(ChatMessage msg) throws RemoteException {
		controller.addChatMessage(msg);
	}
	@Override
	public void playerActed(Action action) throws RemoteException {
		// TODO: update game
		try {
			game.playerActed(action) ;
		} catch(core.controller.CheatException ce) {
			//
		}
		controller.playerActed(action);
	}
	@Override
	public void handBegan(Card[] flop, Card turn, Card river, List<Pair<Card, Card>> playersCards) throws RemoteException {
		currentBet = 0;
		List<Hand> hands = new LinkedList<Hand>();
		for (Pair<Card, Card> cards: playersCards) {
			hands.add(new Hand(cards.getFirst(), cards.getSecond()));
		}
		game.setCurrentRound(new Round(flop[0], flop[1], flop[2], turn, river, hands));
		game.handBegan();
		controller.handBegan();
	}
	@Override
	public void handEnded(List<Pair<List<Client>, Integer>> winners) throws RemoteException {
		game.setCurrentRound(null);
		controller.handEnded(winners);
	}
	@Override
	public Server becomeServer() throws RemoteException {
		System.out.println("OK, I'm the server.");
		final Server server = new ServerImpl(game);
		try {
			LocateRegistry.getRegistry("localhost", port).bind("Server", UnicastRemoteObject.exportObject(server, 0));
			final List<Client> players = game.getPlayers();
			final List<Client> spectatorss = game.getSpectators();
			game.setServer(server);
			final Game g = game;
                        /*
			new Thread() {
				@Override
				public void run() {
                                */
					for (Client c: players) {
						try {
							c.newServer(server);
						} catch (RemoteException e) {
							e.printStackTrace();System.exit(1);
						}
					}
					for (Client c: spectatorss) {
						try {
							c.newServer(server);
						} catch (RemoteException e) {
							e.printStackTrace();System.exit(1);
						}
					}
			//	}
			//}.start();
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		} catch(java.rmi.AlreadyBoundException e) {
			e.printStackTrace();System.exit(1);
		}
                return server;
	}
	@Override
	public void newServer(Server server) throws RemoteException {
		game.setServer(server);
	}
	@Override
	public void play() throws RemoteException {
		controller.play();
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
	public void setGameFrom(GameStatus g) {
		game = new Game(g.minNbPlayers, g.thinkTime, g.buyIn, g.blindUpTime, g.blindUpInc);
		money = g.buyIn ;
		game.setPlayers(new LinkedList<Client>(g.players));
		game.setSpectators(new LinkedList<Client>(g.spectators));
		if (g.currentPlayer != null) {
			List<Hand> hands = new LinkedList<Hand>();
			for (Pair<Card, Card> cards: g.playersCards) {
				hands.add(new Hand(cards.getFirst(), cards.getSecond()));
			}
			Round r = new Round(g.flop[0], g.flop[1], g.flop[2], g.turn, g.river, hands);
			r.setCurrentPlayer(g.currentPlayer);
			r.setDealer(g.dealer);
			r.setState(g.state);
			r.setPots(g.pots);
			game.setCurrentRound(r);
		}
	}
	
	public void setMoney( int m ) throws RemoteException {
		this.money = m ;
	}
	private GUIController controller;
	private Game game;
	private String name;
	private int money;
	private int currentBet;
	private int port;
	private static final long serialVersionUID = 1L;
}
