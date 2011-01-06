package core.model;

import core.protocol.*;
import core.Pair ;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.rmi.RemoteException;
import core.controller.CheatException ;
import java.util.Collections ;

public class Game implements java.io.Serializable {
	public Game(int minNbPlayers, int thinkTime, int buyIn, int blindUpTime, float blindUpInc) {
		this.minNbPlayers = minNbPlayers;
		this.thinkTime = thinkTime;
		this.buyIn = buyIn;
		this.blindUpTime = blindUpTime;
		this.blindUpInc = blindUpInc;
	}
	public Round getCurrentRound() {
		return currentRound;
	}
	public void setCurrentRound(Round currentRound) {
		this.currentRound = currentRound;
	}
	public int getMinNbPlayers() {
		return minNbPlayers;
	}
	public int getThinkTime() {
		return thinkTime;
	}
	public int getBuyIn() {
		return buyIn;
	}
	public int getBlindUpTime() {
		return blindUpTime;
	}
	public float getBlindUpInc() {
		return blindUpInc;
	}
	public List<Client> getPlayers() {
		return players;
	}
	public void setPlayers(List<Client> players) {
		this.players = players;
	}
	public void newPlayer(Client client) throws RemoteException {
		remove(spectators, client.getName());
		players.add (client);
	}
	public List<Client> getSpectators() {
		return spectators;
	}
	public void setSpectators(List<Client> spectators) {
		this.spectators = spectators;
	}
	public void newSpectator(Client client) throws RemoteException {
		remove(players, client.getName());
		spectators.add (client);
	}
	public void removeClient(String name) throws RemoteException {
		if (!remove(spectators, name)) {
			remove(players, name);
		}
	}
	public Server getServer() {
		return server;
	}
	public void setServer(Server server) {
		this.server = server;
	}
	private boolean remove(List<Client> l, String name) throws RemoteException {
		for (Iterator<Client> it = l.iterator(); it.hasNext();) {
			if (it.next().getName().equals(name)) {
				it.remove();
				return true;
			}
		}
		return false;
	}
	
