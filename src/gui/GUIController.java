package gui;

import core.controller.ProtocolController;
import core.protocol.Client;
import core.protocol.ChatMessage;
import core.protocol.Action;
import core.protocol.ActionType;
import core.Pair;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;


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
			System.out.print(action.getPlayer().getName() + " played: ") ; 
			switch (action.getType()) {
				case CHECK : System.out.println("CHECK") ;
				break ;
				case RAISE : System.out.println("RAISE de " + action.getBet()) ;
				break ;
				case CALL : System.out.println("CALL") ;
				break ;
				case FOLD : System.out.println("FOLD") ;
				break ;
				case SITOUT : System.out.println("SITOUT") ;
				break ;
			}
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
		System.out.println("The winner(s) is(are) :") ;
	}
	
	public void play() {
		final ProtocolController ctrl = controller;
		new Thread() {
			@Override
			public void run() {
				Scanner scan = new Scanner(System.in);
				int choice = 0 ;
				int currentbet = 0 ; 
				int currentdiff = 0 ;
				while (choice < 1 || choice > 6) {
					try {
						System.out.println("Stack : " + controller.getClient().getGame().getCurrentRound().getPots().get(0)) ;
						System.out.println("Money : " + controller.getClient().getMoney()) ;
						for (Pair<Client,Integer> p : controller.getClient().getGame().actualPlayers) {
							if (p.getFirst().getName().equals(controller.getClient().getName())) {
								// Me :)
								currentbet = p.getSecond() ;
								break ;
							}
						}
						currentdiff = controller.getClient().getGame().getCurrentMaxBet() - currentbet ;
					} catch (RemoteException ce) {
						//
					}
					System.out.print("Current bet :" + currentbet ) ;
					System.out.println("You're turn ! \n : 1 = Raise ;\n 2 = Call ( it's going to be " + currentdiff + ");\n 3 = Check ;\n 4 = Fold ; \n5 = Sitout ;\n 6 = Logout");
					choice = scan.nextInt();
				}
				switch (choice) {
					case 1 :
						System.out.println("Combien tu veux RAISE ? :");
						int bet = scan.nextInt() ;
						ctrl.act(new Action(ActionType.RAISE,bet));
						break ;
					case 2 :
							ctrl.act(new Action(ActionType.CALL));
							break ;
					case 3 :
						ctrl.act(new Action(ActionType.CHECK));
						break ;
					case 4 :
						ctrl.act(new Action(ActionType.FOLD));
						break ;
					case 5 :
						ctrl.act(new Action(ActionType.SITOUT));
						break ;
					case 6 :
						//ctrl.act(new Action(ActionType.SITOUT));
						break ;
					default : // never happens
					
						
				}
			}
		}.start();
	}
	private ProtocolController controller;
	private static final long serialVersionUID = 1L;
}
