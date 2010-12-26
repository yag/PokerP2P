package gui;

import core.controller.ProtocolController;
import core.model.* ;
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
			System.out.print(action.getPlayer().getName() + " ") ; 
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
		} catch (RemoteException e) {
			e.printStackTrace();System.exit(1);
		}
	}
	public void handBegan() {
		System.out.println("A new hand begins.");
	}
	public void handEnded(List<Pair<List<Client>, Integer>> winners) throws RemoteException{
		System.out.println("The hand ended.");
		int i = 0 ;
		for (Pair<List<Client>,Integer> p : winners) {
			System.out.println("The pot n°" + (i+1) + " is winned by :") ;
			int j = 0 ;
			for (Client c : p.getFirst()) {
				System.out.println(p.getFirst().get(j).getName() + " : " + ( p.getSecond() / p.getFirst().size() ) + " $$$$") ;
				j++ ;
			}
			i++ ;
		}
	}

	public void play() {
		final ProtocolController ctrl = controller;
		new Thread() {
			@Override
			public void run(){
				try {
				Scanner scan = new Scanner(System.in);
				int choice = 0 ;
				int currentbet = 0 ; 
				int currentdiff = 0 ;
				int max = -1 ;
				int index = 0 ;
				int indexAll = 0 ;
				int money = controller.getClient().getMoney() ;
				
				while (choice < 1 || choice > 5) {
						//final String ESC = "\033[";
						//System.out.print(ESC + "2J"); System.out.flush();
						Game g = controller.getClient().getGame() ;
						Round r = g.getCurrentRound() ;
						String name = controller.getClient().getName() ;
						for (Pair<Client,Integer> p : r.getActualPlayers()) {
							if (p.getFirst().getName().equals(name)) {
								currentbet = p.getSecond() ;
								break ;
							} else {
								index += 1 ;
							}
						}
						for (Client c : g.getPlayers()) {
							if (c.getName().equals(name)) {
								break ;
							}
							indexAll += 1 ;
						}
						
						max = g.getCurrentMaxBet() ;
						currentdiff = max - currentbet ;
						Hand h = r.getPlayersCards().get(indexAll) ;
						
						System.out.println("------------ You're turn --------------") ;
						Card[] c = h.getCard() ;
						System.out.println("Card 1 : " + c[0].getSuit() + " " + c[0].getValue() );
						System.out.println("Card 2 : " + c[1].getSuit() + " " + c[1].getValue() );
						int allow = r.getState().ordinal() ; // it's the current state (preflop, turn ..)
						if (allow > 0) {
							// On affiche les cartes du FLOPs
							Card[] flop = r.getFlop() ;
							System.out.println("Flop 1 : " + flop[0].getSuit() + " " + flop[0].getValue() );
							System.out.println("Flop 2 : " + flop[1].getSuit() + " " + flop[1].getValue() );
							System.out.println("Flop 3 : " + flop[2].getSuit() + " " + flop[2].getValue() );
						}
						if (allow > 1) {
							Card turn = r.getTurn() ;
							System.out.println("Turn: " + turn.getSuit() + " " + turn.getValue() );
						}
						if (allow > 2) {
							Card river = r.getRiver() ;
							System.out.println("River: " + river.getSuit() + " " + river.getValue() );
							System.out.println("Ranking : " + r.getHandRank(h)) ;
						}
						System.out.println("Stack : " + r.getPots().get(0).getSecond()) ;
						System.out.println("Money : " + money) ;

					if (currentbet != -1) {
						System.out.println("Current bet :" + currentbet ) ;
					}
					
					if (money > 0) {
						System.out.println("1 = Raise") ;
						if (max > 0) {
							if (max >= money) {
								System.out.println("2 = Call to " + max + ", so you're ALLIN !") ;
							} else {
								System.out.println("2 = Call to" + max + " || the diff is " + currentdiff);
							}
						} else {
							System.out.println("2 = Check");
						}
						System.out.println("3 = Fold") ;
						//System.out.println("4 = Sitout") ;
						System.out.println("5 = Logout") ;
						System.out.println("--------------------------------------") ;
						choice = scan.nextInt();
					} else {
						// we're allin
						System.out.println("You're All In") ;
					}
				}
				
				switch (choice) {
					case 1 :
					System.out.println("Combien tu veux RAISE ? :");
					int bet = scan.nextInt() ;
					if (bet > money) {
						System.out.println("On te met ALLIN avec une mise de " + money) ;
						ctrl.act(new Action(ActionType.ALLIN,money));
					} else {
						ctrl.act(new Action(ActionType.RAISE,bet));
					}
					break ;
					case 2 :
						if (max > 0) {
							if (max >= money) {
								ctrl.act(new Action(ActionType.ALLIN,money));
							} else {
								ctrl.act(new Action(ActionType.CALL,currentdiff));
							}
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
			
			} catch (RemoteException ce) {
				//
			}
			}
			
			}.start();
			
			
		}
		private ProtocolController controller;
		private static final long serialVersionUID = 1L;
	}
