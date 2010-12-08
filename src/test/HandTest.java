package test;

import core.controller.*;
import core.model.*;
import core.protocol.*;
import java.util.Scanner;
import java.util.*;

public class HandTest {
	public static void main(String[] args) {

		List<Card> cards = new LinkedList<Card>();
		for(Value v: Value.values()) {
			for(Suit s: Suit.values()) {
				cards.add(new Card(v, s));
			}
		}
		
		Random rand = new Random();
		Card[] cards1 = new Card[7];
		Card[] cards2 = new Card[7];
		
		for (int i = 0; i < 7 ; i++) {
			//Card c = cards.remove(rand.nextInt(cards.size())) ;
			Card c1 = cards.remove(2 + i * 3) ; 
			cards1[i] = c1;
			Card c2 = cards.remove(rand.nextInt(cards.size())) ; 
			cards2[i] = c2;
		}
		
		System.out.println("------ Card 1 ---------") ; 
		for(Card c : cards1) {
			System.out.println(c.getSuit() + " " + c.getValue() );
		}
		System.out.println(Round.getRank(cards1)) ;
		System.out.println("------ Card 2 ---------") ; 
		for(Card c : cards2) {
			System.out.println(c.getSuit() + " " + c.getValue() );
		}
		System.out.println(Round.getRank(cards2)) ;
		System.out.println("------ Result ---------") ;
		Card[] c3 = Round.getBestCards(cards1,cards2) ;
		
		
	}
}
