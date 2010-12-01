package core.protocol;

import core.Pair;
import core.model.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

abstract class ClientsIterator {
	public ClientsIterator(List<Client> players, List<Client> spectators) {
		this.players = players;
		this.spectators = spectators;
	}
	public void iterate() throws RemoteException {
		iterate(true) ;
	}
	public void iterate(boolean newThread) throws RemoteException {
		if (newThread) {
			(new Thread() {
				@Override
				public void run() {
					ServerImpl.lock.lock();
					try {
						try {
							onStart();
						} catch (RemoteException e) {
							e.printStackTrace();System.exit(1);
						}
						for (Client c: players) {
							try {
								action(c);
							} catch (RemoteException e) {
								e.printStackTrace();System.exit(1);
							}
						}
						for (Client c: spectators) {
							try {
								action(c);
							} catch (RemoteException e) {
								e.printStackTrace();System.exit(1);
							}
						}
						try {
							onEnd();
						} catch (RemoteException e) {
							e.printStackTrace();System.exit(1);
						}
					} finally {
						ServerImpl.lock.unlock();
					}
				}
			}).start();
		} else {
			ServerImpl.lock.lock();
			try {
				onStart();
				for (Client c: players) {
					action(c);
				}
				for (Client c: spectators) {
					action(c);
				}
				onEnd();
			} finally {
				ServerImpl.lock.unlock();
			}
		}
	}
	protected abstract void action(Client c) throws RemoteException;
	protected void onStart() throws RemoteException {
	}
	protected void onEnd() throws RemoteException {
	}
	private final List<Client> players;
	private final List<Client> spectators;
}

