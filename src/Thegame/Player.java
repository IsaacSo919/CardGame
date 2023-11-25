package Thegame;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.io.FileWriter;
import java.io.IOException;

public class Player implements Runnable {
    private final Lock lock = new ReentrantLock();
    private final int playerId;
    private ArrayList<Card> playerHand;

    private FileWriter player_output;
    private boolean isWriterClosed = false;



    public Player(int No) {
        this.playerId = No;
        this.playerHand = new ArrayList<Card>();
        try {
            this.player_output = new FileWriter("player" + No+ "_output.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public FileWriter getPlayer_output(){
        return player_output;
    }

    public int getPlayerId() {
        return playerId;
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
    public void initialHand(){
        String initial_Hand = "Player " + playerId + " initial hand ";
        for ( Card card: playerHand){
            initial_Hand += " " + card.getFaceValue();
        }
        initial_Hand += "\n";
        System.out.println(initial_Hand);
    }
    public void current_Hand(){
        String current_Hand ="Player " + playerId + " current hand:";
        for ( Card card: playerHand){
            current_Hand += " " + card.getFaceValue();
        }
        current_Hand += "\n";
        System.out.println(current_Hand);
    }
    public void final_Hand(){
        String final_Hand ="Player " + playerId + " final hand: ";
        for ( Card card: playerHand){
            final_Hand += " " + card.getFaceValue();
        }
        System.out.println(final_Hand);
    }

    public synchronized void drawCard(Deck d) {
        lock.lock();
        try {

            if (!d.isEmpty()) {

                Card drawnCard = d.drawCardTopCard();
                String playerDraw = "Player " + playerId + " draws a " + drawnCard
                        + " from deck " + d.getDeck_No();
                playerHand.add(drawnCard);
            }
        }  finally {
            lock.unlock();
        }

    }

    public synchronized void discardCard(Deck d) {
        lock.lock();
        try {
            String playerDiscards = "Player " + playerId + " discards  ";
            for (Card card: playerHand) {
                if (card.getFaceValue() != playerId) {
                    System.out.println("Player " + playerId + " discards " + card.getFaceValue() + " to deck " + d.getDeck_No());

                    playerDiscards += card.getFaceValue() + " to deck " + d.getDeck_No();

                    playerHand.remove(card);
                    break;
                }
            }
        } finally {
            lock.unlock();
        }
        writeMessage("Player " + playerId + " discards a card to next player.\n");
    }


    public synchronized boolean hasWinningHand() {
        lock.lock();
        try {
            ArrayList<Card> player_hand = playerHand; // its of type card
            int faceValue = player_hand.get(0).getFaceValue();
            for (int j = 1; j < player_hand.size(); j++) {
                if (player_hand.get(j).getFaceValue() != faceValue) {
                    return false; // If any card has a different face value, return false
                }
            }
            return true; // If all cards have the same face value, return true
        }finally {
            lock.unlock();
        }
    }



    public synchronized void writeMessage(String message) {
        try {
            player_output.write(message);
            player_output.flush(); // Flush to ensure data is written immediatel
            close_player_output();
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
        @Override
        public void run() {//threading undone

            while (!hasWinningHand()) {
                drawCard(deck);
                // Assuming you have a getNextPlayer() method to get the next player in the game
                discardCard(d);
            }
            System.out.println("Player " + playerId + " has won!");

        }
    }
