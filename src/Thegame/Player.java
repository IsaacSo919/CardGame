package Thegame;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Player implements Runnable {
    private final Lock lock = new ReentrantLock();
    private final int playerId;
    private final Deck leftDeck;
    private final Deck playerHand; // Renamed from rightDeck to playerHand
    
    public Deck getLeftDeck() {
        return leftDeck;
    }
    
    public Player(int playerId, Deck leftDeck, Deck playerHand) {
        this.playerId = playerId;
        this.leftDeck = leftDeck;
        this.playerHand = playerHand;
    }

    public void drawCard() {
        lock.lock();
        try {
            Card drawnCard = leftDeck.drawCard();
            playerHand.addCard(drawnCard);
        } finally {
            lock.unlock();
        }
    }

    public void discardCard(Player nextPlayer) {
        lock.lock();
        try {
            List<Card> hand = playerHand.getCards();
            for (int i = 0; i < hand.size(); i++) {
                Card card = hand.get(i);
                if (card.getFaceValue() != playerId) {
                    hand.remove(i);
                    nextPlayer.getLeftDeck().addCard(card);
                    break;
                }
            }
        } finally {
            lock.unlock();
        }
    }


    private boolean hasWinningHand() {
        List<Card> hand = playerHand.getCards();
        if (hand.size() != 4) {
            return false;
        }
        for (Card card : hand) {
            if (card.getFaceValue() != preferredValue) {
                return false;
            }
        }
        return true;
    }


    @Override
    public void run() {
        while (!hasWinningHand()) {
            drawCard();
            discardCard();
        }
        System.out.println("Player " + playerId + " has won!");
    }
}
