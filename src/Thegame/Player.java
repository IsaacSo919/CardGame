package Thegame;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.io.FileWriter;
import java.io.IOException;

public class Player extends Thread {
    private final Lock lock = new ReentrantLock();
    private final int playerId;
    private ArrayList<Card> playerHand;
    private Deck leftDeck;
    private Deck rightDeck;
    private FileWriter player_output;
    private boolean isWriterClosed = false;
    private boolean hasWon = false;
    private CardGame game ;

    public Player(int No, Deck leftDeck, Deck rightDeck, CardGame game) {
        this.playerId = No;
        this.playerHand = new ArrayList<Card>();
        this.leftDeck = leftDeck;
        this.rightDeck = rightDeck;
        try {
            this.player_output = new FileWriter("player" + No + "_output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.game = game;
    }

    public FileWriter getPlayer_output() {
        return player_output;
    }

    public int getPlayerId() {
        return playerId;
    }

    public boolean getHasWon() {
        return hasWon;
    }

    public ArrayList<Card> getPlayerHand() {
        return playerHand;
    }

    //adders XD
    public void add_card_to_playerHand(Card card) {
        playerHand.add(card);
    }

    public boolean isWriterClosed() {
        return isWriterClosed;
    }

    //Players method----------------------------------------------------
    public void writeInitialHand() {
        String initial_Hand = "Player " + playerId + " initial hand ";
        for (Card card : playerHand) {
            initial_Hand += " " + card.getFaceValue();
        }
        initial_Hand += "\n";
        writeMessage(initial_Hand);
    }

    public void current_Hand() {
        String current_Hand = "Player " + playerId + " current hand:";
        for (Card card : playerHand) {
            current_Hand += " " + card.getFaceValue();
        }
        current_Hand += "\n";
        writeMessage(current_Hand);
    }

    public void final_Hand() {
        String final_Hand = "Player " + playerId + " final hand: ";
        for (Card card : playerHand) {
            final_Hand += " " + card.getFaceValue();
        }
        writeMessage(final_Hand);
    }

    public synchronized void drawCard(Deck d) {
        if (!d.isEmpty()) {
            Card drawnCard = d.drawCardTopCard();
            String playerDraw = "Player " + playerId + " draws a " + drawnCard.getFaceValue()
                    + " from deck " + d.getDeck_No()+"\n";
//            System.out.println(playerDraw);
            writeMessage(playerDraw);
            playerHand.add(drawnCard);
        }
    }

    public synchronized void discardCard(Deck d) {
        String playerDiscards = "player " + playerId + " discards a ";
        for (Card card : playerHand) {
            if (card.getFaceValue() != playerId) {
//                System.out.println("Player " + playerId + " discards " + card.getFaceValue() + " to deck " + d.getDeck_No());
                playerDiscards += card.getFaceValue() + " to deck " + d.getDeck_No()+"\n";
                writeMessage(playerDiscards);
                playerHand.remove(card);
                d.addToDeck(card);
                break;
            }
        }

    }


    public synchronized boolean hasWinningHand() {
        lock.lock();
        try {
            ArrayList<Card> player_hand = this.playerHand; // its of type card
            int faceValue = player_hand.get(0).getFaceValue();
            for (int j = 1; j < player_hand.size(); j++) {
                if (player_hand.get(j).getFaceValue() != faceValue) {
                    return false; // If any card has a different face value, return false
                }
            }
            return true; // If all cards have the same face value, return true
        } finally {
            lock.unlock();
        }
    }


    public synchronized void writeMessage(String message) {
        try {
            player_output.write(message);
            player_output.flush(); // Flush to ensure data is written immediately
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close_player_output() {
        if (!isWriterClosed) {
            try {
                player_output.close();
                isWriterClosed = true; // Mark the writer as closed
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private volatile boolean isRunning = true;

    public void stopThread() {
        isRunning = false;
    }
//    public void endGame() {
//        writeMessage("deck", this.playerId, "deck" + this.playerId + " contents: " + deck.getStringOfCardValues());
//        if (game.winningPlayer.get() == playerId) {
//            writeMessage("player", this.playerId, "player " + this.playerId + " wins");
//        } else {
//            writeMessage("player", this.playerId, "player " + game.winningPlayer.get() + " has informed player " + playerId + " that player " + game.winningPlayer.get() + " has won");
//        }
//        writeMessage("player", this.playerId, "player " + this.playerId + " exits");
//        writeMessage("player", this.playerId, "player " + this.
//    }
    @Override
    public void run() {
        //threading undone
        writeInitialHand();
        while (isRunning){

                if (hasWinningHand()) {
                    hasWon = true;
                    // return true when a player has winning hand, vise versa.
                    break;
                }
                if(this.leftDeck.getDecksSize() >= 4 && this.rightDeck.getDecksSize() <= 4 ){
                    /*
                        the size of the deck can be 5, it means a player only draw from left deck
                        when a card in inserted into leftdeck, and it also means when right deck
                        is smaller 4 (still got place left), then a card is insert into it from the player
                    */
                    drawCard(this.leftDeck);
                    discardCard(this.rightDeck);
                    current_Hand();
                }

        }
        close_player_output();
    }

}