public class ServerImpl implements Server {
	public ServerImpl(Game currentGame) {
		this.currentGame = currentGame;
	}
	@Override
	public GameStatus login(String host, int port) throws RemoteException {
		final Client client;
		try {
			client = (Client)LocateRegistry.getRegistry(host, port).lookup("Client");
		} catch (java.rmi.NotBoundException e) {
			return null;
		}
		List<Client> spectators = currentGame.getSpectators();
		spectators.add(client);
		currentGame.setSpectators(spectators);
		System.out.println("[server] " + client.getName() + " logged in.");
		final String clientName = client.getName();
		(new ClientsIterator(currentGame.getPlayers(), currentGame.getSpectators()) {
			@Override
			protected void action(Client c) throws RemoteException {
				if (!c.getName().equals(clientName))
					c.clientLoggedIn(client);
			}
		}).iterate(false);
		GameStatus res = new GameStatus();
		res.players = currentGame.getPlayers();
		res.spectators = currentGame.getSpectators();
		res.minNbPlayers = currentGame.getMinNbPlayers();
		res.thinkTime = currentGame.getThinkTime();
		res.buyIn = currentGame.getBuyIn();
		res.blindUpTime = currentGame.getBlindUpTime();
		res.blindUpInc = currentGame.getBlindUpInc();
		if (currentGame.getCurrentRound() != null) {
			Round round = currentGame.getCurrentRound();
			res.flop = round.getFlop();
			res.turn = round.getTurn();
			res.river = round.getRiver();
			res.playersCards = new LinkedList<Pair<Card, Card>>();
			for (Hand h: round.getPlayersCards()) {
				res.playersCards.add(new Pair<Card, Card>(h.getCard()[0], h.getCard()[1]));
			}
			res.currentPlayer = round.getCurrentPlayer();
			res.dealer = round.getDealer();
			res.state = round.getState();
			res.pots = round.getPots();
		}
		return res;
	}
	@Override
	public void logout(Client client) throws RemoteException {
		// FIXME: List.contains/remove doesn't seem to work; Remote.equals issue?
		lock.lock();
		try {
			List<Client> players = currentGame.getPlayers();
			System.out.println("[server] " + client.getName() + " logged out.");
			boolean isplayer = false;
			for (Client c : players) {
				if (c.getName().equals(client.getName())) {
					isplayer = true;
					break;
				}
			}
			if(isplayer) {
				if (currentGame.getCurrentRound() != null) {
					players.set(players.indexOf(client), // FIXME
							new FakeClient(client.getName(), client.getMoney(), client.getCurrentBet(), currentGame));
					return;
				} else {
					remove(players, client);
				}
				currentGame.setPlayers(players);
			} else {
				List<Client> spectators = currentGame.getSpectators();
				remove(spectators, client);
				currentGame.setSpectators(spectators);
			}
			final String name = client.getName();
			final Game game = currentGame;
			(new ClientsIterator(currentGame.getPlayers(), currentGame.getSpectators()) {
				@Override
				protected void action(Client c) throws RemoteException {
					c.clientLoggedOut(name);
				}
			}).iterate(false);
		} finally {
			lock.unlock();
		}
	}
	@Override
	public boolean becomePlayer(Client client) throws RemoteException {
		if (currentGame.getCurrentRound() == null && currentGame.getPlayers().size() < Game.MAX_NB_PLAYERS) {
			List<Client> list;
			lock.lock();
			try {
				list = currentGame.getSpectators();
				remove(list, client); // FIXME: list.remove(client);
				currentGame.setSpectators(list);
				list = currentGame.getPlayers();
				list.add(client);
				currentGame.setPlayers(list);
			} finally {
				lock.unlock();
			}
			final Client cl = client;
			final String clname = cl.getName();
			final boolean start = !gameBegan && list.size () >= currentGame.getMinNbPlayers();
			final Game game = currentGame;
			(new ClientsIterator(currentGame.getPlayers(), currentGame.getSpectators()) {
				@Override
				protected void action(Client c) throws RemoteException {
					if (!c.getName().equals(clname))
						c.clientBecamePlayer(cl);
				}
				@Override
				protected void onEnd() throws RemoteException {
					if (start) {
						gameBegan = true;
						beginRound();
					}
				}
			}).iterate(false);
			return true;
		}
		return false;
	}
	@Override
	public boolean becomeSpectator(Client client) throws RemoteException {
		if(currentGame.getCurrentRound() == null && currentGame.getPlayers().size() > 2) {
			lock.lock();
			try {
				List<Client> list = currentGame.getPlayers();
				remove(list, client); // FIXME: list.remove(client);
				currentGame.setPlayers(list);
				list = currentGame.getSpectators();
				list.add(client);
				currentGame.setSpectators(list);
			} finally {
				lock.unlock();
			}
			final Client cl = client;
			final String clname = cl.getName();
			final Game game = currentGame;
			(new ClientsIterator(currentGame.getPlayers(), currentGame.getSpectators()) {
				@Override
				protected void action(Client c) throws RemoteException {
					if (!c.getName().equals(clname))
						c.clientBecameSpectator(cl);
				}
			}).iterate(false);
			return true;
		}
		return false;
	}
	@Override
	public void postChatMessage(Client client, String text) throws RemoteException {
		// TODO
	}
	@Override
	public void act(Action action) throws RemoteException {
		final Action act = action;
		final Round round = currentGame.getCurrentRound();
		List<Client> players = currentGame.getPlayers();
		boolean turnended = false;
		int curbet = -1;
		int nbplayers = 0;
		int index = 0;
		round.addToPot(action.getBet());
		String curplayername = round.getCurrentPlayer().getName();
		for (Client c: players) {
			if (c.getName().equals(curplayername)) {
				break ;
			}
			index += 1;
		}
		final Client next = players.get((index + 1) % players.size());
		if (round.getCurrentPlayer().getName().equals(round.getDealer().getName())) {
			turnended = true;
			for (Client c: players) {
				int bet = c.getCurrentBet();
				if (bet != -1) {
					nbplayers += 1;
					if (curbet == -1) {
						curbet = bet;
					} else {
						if (curbet != bet) {
							turnended = false;
							break;
						}
					}
				}
			}
			turnended = turnended || nbplayers == 1;
		}
		final boolean turnendedf = turnended;
		final int nbplayersf = nbplayers;
		(new ClientsIterator(currentGame.getPlayers(), currentGame.getSpectators()) {
			@Override
			protected void action(Client c) throws RemoteException {
				c.playerActed(act);
			}
			@Override
			protected void onEnd() throws RemoteException {
				if (turnendedf) {
					if (round.getState() == RoundState.RIVER || nbplayersf <= 1) {
						System.out.println("[server] the hand is finished. The pot is " + currentGame.getCurrentRound().getPots().get(0) + ".");
						currentGame.setCurrentRound(null);
						(new ClientsIterator(currentGame.getPlayers(), currentGame.getSpectators()) {
							@Override
							protected void action(Client c) throws RemoteException {
								c.handEnded(null); // FIXME: list of the winners
							}
						}).iterate();
						return;
					} else {
						round.setState(RoundState.values()[round.getState().ordinal() + 1]);
						System.out.println("[server] round state is " + round.getState());
					}
				}
				round.setCurrentPlayer(next);
				System.out.println("[server] it's " + round.getCurrentPlayer().getName() + "'s turn.");
				round.getCurrentPlayer().play();
			}
		}).iterate();
	}
	private void beginRound() {
		(new Thread() {
			@Override
			public void run() {
				List<Card> cards = new LinkedList<Card>();
				for(Value v: Value.values()) {
					for(Suit s: Suit.values()) {
						cards.add(new Card(v, s));
					}
				}
				Random rand = new Random();
				List<Hand> hands = new LinkedList<Hand>();
				List<Pair<Card, Card>> pcards = new LinkedList<Pair<Card, Card>>();
				for (int i = 0, end = currentGame.getPlayers().size() ; i < end ; ++i) {
					Card c1 = cards.remove(rand.nextInt(cards.size())) ;
					Card c2 = cards.remove(rand.nextInt(cards.size())) ;
					hands.add(new Hand(c1, c2));
					pcards.add(new Pair<Card, Card>(c1, c2));
				}
				currentGame.setCurrentRound(new Round(
						// Flop: 3 cards
						cards.remove(rand.nextInt(cards.size())),
						cards.remove(rand.nextInt(cards.size())),
						cards.remove(rand.nextInt(cards.size())),
						// Turn
						cards.remove(rand.nextInt(cards.size())),
						// River
						cards.remove(rand.nextInt(cards.size())),
						hands));
				try {
					Client first = currentGame.getPlayers().get(1);
					currentGame.getCurrentRound().setDealer(currentGame.getPlayers().get(0));
					System.out.println("[server] the dealer is " + currentGame.getCurrentRound().getDealer().getName());
					currentGame.getCurrentRound().setCurrentPlayer(first);
					List<Client> list = currentGame.getPlayers();
					Card[] flop = currentGame.getCurrentRound().getFlop();
					Card turn = currentGame.getCurrentRound().getTurn();
					Card river = currentGame.getCurrentRound().getRiver();
					for (Client c: list) {
						c.handBegan(flop, turn, river, pcards);
					}
					list = currentGame.getSpectators();
					for (Client c: list) {
						c.handBegan(flop, turn, river, pcards);
					}
					System.out.println("[server] it's " + currentGame.getCurrentRound().getCurrentPlayer().getName() + "'s turn.");
					first.play();
				} catch (RemoteException e) {
					e.printStackTrace();System.exit(1);
				}
			}
		}).start();
	}
	public void disconnect(Client self) throws RemoteException {
		int index;
		List<Client> players = currentGame.getPlayers();
		if (players.size() == 0) {
			System.out.println("There's nobody else.");
			return;
		}
		final Client next = players.get(0);
		System.out.println("[server] disconnecting, asking to " + next.getName() + " to become server");
		new Thread() {
			@Override
			public void run() {
				try {
					next.becomeServer();
				} catch (RemoteException e) {
					e.printStackTrace();System.exit(1);
				}
			}
		}.start();
	}
	private void remove(List<Client> l, Client c) throws RemoteException {
		String name = c.getName();
		for (Iterator<Client> it = l.iterator(); it.hasNext();) {
			if (it.next().getName().equals(name)) {
				it.remove();
				break;
			}
		}
	}
	private Game currentGame;
	private boolean gameBegan = false;
	private static final long serialVersionUID = 1L;
	protected static ReentrantLock lock = new ReentrantLock();
}
