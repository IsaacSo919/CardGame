package Thegame;

import java.io.*;
import java.util.*;

public class CardGame {
    //local variables for CardGame
    private final int numberofPlayers;
    private final ArrayList<Card> pack;
    private final ArrayList<Player> players;
    private final ArrayList<Deck> decks;

    // constructors for CardGame
    public CardGame(int numberofPlayers) {
        this.numberofPlayers = numberofPlayers;
        this.pack = this.initialize_a_game(askForPacklocation(numberofPlayers));
        this.players = new ArrayList<Player>();
        this.decks = new ArrayList<Deck>();
        for (int i = 0; i < numberofPlayers; i++) {
            this.decks.add(new Deck(i + 1));
        }
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Deck> getDecks() {
        return decks;
    }

    //  ----Function that allows user to input the number of players are in the game---------------------
    private static int askForPlayers() {
        int numberOfPlayers = 0; // Initialize to a default value

        try {
            while ((numberOfPlayers <= 2)) {// New Scanner input
                if (numberOfPlayers == 0) {
                    System.out.println("numberOfPlayers cannot be equal 0");
                } else if (numberOfPlayers <= 2) {
                    System.out.println("numberOfPlayers must be greater than 2");
                }

                Scanner scanner = new Scanner(System.in);
                // Asking user for number of players
                System.out.print("Enter the number of players: \n");
                numberOfPlayers = scanner.nextInt();
                // Clear the newline character from the buffer
                scanner.nextLine();

                // Asking users for the location of a valid plain input_text file
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number of players. Must be an integer.");
            System.exit(1);
        }
        return numberOfPlayers;
    }

    //    -----------------------------------------------------------------------------------
    public String askForPacklocation(int numberOfPlayers) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter the location of the pack to load: ");
        String inputPackLocation = scanner.nextLine();
        // Check if file exists
        try {
            File file = new File(inputPackLocation);
            if (!file.exists()) {
                System.out.println("File does not exist. Generating a valid card pack...");
                generateCardPack(numberOfPlayers, inputPackLocation);
            } else {
                System.out.println("Checking if input-pack is valid ");
                Boolean validity;

                validity = ispackvalid(inputPackLocation); //ispackvalid checks whether the pack is valid,
                // it will return true if valid false, if not valid
                if (!validity) {
                    throw new RuntimeException("the pack is not valid");
                }
            }
            // Close the scanner when you are done using it
            scanner.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return (inputPackLocation);
    }

    //  ---------------------------------------------------------------------------------------------------
    private boolean ispackvalid(String inputpacklocation) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputpacklocation))) {
            // row count must be equal to 8*n;
            int rowcount = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    int cardValue = Integer.parseInt(line.trim());
                    if (cardValue < 0) {
                        System.out.println("Invalid card value: " + cardValue);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid format. Each row should contain a non-negative integer value.");
                    return false;
                }
                rowcount++;
            }
            // Check if the total number of rows is 8n
            if (rowcount != 8 * this.numberofPlayers) {
                System.out.println("Invalid number of rows. The file should have 8n rows");
                return false;
            }
            return true;
        }
    }

    //  ---------------------------------------------------------------------------------------------------
    public void WriteCardPacktoFile(List<Integer> cardValues, String filename){
        try (FileWriter fileWriter = new FileWriter(filename)) {
            for (Integer cardValue : cardValues) {
                fileWriter.write(cardValue + "\n");// Write shuffled values to the file
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static int generateCardPack(int numberOfPlayers, ) {
        // Checks whether there is a valid number of players
        if (numberOfPlayers <= 0) {
            System.out.println("Invalid number of players. Please enter a valid number of players");
            return;
        }

        int maxCardValue = numberOfPlayers; // Maximum face value is the number of players
        int totalCards = 8 * numberOfPlayers; // Total number of cards
        int remainingCards = totalCards % maxCardValue; // Calculate remaining cards


            // Create a list to hold the card values
            List<Integer> cardValues = new ArrayList<>();

            // Generate cards with the specified distribution of face values
            for (int i = 1; i <= maxCardValue; i++) {
                int cardCount = totalCards / maxCardValue;
                if (remainingCards > 0 && i <= remainingCards) {
                    cardCount++;
                }
                for (int j = 0; j < cardCount; j++) {
                    cardValues.add(i);
                }
            }

            // Shuffle the list of card values
            Collections.shuffle(cardValues);
            return cardValues;
        }


    //  ---------------------------------------------------------------------------------------------------
    public ArrayList<Card> initialize_a_game(String filename) {
        ArrayList<Card> pack = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                int value = Integer.parseInt(line.trim());
                if (value <= this.numberofPlayers)
                    pack.add(new Card(value));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.shuffle(pack);
        return pack;
    }

    public void createPlayers() {
        for (int i = 0; i < this.numberofPlayers; i++) {
            this.players.add(new Player(i + 1, this.decks.get(i), this.decks.get((i + 1) % numberofPlayers), this));
        }
    }

    public void distribute_card_to_playerHand() {
        int index = 0;
        for (int i = 1; i <= 4; i++) {
            for (Player player : this.players) {
                player.add_card_to_playerHand(this.pack.get(index));// index is pointing which player we are at
                index++;
            }
        }
    }

    public void distribute_card_to_deck() {
        int index = this.numberofPlayers * 4;
        for (int i = 1; i <= 4; i++) {
            for (Deck deck : this.decks) {
                deck.addToDeck(this.pack.get(index));
                index++;
            }
        }
    }


    public void endGame() {
        System.out.println("The game has ended.");
    }


    //  ----------------------------------------MAIN----------------------------------------------------
    public static void main(String[] args) {
        int numberofplayers = askForPlayers();
        System.out.println("Number of players in game : " + numberofplayers + " players");
        String outputfilepath = "card_pack.txt";
        List<Integer> cardValues = generateCardPack(numberofplayers);
        WriteCardPacktoFile(cardValues,outputfilepath);
        CardGame game = new CardGame(numberofplayers);
        /* when called, pack is intialize and added contents, however for players and decks(ArrayList),these two is
        only initialized */

        game.createPlayers();
        game.distribute_card_to_playerHand();// Distribute 4 cards to each players' hands
        game.distribute_card_to_deck();
        for (Player player : game.players) {
            player.start();// thread start here!
        }
        boolean gameEnd = false;
        Player winner = null;
        while (!gameEnd) {
            for (int i = 0; i < numberofplayers; i++) {
                Player player = game.players.get(i);
                if (player.getHasWon()) {
                    for (Player otherPlayer : game.players) {
                        otherPlayer.stopThread();
                    }
                    gameEnd = true;
                    winner = player;
                    break;
                }
            }
        }
        System.out.println("Player " + winner.getPlayerId() + " wins!");


    }
}