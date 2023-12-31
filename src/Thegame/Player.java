package Thegame;

import java.lang.reflect.Field;
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
    private FileWriter deck_output;
    private boolean isPlayerWriterClosed = false;
    private boolean isDeckWriterClosed = false;
    private boolean hasWon = false;
    private CardGame game;

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


    //Players method----------------------------------------------------
    public void writeInitialHand() {
        String initial_Hand = "Player " + playerId + " initial hand ";
        for (Card card : playerHand) {
            initial_Hand += " " + card.getFaceValue();
        }
        initial_Hand += "\n";
        writeMessageToPlayer(initial_Hand);
    }

    public void current_Hand() {
        String current_Hand = "Player " + playerId + " current hand:";
        for (Card card : playerHand) {
            current_Hand += " " + card.getFaceValue();
        }
        current_Hand += "\n";
        writeMessageToPlayer(current_Hand);
    }

    public void final_Hand() {
        String final_Hand = "Player " + playerId + " final hand: ";
        for (Card card : playerHand) {
            final_Hand += " " + card.getFaceValue();
        }
        writeMessageToPlayer(final_Hand);
    }

    public synchronized void drawCard(Deck d) {
        lock.lock();
        try {
            if (!d.isEmpty()) {
                Card drawnCard = d.drawCardTopCard();
                String playerDraw = "Player " + playerId + " draws a " + drawnCard.getFaceValue()
                        + " from deck " + d.getDeck_No() + "\n";
//            System.out.println(playerDraw);
                writeMessageToPlayer(playerDraw);// print draw
                playerHand.add(drawnCard);
            }
        } finally {
            lock.unlock();
        }
    }

    public synchronized void discardCard(Deck d) {
        lock.lock();
        try {
            String playerDiscards = "player " + playerId + " discards a ";
            for (Card card : playerHand) {
                if (card.getFaceValue() != playerId) {
                    //System.out.println("Player " + playerId + " discards " + card.getFaceValue() + " to deck " + d.getDeck_No());
                    playerDiscards += card.getFaceValue() + " to deck " + d.getDeck_No() + "\n";
                    writeMessageToPlayer(playerDiscards);// print discard
                    playerHand.remove(card);
                    d.addToDeck(card);
                    break;
                }
            }
        } finally {
            lock.unlock();
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


    public synchronized void writeMessageToPlayer(String message) {
        try {
            player_output.write(message);
            player_output.flush(); // Flush to ensure data is written immediately
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void writeMessageToDeck(Deck deck) {
        String deckMessage = ("deck" + deck.getDeck_No() + " contents: ");
        deckMessage += deck.getDeckContentInString();
        try {
            deck_output.write(deckMessage);
            deck_output.flush(); // Flush to ensure data is written immediately
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void close_player_output() {
        if (!isPlayerWriterClosed) {
            try {
                player_output.close();
                isPlayerWriterClosed = true; // Mark the writer as closed
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void close_deck_output() {
        if (!isDeckWriterClosed) {
            try {
                deck_output.close();
                isDeckWriterClosed = true; // Mark the writer as closed
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void declareWinning() {
        writeMessageToPlayer("player " + this.playerId + " wins\n");
        writeMessageToPlayer("player " + this.playerId + " exits\n");
        writeMessageToPlayer("player " + this.playerId + " final hand:");
        for (Card card : this.playerHand) {
            writeMessageToPlayer(" " + card.getFaceValue());
        }
    }

    public void declareOthers() {
        /*How this is done is by saving the game object to each of the players,
        so when this.player declare he wins, this.player can inform other players that he wins
        the if statement is for excluding this.player also known as the player itself*/
        ArrayList<Player> players = game.getPlayers();
        for (Player player : players) {
            if (player.playerId != this.playerId) {
                player.writeMessageToPlayer("player " + this.playerId + " has informed player " + player.playerId + " that player " + this.playerId + " has won\n");
                player.writeMessageToPlayer("player " + this.playerId + " exits\n");
                player.writeMessageToPlayer("player " + player.playerId + " hand:");
                for (Card card : player.getPlayerHand()) {
                    player.writeMessageToPlayer(" " + card.getFaceValue());
                }
            }
        }
    }

    private volatile boolean isRunning = true;

    public void stopThread() {
        isRunning = false;
    }


    @Override
    public void run() {
        //threading undone
        writeInitialHand();
        while (isRunning) {

            if (hasWinningHand()) {
                hasWon = true;
                declareWinning();// print winning message
                declareOthers();// inform other players by printing a message to them
                try {
                    for (Deck deck : game.getDecks()) {
                        this.deck_output = new FileWriter("deck" + deck.getDeck_No() + "_output.txt");
                        writeMessageToDeck(deck);// just once when game has finished
                        close_deck_output();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                // return true when a player has winning hand, vise versa.
                break;
            }
            if (this.leftDeck.getDecksSize() >= 4 && this.rightDeck.getDecksSize() <= 4) {
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
    }

}
