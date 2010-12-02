package gui;

import core.controller.ProtocolController;
import core.protocol.Client;
import core.protocol.ChatMessage;
import core.protocol.Action;
import core.protocol.ActionType;
import core.Pair;
import java.rmi.RemoteException;
import java.util.List;

public class GUIController implements java.io.Serializable {
	public GUIController(ProtocolController controller) {
		this.controller = controller;
	}
	public void clientLoggedIn(Client client) {
		try {
			System.out.println(client.getName() + " logged in.");
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		}
	}
	public void clientLoggedOut(String name) {
		System.out.println(name + " logged out.");
	}
	public void clientBanned(String name) {
		try {
			if (name.equals(controller.getClient().getName())) {
				System.out.println("Oops... you've been banned.");
				controller.realLogout();
			} else {
				System.out.println(name + " has been banned.");
			}
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		}
	}
	public void clientBecamePlayer(Client client) {
		try {
			System.out.println(client.getName() + " became a player.");
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		}
	}
	public void clientBecameSpectator(Client client) {
		try {
			System.out.println(client.getName() + " became a spectator.");
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		}
	}
	public void playerActed(Action action) {
		try {
			System.out.println(action.getPlayer().getName() + " played: " + action.getType());
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		}
	}
	public void addChatMessage(ChatMessage msg) {
		try {
			System.out.println(msg.getAuthor().getName() + " posted " + msg.getText());
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		}
	}
	public void handBegan() {
		System.out.println("A new hand begins.");
	}
	public void handEnded(List<Pair<Client, Integer>> winners) {
		System.out.println("The hand ended.");
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(new java.util.Random().nextInt(500)); // To avoid simultaneous logout
				} catch (InterruptedException e) {
				}
				System.out.println("Asking to log out.");
				controller.logout();
			}
		}.start();
	}
	public void play() {
		final ProtocolController ctrl = controller;
		new Thread() {
			@Override
			public void run() {
				ctrl.act(new Action(ActionType.FOLD, 0));
			}
		}.start();
	}
	private ProtocolController controller;
	private static final long serialVersionUID = 1L;
}
