package Thegame;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.io.FileWriter;
import java.io.IOException;

public class Player implements Runnable {

    private CardGame game;
    private final Lock lock = new ReentrantLock();
    private final int playerId;
    private final Deck leftDeck;
    private final Deck playerHand;

    private FileWriter writer;
    private boolean isWriterClosed = false;

    private List<Player> allPlayers;
    public Deck getLeftDeck() {
        return leftDeck;
    }

    public Player(int playerId, Deck leftDeck, Deck playerHand, List<Player> allPlayers,CardGame game) {
        this.playerId = playerId;
        this.leftDeck = leftDeck;
        this.playerHand = playerHand;
        this.allPlayers = allPlayers;
        this.game = game;
        try {
            this.writer = new FileWriter("player" + playerId + "_output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void drawCard() {
        lock.lock();
        try {
            if (!leftDeck.isEmpty()) {
                Card drawnCard = leftDeck.drawCard();
                playerHand.addCard(drawnCard);
            }
        } finally {
            lock.unlock();
        }
        writeMessage("Player " + playerId + " draws a card from left deck.\n");
    }

    public synchronized void discardCard(Player nextPlayer) {
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
        writeMessage("Player " + playerId + " discards a card to next player.\n");
    }



    private synchronized boolean hasWinningHand() {
        lock.lock();
        try {
            List<Card> hand = playerHand.getCards();
            for (Card card : hand) {
                if (card.getFaceValue() != playerId) {
                    return false;
                }
            }
        } finally {
            lock.unlock();
        }

        // If we reach this point, the player has a winning hand
        try {
            writer.write("Player " + playerId + " has a winning hand.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
    public void closeWriter() {
        if (!isWriterClosed) {
            try {
                writer.close();
                isWriterClosed = true; // Mark the writer as closed
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public synchronized void writeMessage(String message) {
        try {
            writer.write(message);
            writer.flush(); // Flush to ensure data is written immediately
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            game.endGame();
        }
    }
