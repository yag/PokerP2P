package gui;

import static core.notifications.NotificationCenter.defaultCenter;
import static core.notifications.ClientNotification.*;
import core.controller.ProtocolController;
import core.model.* ;
import core.notifications.UserInfo;
import core.protocol.Client;
import core.protocol.ChatMessage;
import core.protocol.Action;
import core.protocol.Card;
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
			
			//Notifications
			defaultCenter().postNotification(clientLoggedIn, this,
					new UserInfo().add(kPlayerName, client.getName()));
			
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		}
	}
	
	public void clientLoggedOut(String name) {
		System.out.println(name + " logged out.");
		
		//Notifications
		defaultCenter().postNotification(clientLoggedOut, this,
				new UserInfo().add(kPlayerName, name));
	}
	
	public void clientBanned(String name) {
		try {
			if (name.equals(controller.getClient().getName())) {
				System.out.println("Oops... you've been banned.");
				
				//Notifications
				defaultCenter().postNotification(clientBanned, this,
						new UserInfo().add(kPlayerName, name));
				
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
			
			//Notifications
			defaultCenter().postNotification(clientBecamePlayer, this,
					new UserInfo().add(kPlayerName, client.getName()));
			
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		}
	}
	
	public void clientBecameSpectator(Client client) {
		try {
			System.out.println(client.getName() + " became a spectator.");
			
			//Notifications
			defaultCenter().postNotification(clientBecameSpectator, this,
					new UserInfo().add(kPlayerName, client.getName()));
			
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		}
	}
	
	public void playerActed(Action action) {
		try {
			System.out.print(action.getPlayer().getName() + " ") ;
			
			//Notification
			defaultCenter().postNotification(playerActed, this,
					new UserInfo().add(kPlayerName, action.getPlayer().getName()));
			
			switch (action.getType()) {
				case CHECK : System.out.println("checked") ;
				break ;
				case RAISE : System.out.println("raised to " + action.getBet()) ;
				break ;
				case CALL : System.out.println("called") ;
				break ;
				case FOLD : System.out.println("folded") ;
				break ;
				case SITOUT : System.out.println("is sitout") ;
				break ;
			}
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		}
	}
	
	public void addChatMessage(ChatMessage msg) {
		try {
			System.out.println(msg.getAuthor().getName() + " posted " + msg.getText());
			
			//Notification
			defaultCenter().postNotification(newChatMessage, this,
					new UserInfo().add(kChatAuthor, msg.getAuthor().getName())
					              .add(kChatMsg, msg.getText()));
			
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		}
	}
	
	public void handBegan() {
		System.out.println("A new hand begins.");
		
		//Notification
		defaultCenter().postNotification(handBegan, this);
	}
	
	public void handEnded(List<Pair<Client, Integer>> winners) {
		System.out.println("The hand ended.");
		System.out.println("The winner(s) is(are) :") ;
		
		//Notification
		defaultCenter().postNotification(handEnded, this,
				new UserInfo().add(kWinnersList, winners));
		
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
				int max = -1 ;
				int index = 0 ;
				int indexAll = 0 ;
				while (choice < 1 || choice > 5) {
					try {
						//final String ESC = "\033[";
						//System.out.print(ESC + "2J"); System.out.flush();
						
						for (Pair<Client,Integer> p : controller.getClient().getGame().actualPlayers) {
							if (p.getFirst().getName().equals(controller.getClient().getName())) {
								currentbet = p.getSecond() ;
								break ;
							} else {
								index += 1 ;
							}
						}
						for (Client c : controller.getClient().getGame().getPlayers()) {
							if (c.getName().equals(controller.getClient().getName())) {
								break ;
							}
							indexAll += 1 ;
						}
						currentdiff = controller.getClient().getGame().getCurrentMaxBet() - currentbet ;
						max = controller.getClient().getGame().getCurrentMaxBet() ;
						Hand h = controller.getClient().getGame().getCurrentRound().getPlayersCards().get(indexAll) ;
						System.out.println("------------ You're turn --------------") ;
						Card[] c = h.getCard() ;
						System.out.println("Card 1 : " + c[0].getSuit() + " " + c[0].getValue() );
						System.out.println("Card 2 : " + c[1].getSuit() + " " + c[1].getValue() );
						if (controller.getClient().getGame().getCurrentRound().getState().ordinal() > 0) {
							// On affiche les cartes du FLOPs
							Card[] flop = controller.getClient().getGame().getCurrentRound().getFlop() ;
							System.out.println("Flop 1 : " + flop[0].getSuit() + " " + flop[0].getValue() );
							System.out.println("Flop 2 : " + flop[1].getSuit() + " " + flop[1].getValue() );
							System.out.println("Flop 3 : " + flop[2].getSuit() + " " + flop[2].getValue() );
						}
						if (controller.getClient().getGame().getCurrentRound().getState().ordinal() > 1) {
							Card turn = controller.getClient().getGame().getCurrentRound().getTurn() ;
							System.out.println("Turn: " + turn.getSuit() + " " + turn.getValue() );
						}
						if (controller.getClient().getGame().getCurrentRound().getState().ordinal() > 2) {
							Card river = controller.getClient().getGame().getCurrentRound().getRiver() ;
							System.out.println("River: " + river.getSuit() + " " + river.getValue() );
							System.out.println("Ranking : " + controller.getClient().getGame().getCurrentRound().getHandRank(h)) ;
						}
						System.out.println("Stack : " + controller.getClient().getGame().getCurrentRound().getPots().get(0)) ;
						System.out.println("Money : " + controller.getClient().getMoney()) ;
					} catch (RemoteException ce) {
						//
					}
					if (currentbet != -1) {
						System.out.println("Current bet :" + currentbet ) ;
					}
					System.out.println("1 = Raise") ;
					if (max > 0) {
						System.out.println("2 = Call to" + max + " (Diff: " + currentdiff);
					} else {
						System.out.println("2 = Check");
					}
					System.out.println("3 = Fold") ;
					//System.out.println("4 = Sitout") ;
					System.out.println("5 = Logout") ;
					System.out.println("--------------------------------------") ;
					choice = scan.nextInt();
				}
				switch (choice) {
					case 1 :
					System.out.println("Combien tu veux RAISE ? :");
					int bet = scan.nextInt() ;
					ctrl.act(new Action(ActionType.RAISE,bet));
					break ;
					case 2 :
						if (max > 0) {
							ctrl.act(new Action(ActionType.CALL));
						} else {
							ctrl.act(new Action(ActionType.CHECK));
						}
					break ;
					case 3 :
					ctrl.act(new Action(ActionType.FOLD));
					break ;
					case 4 :
					ctrl.act(new Action(ActionType.SITOUT));
					break ;
					case 5 :
					controller.logout() ;
					break ;
					default : // never happens
				}
			}
			}.start();
		}
		private ProtocolController controller;
		private static final long serialVersionUID = 1L;
	}