	/* This method receive an action done by some player, update the status of the game (pot,round,..)
	Return a boolean :
		- true if the hand is finished
		- false if the hand is not finished
	*/
	public Client playerActed(Action act) throws RemoteException,CheatException {
		
		// D'abord on cherche le nom du joueur actuel
		Client pl = act.getPlayer();
		String curplayername = act.getPlayer().getName();
		// Son index
		// Le montant de son pari avant cet action
		int current = -1 ;
		int index = 0 ;
		int money = 0 ;
		Pair<Client,Integer> player ;
		for (Pair<Client,Integer> p: currentRound.getActualPlayers()) {
			if (p.getFirst().getName().equals(curplayername)) {
				current = p.getSecond() ;
				money = p.getFirst().getMoney() ;
				break ;
			}
			index += 1;
		}
		
		player = currentRound.getActualPlayers().get(index) ;
		if (current < 0 ) { current = 0 ; }
		// On cherche le pari max actuel
		int max = getCurrentMaxBet() ;
		
		// on initilise le joueur suivant
		Client nextPlayer = null ;

		// On prend l'action en compte
		switch (act.getType()) {
			case RAISE :
				if (  ( act.getBet() + current ) > money) { // It's not a raise, it's a call
					throw new CheatException() ;
				}
				//currentRound.addToPot(act.getBet()) ;
				player.setSecond(act.getBet() + current) ; // on ajoute
				nextPlayer = currentRound.getActualPlayers().get((++index)%currentRound.getActualPlayers().size()).getFirst() ;
				break ;
				
			case FOLD :
                                currentRound.foldedPot.add(current);
				currentRound.getActualPlayers().remove(index) ;
				nextPlayer = currentRound.getActualPlayers().get((index)%currentRound.getActualPlayers().size()).getFirst() ;
				break ;
					
			case CHECK :
			 	// pour checker, il faut que tous les joueurs ait checké (ou n'aient pas encore parlé)
				if (max >  0 || player.getSecond() > -1 ) {
					throw new CheatException() ;
                                }
				player.setSecond(0) ;
				nextPlayer = currentRound.getActualPlayers().get((++index)%currentRound.getActualPlayers().size()).getFirst() ;
				break ;
				
			case CALL :
                                //currentRound.addToPot(act.getBet()) ;
				if (  ( act.getBet() + current ) > money) { // It's not a raise, it's a call
					throw new CheatException() ;
				}
                                player.setSecond(current + act.getBet()) ;
                                /*
                                if ((max-current) >= money) {
                                        currentRound.closePot() ;
                                        currentRound.addPot() ;
                                }
                                */
                                nextPlayer = currentRound.getActualPlayers().get((++index)%currentRound.getActualPlayers().size()).getFirst() ;
                                break ;
		}

		/* y'a t'il un joueur suivant qui doit parler ?
		est- ce la fin du round ?
		est-ce la fin du tour ?
		*/
		
		if (currentRound.getActualPlayers().size() <= 1) {
			// Le round est fini car il n'ya plus q'un joueur, il y a un gagnant
                        List<Pair<Client, Integer>> copy = new LinkedList<Pair<Client, Integer>>();
                        for (Pair<Client, Integer> p : currentRound.getActualPlayers())
                                copy.add(new Pair<Client, Integer> (p.getFirst(), p.getSecond())) ;
                        separatePot(copy);
			return  null ;
		} else if ((currentRound.getState() == RoundState.RIVER) && endOfSpeak()) {
			// Le round est fini car on est à la river et tout le monde a parle
                        List<Pair<Client, Integer>> copy = new LinkedList<Pair<Client, Integer>>();
                        for (Pair<Client, Integer> p : currentRound.getActualPlayers())
                                copy.add(new Pair<Client, Integer> (p.getFirst(), p.getSecond())) ;
                        separatePot(copy);
			return null ;
		} else if ( endOfSpeak() ) {
			// Le round continue, on passe au tour de parole suivant
			currentRound.setState(RoundState.values()[currentRound.getState().ordinal() + 1]) ;
                        List<Pair<Client, Integer>> copy = new LinkedList<Pair<Client, Integer>>();
                        for (Pair<Client, Integer> p : currentRound.getActualPlayers())
                                copy.add(new Pair<Client, Integer> (p.getFirst(), p.getSecond())) ;
                        separatePot(copy);
			currentRound.setCurrentPlayer(currentRound.getActualPlayers().get(0).getFirst()) ;
			// On remet les paris à -1 pour le tour suivant
			for (Pair<Client,Integer> p : currentRound.getActualPlayers()) {
				p.setSecond(-1) ;
			}
			return ( noOneCanSpeak() ? null : currentRound.getActualPlayers().get(0).getFirst() ) ;
		} else {
			while (nextPlayer.getMoney() == 0 ) {
				nextPlayer = currentRound.getActualPlayers().get((++index)%currentRound.getActualPlayers().size()).getFirst() ;
			}
			currentRound.setCurrentPlayer(nextPlayer) ;
			// On continue le tour de parole
			return nextPlayer ;
		}

	}

