package core.model;

import core.protocol.Card;
import core.Pair ;
import core.protocol.RoundState;
import core.protocol.Client;
import core.protocol.Value;
import java.util.List;
import java.util.LinkedList;
import java.rmi.RemoteException;

public class Round implements java.io.Serializable {
	public Round(Card f1, Card f2, Card f3, Card t, Card r, List<Hand> p) {
		flop[0] = f1;
		flop[1] = f2;
		flop[2] = f3;
		turn = t;
		river = r;
		playersCards = p;
		actualPlayers = null ; 
		pots.add(new Pair<List<Client>,Integer>(null,0)) ;

	}
	public Card[] getFlop() {
		return flop;
	}
	public Card getTurn() {
		return turn;
	}
	public Card getRiver() {
		return river;
	}
	public List<Hand> getPlayersCards() {
		return playersCards;
	}
	public List<Pair<Client,Integer>> getActualPlayers() {
		return actualPlayers ;
	}
	public void setActualPlayers(List<Pair<Client,Integer>> actualPlayers) {
		this.actualPlayers = actualPlayers ;
	}
	public Client getCurrentPlayer() {
		return currentPlayer;
	}
	public void setCurrentPlayer(Client currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	public Client getDealer() {
		return dealer;
	}
	public void setDealer(Client dealer) {
		this.dealer = dealer;
	}
	public RoundState getState() {
		return state;
	}
	public void setState(RoundState state) {
		this.state = state;
	}
	public List<Pair<List<Client>,Integer>> getPots() {
		return pots;
	}
	public void setPots(List<Pair<List<Client>,Integer>> pots) {
		this.pots = pots;
	}
	public void addToPot(int bet) {
		int n = pots.size() - 1 ;
		pots.get(n).setSecond( pots.get(n).getSecond() + bet) ;
	}

	public void closePot() {
		int n = pots.size() - 1 ;
		if ( pots.get(n).getSecond() == 0 && n >= 1) {
			pots.remove(n) ;
			return ;
		}
		List<Client> lc = new LinkedList<Client>() ;
		for (Pair<Client,Integer> p : actualPlayers) {
					lc.add(p.getFirst()) ;
		}
		pots.get(n).setFirst(lc) ;
	}
	
	public void addPot() {
		pots.add(new Pair<List<Client>,Integer>(null,0)) ;
	}

	public List<Pair<List<Client>,Integer>> getWinners() {
		List<Pair<List<Client>,Integer>> win  = new LinkedList<Pair<List<Client>,Integer>>() ;
		/* Takes no arguments : he already has the pots ! 
		Return for each pot, the list of the winners (in case of equality) and the amount of money */
		if (actualPlayers.size() < 2) {
			Client c  = actualPlayers.get(0).getFirst() ;
			List<Client> l = new LinkedList<Client>() ;
			l.add(c) ;
			Pair<List<Client>,Integer> p = new Pair<List<Client>,Integer>(
				l,
				pots.get(0).getSecond()
				);
			win.add(p) ;
		} else {
			for ( Pair<List<Client>,Integer> p : pots ) {                       
				List<Client> winners = getPotWinners(p.getFirst()) ;
				win.add(new Pair<List<Client>,Integer>(winners,p.getSecond())) ;
			}
		}
		return win ;
	}


	public  List<Client> getPotWinners (List<Client> clients)   {

		List<Client> winners = new LinkedList<Client>() ;
		
		winners.add (clients.get(0) ) ;

		for (int i = 1 ; i < clients.size() ; i++) {
			Client new_winner = getBestClient( winners.get(0) , clients.get(i) );
			if (new_winner	== null ) {
				winners.add( clients.get(i)) ;
			} else if (new_winner != winners.get(0)) {
				// The new is better
				winners.clear() ;
				winners.add( clients.get(i)) ;
			}
		}
		
		return winners;
	}

	public Client getBestClient(Client c1, Client c2) {
		// Find P1 and P2
		int i1 = 0 , i2 = 0 ;
		int index = 0 ;
		try {
		for (Pair<Client,Integer> p: actualPlayers) {
				if (p.getFirst().getName().equals(c1.getName()) ) {
					i1 = index ; 
				} else if (p.getFirst().getName().equals(c2.getName()) ) {
					i2 = index ;
				}
				index += 1;
			}


		Card[] card1 = getCompleteHand( playersCards.get(i1) ) ;
		Card[] card2 = getCompleteHand( playersCards.get(i2) ) ;

		Ranking r1 = getRank(card1) ;
		Ranking r2 = getRank(card2) ;

		if (r1.ordinal() > r2.ordinal()) {
			System.out.println("La gagnant est" + c1.getName() ) ;
			return c1 ;
		} else if (r1.ordinal() < r2.ordinal()) {
			System.out.println("La gagnant est" + c2.getName() ) ;

			return c2 ;
		} else {
			//Equal , we have to check each case
			int b1 = getBestCard(card1).getValue().ordinal() ;
			int b2 = getBestCard(card2).getValue().ordinal() ;

			switch (r1) {

				case ROYAL_FLUSH :
				return null ;
				
				case STRAIGHT_FLUSH :
				case STRAIGHT :
				case FOUR_OF_A_KIND :
				case FULL_HOUSE :
				case FLUSH :
				case THREE_OF_A_KIND:
				case DOUBLE_PAIR:
				case PAIR:
				case HIGH_HAND :
				return (b1 > b2 ? c1 : (b2 > b1 ? c2 : null ) ) ;
				
				default:
				return null ;
			}
		}

		} catch (RemoteException re) {
		}
		return null ;

	}

	public static Card getBestCard(Card[] cards) {
		Card best = cards[0] ; 
		for (Card c: cards) {
			if (best.getValue().ordinal() < c.getValue().ordinal()) {
				best = c ;
			}

		}
		return best ;
	}

	public static int isStraight(Card[] cards) {
		Card best = getBestCard(cards) ;
		boolean straight = false ;
		boolean prec1 = false ;
		boolean prec2 = false ;
		boolean prec3 = false ;
		boolean prec4 = false ;
		int ord = best.getValue().ordinal() ;

		for (Card c: cards) {
			if (c.getValue().ordinal() == (ord-1) ) {				
				prec1 = true ;
			} else if (c.getValue().ordinal() == (ord-2) ) {
				prec2 = true ;
			} else if (c.getValue().ordinal() == (ord-3) ) {
				prec3 = true ;
			} else if (c.getValue().ordinal() == (ord-4) ) {
				prec4 = true ;	
			}
		}

		straight = prec1 && prec2 && prec3 && prec4 ;

		if (straight && best.getValue() == Value.ACE) {
			return 2 ;
		} else if (straight) {
			return 1 ;
		} else if (cards.length <= 5) {
			return 0 ;
		} else {
			// Remove best card, and recursif call
			return isStraight(removeBestCard(cards)) ;
		}

	}

	public static Card[] removeBestCard(Card[] cards) {
		int lg=0 ;
		for (Card c : cards) {
			if (c != getBestCard(cards)) {
				lg++ ;
			}
		}
		Card[] ncards = new Card[lg] ;
		int i = 0 ;
		for (Card c : cards) {
			if (c != getBestCard(cards)) {
				ncards[i] = c ;
				i++ ;
			}
		}
		return ncards ;
	}

	public Card[] getCompleteHand(Hand h) {
		Card[] all  = new Card[7] ;
		all[0] = h.getCard()[0] ;
		all[1] = h.getCard()[1] ; 
		all[2] = flop[0] ; 
		all[3] = flop[1] ; 
		all[4] = flop[2] ; 
		all[5] = turn ; 
		all[6] = river ; 
		return all ;
	}

	public static Ranking getRank(Card[] all) {

		int[] vals = new int[13] ;
		int[] suits = new int[4] ;

		for (Card c: all) {
			vals[c.getValue().ordinal()]++ ;
			suits[c.getSuit().ordinal()]++ ;
		}

		boolean flush = !(suits[0] < 5 && suits[1] < 5 && suits[2] < 5 && suits[3] < 5) ;
		boolean four_of_a_kind = false ;
		boolean three_of_a_kind = false ;
		boolean double_pair = false ;
		boolean full_house = false ;
		boolean pair = false ;

		for (int i : vals) {
			if (i >= 4) {
				four_of_a_kind = true ;
			} else if (i >= 3) {
				if (pair) {
					full_house = true ;
				} else {
					three_of_a_kind = true ;
				}
			} else if (i>= 2) {
				if (three_of_a_kind) {
					full_house = true ;
				} else if (pair) {
					double_pair = true ;
				} else {
					pair = true ;
				}
			}
		}

		if (flush) {
			// What suit do we have more ?
			int suit = 0 ;
			int max = suits[suit];   // start with the first value
			for (int i=1; i<suits.length; i++) {
				if (suits[i] > max) {
					max = suits[i];   // new maximum
					suit = i ; // the suit
				}
			}
			// Seek for a straight with the same suit
			Card[] f = new Card[max] ;
			int i = 0 ;
			for (Card c: all) {				
				if (c.getSuit().ordinal() == suit) { // correct suit
					f[i] = c ;
					i++ ;
				}
			}

			int straight = isStraight(f) ;
			if (straight == 2) {
				return Ranking.ROYAL_FLUSH ;
			} else if (straight == 1) {
				return Ranking.STRAIGHT_FLUSH ;
			}
		}

		if (four_of_a_kind) {
			return Ranking.FOUR_OF_A_KIND ;
		} else if (full_house) {
			return Ranking.FULL_HOUSE ;
		} else if (flush) {
			return Ranking.FLUSH ;
		} else if (isStraight(all) > 0) {
			return Ranking.STRAIGHT ;
		} else if (three_of_a_kind){
			return Ranking.THREE_OF_A_KIND ;
		} else if (double_pair) {
			return Ranking.DOUBLE_PAIR ;
		} else if (pair) {
			return Ranking.PAIR ;
		} else {
			return Ranking.HIGH_HAND ;
		}
	}

	public void updatePlayer(String name, Client client) throws RemoteException {
		if (currentPlayer != null && currentPlayer.getName().equals(name)) {
			currentPlayer = client;
		}
		for (Pair<Client,Integer> p: actualPlayers) {
			if (p.getFirst().getName().equals(name)) {
				p.setFirst(client);
				break;
			}
		}
		for (Pair<List<Client>,Integer> p1: pots) {
			List<Client> lc = p1.getFirst();
			if (lc != null) {
				for (int i = 0 ; i < lc.size() ; ++i) {
					if (lc.get(i).getName().equals(name)) {
						lc.set(i, client);
						break;
					}
				}
			}
		}
	}

	private Card[] flop = new Card[3];
	private Card turn;
	private Card river;
	private List<Hand> playersCards;
	private Client currentPlayer;
	public List<Pair<Client,Integer>> actualPlayers;
	private Client dealer;
	private RoundState state = RoundState.PREFLOP;
	// a list of the differents pots with for each, players in it and money
	private List<Pair<List<Client>,Integer>> pots = new LinkedList<Pair<List<Client>,Integer>>();
	private static final long serialVersionUID = 1L;
}
