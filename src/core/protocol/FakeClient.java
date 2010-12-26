package core.protocol;

import core.model.Game;
import core.Pair;
import java.rmi.RemoteException;
import java.util.List;

public class FakeClient implements Client {
	public FakeClient(String name, int money, int currentBet, Game game) {
		this.name = name;
		this.money = money;
		this.currentBet = currentBet;
		this.game = game;
	}
	@Override
	public void clientLoggedIn(Client client) throws RemoteException {
	}
	@Override
	public void clientLoggedOut(String name) throws RemoteException {
	}
	@Override
	public void clientBanned(String name) throws RemoteException {
	}
	@Override
	public void clientBecamePlayer(Client client) throws RemoteException {
	}
	@Override
	public void clientBecameSpectator(Client client) throws RemoteException {
	}
	@Override
	public void addChatMessage(ChatMessage msg) throws RemoteException {
	}
	@Override
	public void playerActed(Action action) throws RemoteException {
	}
	@Override
	public void handBegan(Card[] flop, Card turn, Card river, List<Pair<Card, Card>> playersCards) throws RemoteException {
	}
	@Override
	public void handEnded(List<Pair<List<Client>, Integer>> winners) throws RemoteException {
		final Client self = this;
		(new Thread() {
			public void run() {
				try {
					game.getServer().logout(self);
				}
				catch(RemoteException e) {
					// FIXME
				}
			}
		}).start();
	}
	@Override
	public void becomeServer() throws RemoteException {
		// Nothing to do
	}
	@Override
	public void newServer(Server server) throws RemoteException {
		game.setServer(server);
	}
	@Override
	public void play() throws RemoteException {
		final Client self = this;
		(new Thread() {
			public void run() {
				try {
					game.getServer().act(new Action(self, ActionType.FOLD, 0));
				}
				catch(RemoteException e) {
					// FIXME
				}
			}
		}).start();
	}
	@Override
	public String getName() throws RemoteException {
		return name;
	}
	@Override
	public int getMoney() throws RemoteException {
		return money;
	}
	public void setMoney( int m ) throws RemoteException {
		this.money = m ;
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
	private String name;
	private int money;
	private int currentBet;
	private Game game;
}
