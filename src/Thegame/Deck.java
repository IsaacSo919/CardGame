package Thegame;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Deck {
    private final Lock lock = new ReentrantLock();
    private Queue<Card> decks;
    private int deck_No;

    public Deck(int No) {
        this.decks = new LinkedList<Card>();
        this.deck_No = No;
    }

    public void addToDeck(Card card) {
        lock.lock();
        try {
            decks.add(card);
        } finally {
            lock.unlock();
        }
    }


    public int getDeck_No() {
        return deck_No;
    }

    public int GetValueTopCard() {
        assert decks.peek() != null;
        int faceValue = decks.peek().getFaceValue();
        return faceValue;
    }

    public Card drawCardTopCard() {
        lock.lock();
        try {
            return decks.poll();
        } finally {
            lock.unlock();
        }
    }

    public StringBuilder getDeckContentInString() {
        StringBuilder message = new StringBuilder();
        for (Card card:this.decks){
            message.append(card.getFaceValue()+" ");
        }

        return message;
    }

    public Queue<Card> getDecks() {
        return decks;
    }

    public boolean isEmpty() {
        return decks.isEmpty();
    }

    public int getDecksSize() {
        return decks.size();
    }
}


