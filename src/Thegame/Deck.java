package Thegame;

import java.util.Stack;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Deck {
    private final Lock lock = new ReentrantLock();
    private Stack<Card> deck = new Stack<Card>();

    public Deck() {
        deck = new Stack<>();
    }

    public void addCard(Card card) {
        lock.lock();
        try {
            deck.push(card);
        } finally {
            lock.unlock();
        }
    }

    public Card drawCard() {
        lock.lock();
        try {
            return deck.pop();
        } finally {
            lock.unlock();
        }
    }
    public Card dealCard() {
        if (isEmpty()) {
            throw new IllegalStateException("Deck is empty");
        }

        // Remove and return the top card (last card in the list)
        return deck.remove(deck.size() - 1);
    }
    public boolean isEmpty() {
        return deck.isEmpty();
    }
    public Stack<Card> getCards() {
        return deck;
    }
}

