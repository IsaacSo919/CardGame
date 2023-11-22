package Thegame;

import java.io.*;
import java.util.*;
public class CardGame {
	//local variables for CardGame
    private final int numberofPlayers;
    private List<Player> allPlayers;

    // constructors for CardGame
    public CardGame(int numberOfPlayers){
        this.numberofPlayers = numberOfPlayers;
        this.allPlayers = new ArrayList<>();  // Initialize the list in the constructor
    }


//  ----Function that allows user to input the number of players are in the game---------------------
    private static  int askForPlayers() {
        int numberOfPlayers = 0; // Initialize to a default value

        try {
            // New Scanner input
            Scanner scanner = new Scanner(System.in);

            // Asking user for number of players
            System.out.print("Enter the number of players: \n");
            numberOfPlayers = scanner.nextInt();
            // Clear the newline character from the buffer
            scanner.nextLine();

            // Asking users for the location of a valid plain input_text file
            
        } catch (NumberFormatException e) {
            System.out.println("Invalid number of players. Must be an integer.");
            System.exit(1);
        } 
        return numberOfPlayers;
    }
//    -----------------------------------------------------------------------------------
    private static String askForPacklocation(int numberOfPlayers) {
    	
    	
    	Scanner scanner = new Scanner(System.in);
    	System.out.print("Please enter the location of the pack to load: ");
        String inputPackLocation = scanner.nextLine();
        // Check if file exists
	        try {
	        File file = new File(inputPackLocation);
	        if(!file.exists()){
	            System.out.println("File does not exist. Generating a valid card pack...");
	            generateCardPack(numberOfPlayers,inputPackLocation);
	        }else{
	            System.out.println("Checking if input-pack is valid ");
	            ispackvalid(numberOfPlayers,inputPackLocation); //we need an exception here
	        }
	        // Close the scanner when you are done using it
	        scanner.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        return (inputPackLocation);
    }
//  ---------------------------------------------------------------------------------------------------
    private static boolean ispackvalid(int numberofPlayers, String inputpacklocation) throws IOException {
        try(BufferedReader reader = new BufferedReader(new FileReader(inputpacklocation))){
            // row count must be equal to 8*n;
            int rowcount = 0;
            String line;
            while((line = reader.readLine()) != null){
                try{
                    int cardValue = Integer.parseInt(line.trim());
                    if(cardValue < 0){
                        System.out.println("Invalid card value: " + cardValue);
                    }
                } catch(NumberFormatException e){
                    System.out.println("Invalid format. Each row should contain a non-negative integer value.");
                    return false;
                }
                rowcount++;
            }
            // Check if the total number of rows is 8n
            if(rowcount != 8 * numberofPlayers){
                System.out.println("Invalid number of rows. The file should have 8n rows");
                return false;
            }
            return true;
        }
    }
//  ---------------------------------------------------------------------------------------------------
    private static void generateCardPack(int numberOfPlayers, String filename) {
        // Checks whether there is a valid number of players
        if (numberOfPlayers <= 0) {
            System.out.println("Invalid number of players. Please enter a valid number of players");
            return;
        }

        int maxCardValue = numberOfPlayers + 1; // Maximum face value is the number of players
        int totalCards = 8 * numberOfPlayers; // Total number of cards
        int remainingCards = totalCards % maxCardValue; // Calculate remaining cards

        try (FileWriter fileWriter = new FileWriter(filename)) {
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

            // Write shuffled values to the file
            for (Integer cardValue : cardValues) {
                fileWriter.write(cardValue + "\n"); // Each row contains a single non-negative integer value
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
//  ---------------------------------------------------------------------------------------------------
    public void initialize_a_game(String filename) {
        List<Card> deck = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                int value = Integer.parseInt(line.trim());
                deck.add(new Card(value));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        distributeCards(deck);
    }
    
    public void distributeCards(List<Card> deck) {
        // Create a list to hold the decks for each player's hand
        List<Deck> hands = new ArrayList<>();

        // Shuffle the deck
        Collections.shuffle(deck);

        // Distribute the first 4N cards to the players' hands
        for (int i = 0; i < numberofPlayers; i++) {
            Deck hand = new Deck();
            for (int j = 0; j < 4; j++) {
                hand.addCard(deck.remove(deck.size() - 1));
            }
            hands.add(hand);
        }

        // The remaining cards form the initial left deck of the first player
        Deck initialLeftDeck = new Deck();
        for (Card card : deck) {
            initialLeftDeck.addCard(card);
        }

        // Create a Player object for each player
        for (int i = 0; i < numberofPlayers; i++) {
            Deck rightDeck = hands.get(i);
            Deck leftDeck = (i == 0) ? initialLeftDeck : hands.get((i - 1) % numberofPlayers);
            Player player = new Player(i + 1, leftDeck, rightDeck, allPlayers, this);  // Pass 'this' as the CardGame instance
            allPlayers.add(player);
        }

        for (Player player : allPlayers) {
            player.setAllPlayers(allPlayers);
            PlayerThread playerThread = new PlayerThread(player);
            playerThread.start();
        }
    }
    public void endGame() {
        System.out.println("The game has ended.");
        for (Player player : allPlayers) {
            player.closeWriter();
        }
    }

  

//  ----------------------------------------MAIN----------------------------------------------------
    public static void main(String[] args) {
        int numberofplayers = askForPlayers();
        String location = askForPacklocation(numberofplayers);
//        System.out.println("location:"+location);// test for location returned
        System.out.println("Number of players in game : " + numberofplayers + " players");
        String outputfilepath = "card_pack.txt";
        generateCardPack(numberofplayers,outputfilepath);
        CardGame game = new CardGame(numberofplayers);
        game.initialize_a_game(location);


    }
}