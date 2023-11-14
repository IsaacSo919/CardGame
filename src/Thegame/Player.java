package Thegame;

import java.util.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class Player extends Thread {
    private final List<Card> hand;
    private final int preferredValue;
    private final Deck leftDeck;
    private final Deck rightDeck;

    public Player(int preferredValue, Deck leftDeck, Deck rightDeck) {
        this.preferredValue = preferredValue;
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        this.hand = new ArrayList<>();
    }

    public synchronized void drawCard() {
        Card drawnCard = leftDeck.drawCard();
        hand.add(drawnCard);
    }

    public synchronized void discardCard() {
        for (Iterator<Card> iterator = hand.iterator(); iterator.hasNext();) {
            Card card = iterator.next();
            if (card.getValue() != preferredValue) {
                rightDeck.addCard(card);
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            drawCard();
            discardCard();
            if (hasWinningHand()) {
                System.out.println("Player " + preferredValue + " wins");
                System.exit(0);
            }
        }
    }

    private boolean hasWinningHand() {
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

