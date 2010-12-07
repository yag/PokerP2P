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
		for (Pair<Client,Integer> p: actualPlayers) {
			if (p.getFirst().getName().equals(curplayername)) {
				current = p.getSecond() ;
				money = p.getFirst().getMoney() ;
				break ;
			}
			index += 1;
		}
		player = actualPlayers.get(index) ;
		// On cherche le pari max actuel
		int max = getCurrentMaxBet() ;
		
		// on initilise le joueur suivant
		Client nextPlayer = null ;
		// On prend l'action en compte
		switch (act.getType()) {
			case RAISE :
				if (act.getBet() < max || act.getBet() - player.getSecond() > money ) {
					throw new CheatException() ;
				}
				currentRound.addToPot(act.getBet()) ;
				player.setSecond(act.getBet() + player.getSecond()) ; // on ajoute
				nextPlayer = actualPlayers.get((index+1)%actualPlayers.size()).getFirst() ;
				break ;
				
			case FOLD :
				actualPlayers.remove(index) ;
				nextPlayer = actualPlayers.get((index)%actualPlayers.size()).getFirst() ;
				break ;
					
			case CHECK :
			 	// pour checker, il faut que tous les joueurs ait checké (ou n'est pas encore parlé)
				if (max >  0 || player.getSecond() > -1 ) {
					throw new CheatException() ;
				}
				player.setSecond(0) ;
				nextPlayer = actualPlayers.get((index+1)%actualPlayers.size()).getFirst() ;
				break ;
				
			case CALL :

			// On verifie si il a assez d'argent
			if ((max-current) > money) {
					// il a parie plus qu'il ne possede -> triche
					throw new CheatException() ;
			}
			// On met à jour la liste
			player.setSecond(max) ;
			currentRound.addToPot(max - current) ;
			nextPlayer = actualPlayers.get((index+1)%actualPlayers.size()).getFirst() ;
			break ;
			
			case SITOUT :
			actualPlayers.remove(index) ;
			nextPlayer = actualPlayers.get((index)%actualPlayers.size()).getFirst() ;
			break ;			
		}

		/* y'a t'il un joueur suivant qui doit parler ?
		est- ce la fin du round ?
		est-ce la fin du tour ?
		*/
		
		if (actualPlayers.size() <= 1) {
			// Le round est fini car il n'ya plus q'un joueur, il y a un gagnant
			return null ;
		} else if ((currentRound.getState() == RoundState.RIVER) && endOfSpeak()) {
			// Le round est fini car on est à la river et tout le monde a parle
			return null ;
		} else if ( endOfSpeak() ) {
			// Le round continue, on passe au tour de parole suivant
			currentRound.setState(RoundState.values()[currentRound.getState().ordinal() + 1]) ;
			currentRound.setCurrentPlayer(actualPlayers.get(0).getFirst()) ;
			// On remet les paris à -1 pour le tour suivant
			for (Pair<Client,Integer> p : actualPlayers) {
				//currentRound.addToPot(p.getSecond()) ;
				p.setSecond(-1) ;
			}
			return actualPlayers.get(0).getFirst() ;
		} else {
			currentRound.setCurrentPlayer(nextPlayer) ;
			
			// On continue le tour de parole
			return nextPlayer ;
		}

	}
		
	public boolean endOfSpeak() {
		Client dealer = currentRound.getDealer() ;
		boolean allChecked = true ;
		boolean allMax = true ;
		int max = 0 ;
		// D'abord on teste si tout les joeurs présents on check .
		for (Pair<Client,Integer> p : actualPlayers) {
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
		for (Pair<Client,Integer> p : actualPlayers) {
			if (p.getSecond() != max) {
				allMax = false ;
			}
		}
		return allChecked || allMax ;
	}
	
	public int getCurrentMaxBet() {
		int max = -1 ;
		for (Pair<Client,Integer> p: actualPlayers) {
			if (p.getSecond() > max) {
				max = p.getSecond() ;
			}
		}
		return max ;
	}
	
	public void handBegan() {
		actualPlayers = new LinkedList<Pair<Client, Integer>>();
		for (Client c: players) {
			actualPlayers.add(new Pair<Client, Integer>(c, -1));
		}
		Collections.rotate(actualPlayers,-3) ;
	}
	
	private Round currentRound;
	private int minNbPlayers;
	private int thinkTime;
	private int buyIn;
	private int blindUpTime;
	private float blindUpInc;
	// FIXME: use java.util.Collections.synchronizedList?
	private List<Client> players = new LinkedList<Client>();
	public List<Pair<Client,Integer>> actualPlayers;
	private List<Client> spectators = new LinkedList<Client>();
	private Server server;
	public static final int MAX_NB_PLAYERS = 10;
	private static final long serialVersionUID = 1L;
}
