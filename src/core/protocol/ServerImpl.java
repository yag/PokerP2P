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
import java.util.Collections ;


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
	protected final List<Client> players;
	protected final List<Client> spectators;
}

public class ServerImpl implements Server {
	public ServerImpl(Game currentGame) {
		this.currentGame = currentGame;
		currentGame.setServer(this);
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
			final String name = client.getName();
			System.out.println("[server] " + name + " logged out.");
			Client nclient = null;
			if (currentGame.getCurrentRound() != null) {
				nclient = new FakeClient(name, client.getMoney(), client.getCurrentBet(), currentGame);
				// Just have to replace the client with a fake
				for (int i = 0 ; i < players.size() ; ++i) {
					if (players.get(i).getName().equals(name)) {
						players.set(i, nclient);
					}
				}
				currentGame.getCurrentRound().updatePlayer(name, nclient);
				if (currentGame.getCurrentRound() != null && currentGame.getCurrentRound().getCurrentPlayer() != null && currentGame.getCurrentRound().getCurrentPlayer().getName().equals(name))
					act(new Action(nclient, ActionType.FOLD, 0));
			} else {
				currentGame.removeClient(name);
			}
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
                        }).iterate();
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
		Client nextPlayertemp = null ;
		try {
			nextPlayertemp = currentGame.playerActed(action) ;
		} catch (core.controller.CheatException ce) {
			// Bannir
			final Action act = action;
			(new ClientsIterator(currentGame.getPlayers(), currentGame.getSpectators()) {
					@Override
					protected void action(Client c) throws RemoteException {
						c.clientBanned(act.getPlayer().getName()) ;	
					}					
			}).iterate(false);
			action =  new Action(action.getPlayer(), ActionType.FOLD, 0);
			try {
				nextPlayertemp = currentGame.playerActed(action);
			} catch (core.controller.CheatException ce2) {
				// Will never happen
			}
		}
		final Action act = action;
		
		final Client nextPlayer = nextPlayertemp ;
		final Round nextRound = currentGame.getCurrentRound() ;
		// Pour tous les joueurs/spectateurs de la table
		(new ClientsIterator(currentGame.getPlayers(), currentGame.getSpectators()) {
			// On met à jour le statut de Game
			@Override
			protected void action(Client c) throws RemoteException {
				c.playerActed(act);
			}
			@Override
			protected void onEnd() throws RemoteException {
				if (nextPlayer == null) { // On fini le round courant si necessaire
						//currentGame.getCurrentRound().closePot() ;
						System.out.println("[server] the hand is finished") ;
						// On donne l'argent aux gagnants
						for (Pair<List<Client>,Integer> p : currentGame.getCurrentRound().getWinners()) {
							for (Client cl : p.getFirst()) {
                                                                System.out.println("[sever]" + cl.getName()+" est un gagnant"+p.getSecond());
								cl.setMoney( cl.getMoney() + (p.getSecond()/p.getFirst().size())) ;
							}
						}
						(new ClientsIterator(currentGame.getPlayers(), currentGame.getSpectators()) {
							@Override
							protected void action(Client c) throws RemoteException {
								c.handEnded(currentGame.getCurrentRound().getWinners());
							}
						}).iterate(false);
                                                currentGame.setCurrentRound(null);
						// Began a new hand
                                                // On vire les joueurs qui n'ont plus d'argent et les fakes
                                                final List<Client> tologout = new LinkedList<Client>();
                                                (new ClientsIterator(currentGame.getPlayers(), currentGame.getSpectators()) {
                                                        @Override
                                                        protected void action(Client c) throws RemoteException {
                                                                if (c.getMoney() == 0 ) {
                                                                        System.out.println("[server] " + c + "has no money, logout !") ;
                                                                        tologout.add(c);
                                                                } else if (c instanceof FakeClient) {
                                                                        tologout.add(c);
                                                                }
                                                        }
                                                }).iterate(false);
                                                for (Client c: tologout) {
                                                        logout(c);
                                                }
						if (currentGame.getPlayers().size() > 1) {
							// On fait une rotation pour tourner le dealer
							Collections.rotate(currentGame.getPlayers(),1) ;
							beginRound() ;
						} else {
							// La partie est fini
							(new ClientsIterator(currentGame.getPlayers(), currentGame.getSpectators()) {
								@Override
								protected void action(Client c) throws RemoteException {
										System.out.println("Ok, la partie est fini, tout le monde est ruiné sauf le grand gagnant : " + currentGame.getPlayers().get(0).getName());
										logout(c) ;
								}
							}).iterate(false);
						}

					} else {
						System.out.println("[server] round state is " + nextRound.getState());
						System.out.println("[server] it's " + nextRound.getCurrentPlayer().getName() + "'s turn.");
						nextRound.getCurrentPlayer().play();
					}

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
				currentGame.handBegan() ;
				try {
					currentGame.getCurrentRound().setDealer(currentGame.getPlayers().get(0));
					System.out.println("[server] the dealer is " + currentGame.getCurrentRound().getDealer().getName());
					
					Client first = currentGame.getPlayers().get(3%currentGame.getPlayers().size());
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
		final Client next = players.get(players.size()-1);
		System.out.println("[server] disconnecting, asking to " + next.getName() + " to become server");
                final Client s = self;
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
