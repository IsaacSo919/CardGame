package Thegame;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Player implements Runnable {
    private final Lock lock = new ReentrantLock();
    private final int playerId;
    private final Deck leftDeck;
    private final Deck playerHand; // Renamed from rightDeck to playerHand

    private List<Player> allPlayers;
    public Deck getLeftDeck() {
        return leftDeck;
    }

    public Player(int playerId, Deck leftDeck, Deck playerHand, List<Player> allPlayers) {
        this.playerId = playerId;
        this.leftDeck = leftDeck;
        this.playerHand = playerHand;
        this.allPlayers = allPlayers;
    }

    public void drawCard() {
        lock.lock();
        try {
            if (!leftDeck.isEmpty()) {
                Card drawnCard = leftDeck.drawCard();
                playerHand.addCard(drawnCard);
            }
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
        lock.lock();
        try {
            List<Card> hand = playerHand.getCards();
            for (Card card : hand) {
                if (card.getFaceValue() != playerId) {
                    return false;
                }
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    private Player getNextPlayer() {
        int currentIndex = allPlayers.indexOf(this);
        int nextIndex = (currentIndex + 1) % allPlayers.size();
        return allPlayers.get(nextIndex);
    }
    public void setAllPlayers(List<Player> allPlayers) {
        this.allPlayers = allPlayers;
    }


    @Override
    public void run() {
        while (!hasWinningHand()) {
            drawCard();
            // Assuming you have a getNextPlayer() method to get the next player in the game
            Player nextPlayer = getNextPlayer();
            discardCard(nextPlayer);
        }
        System.out.println("Player " + playerId + " has won!");
    }
}