        private void separatePot(List<Pair<Client, Integer>> actualPlayers) {
                boolean fini = true ;
                for (int i = 0 ; i < actualPlayers.size () && fini ; ++i)
                        if (actualPlayers.get(i).getSecond() > 0)
                                fini = false ;
                if (fini)
                        return ;
                int min = actualPlayers.get(0).getSecond() ;
                int minidx = 0 ;
                for (int i = 0 ; i < actualPlayers.size() ; ++i) {
                        if (actualPlayers.get(i).getSecond() < min) {
                                min = actualPlayers.get(i).getSecond() ;
                                minidx = i ;
                        }
                }
		List<Client> lc = new LinkedList<Client>() ;
		for (Pair<Client,Integer> p : actualPlayers) {
                        lc.add(p.getFirst()) ;
		}
                Pair<List<Client>, Integer> pot = new Pair<List<Client>, Integer> (lc, min * actualPlayers.size());
                currentRound.getPots().add(pot);
                for (int i = 0 ; i < actualPlayers.size() ;) {
                        int n = actualPlayers.get(i).getSecond() - min ;
                        if (n == 0)
                                actualPlayers.remove(i);
                        else
                                actualPlayers.get(i++).setSecond(n);
                }
                for (int i = 0 ; i < currentRound.foldedPot.size() ;) {
                        int n = currentRound.foldedPot.get(i) ;
                        if (n > min) {
                                n -= min ;
                                pot.setSecond(pot.getSecond() + min);
                                currentRound.foldedPot.set(i++, n);
                        } else {
                                pot.setSecond(pot.getSecond() + n);
                                n = 0;
                                currentRound.foldedPot.remove(i);
                        }
                }
                separatePot(actualPlayers);
        }

	private boolean noOneCanSpeak() throws RemoteException {
		// Return true if they're all allin or just one client with money and all the other allin
		int ret = 0 ;
		for (Pair<Client,Integer> p : currentRound.getActualPlayers()) {
			if (p.getFirst().getMoney() > 0) {
				// we don't count the allin
				if (ret > 0) {
					return false ;
				} else {
					ret++ ;
				}
			}
		}
		return true ;
	}

        public boolean mayCheck() {
		for (Pair<Client,Integer> p : currentRound.getActualPlayers()) {
                        if (p.getSecond() > 0) {
                                return false;
                        }
		}
                return true;
        }

        /*
        public int toCall(Client client) {
                List<Pair<Client, Integer>> lp = currentRound.getActualPlayers();
                int max = lp.get(0).getSecond();
                for (int i = 1 ; i < lp.size() ; ++i) {
                        if (lp.get(i).getSecond() > max) {
                                max = lp.get(i).getSecond();
                        }
                }
                if (client.getMoney() < max) {
                        return client.getMoney();
                }
                return max;
        }
        */

	public boolean endOfSpeak() throws RemoteException {
		Client dealer = currentRound.getDealer() ;
		boolean allChecked = true ;
		boolean allMax = true ;

		int max = 0 ;
		for (Pair<Client,Integer> p : currentRound.getActualPlayers()) {
			int current = p.getSecond() ;
			if (current == -1) {
				return false ;
			} else if (current != 0) {
				allChecked = false ;
				if (current > max) {
					max = current ;
				} 
			}
		}
		for (Pair<Client,Integer> p : currentRound.getActualPlayers()) {
			if (p.getSecond() != max && p.getFirst().getMoney() > 0) {
				// we don't count the allin
				allMax = false ;
			}
		}
		return allChecked || allMax ;
	}
	
	public int getCurrentMaxBet() {
		int max = -1 ;
		for (Pair<Client,Integer> p: currentRound.getActualPlayers()) {
			if (p.getSecond() > max) {
				max = p.getSecond() ;
			}
		}
		return max ;
	}
	
	public void handBegan() {
		currentRound.setActualPlayers(new LinkedList<Pair<Client, Integer>>()) ;
		for (Client c: players) {
			currentRound.getActualPlayers().add(new Pair<Client, Integer>(c, -1));
		}
		Collections.rotate(currentRound.getActualPlayers(),-3) ;
	}
	
	private Round currentRound;
	private int minNbPlayers;
	private int thinkTime;
	private int buyIn;
	private int blindUpTime;
	private float blindUpInc;
	private List<Client> players = new LinkedList<Client>();
	private List<Client> spectators = new LinkedList<Client>();
	private Server server;
	public static final int MAX_NB_PLAYERS = 10;
	private static final long serialVersionUID = 1L;
}
