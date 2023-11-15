package Thegame;

import java.util.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Player implements Runnable {
	  private final int preferredValue;
	  private final Deck leftDeck;
	  private final Deck rightDeck;

	  public Player(int preferredValue, Deck leftDeck, Deck rightDeck) {
	      this.preferredValue = preferredValue;
	      this.leftDeck = leftDeck;
	      this.rightDeck = rightDeck;
	  }

	  public synchronized void drawCard() {
	      Card drawnCard = leftDeck.drawCard();
	      rightDeck.addCard(drawnCard);
	  }

	  @Override
	  public void run() {
	      while (true) {
	          drawCard();
	          if (hasWinningHand()) {
	              System.out.println("Player " + preferredValue + " wins");
	              System.exit(0);
	          }
	      }
	  }

	  private boolean hasWinningHand() {
		   List<Card> hand = rightDeck.getCards();
		   if (hand.size() != 4) {
		       return false;
		   }
		   for (Card card : hand) {
		       if (card.getValue() != preferredValue) {
		           return false;
		       }
		   }
		   return true;
		}
	}
