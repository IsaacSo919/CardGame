import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Player implements Runnable {
    private final Lock lock = new ReentrantLock();
    private final int playerId;
    private final Deck leftDeck;
    private final Deck playerHand; // Renamed from rightDeck to playerHand

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

    public void discardCard() {
        lock.lock();
        try {
            Card discardedCard = playerHand.discardNonPreferredCard(playerId);
            playerHand.addCard(discardedCard);
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
