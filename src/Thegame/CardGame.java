package Thegame;

import java.io.*;
import java.util.*;
public class CardGame {
	//local variables for CardGame
    private static int numberofPlayers = 0;
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
            while((numberOfPlayers == 0)||(numberOfPlayers <=2)){// New Scanner input
                if (numberOfPlayers == 0){
                    System.out.println("numberOfPlayers cannot be equal 0");
                } else if (numberOfPlayers <=2) {
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
                Boolean validity;

                validity = ispackvalid(numberOfPlayers,inputPackLocation); //ispackvalid checks whether the pack is valid,
                // it will return true if valid false, if not valid
                if (validity == false){
                    System.out.println("the pack is not valid");
                }
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

        int maxCardValue = numberOfPlayers; // Maximum face value is the number of players
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
    public ArrayList<Card> initialize_a_game(String filename) {
        ArrayList<Card> pack = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                int value = Integer.parseInt(line.trim());
                pack.add(new Card(value));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pack;
    }

    public static void createPlayers(int numberofPlayers, ArrayList<Player> p) {
        for(int i = 1; i< numberofPlayers+1; i++) {
            p.add(new Player(i));
        }
    }
    public static void createDeck(int numberofPlayers, ArrayList<Deck> deck) {
        for(int i = 1; i< numberofPlayers+1; i++) {
            deck.add(new Deck(i));
        }
    }
    public static void distributeCards(ArrayList<Card> pack, ArrayList<Player> players,ArrayList<Deck>decks) {
        // Create a list to hold the decks for each player's hand

// ```       testing--------------------------------------------- ```
//        System.out.println("Before distributing the cards");
//        for (Card deeck:deck){
//            System.out.println( deeck.getFaceValue());
//        }
//```  testing---------------------------------------------```
        // Shuffle the deck
        Collections.shuffle(pack);
        distribute_card_to_playerHand(players,pack);// Distribute 4 cards to each players' hands
        distribute_card_to_deck( decks, pack, numberofPlayers);
    }
    public static void distribute_card_to_playerHand(ArrayList<Player> players, ArrayList<Card> pack){
    int index = 0;
        for (int i = 1; i<=4; i++){
            for (Player player: players){
                player.add_card_to_playerHand(pack.get(index));// index is pointing which player we are at
                index++;
            }
        }
    }
    public static void distribute_card_to_deck(ArrayList<Deck> decks, ArrayList<Card> pack, int NumberofPlayers){
        int index = NumberofPlayers * 4;
        for (int i = 1; i<=4; i++){
            for (Deck deck: decks){
                deck.addToDeck(pack.get(index));
                index++;
            }
        }
    }
    public static void starting_the_threads(){
//        PlayerThread playerThread = new PlayerThread(player);
//        playerThread.start();
    }
    public void endGame() {
        System.out.println("The game has ended.");
    }



//  ----------------------------------------MAIN----------------------------------------------------
    public static void main(String[] args) {
        int numberofplayers = askForPlayers();
        ArrayList<Player> players = new ArrayList<>();
        ArrayList<Deck> decks = new ArrayList<>();

        String location = askForPacklocation(numberofplayers);
//        System.out.println("location:"+location);// test for location returned
        System.out.println("Number of players in game : " + numberofplayers + " players");
        String outputfilepath = "card_pack.txt";
        generateCardPack(numberofplayers,outputfilepath);
        CardGame game = new CardGame(numberofplayers);
        ArrayList<Card> pack = (ArrayList<Card>) game.initialize_a_game(location);
        createPlayers(numberofplayers,players);
        createDeck(numberofPlayers,decks);
        distributeCards(pack ,players, decks);

        for(Player player:players) {
            player.initialHand();
        }




    }
}